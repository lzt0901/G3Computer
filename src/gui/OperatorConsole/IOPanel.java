/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.OperatorConsole;

import computer.ComputerExceptions.MemoryAddressException;
import computer.Computer;
import gui.UI;
import gui.TextLineNumber;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author Administrator
 */
public class IOPanel extends JPanel {

    private final Computer computer;
    private final UI ui;

    public JTextArea outputTextArea;
    public JTextField inputTextField;

    private boolean isNewLine;

    public IOPanel(Computer computer, UI ui) {
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
        this.outputTextArea.setRows(13);
        JScrollPane scrollPane = new JScrollPane(this.outputTextArea);
        TextLineNumber tln = new TextLineNumber(this.outputTextArea);
        scrollPane.setRowHeaderView(tln);
        // Set auto scroll down.
        DefaultCaret caret = (DefaultCaret) this.outputTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        this.add(scrollPane);

        // Input
        this.inputTextField = new JTextField();
        this.inputTextField.addActionListener((ActionEvent ae) -> {
            this.validateInputAndRecover();
        });
        this.add(inputTextField);
    }

    public void focusOnInputAndSelectAll() {
        this.inputTextField.requestFocus();
        this.inputTextField.selectAll();
    }

    private void validateInputAndRecover() {
        // Validate the timing.
        if (!this.computer.cpu.isInterrupted(true)) {
            this.ui.operatorConsole.showError("Input denied: No input instruction executed.", "Input Error");
            return;
        }

        try {
            if (this.computer.memory.directRead(7) == 0) {
                // Validate the input.
                try {
                    this.getNumberInput();
                } catch (NumberFormatException ex) {
                    this.ui.operatorConsole.showError("Incorrect input.", "Input Error");
                    this.focusOnInputAndSelectAll();
                    return;
                }
            } else {
                if (this.getStringInput().equals("")) {
                    this.ui.operatorConsole.showError("Input can't be empty.", "Input Error");
                    this.focusOnInputAndSelectAll();
                    return;
                }
            }
        } catch (MemoryAddressException ex) {
        }

        this.computer.cpu.recover();
    }

    public int getNumberInput() {
        return Integer.parseInt(this.inputTextField.getText());
    }

    public String getStringInput() {
        // Characters are converted to lowercase.
        return this.inputTextField.getText().toLowerCase();
    }

    public void setOutput(int content) {
        if (this.isNewLine) {
            this.outputTextArea.append(System.lineSeparator());
            this.isNewLine = false;
        }
        this.outputTextArea.append(String.valueOf(content));
        this.isNewLine = true;
    }

    public void setOutput(char content) {
        if (this.isNewLine) {
            this.outputTextArea.append(System.lineSeparator());
            this.isNewLine = false;
        }
        if (content == '\n') {
            this.isNewLine = true;
        } else {
            this.outputTextArea.append(String.valueOf(content));
        }
    }

    public void setOutput(String content) {
        if (this.isNewLine) {
            this.outputTextArea.append(System.lineSeparator());
            this.isNewLine = false;
        }
        this.outputTextArea.append(content);
        this.isNewLine = true;
    }
    
    public void reset() {
        this.outputTextArea.setText("");
        this.isNewLine = false;
    }
}
