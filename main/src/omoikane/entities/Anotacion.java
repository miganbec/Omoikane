package omoikane.entities;

import omoikane.producto.Producto;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 14/07/11
 * Time: 04:04
 * To change this template use File | Settings | File Templates.
 */
//@Entity
public class Anotacion {

    @NotNull
    private int productoId;

    @NotNull
    private String texto;

    @Column(name = "producto_id")
    @Id
    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    @Column(name = "texto", length = 65535)
    @Basic
    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Anotacion anotacion = (Anotacion) o;

        if (productoId != anotacion.productoId) return false;
        if (texto != null ? !texto.equals(anotacion.texto) : anotacion.texto != null) return false;

        return true;
    }

    private Producto productoByProductoId;

    @OneToOne
    public
    @JoinColumn(name = "producto_id", referencedColumnName = "id", nullable = false, table = "anotacion")
    Producto getProductoByProductoId() {
        return productoByProductoId;
    }

    public void setProductoByProductoId(Producto productoByProductoId) {
        this.productoByProductoId = productoByProductoId;
    }
}
