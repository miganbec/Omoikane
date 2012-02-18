package omoikane.entities;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 14/07/11
 * Time: 04:04
 * To change this template use File | Settings | File Templates.
 */
@Table(name = "movimiento_almacen_detalle", catalog = "Omoikane")
@Entity
public class MovimientoAlmacenDetalle {

                        private int id;

    @NotNull            private int movimientoId;

    @Min(1)             private BigDecimal cantidad;

    @DecimalMin("0.01") private BigDecimal costo;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "movimiento_id")
    @Basic
    public int getMovimientoId() {
        return movimientoId;
    }

    public void setMovimientoId(int movimientoId) {
        this.movimientoId = movimientoId;
    }

    @Column(name = "cantidad")
    @Basic
    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    @Column(name = "costo")
    @Basic
    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovimientoAlmacenDetalle that = (MovimientoAlmacenDetalle) o;

        if (that.cantidad.compareTo(cantidad) != 0) return false;
        if (that.costo.compareTo( costo) != 0) return false;
        if (id != that.id) return false;
        if (movimientoId != that.movimientoId) return false;

        return true;
    }

    private MovimientoAlmacen movimientoAlmacenByProductoId;

    @ManyToOne
    public
    @JoinColumn(name = "producto_id", referencedColumnName = "id", nullable = false)
    MovimientoAlmacen getMovimientoAlmacenByProductoId() {
        return movimientoAlmacenByProductoId;
    }

    public void setMovimientoAlmacenByProductoId(MovimientoAlmacen movimientoAlmacenByProductoId) {
        this.movimientoAlmacenByProductoId = movimientoAlmacenByProductoId;
    }

    private Producto productoByProductoId;

    @ManyToOne
    public
    @JoinColumn(name = "producto_id", referencedColumnName = "id", nullable = false, insertable=false, updatable=false)
    Producto getProductoByProductoId() {
        return productoByProductoId;
    }

    public void setProductoByProductoId(Producto productoByProductoId) {
        this.productoByProductoId = productoByProductoId;
    }
}
