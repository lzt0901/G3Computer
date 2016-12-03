/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.FieldEngineerConsole;

import computer.Computer;
import gui.RegisterGUI;
import gui.UI;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 *
 * @author Administrator
 */
public class RegisterPanel extends JPanel {

    private final Computer computer;
    private final UI ui;
    public RegisterGUI[] fprGUI;
    public RegisterGUI iarGUI;
    public RegisterGUI irrGUI;

    public RegisterPanel(Computer computer, UI ui) {
        super();
        this.computer = computer;
        this.ui = ui;
        this.initComponents();
    }

    private void initComponents() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(boxLayout);
        this.add(Box.createVerticalGlue());
        this.fprGUI = new RegisterGUI[2];
        for (int i = 0; i < 2; ++i) {
            if (i == 0) {
                this.fprGUI[i] = new RegisterGUI(this.computer.cpu.registers.fpr[i], "FP", i, this.computer, this.ui);
            } else // With only index
            {
                this.fprGUI[i] = new RegisterGUI(this.computer.cpu.registers.fpr[i], i, this.computer, this.ui);
            }
            this.add(this.fprGUI[i]);
        }
        this.add(this.iarGUI = new RegisterGUI(this.computer.cpu.registers.iar, "IAR", this.computer, this.ui));
        this.add(this.irrGUI = new RegisterGUI(this.computer.cpu.registers.iar, "IRR", this.computer, this.ui));
    }

}
