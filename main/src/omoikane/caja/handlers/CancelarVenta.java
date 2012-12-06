package omoikane.caja.handlers;

import javafx.event.Event;
import omoikane.caja.presentation.CajaController;
import omoikane.sistema.Usuarios;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 6/12/12
 * Time: 01:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class CancelarVenta extends ICajaEventHandler {
    public static Logger logger = Logger.getLogger(CancelarVenta.class);

    public CancelarVenta(CajaController controller) {
        super(controller);
    }

    @Override
    public void handle(Event event) {
        try {
            Usuarios.login();
            if(Usuarios.cerrojo(Usuarios.SUPERVISOR)) {
                getController().getCajaLogic().nuevaVenta();
            } else {
                getController().getCapturaTextField().requestFocus();
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
