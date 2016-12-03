/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.FieldEngineerConsole.FieldEngineerConsole;
import gui.OperatorConsole.OperatorConsole;
import computer.Computer;
import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 *
 * @author Administrator
 */
public class UI extends JFrame {

    private final Computer computer;

    public OperatorConsole operatorConsole;
    public FieldEngineerConsole fieldEngineerConsole;

    public UI(Computer computer) {
        this.computer = computer;
        this.initComponents();
    }

    private void initComponents() {
        this.setSize(1700, 950);
        this.setLocationRelativeTo(null);

        this.operatorConsole = new OperatorConsole(this.computer, this);
        this.getContentPane().add(BorderLayout.WEST, this.operatorConsole);

        this.fieldEngineerConsole = new FieldEngineerConsole(this.computer, this);
        this.getContentPane().add(BorderLayout.CENTER, this.fieldEngineerConsole);

        // If it's not set the frame would just be hidden and the Java virtual machine (VM) will not terminate.
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }
}
