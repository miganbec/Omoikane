package omoikane.caja.handlers;

import javafx.event.Event;
import javafx.event.EventHandler;
import omoikane.caja.presentation.CajaController;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 6/12/12
 * Time: 01:26 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ICajaEventHandler implements EventHandler<Event> {
    CajaController cajaController;
    public ICajaEventHandler(CajaController controller) {
        setCajaController(controller);
    }

    public CajaController getController() {
        return cajaController;
    }

    public void setCajaController(CajaController controller) {
        this.cajaController = controller;
    }
}
