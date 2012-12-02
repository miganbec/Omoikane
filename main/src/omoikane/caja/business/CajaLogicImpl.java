package omoikane.caja.business;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.dialogs.DialogFX;
import name.antonsmirnov.javafx.dialog.Dialog;
import omoikane.caja.data.IProductosDAO;
import omoikane.caja.presentation.BuscarMasDummyProducto;
import omoikane.caja.presentation.CajaController;
import omoikane.caja.presentation.CajaModel;
import omoikane.caja.presentation.ProductoModel;
import omoikane.entities.Caja;
import omoikane.entities.LegacyVenta;
import omoikane.entities.LegacyVentaDetalle;
import omoikane.principal.Principal;
import omoikane.producto.Producto;
import omoikane.sistema.Comprobantes;
import omoikane.sistema.Dialogos;
import omoikane.sistema.Usuarios;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.synyx.hades.domain.PageRequest;
import org.synyx.hades.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 13/09/12
 * Time: 02:01 AM
 * To change this template use File | Settings | File Templates.
 */


public class CajaLogicImpl implements ICajaLogic {
    public static Logger logger = Logger.getLogger(CajaLogicImpl.class);
    Boolean capturaBloqueada = false;

    @Autowired
    IProductosDAO productosDAO;

    @Autowired
    Comprobantes comprobantes;

    @PersistenceContext
    EntityManager entityManager;
    private CajaController controller;

    /**
     * Pseudo evento gatillado cuando se intenta capturar un producto en la "línea de captura".
     * Ignora cualquier intento de captura si ya existe una en curso
     */
    public synchronized void onCaptura(CajaModel model) {
        if(!capturaBloqueada) {
            capturaBloqueada = true;
            try {
                LineaDeCaptura captura = new LineaDeCaptura(model.getCaptura().get());
                model.getCaptura().set("");

                addProducto(model, captura);

            } catch (Exception e) {
                logger.error("Error durante captura ('evento onCaptura')", e);
            }
            capturaBloqueada = false;
        }
    }

    public void buscar(CajaModel model) {
        Pageable pagina = model.getPaginacionBusqueda();
        String descripcion = model.getCaptura().get();
        ArrayList<Producto> productos = (ArrayList<Producto>) productosDAO.findByDescripcionLike( "%"+descripcion+"%", pagina);
        ObservableList<ProductoModel> obsProductos = model.getProductos();

        if (pagina.getPageNumber()==0 )
            obsProductos.clear();
        else
            obsProductos.remove( obsProductos.size() - 1 ); //Remueve el renglón "Buscar más productos"

        for( Producto p : productos ) {
            ProductoModel productoModel = new ProductoModel();
            productoToProductoModel(p, productoModel);
            productoModel.setCantidad(new SimpleObjectProperty<BigDecimal>( new BigDecimal(1) ));

            obsProductos.add(productoModel);
        }
        if(obsProductos.size() > 0) obsProductos.add( new BuscarMasDummyProducto() );

        model.setPaginacionBusqueda(new PageRequest(pagina.getPageNumber()+1, pagina.getPageSize()));
    }


    private void addProducto(CajaModel model, LineaDeCaptura captura) {
        model.getProductos().clear(); // Borra resultados de la búsqueda integrada

        Producto producto = productosDAO.findByCodigo(captura.getCodigo()).get(0);
        /*Articulo producto = productoRepo.findByCodigo(captura.getCodigo()).get(0);*/

        ProductoModel productoModel = new ProductoModel();
        productoToProductoModel(producto, productoModel);
        productoModel.setCantidad(new SimpleObjectProperty<BigDecimal>(captura.getCantidad()));

        model.getVenta().add(productoModel);
    }

    private void productoToProductoModel(Producto producto, ProductoModel productoModel) {
        productoModel.getId().set( producto.getId() );
        productoModel.setCodigo(new SimpleStringProperty(producto.getCodigo()));
        productoModel.setPrecioBase(new SimpleObjectProperty<BigDecimal>(producto.getPrecio().getPrecioBase()));
        productoModel.setConcepto(new SimpleStringProperty(producto.getDescripcion()));
        productoModel.setDescuentos(new SimpleObjectProperty<BigDecimal>(producto.getPrecio().getDescuento()));
        productoModel.setImpuestos(new SimpleObjectProperty<BigDecimal>(producto.getPrecio().getImpuestos()));
        productoModel.setPrecio(new SimpleObjectProperty<BigDecimal>(producto.getPrecio().getPrecio()));
        productoModel.setProductoData(producto);
    }


    public void calcularCambio(CajaModel model )
    {
        BigDecimal efectivo = model.getEfectivo().get();
        BigDecimal total    = model.getTotal().get();
        BigDecimal cambio   = efectivo.subtract(total);
        model.getCambio().setValue( cambio );
    }

    @Transactional
    public void terminarVenta(CajaModel model) {

        BigDecimal ventaTotal = model.getTotal().get();
        if( ventaTotal.compareTo( new BigDecimal("0.10") ) > 0 ) {
            try {
                LegacyVenta venta = guardarVenta(model);
                imprimirVenta(venta);

                Dialog.showInfo("Venta registrada",
                        "Venta registrada");

                getController().setModel( new CajaModel() );
                getController().getCapturaTextField().requestFocus();
            } catch (Exception e) {
                logger.error("Error al guardar venta, venta no registrada.", e);
            }
        }

    }

    private void imprimirVenta(LegacyVenta venta) {
        comprobantes.ticketVenta(venta.getId()); //imprimir ticket
        comprobantes.imprimir();
    }

    @Transactional
    private LegacyVenta guardarVenta(CajaModel model) {
        Integer idCaja    = Principal.IDCaja;
        Integer idAlmacen = Principal.IDAlmacen;
        Integer idUsuario = Usuarios.getIDUsuarioActivo();
        Double  efectivo  = model.getEfectivo().get().doubleValue();
        Double  cambio    = model.getCambio().get().doubleValue();
        Date    fechaHora = (Date) entityManager.createNativeQuery("SELECT current_timestamp").getSingleResult();

        Integer folio     = asignarFolio(idCaja);

        LegacyVenta venta = new LegacyVenta();
        venta.setIdCliente ( 1 );
        venta.setIdUsuario(idUsuario);
        venta.setIdAlmacen(idAlmacen);
        venta.setIdCaja(idCaja);
        venta.setFolio(folio);
        venta.setEfectivo(efectivo);
        venta.setCambio(cambio);
        venta.setCentecimosredondeados(0d);
        venta.setCompletada(1);
        venta.setFacturada(0);
        venta.setFechaHora ( fechaHora );
        venta.setDescuento (model.getDescuento().get().doubleValue());
        venta.setSubtotal  (model.getSubtotal().get().doubleValue());
        venta.setImpuestos (model.getImpuestos().getValue().doubleValue());
        venta.setTotal     (model.getTotal().get().doubleValue());


        entityManager.persist(venta);

        for (ProductoModel producto : model.getVenta()) {
            LegacyVentaDetalle lvd = new LegacyVentaDetalle();
            lvd.setIdAlmacen ( idAlmacen );
            lvd.setIdArticulo( producto.getLongId().intValue() );
            lvd.setIdCaja    ( idCaja );
            lvd.setIdLinea   ( producto.getProductoData().getLineaByLineaId().getId() );
            lvd.setIdVenta   ( venta.getId() );
            lvd.setCantidad  ( producto.getCantidad().get().doubleValue() );
            lvd.setPrecio    ( producto.getPrecio().get().doubleValue() );
            lvd.setDescuento ( producto.getDescuentos().get().doubleValue() );
            lvd.setImpuestos ( producto.getImpuestos().get().doubleValue() );
            lvd.setSubtotal  ( producto.getSubtotal().doubleValue() );
            lvd.setTotal     ( producto.getImporte().doubleValue() );
            lvd.setTipoSalida( "" );

            entityManager.persist( lvd );
        }
        entityManager.flush();
        return venta;
    }

    public Integer asignarFolio(Integer idCaja) {
        //Query q = entityManager.createQuery("SELECT Caja.uFolio where id_caja = ?" );
        Caja caja = entityManager.find(Caja.class, idCaja);

        Integer folioActual = caja.getUFolio();
        folioActual++;
        caja.setUFolio( folioActual );
        return folioActual;
    }

    public void onProductListChanged(CajaModel model) {
        BigDecimal subtotal = new BigDecimal(0);
        subtotal.setScale(2, BigDecimal.ROUND_HALF_UP);

        BigDecimal descuentos = new BigDecimal(0);
        descuentos.setScale(2, BigDecimal.ROUND_HALF_UP);

        BigDecimal impuestos = new BigDecimal(0);
        impuestos.setScale(2, BigDecimal.ROUND_HALF_UP);

        BigDecimal total = new BigDecimal(0);
        total.setScale(2, BigDecimal.ROUND_HALF_UP);

        for ( ProductoModel producto : model.getVenta() ) {
            subtotal   = subtotal  .add( producto.getSubtotal() );
            descuentos = descuentos.add( producto.getDescuentos().get() );
            impuestos  = impuestos .add( producto.getImpuestos().get() );
        }

        total = total.add( subtotal );
        total = total.subtract( descuentos );
        total = total.add( impuestos );

        model.getSubtotal().set( subtotal );
        model.getDescuento().set( descuentos );
        model.getImpuestos().set( impuestos );
        model.getTotal().set( total );
    }

    @Override
    public void setController(CajaController cajaController) {
        this.controller = cajaController;
    }

    public CajaController getController() {
        return controller;
    }
}
