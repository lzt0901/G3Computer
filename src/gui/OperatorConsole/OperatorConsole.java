/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.OperatorConsole;

import computer.Computer;
import gui.UI;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Administrator
 */
public class OperatorConsole extends JPanel {

    private final Computer computer;
    private final UI ui;

    public IOPanel ioPanel;
    public RegisterPanel registerPanel;
    public ControlPanel controlPanel;

    public OperatorConsole(Computer computer, UI ui) {
        super();
        this.computer = computer;
        this.ui = ui;
        this.initComponents();
    }

    private void initComponents() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(boxLayout);

        this.ioPanel = new IOPanel(this.computer, this.ui);
        this.ioPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, this.ioPanel.getPreferredSize().height));
        this.add(this.ioPanel);

        this.registerPanel = new RegisterPanel(this.computer, this.ui);
        this.add(this.registerPanel);

        this.controlPanel = new ControlPanel(this.computer, this.ui);
        this.controlPanel.setMaximumSize(this.controlPanel.getPreferredSize());
        this.add(this.controlPanel);

        // Set title for the console
        TitledBorder border = new TitledBorder("Operator Console");
        border.setTitleJustification(TitledBorder.CENTER);
        border.setTitlePosition(TitledBorder.TOP);
        border.setTitleFont(new Font("Arial", Font.ITALIC, 23));
        this.setBorder(border);
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
