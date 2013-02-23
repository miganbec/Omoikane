package omoikane.entities;

import omoikane.producto.Articulo;
import omoikane.producto.Producto;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 14/07/11
 * Time: 04:04
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Paquete {
                private int id;

    @DecimalMin(value = "1")
    @NotNull
    private BigDecimal cantidad;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "cantidad")
    @Basic
    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Paquete paquete = (Paquete) o;

        if (cantidad != paquete.cantidad) return false;
        if (id != paquete.id) return false;


        return true;
    }

    @NotNull
    private Articulo productoContenedor;

    @ManyToOne
    public
    @JoinColumn(referencedColumnName = "id_articulo")
    Articulo getProductoContenedor() {
        return productoContenedor;
    }

    public void setProductoContenedor(Articulo productoByProductoId) {
        this.productoContenedor = productoByProductoId;
    }

    @NotNull
    private Articulo productoContenido;

    @ManyToOne(fetch = FetchType.EAGER)
    public
    @JoinColumn(referencedColumnName = "id_articulo")
    Articulo getProductoContenido() {
        return productoContenido;
    }

    public void setProductoContenido(Articulo productoByProductoId) {
        this.productoContenido = productoByProductoId;
    }
    @Transient
    public String getDescripcion() {
        return getProductoContenido().getDescripcion();
    }
    @Transient
    public BigDecimal getPrecio() {
        return getProductoContenido().getPrecio().getPrecio();
    }
    @Transient
    public String getPrecioString() {
        BigDecimal precio = getPrecio();
        NumberFormat nb = NumberFormat.getCurrencyInstance();
        nb.setMinimumFractionDigits(2);
        nb.setMaximumFractionDigits(2);
        return nb.format(precio.doubleValue());
    }
}
