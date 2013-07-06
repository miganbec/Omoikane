package omoikane.caja.handlers;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 3/07/13
 * Time: 03:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class CerrarCajaSwingHandler implements EventHandler<Event> {
    JInternalFrame frame;
    public CerrarCajaSwingHandler(JInternalFrame frame) {
        this.frame = frame;
    }

    @Override
    public void handle(Event event) {
        frame.setVisible(false);
        frame.dispose();
    }
}
