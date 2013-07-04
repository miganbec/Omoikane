package omoikane.caja.handlers;

import com.sun.javafx.stage.EmbeddedWindow;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.stage.Stage;
import omoikane.caja.presentation.CajaController;
import omoikane.caja.presentation.ProductoModel;
import omoikane.entities.Cancelacion;
import omoikane.entities.Usuario;
import omoikane.principal.Principal;
import omoikane.producto.Articulo;
import omoikane.repository.CancelacionRepo;
import omoikane.repository.VentaRepo;
import omoikane.sistema.Usuarios;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 6/12/12
 * Time: 01:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class CerrarCajaHandler extends ICajaEventHandler {
    public static Logger logger = Logger.getLogger(CerrarCajaHandler.class);

    public CerrarCajaHandler(CajaController controller) {
        super(controller);
    }

    @Override
    public void handle(Event event) {
        Node  source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }


}
