/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computer;

import gui.UI;

/**
 *
 * @author Administrator
 */
public class Computer {

    private UI ui;

    public CPU cpu;
    public MemorySystem memory;

    public Computer() {
        this.memory = new MemorySystem(2048);
        this.cpu = new CPU(16, this.memory, 32);
        this.memory.setCPU(this.cpu);
    }

    public void setUI(UI ui) {
        this.ui = ui;
        this.cpu.setUI(this.ui);
        this.memory.setUI(this.ui);
    }

    public void clearAll() {
        if (this.cpu.isInterrupted(true)) {
            this.ui.fieldEngineerConsole.showError("Disabled by interrupt.", "CPU Error");
            this.ui.operatorConsole.ioPanel.focusOnInputAndSelectAll();
            return;
        }
        
        this.memory.clear();
        this.cpu.registers.reset(0);
        
        this.ui.operatorConsole.ioPanel.reset();
        this.ui.fieldEngineerConsole.outputPanel.reset();
        
        this.ui.fieldEngineerConsole.showMessage("Done. Now you can load a new boot program.");
    }
}
