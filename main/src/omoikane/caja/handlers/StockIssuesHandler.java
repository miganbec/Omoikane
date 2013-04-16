package omoikane.caja.handlers;

import omoikane.caja.presentation.CajaController;
import omoikane.caja.presentation.ProductoModel;
import omoikane.entities.Paquete;
import omoikane.inventarios.Stock;
import omoikane.principal.Principal;
import omoikane.producto.Articulo;
import omoikane.producto.Producto;
import omoikane.repository.CancelacionRepo;
import omoikane.repository.ProductoRepo;
import omoikane.repository.VentaRepo;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 16/04/13
 * Time: 02:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class StockIssuesHandler {
    ProductoRepo repo;
    CajaController controller;

    public StockIssuesHandler(CajaController c) {
        repo = (ProductoRepo) Principal.applicationContext.getBean("productoRepo");
        controller = c;
    }

    /**
     * Quantity sold, there are 2 cases:
     * - Simple product sold: Just reduces the quantity sold
     * - Package product: Infers this type and load the child products and quantity per package from database,
     * then, multiplies quantity per package by quantity sold
     */
    public void handle() {
        for(ProductoModel pm : controller.getModel().getVenta()) {
            Articulo p = repo.readByPrimaryKey(pm.getLongId());
            if(p.getEsPaquete()) {
                for(Paquete paquete : p.getRenglonesPaquete()) {
                    Articulo productoContenido = paquete.getProductoContenido();
                    Stock s = productoContenido.getStock();
                    BigDecimal quantitySold = paquete.getCantidad().multiply( pm.getCantidad() );
                    s.setEnTienda( s.getEnTienda().subtract(quantitySold) );
                }
            } else {
                BigDecimal quantitySold = pm.getCantidad();
                Stock s = p.getStock();
                s.setEnTienda( s.getEnTienda().subtract( quantitySold ) );
            }
        }
    }
}
