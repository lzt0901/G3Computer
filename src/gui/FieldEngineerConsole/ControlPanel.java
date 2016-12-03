/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.FieldEngineerConsole;

import computer.Computer;
import gui.UI;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Administrator
 */
public class ControlPanel extends JPanel {

    private final Computer computer;
    private final UI ui;

    public ControlPanel(Computer computer, UI ui) {
        super();
        this.computer = computer;
        this.initComponents();
        this.ui = ui;
    }

    private void initComponents() {
        JButton resetButton = new JButton("Clear All");
        resetButton.addActionListener((ActionEvent ae) -> {
            if (!this.computer.cpu.available()) {
                return;
            }
            if (this.computer.cpu.isInterrupted(true)) {
                this.ui.fieldEngineerConsole.showError("Disabled by interrupt.", "CPU Error");
                return;
            }
            this.computer.clearAll();
            this.ui.operatorConsole.ioPanel.outputTextArea.setText("");
            this.ui.fieldEngineerConsole.outputPanel.outputTextArea.setText("");
            this.ui.fieldEngineerConsole.showMessage("Done. Now you can load a new boot program.");
        });
        this.add(resetButton);
    }

}
