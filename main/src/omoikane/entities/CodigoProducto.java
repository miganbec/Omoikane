package omoikane.entities;

import omoikane.producto.Articulo;
import omoikane.producto.Producto;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 14/07/11
 * Time: 04:04
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "codigo_producto")
public class CodigoProducto {
    private Long id;

    @Column(name = "id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0, columnDefinition = "int(11)")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String codigo;

    @Column(name = "codigo", nullable = false, insertable = true, updatable = true, length = 255, precision = 0)
    @Basic
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    private Articulo producto;

    @ManyToOne
    public
    @JoinColumn(referencedColumnName = "id_articulo")
    Articulo getProducto() {
        return producto;
    }

    public void setProducto(Articulo producto) {
        this.producto = producto;
    }
}
