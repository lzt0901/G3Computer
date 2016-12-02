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
public class UnexpectedInstructionException extends Exception {

    public UnexpectedInstructionException() {
    }

    public void showAlert(UI ui) {
        ui.showError("Unexpected instruction encountered.", "Unexpected Instruction");
    }
}
