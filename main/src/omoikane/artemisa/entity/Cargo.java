package omoikane.artemisa.entity;

import omoikane.producto.Articulo;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 25/07/13
 * Time: 01:43 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Cargo extends Transaccion {

    @Column
    private
    BigDecimal cantidad;

    @Transient
    private
    BigDecimal total;

    @ManyToOne
    private
    Articulo producto;

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public Articulo getProducto() {
        return producto;
    }

    public void setProducto(Articulo producto) throws Exception {
        if(getCantidad() == null || getCantidad().equals(new BigDecimal(0))) throw new Exception("No se ha definido una cantidad");
        this.producto = producto;
        setConcepto(getCantidad()+ " x " + producto.getDescripcion());
        setCargo(cantidad.multiply(producto.getPrecio().getPrecio()));

    }

    public BigDecimal getTotal() {
        if(producto == null || total == null) new BigDecimal(0);
        total = cantidad.multiply(producto.getPrecio().getPrecio());
        return total;
    }
}
