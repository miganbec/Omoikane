package omoikane.entities;

import omoikane.producto.Producto;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 14/07/11
 * Time: 04:04
 * To change this template use File | Settings | File Templates.
 */
//@Table(name = "venta_detalle", catalog = "Omoikane")
//@Entity
public class VentaDetalle {
    private int id;

    @Column(name = "id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private double precio;

    @Column(name = "precio", nullable = false, insertable = true, updatable = true, length = 22, precision = 0)
    @Basic
    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    private double cantidad;

    @Column(name = "cantidad", nullable = true, insertable = true, updatable = true, length = 22, precision = 0)
    @Basic
    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    private String tipoSalida;

    @Column(name = "tipo_salida", nullable = true, insertable = true, updatable = true, length = 3, precision = 0)
    @Basic
    public String getTipoSalida() {
        return tipoSalida;
    }

    public void setTipoSalida(String tipoSalida) {
        this.tipoSalida = tipoSalida;
    }

    private double subtotal;

    @Column(name = "subtotal", nullable = true, insertable = true, updatable = true, length = 22, precision = 0)
    @Basic
    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    private double impuestos;

    @Column(name = "impuestos", nullable = true, insertable = true, updatable = true, length = 22, precision = 0)
    @Basic
    public double getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(double impuestos) {
        this.impuestos = impuestos;
    }

    private double descuento;

    @Column(name = "descuento", nullable = true, insertable = true, updatable = true, length = 22, precision = 0)
    @Basic
    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    private double total;

    @Column(name = "total", nullable = true, insertable = true, updatable = true, length = 22, precision = 0)
    @Basic
    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VentaDetalle that = (VentaDetalle) o;

        if (Double.compare(that.cantidad, cantidad) != 0) return false;
        if (Double.compare(that.descuento, descuento) != 0) return false;
        if (id != that.id) return false;
        if (Double.compare(that.impuestos, impuestos) != 0) return false;
        if (Double.compare(that.precio, precio) != 0) return false;
        if (Double.compare(that.subtotal, subtotal) != 0) return false;
        if (Double.compare(that.total, total) != 0) return false;
        if (tipoSalida != null ? !tipoSalida.equals(that.tipoSalida) : that.tipoSalida != null) return false;

        return true;
    }

    private Producto productoByProductoId;

    @ManyToOne
    public
    @JoinColumn(name = "producto_id", referencedColumnName = "id", nullable = false)
    Producto getProductoByProductoId() {
        return productoByProductoId;
    }

    public void setProductoByProductoId(Producto productoByProductoId) {
        this.productoByProductoId = productoByProductoId;
    }

    private Caja cajaByCajaId;

    @ManyToOne
    public
    @JoinColumn(name = "caja_id", referencedColumnName = "id", nullable = false)
    Caja getCajaByCajaId() {
        return cajaByCajaId;
    }

    public void setCajaByCajaId(Caja cajaByCajaId) {
        this.cajaByCajaId = cajaByCajaId;
    }

    private Venta ventaByVentaId;

    @ManyToOne
    public
    @JoinColumn(name = "venta_id", referencedColumnName = "id", nullable = false)
    Venta getVentaByVentaId() {
        return ventaByVentaId;
    }

    public void setVentaByVentaId(Venta ventaByVentaId) {
        this.ventaByVentaId = ventaByVentaId;
    }
}
