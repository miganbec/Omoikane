package omoikane.entities;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 14/07/11
 * Time: 04:04
 * To change this template use File | Settings | File Templates.
 */
@javax.persistence.Table(name = "codigo_producto", catalog = "Omoikane")
@Entity
public class CodigoProducto {
    private Long id;

    @Column(name = "id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private int version;

    @Column(name = "version", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CodigoProducto that = (CodigoProducto) o;

        if (id != that.id) return false;
        if (version != that.version) return false;
        if (codigo != null ? !codigo.equals(that.codigo) : that.codigo != null) return false;

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
}
