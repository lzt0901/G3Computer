/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.FieldEngineerConsole;

import computer.Computer;
import gui.UI;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Administrator
 */
public class FieldEngineerConsole extends JPanel {

    private final Computer computer;
    private final UI ui;

    public OutputPanel outputPanel;
    public RegisterPanel registerPanel;
    public ControlPanel controlPanel;

    public FieldEngineerConsole(Computer computer, UI ui) {
        super();
        this.computer = computer;
        this.ui = ui;
        this.initComponents();
    }

    private void initComponents() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(boxLayout);

        this.outputPanel = new OutputPanel(this.computer, this.ui);
        this.outputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, this.outputPanel.getPreferredSize().height));
        this.add(this.outputPanel);

        this.registerPanel = new RegisterPanel(this.computer, this.ui);
        this.add(this.registerPanel);

        this.controlPanel = new ControlPanel(this.computer, this.ui);
        this.controlPanel.setMaximumSize(this.controlPanel.getPreferredSize());
        this.add(this.controlPanel);

        // Set title for the console
        TitledBorder border = new TitledBorder("Field Engineer Console");
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
}
