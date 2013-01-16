package omoikane.caja.handlers;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import omoikane.caja.presentation.CajaController;
import omoikane.caja.presentation.ProductoModel;
import omoikane.entities.Cancelacion;
import omoikane.entities.Usuario;
import omoikane.principal.Principal;
import omoikane.producto.Articulo;
import omoikane.repository.CancelacionRepo;
import omoikane.sistema.Usuarios;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 6/12/12
 * Time: 04:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class CancelarProducto extends ICajaEventHandler {

    VentaKBHandler ventaKBHandler;
    CancelacionRepo repo;

    public static Logger logger = Logger.getLogger(CancelarVenta.class);

    public CancelarProducto(CajaController controller) {
        super(controller);
        ventaKBHandler = new VentaKBHandler();
        repo = (CancelacionRepo) Principal.applicationContext.getBean("cancelacionRepo");
    }

    @Override
    public void handle(Event event) {
        modoCancelar();
    }

    public void cancelar() {
        try {
            if(Usuarios.autentifica(Usuarios.SUPERVISOR)) {
                ProductoModel quitar = getController().getVentaTableView().getSelectionModel().getSelectedItem();
                getController().getModel().getVenta().remove(quitar);
                registrar(quitar);
            } else {
                getController().getCapturaTextField().requestFocus();
            }
        } catch (Exception e) {
            logger.error("Error al cancelar producto", e);
        }
    }

    private void registrar(ProductoModel quitar) {
        Cancelacion c = new Cancelacion();
        c.setArticulo   ( new Articulo( quitar.getLongId() ) );
        c.setCajero     ( new Usuario( new Long(Usuarios.getIDUsuarioActivo()   ) ) );
        c.setAutorizador( new Usuario( new Long(Usuarios.getIDUltimoAutorizado()) ) );
        repo.save(c);
    }

    public void modoCancelar() {
        getController().setCapturaPaneDisable(true);
        getController().setMainToolBarDisable(true);
        getController().showHud("[Enter] Cancelar producto \n[Esc] No cancelar");
        getController().getVentaTableView().requestFocus();
        getController().getVentaTableView().onKeyReleasedProperty().set(ventaKBHandler);
    }

    public void modoNormal() {
        getController().setCapturaPaneDisable(false);
        getController().setMainToolBarDisable(false);
        getController().hideHud();
        getController().getVentaTableView().onKeyReleasedProperty().set(null);
        getController().getCapturaTextField().requestFocus();
    }

    public class VentaKBHandler implements EventHandler<KeyEvent> {

        @Override
        public void handle(KeyEvent event) {
            CajaController controller = CancelarProducto.this.getController();

            if(event.getCode() == KeyCode.ENTER) {
                CancelarProducto.this.cancelar();
                CancelarProducto.this.modoNormal();
            }
            if(event.getCode() == KeyCode.ESCAPE) {
                CancelarProducto.this.modoNormal();
            }
        }
    }
}
