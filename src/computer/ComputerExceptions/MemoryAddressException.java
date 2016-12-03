/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.ComputerExceptions;

import gui.UI;

/**
 *
 * @author Administrator
 */
public class MemoryAddressException extends Exception {

    public MemoryAddressException() {
    }

    public void showAlert(UI ui) {
        ui.operatorConsole.showError("Wrong address.", "Memory Error");
    }
}
