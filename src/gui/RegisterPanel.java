/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import computer.Computer;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 *
 * @author Administrator
 */
public class RegisterPanel extends JPanel {

    private final Computer computer;
    private final UI parentframe;

    public JPanel leftPanel;
    public JPanel rightPanel;

    public RegisterGUI[] gprGUI;
    public RegisterGUI[] xGUI;
    public RegisterGUI marGUI;
    public RegisterGUI mbrGUI;
    public RegisterGUI mfrGUI;
    public RegisterGUI irGUI;
    public RegisterGUI pcGUI;
    public RegisterGUI ccGUI;

    public RegisterPanel(Computer computer, UI parentframe) {
        super();
        this.computer = computer;
        this.parentframe = parentframe;
        this.initComponents();
    }

    private void initComponents() {
        // Grid of 1 by 2
        GridLayout grid = new GridLayout(1, 2);
        grid.setHgap(1);
        this.setLayout(grid);

        this.leftPanel = new JPanel();
        this.rightPanel = new JPanel();
        BoxLayout leftBoxlayout = new BoxLayout(this.leftPanel, BoxLayout.Y_AXIS);
        BoxLayout rightBoxlayout = new BoxLayout(this.rightPanel, BoxLayout.Y_AXIS);
        this.leftPanel.setLayout(leftBoxlayout);
        this.rightPanel.setLayout(rightBoxlayout);

        this.add(this.leftPanel);
        this.add(this.rightPanel);

        this.leftPanel.add(Box.createVerticalGlue());
        this.gprGUI = new RegisterGUI[4];
        for (int i = 0; i < 4; ++i) {
            if (i == 0) // With name and index
            {
                this.gprGUI[i] = new RegisterGUI(this.computer.cpu.registers.gpr[i], "GPR", i, this.computer, this.parentframe);
            } else // With only index
            {
                this.gprGUI[i] = new RegisterGUI(this.computer.cpu.registers.gpr[i], i, this.computer, this.parentframe);
            }
            this.leftPanel.add(this.gprGUI[i]);
        }
        this.xGUI = new RegisterGUI[4];
        for (int i = 1; i < 4; ++i) {
            if (i == 1) // With name and index
            {
                this.xGUI[i] = new RegisterGUI(this.computer.cpu.registers.x[i], "IX", i, this.computer, this.parentframe);
            } else // With only index
            {
                this.xGUI[i] = new RegisterGUI(this.computer.cpu.registers.x[i], i, this.computer, this.parentframe);
            }
            this.leftPanel.add(this.xGUI[i]);
        }

        this.rightPanel.add(Box.createVerticalGlue());
        // With only name
        this.rightPanel.add(this.marGUI = new RegisterGUI(this.computer.cpu.registers.mar, "MAR", this.computer, this.parentframe));
        this.rightPanel.add(this.mbrGUI = new RegisterGUI(this.computer.cpu.registers.mbr, "MBR", this.computer, this.parentframe));
        this.rightPanel.add(this.irGUI = new RegisterGUI(this.computer.cpu.registers.ir, "IR", this.computer, this.parentframe));
        this.rightPanel.add(this.pcGUI = new RegisterGUI(this.computer.cpu.registers.pc, "PC", this.computer, this.parentframe));
        this.rightPanel.add(this.ccGUI = new RegisterGUI(this.computer.cpu.registers.cc, "CC", this.computer, this.parentframe));
        this.rightPanel.add(this.mfrGUI = new RegisterGUI(this.computer.cpu.registers.mfr, "MFR", this.computer, this.parentframe));
    }
}
