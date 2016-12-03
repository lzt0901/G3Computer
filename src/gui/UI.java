/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import computer.Computer;
import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class UI extends JFrame {

    private final Computer computer;

    public IOPanel ioPanel;
    public RegisterPanel registerPanel;
    public ControlPanel controlPanel;

    public UI(Computer computer) {
        super();
        this.computer = computer;
        this.initComponents();
    }

    private void initComponents() {
        this.setSize(1250, 950);
        this.setLocationRelativeTo(null);

        this.ioPanel = new IOPanel(this.computer, this);
        this.getContentPane().add(BorderLayout.NORTH, this.ioPanel);

        this.registerPanel = new RegisterPanel(this.computer, this);
        this.getContentPane().add(BorderLayout.CENTER, this.registerPanel);

        this.controlPanel = new ControlPanel(this.computer, this);
        this.getContentPane().add(BorderLayout.SOUTH, this.controlPanel);

        // If it's not set the frame would just be hidden and the Java virtual machine (VM) will not terminate.
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    public void showMessage(String content) {
        JOptionPane.showMessageDialog(this, content);
    }

    public void showWarning(String content, String title) {
        JOptionPane.showMessageDialog(this, content, title, JOptionPane.WARNING_MESSAGE);
    }

    public void showError(String content, String title) {
        JOptionPane.showMessageDialog(this, content, title, JOptionPane.ERROR_MESSAGE);
    }

    public File chooseFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            this.showWarning("Canceled.", "Loading Files");
            return null;
        } else {
            return chooser.getSelectedFile();
        }
    }
}
