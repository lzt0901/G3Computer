/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computer;

import gui.UI;
import computer.ComputerExceptions.MemoryAddressException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class RomLoader {

    private UI ui;

    private final MemorySystem memory;
    private final CPU cpu;

    private boolean bootReady;
    private boolean defaultBootReady;

    public RomLoader(MemorySystem memory, CPU cpu) {
        this.memory = memory;
        this.cpu = cpu;
        this.bootReady = false;
        this.defaultBootReady = false;
    }

    public void setUI(UI ui) {
        this.ui = ui;
    }

    public void loadFromFile(int radix) {
        if (this.cpu.isInterrupted()) {
            return;
        }

        // Call file chooser to read a file
        File file = this.ui.operatorConsole.chooseFile();
        if (file == null) {
            return;
        }

        int count = 0;
        // Construct a list for instructions
        List<Integer> instructions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                // Skip non-parsable lines
                try {
                    // Accept data in given radix (currently it could be 2 or 8 or 16).
                    instructions.add(Integer.parseInt(line, radix));
                    ++count;
                } catch (NumberFormatException nfx) {
                }
            }
        } catch (IOException x) {
            this.ui.operatorConsole.showError("IO Exception occurred.", "IO Error");
            return;
        }

        if (instructions.isEmpty()) {
            this.ui.operatorConsole.showError("Load failed. Please check the content and format of your file.", "Load Error");
            return;
        }

        this.memory.cache.openTraceFile();
        int start = this.memory.allocate(instructions);
        this.memory.cache.closeTraceFile();
        if (start != -1) {
            this.ui.operatorConsole.showMessage("Loaded " + count + (count == 1 ? " instruction. " : " instructions. ") + "Your program starts at address " + start + ".");
            if (!this.bootReady) {
                this.ui.operatorConsole.showWarning("No default initial program found. This program will serve as one.", "Boot Program");
                this.setBootReady();
            }
        }
    }

    public void loadInitialProgram() throws MemoryAddressException {
        if (this.cpu.isInterrupted()) {
            return;
        }

        if (this.bootReady) {
            this.ui.operatorConsole.showError("Failed because a boot program has already been loaded.", "Boot Program");
            return;
        }

        this.memory.cache.openTraceFile();

        // Directly setting memory instead of using instructions.
        this.memory.write(5, 0);
        this.memory.write(6, 1300);
        this.memory.write(7, 0);
        this.memory.write(8, 0);
        this.memory.write(9, 600);
        this.memory.write(10, 13312);
        this.memory.write(12, 32);
        this.memory.write(13, 44);
        this.memory.write(14, 46);
        this.memory.write(15, 63);
        this.memory.write(1303, 1);
        this.memory.write(1304, 1);

        this.memory.write(1329, 65535);
        this.memory.write(1331, 65535);

        this.memory.cache.closeTraceFile();

        this.setDefaultBootReady();

        this.ui.operatorConsole.showMessage("Loaded default initial program.");
    }

    private void setDefaultBootReady() {
        this.defaultBootReady = true;
        this.bootReady = true;
        this.cpu.resetRegisters();
    }

    private void setBootReady() {
        this.bootReady = true;
        this.cpu.resetRegisters();
    }

    public boolean shouldRunBoot() {
        return this.bootReady && !this.defaultBootReady;
    }

    public void clear() {
        this.defaultBootReady = false;
        this.bootReady = false;
    }
}
