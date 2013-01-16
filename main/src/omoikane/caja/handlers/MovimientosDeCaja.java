package omoikane.caja.handlers;

import javafx.event.Event;
import omoikane.caja.presentation.CajaController;
import omoikane.principal.Caja;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 08/12/12
 * Time: 17:12
 * To change this template use File | Settings | File Templates.
 */
public class MovimientosDeCaja extends ICajaEventHandler {
    public MovimientosDeCaja(CajaController controller) {
        super(controller);
    }

    @Override
    public void handle(Event event) {
        Caja.btnMovimientosAction();
    }
}
