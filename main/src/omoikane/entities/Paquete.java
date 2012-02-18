package omoikane.entities;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;

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

    @Min(1)     private int cantidad;

    @NotEmpty   private String codigo;

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
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Column(name = "codigo")
    @Basic
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Paquete paquete = (Paquete) o;

        if (cantidad != paquete.cantidad) return false;
        if (id != paquete.id) return false;
        if (codigo != null ? !codigo.equals(paquete.codigo) : paquete.codigo != null) return false;

        return true;
    }

    private Producto productoByProductoId;

    @ManyToOne
    public
    @JoinColumn(name = "producto_id", referencedColumnName = "id")
    Producto getProductoByProductoId() {
        return productoByProductoId;
    }

    public void setProductoByProductoId(Producto productoByProductoId) {
        this.productoByProductoId = productoByProductoId;
    }
}
