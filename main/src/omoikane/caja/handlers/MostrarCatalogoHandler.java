package omoikane.caja.handlers;

import javafx.event.Event;
import omoikane.caja.presentation.CajaController;
import omoikane.principal.Articulos;
import omoikane.principal.Caja;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 08/12/12
 * Time: 17:12
 * To change this template use File | Settings | File Templates.
 */
public class MostrarCatalogoHandler extends ICajaEventHandler {
    public MostrarCatalogoHandler(CajaController controller) {
        super(controller);
    }

    @Override
    public void handle(Event event) {
        String retorno = Articulos.lanzarDialogoCatalogo();

        retorno = (retorno==null)?"":retorno;
        String captura = cajaController.getModel().getCaptura().get();
        captura = (captura==null)?"":captura;
        cajaController.getModel().getCaptura().set(captura + retorno);

        cajaController.getMainAnchorPane().requestFocus();
        cajaController.getCapturaTextField().requestFocus();
    }
}
