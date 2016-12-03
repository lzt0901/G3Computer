/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.FieldEngineerConsole;

import computer.Computer;
import gui.UI;
import gui.TextLineNumber;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author Administrator
 */
public class OutputPanel extends JPanel {

    private final Computer computer;
    private final UI ui;

    public JTextArea outputTextArea;

    private boolean isNewLine;

    public OutputPanel(Computer computer, UI ui) {
        super();
        this.computer = computer;
        this.ui = ui;
        this.initComponents();

        this.isNewLine = false;
    }

    private void initComponents() {
        BoxLayout ioBoxlayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(ioBoxlayout);

        // Output
        this.outputTextArea = new JTextArea();
        this.outputTextArea.setEditable(false);
        this.outputTextArea.setRows(26);
        JScrollPane scrollPane = new JScrollPane(this.outputTextArea);
        TextLineNumber tln = new TextLineNumber(this.outputTextArea);
        scrollPane.setRowHeaderView(tln);
        // Set auto scroll down.
        DefaultCaret caret = (DefaultCaret) this.outputTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        this.add(scrollPane);
    }

    public void setOutput(String content) {
        if (this.isNewLine) {
            this.outputTextArea.append(System.lineSeparator());
            this.isNewLine = false;
        }
        this.outputTextArea.append(content);
        this.isNewLine = true;
    }
}
