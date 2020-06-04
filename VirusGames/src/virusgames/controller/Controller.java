/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virusgames.controller;

import javafx.scene.control.Control;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 *
 * @author esanchez
 */
public abstract class Controller {

    private Stage stage;
    private String accion;

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void sendTabEvent(KeyEvent event) {
        event.consume();
        KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, null, null, KeyCode.TAB, false, false, false, false);
        ((Control) event.getSource()).fireEvent(keyEvent);
    }

    public boolean validaListaAyuda(KeyEvent event) {
        if (event.getCode() == KeyCode.F9) {
            try {
                TextInputControl control = (TextInputControl) event.getSource();
                return control.isEditable() && !control.isDisable();
            } catch (Exception ex) {
                try {
                    Control control = (Control) event.getSource();
                    return !control.isDisable();
                } catch (Exception exc) {
                    return false;
                }
            }
        }
        return false;
    }

    public abstract void initialize();
}
