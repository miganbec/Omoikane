package omoikane.entities;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 14/07/11
 * Time: 04:04
 * To change this template use File | Settings | File Templates.
 */
//@Entity
public class Caja {

    private int id;

    @NotBlank
    private String descripcion;

    @NotNull
    private Timestamp creacion;

    @NotNull
    private Timestamp umodificacion;

    @PrePersist
    protected void onCreate() {
        creacion      = new Timestamp(Calendar.getInstance().getTime().getTime());
        umodificacion = new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    @PreUpdate
    protected void onUpdate() {
        umodificacion = new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "descripcion", length = 128)
    @Basic
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Column(name = "creacion")
    @Basic
    public Timestamp getCreacion() {
        return creacion;
    }

    public void setCreacion(Timestamp creacion) {
        this.creacion = creacion;
    }

    @Column(name = "uModificacion")
    @Basic
    public Timestamp getUmodificacion() {
        return umodificacion;
    }

    public void setUmodificacion(Timestamp umodificacion) {
        this.umodificacion = umodificacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Caja caja = (Caja) o;

        if (id != caja.id) return false;
        if (creacion != null ? !creacion.equals(caja.creacion) : caja.creacion != null) return false;
        if (descripcion != null ? !descripcion.equals(caja.descripcion) : caja.descripcion != null) return false;
        if (umodificacion != null ? !umodificacion.equals(caja.umodificacion) : caja.umodificacion != null)
            return false;

        return true;
    }

    private Corte corte;

    @ManyToOne
    public
    @JoinColumns({@JoinColumn(name = "id", referencedColumnName = "id_caja", insertable = false, updatable = false),
                  @JoinColumn(name = "corte_sucursal_id", referencedColumnName = "corte_sucursal_id", insertable=false, updatable=false)})
    Corte getCorte() {
        return corte;
    }

    public void setCorte(Corte corte) {
        this.corte = corte;
    }

    private Collection<Venta> ventasById;

    @OneToMany(mappedBy = "cajaByCajaId")
    public Collection<Venta> getVentasById() {
        return ventasById;
    }

    public void setVentasById(Collection<Venta> ventasById) {
        this.ventasById = ventasById;
    }

    private Collection<VentaDetalle> ventaDetallesById;

    @OneToMany(mappedBy = "cajaByCajaId")
    public Collection<VentaDetalle> getVentaDetallesById() {
        return ventaDetallesById;
    }

    public void setVentaDetallesById(Collection<VentaDetalle> ventaDetallesById) {
        this.ventaDetallesById = ventaDetallesById;
    }
}
