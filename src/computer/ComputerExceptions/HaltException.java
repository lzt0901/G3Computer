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
public class HaltException extends Exception {

    public HaltException() {
    }

    public void showAlert(UI ui) {
        ui.operatorConsole.showWarning("Halt.", "Halt Instruction");
    }
}
