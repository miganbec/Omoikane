package omoikane.caja.handlers;

import javafx.event.Event;
import javafx.event.EventHandler;
import omoikane.caja.presentation.CajaController;
import omoikane.sistema.Comprobantes;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 5/12/12
 * Time: 12:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class AbrirCajon extends ICajaEventHandler {

    public AbrirCajon(CajaController controller) {
        super(controller);
    }

    @Override
    public void handle(Event event) {
        Comprobantes c = new Comprobantes();
        c.abrirCajon();
    }
}
