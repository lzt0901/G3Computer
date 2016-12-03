/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.OperatorConsole;

import computer.Computer;
import computer.ComputerExceptions.MemoryAddressException;
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
        JButton iplButton = new JButton("Default IPL");
        JButton loadHexButton = new JButton("Load Hex");
        JButton loadOctButton = new JButton("Load Oct");
        JButton loadBinButton = new JButton("Load Bin");
        JButton runButton = new JButton("RUN");
        JButton ssButton = new JButton("Single Step");
        JButton cardReaderButton = new JButton("Card Reader");

        iplButton.addActionListener((ActionEvent ae) -> {
            if (!this.computer.cpu.available()) {
                return;
            }

            try {
                this.computer.memory.romLoader.loadInitialProgram();
            } catch (MemoryAddressException ex) {
                ex.showAlert(this.ui);
            }
        });

        loadHexButton.addActionListener((ActionEvent ae) -> {
            if (!this.computer.cpu.available()) {
                return;
            }

            this.computer.memory.romLoader.loadFromFile(16);
        });

        loadOctButton.addActionListener((ActionEvent ae) -> {
            if (!this.computer.cpu.available()) {
                return;
            }

            this.computer.memory.romLoader.loadFromFile(8);
        });

        loadBinButton.addActionListener((ActionEvent ae) -> {
            if (!this.computer.cpu.available()) {
                return;
            }

            this.computer.memory.romLoader.loadFromFile(2);
        });

        runButton.addActionListener((ActionEvent ae) -> {
            if (!this.computer.cpu.available()) {
                return;
            }

            this.computer.cpu.run();
        });

        ssButton.addActionListener((ActionEvent ae) -> {
            if (!this.computer.cpu.available()) {
                return;
            }

            this.computer.memory.cache.openTraceFile();
            this.computer.cpu.singleStep();
            this.computer.memory.cache.closeTraceFile();
        });

        cardReaderButton.addActionListener((ActionEvent ae) -> {
            if (!this.computer.cpu.available()) {
                return;
            }

            this.computer.memory.cardReader.load();
        });

        this.add(iplButton);
        this.add(loadHexButton);
        this.add(loadOctButton);
        this.add(loadBinButton);
        this.add(runButton);
        this.add(ssButton);
        this.add(cardReaderButton);
    }
}
