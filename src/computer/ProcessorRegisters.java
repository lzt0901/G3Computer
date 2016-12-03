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
public class ProcessorRegisters {

    private UI ui;

    public Register pc;
    // cc(0), cc(1), cc(2), cc(3): overflow, underflow, division by zero, equal-or-not.
    public Register cc;
    public Register ir;
    public Register iar;
    public Register irr;
    public Register mar;
    public Register mbr;
    public Register mfr;
    // X[1], X[2], X[3]
    public Register[] x;
    // R[0], R[1], R[2], R[3]
    public Register[] gpr;
    // FPR[0], FPR[1]
    public Register[] fpr;

    public ProcessorRegisters() {
        this.pc = new Register(12);
        this.cc = new Register(4);
        this.ir = new Register(16);
        this.iar = new Register(16);
        this.irr = new Register(16);
        this.mar = new Register(16);
        this.mbr = new Register(16);
        this.mfr = new Register(4);
        this.gpr = new Register[4];
        for (int i = 0; i < this.gpr.length; ++i) {
            this.gpr[i] = new Register(16);
        }
        this.x = new Register[4];
        for (int i = 1; i < this.x.length; ++i) {
            this.x[i] = new Register(16);
        }
        this.fpr = new Register[2];
        for (int i = 0; i < this.fpr.length; ++i) {
            this.fpr[i] = new Register(16);
        }
    }

    public void reset(int initalProgramAddress) {
        this.pc.setContent(initalProgramAddress);
        this.cc.setContent(0);
        this.ir.setContent(0);
        this.iar.setContent(0);
        this.irr.setContent(0);
        this.mar.setContent(0);
        this.mbr.setContent(0);
        this.mfr.setContent(0);
        for (int i = 0; i < this.gpr.length; ++i) {
            this.gpr[i].setContent(0);
        }
        for (int i = 1; i < this.x.length; ++i) {
            this.x[i].setContent(0);
        }
        for (int i = 0; i < this.fpr.length; ++i) {
            this.fpr[i].setContent(0);
        }
    }

    public void setUI(UI ui) {
        this.ui = ui;
        this.pc.setRegisterGUI(this.ui.operatorConsole.registerPanel.pcGUI);
        this.cc.setRegisterGUI(this.ui.operatorConsole.registerPanel.ccGUI);
        this.ir.setRegisterGUI(this.ui.operatorConsole.registerPanel.irGUI);
        this.mar.setRegisterGUI(this.ui.operatorConsole.registerPanel.marGUI);
        this.mbr.setRegisterGUI(this.ui.operatorConsole.registerPanel.mbrGUI);
        this.mfr.setRegisterGUI(this.ui.operatorConsole.registerPanel.mfrGUI);
        for (int i = 0; i < this.gpr.length; ++i) {
            this.gpr[i].setRegisterGUI(this.ui.operatorConsole.registerPanel.gprGUI[i]);
        }
        for (int i = 1; i < this.x.length; ++i) {
            this.x[i].setRegisterGUI(this.ui.operatorConsole.registerPanel.xGUI[i]);
        }

        this.iar.setRegisterGUI(this.ui.fieldEngineerConsole.registerPanel.iarGUI);
        this.irr.setRegisterGUI(this.ui.fieldEngineerConsole.registerPanel.irrGUI);
        for (int i = 0; i < this.fpr.length; ++i) {
            this.fpr[i].setRegisterGUI(this.ui.fieldEngineerConsole.registerPanel.fprGUI[i]);
        }
    }
}
