package omoikane.caja.presentation;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.synyx.hades.domain.Pageable;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 12/09/12
 * Time: 10:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class CajaModel {
    private StringProperty captura;
    private ObservableList<ProductoModel> venta;
    private ObservableList<ProductoModel> productos;
    private ObjectProperty<BigDecimal> subtotal;
    private ObjectProperty<BigDecimal> descuento;
    private ObjectProperty<BigDecimal> impuestos;
    private ObjectProperty<BigDecimal> total;
    private Pageable paginacionBusqueda;

    /**
     * Instancía todos los atributos
     */
    public CajaModel() {
        setCaptura(new SimpleStringProperty());
        descuento   = new SimpleObjectProperty<BigDecimal>( new BigDecimal( 0 ) );
        impuestos   = new SimpleObjectProperty<BigDecimal>( new BigDecimal( 0 ) );
        subtotal    = new SimpleObjectProperty<BigDecimal>( new BigDecimal( 0 ) );
        total       = new SimpleObjectProperty<BigDecimal>( new BigDecimal( 0 ) );

        getDescuento().get().setScale( 2, BigDecimal.ROUND_HALF_UP );
        getImpuestos().get().setScale( 2, BigDecimal.ROUND_HALF_UP );
        getSubtotal ().get().setScale( 2, BigDecimal.ROUND_HALF_UP );
        getTotal    ().get().setScale( 2, BigDecimal.ROUND_HALF_UP );

        ObservableList<ProductoModel> list = FXCollections.observableArrayList();
        setVenta(list);
        ObservableList<ProductoModel> productos = FXCollections.observableArrayList();
        setProductos(productos);
    }

    public StringProperty getCaptura() {
        return captura;
    }

    public void setCaptura(StringProperty captura) {
        this.captura = captura;
    }


    public ObservableList<ProductoModel> getVenta() {
        return venta;
    }

    public void setVenta(ObservableList<ProductoModel> venta) {
        this.venta = venta;
    }

    public ObservableList<ProductoModel> getProductos() {
        return productos;
    }

    public void setProductos(ObservableList<ProductoModel> productos) {
        this.productos = productos;
    }

    public ObjectProperty<BigDecimal> getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal.set( subtotal );
    }

    public ObjectProperty<BigDecimal> getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento.set( descuento );
    }

    public ObjectProperty<BigDecimal> getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(BigDecimal impuestos) {
        this.impuestos.set( impuestos );
    }

    public ObjectProperty<BigDecimal> getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total.set( total );
    }

    public Pageable getPaginacionBusqueda() {
        return paginacionBusqueda;
    }

    public void setPaginacionBusqueda(Pageable paginacionBusqueda) {
        this.paginacionBusqueda = paginacionBusqueda;
    }
}
