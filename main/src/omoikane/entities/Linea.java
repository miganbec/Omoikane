package omoikane.entities;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;

/**
 * Proyecto Omoikane: SmartPOS 2.0
 * User: octavioruizcastillo
 * Date: 14/07/11
 * Time: 04:04
 *
 */
//@Entity
// **25/09/2012: Retrocompatibilidad ==(nuevo)linea --> lineas(antiguo)==
//@Table(name="lineas")
public class Linea {
    private int id;

    @NotEmpty   private String descripcion;

    @Min(0)     private BigDecimal descuento;

    @NotNull    private Timestamp creado;

    @NotNull    private Timestamp modificado;

    @PrePersist
    protected void onCreate() {
        creado = new Timestamp(Calendar.getInstance().getTime().getTime());
        modificado = new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    @PreUpdate
    protected void onUpdate() {
        modificado = new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    @Column(name = "id")
    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "descripcion", length = 65535)
    @Basic
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Column(name = "descuento")
    @Basic
    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    @Column(name = "creado")
    @Basic
    public Timestamp getCreado() {
        return creado;
    }

    public void setCreado(Timestamp creado) {
        this.creado = creado;
    }

    @Column(name = "modificado")
    @Basic
    public Timestamp getModificado() {
        return modificado;
    }

    public void setModificado(Timestamp umodificacion) {
        this.modificado = umodificacion;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Linea linea = (Linea) o;

        if (linea.descuento.compareTo(descuento) != 0) return false;
        if (id != linea.id) return false;
        if (descripcion != null ? !descripcion.equals(linea.descripcion) : linea.descripcion != null) return false;
        if (modificado != null ? !modificado.equals(linea.modificado) : linea.modificado != null)
            return false;
        if (creado != null ? !creado.equals(linea.creado) : linea.creado != null)
            return false;

        return true;
    }

    private Collection<Producto> productosByLineaId;

    @OneToMany(mappedBy = "lineaByLineaId")
    public Collection<Producto> getProductosByLineaId() {
        return productosByLineaId;
    }

    public void setProductosByLineaId(Collection<Producto> productosByLineaId) {
        this.productosByLineaId = productosByLineaId;
    }
}
