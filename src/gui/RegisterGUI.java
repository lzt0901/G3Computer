/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import computer.Computer;
import computer.Register;
import javax.swing.*;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Administrator
 */
public class RegisterGUI extends JPanel {

    private final Computer computer;
    private final UI ui;

    private final String name;
    private final int index;
    private final Register register;
    private JRadioButton[] radioButtons;
    private JTextField valueField;
    private JPanel lightPanel;

    public RegisterGUI(Register register, String name, Computer computer, UI ui) {
        super();
        this.computer = computer;
        this.ui = ui;
        this.name = name;
        this.register = register;
        this.index = -1;
        this.initComponents();
    }

    public RegisterGUI(Register register, int index, Computer computer, UI ui) {
        super();
        this.computer = computer;
        this.ui = ui;
        this.name = null;
        this.register = register;
        this.index = index;
        this.initComponents();
    }

    public RegisterGUI(Register register, String name, int index, Computer computer, UI ui) {
        super();
        this.computer = computer;
        this.ui = ui;
        this.name = name;
        this.register = register;
        this.index = index;
        this.initComponents();
    }

    private void initComponents() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        if (this.name != null) {
            this.setupName();
        }
        // Pack the values and buttons together into a panel
        this.lightPanel = new JPanel();
        if (this.index != -1) {
            this.setupIndex();
        }
        this.setupValues();
    }

    private void setupName() {
        // Register name
        JLabel label = new JLabel(this.name);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(label);
    }

    private void setupIndex() {
        // Register index (for GPR and IX)
        this.lightPanel.add(new JLabel(Integer.toString(this.index)));
    }

    private void setupValues() {
        // Register value
        this.radioButtons = new JRadioButton[this.register.getLength()];
        for (int i = 0; i < this.radioButtons.length; ++i) {
            this.radioButtons[i] = new JRadioButton();
            this.radioButtons[i].addActionListener((ActionEvent ae) -> {
                this.setTextByLights();
            });
            this.lightPanel.add(this.radioButtons[i]);
        }

        // Set TextField
        this.valueField = new JTextField("0");
        this.valueField.setColumns(4);
        this.valueField.setHorizontalAlignment(SwingConstants.RIGHT);
        this.valueField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent de) {
                setLightsByText();
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                setLightsByText();
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                setLightsByText();
            }

        });
        this.lightPanel.add(valueField);

        // Set button
        JButton button = new JButton("Set");
        this.lightPanel.add(button);
        button.addActionListener((ActionEvent ae) -> {
            if (!this.computer.cpu.available()) {
                return;
            }

            try {
                int value = Integer.parseInt(this.valueField.getText());
                // Prohibit response for efficiency (otherwise the setRadioButtons method would be called again, which is unnecessary).
                register.setContent(value, true);
                this.ui.operatorConsole.showMessage("Value set.");
            } catch (NumberFormatException nfx) {
                this.ui.operatorConsole.showError("Invalid value. Please input again.", "Input Error");
            }
        });

        // All set. Add to the parent panel.
        this.add(this.lightPanel);
    }

    private int lightsToValue() {
        int value = 0;
        for (int i = 0; i < radioButtons.length; ++i) {
            if (radioButtons[i].isSelected()) {
                value |= 1 << radioButtons.length - i - 1;
            }
        }
        return value;
    }

    public void setLights(int value) {
        for (int i = this.radioButtons.length - 1; i >= 0; --i) {
            if ((value & 1) == 0) {
                this.radioButtons[i].setSelected(false);
            } else {
                this.radioButtons[i].setSelected(true);
            }
            value >>= 1;
        }
    }

    public void setValueText(int value) {
        this.valueField.setText(String.valueOf(value));
    }

    private void setLightsByText() {
        try {
            int value = Integer.parseInt(this.valueField.getText());
            this.setLights(value);
        } catch (NumberFormatException nfx) {
        }
    }

    private void setTextByLights() {
        int value = this.lightsToValue();
        // Prohibit response for efficiency (otherwise the setRadioButtons method would be called again, which is unnecessary).
        this.setValueText(value);
    }
}
