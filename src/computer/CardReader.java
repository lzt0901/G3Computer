/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computer;

import computer.ComputerExceptions.DeviceFailureException;
import gui.UI;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author Administrator
 */
public class CardReader {

    private UI ui;

    private final MemorySystem memory;
    private final CPU cpu;

    private int status;
    private int pointer;
    private String contents;

    public CardReader(MemorySystem memory, CPU cpu) {
        this.memory = memory;
        this.cpu = cpu;
        this.status = 0;
        this.pointer = 0;
    }

    public void setUI(UI ui) {
        this.ui = ui;
    }

    public int getStatus() {
        return this.status;
    }

    public void load() {
        if (this.cpu.isInterrupted()) {
            return;
        }

        // Call file chooser to read a file
        File file = this.ui.operatorConsole.chooseFile();
        if (file == null) {
            return;
        }

        try {
            // Put all contents of the file into a string variable
            this.contents = new String(Files.readAllBytes(Paths.get(file.getPath())));
            // Required by unicornyzh
            this.contents = this.contents.trim();
            // Required by myself
            this.contents = this.contents.toLowerCase();
        } catch (IOException ex) {
            this.ui.operatorConsole.showError("IO Exception occurred.", "IO Error");
            return;
        }

        // Right now we would assume the device would be always in the card reader after it is inserted.
        // So, there is no operation for flipping the status back to 0.
        this.status = 1;
        this.ui.operatorConsole.showMessage("Card Reader is ready.");
    }

    // Return the ASCII of characters.
    public int getASCII() throws DeviceFailureException {
        if (this.status == 0) {
            throw new DeviceFailureException();
        }
        if (this.pointer < this.contents.length()) {
            return this.contents.charAt(this.pointer++);
        } else {
            this.pointer = 0;
            return '\0';
        }
    }
}
