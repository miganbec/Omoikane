package omoikane.entities;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 14/07/11
 * Time: 04:04
 */
@Entity
public class Grupo {
    private int id;

    @NotEmpty private String descripcion;

    @Min(0)   private BigDecimal descuento;

    @NotNull  private Timestamp creado;

    @NotNull  private Timestamp modificado;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "descripcion")
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

    @Column(name = "modificado")
    @Basic
    public Timestamp getModificado() {
        return modificado;
    }

    public void setModificado(Timestamp modificado) {
        this.modificado = modificado;
    }

    @Column(name = "creado")
    @Basic
    public Timestamp getCreado() {
        return creado;
    }

    public void setCreado(Timestamp creado) {
        this.creado = creado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Grupo grupo = (Grupo) o;

        if (grupo.descuento.compareTo(descuento) != 0) return false;
        if (id != grupo.id) return false;
        if (descripcion != null ? !descripcion.equals(grupo.descripcion) : grupo.descripcion != null) return false;
        if (creado != null ? !creado.equals(grupo.creado) : grupo.creado != null)
            return false;
        if (modificado != null ? !modificado.equals(grupo.modificado) : grupo.modificado != null)
            return false;

        return true;
    }

    private Collection<Producto> productosByGrupoId;

    @OneToMany(mappedBy = "grupoByGrupoId")
    public Collection<Producto> getProductosByGrupoId() {
        return productosByGrupoId;
    }

    public void setProductosByGrupoId(Collection<Producto> productosByGrupoId) {
        this.productosByGrupoId = productosByGrupoId;
    }
}
