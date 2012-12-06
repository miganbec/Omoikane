package omoikane.caja.handlers;

import javafx.event.Event;
import omoikane.caja.presentation.CajaController;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 6/12/12
 * Time: 04:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class CancelarProducto extends ICajaEventHandler {
    public CancelarProducto(CajaController controller) {
        super(controller);
    }

    @Override
    public void handle(Event event) {
        getController().setCapturaPaneDisable(true);
        getController().getVentaTableView().requestFocus();
    }
}
