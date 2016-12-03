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
public class DeviceFailureException extends Exception {

    public DeviceFailureException() {
    }

    public void showAlert(UI ui) {
        // Hard coded to show alert for Card Reader.
        ui.operatorConsole.showError("Card Reader is not ready.", "Device Failure");
    }
}
