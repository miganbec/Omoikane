package omoikane.caja.handlers;

import javafx.collections.ObservableList;
import javafx.event.Event;
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
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 6/12/12
 * Time: 01:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class CancelarVenta extends ICajaEventHandler {
    public static Logger logger = Logger.getLogger(CancelarVenta.class);
    CancelacionRepo repo;

    VentaRepo ventaRepo;

    public CancelarVenta(CajaController controller) {
        super(controller);
        repo = (CancelacionRepo) Principal.applicationContext.getBean("cancelacionRepo");
        ventaRepo = (VentaRepo) Principal.applicationContext.getBean("ventaRepo");
    }

    @Override
    public void handle(Event event) {
        try {
            if(Usuarios.autentifica(Usuarios.SUPERVISOR)) {
                registrar(getController().getModel().getVenta());
                ventaRepo.delete(getController().getCajaLogic().getVentaAbiertaBean());
                getController().getCajaLogic().nuevaVenta();
            } else {
                getController().getCapturaTextField().requestFocus();
            }
        } catch (Exception e) {
            logger.error("Error al cancelar venta", e);
        }
    }

    @Transactional
    private void registrar(ObservableList<ProductoModel> venta) {

        for(ProductoModel pm : venta) {
            Cancelacion c = new Cancelacion();
            c.setArticulo   ( new Articulo( pm.getLongId() ) );
            c.setCajero     ( new Usuario( new Long(Usuarios.getIDUsuarioActivo()   ) ) );
            c.setAutorizador( new Usuario( new Long(Usuarios.getIDUltimoAutorizado()) ) );
            repo.save(c);
        }
    }
}
