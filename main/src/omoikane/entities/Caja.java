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
@Entity
// **15/11/2012: Retrocompatibilidad ==(nuevo)caja --> cajas(antiguo)==
@Table(name="cajas")
public class Caja {

    private int id;

    private int idAlmacen;

    @NotBlank
    private String descripcion;

    @NotNull
    private Timestamp creado;
    @NotNull
    private Timestamp umodificacion;
    private Timestamp horaAbierta;
    private Timestamp horaCerrada;
    private boolean abierta;
    private Integer uFolio;

    @PrePersist
    protected void onCreate() {
        creado      = new Timestamp(Calendar.getInstance().getTime().getTime());
        umodificacion = new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    @PreUpdate
    protected void onUpdate() {
        umodificacion = new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    @Column(name = "id_caja")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "id_almacen")
    public int getIdAlmacen() {
        return idAlmacen;
    }

    @Deprecated
    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    @Column(name = "descripcion", length = 128)
    @Basic
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Column(name = "creado")
    @Basic
    public Timestamp getCreado() {
        return creado;
    }

    public void setCreado(Timestamp creado) {
        this.creado = creado;
    }

    @Column(name = "uModificacion")
    @Basic
    public Timestamp getUmodificacion() {
        return umodificacion;
    }

    public void setUmodificacion(Timestamp umodificacion) {
        this.umodificacion = umodificacion;
    }

    @Deprecated
    @Column(name = "horaabierta")
    @Basic
    public Timestamp getHoraAbierta() {
        return horaAbierta;
    }

    @Deprecated
    public void setHoraAbierta(Timestamp horaAbierta) {
        this.horaAbierta = horaAbierta;
    }

    @Deprecated
    @Column(name = "horacerrada")
    @Basic
    public Timestamp getHoraCerrada() {
        return horaCerrada;
    }

    @Deprecated
    public void setHoraCerrada(Timestamp horaCerrada) {
        this.horaCerrada = horaCerrada;
    }

    @Deprecated
    @Column(name = "abierta")
    @Basic
    public boolean getAbierta() {
        return abierta;
    }

    @Deprecated
    public void setAbierta(boolean abierta) {
        this.abierta = abierta;
    }

    @Deprecated
    @Column(name = "uFolio")
    @Basic
    public Integer getUFolio() {
        return uFolio;
    }

    @Deprecated
    public void setUFolio(Integer uFolio) {
        this.uFolio = uFolio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Caja caja = (Caja) o;

        if (id != caja.id) return false;
        if (creado != null ? !creado.equals(caja.creado) : caja.creado != null) return false;
        if (descripcion != null ? !descripcion.equals(caja.descripcion) : caja.descripcion != null) return false;
        if (umodificacion != null ? !umodificacion.equals(caja.umodificacion) : caja.umodificacion != null)
            return false;

        return true;
    }

    /*
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
    */
}
