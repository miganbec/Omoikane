package omoikane.entities;

import org.hibernate.cache.impl.bridge.EntityRegionAdapter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
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
//@Table(name = "movimiento_almacen", catalog = "Omoikane")
//@Entity
public class MovimientoAlmacen {

    public enum Tipo {
            ENTRADA, SALIDA, AJUSTE
    };

                                    private int id;

    @NotEmpty                       private String descripcion;

    @Enumerated(EnumType.STRING)    private Tipo tipo;

    @NotNull                        private BigDecimal monto;

    @NotNull                        private Date fecha;

    @NotNull                        private Time hora;

    @NotNull                        private String folio;

    @NotNull                        private Timestamp creado;

    @NotNull                        private Timestamp modificado;

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

    @Column(name = "descripcion", length = 65535)
    @Basic
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Column(name = "tipo", length = 65535)
    @Basic
    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    @Column(name = "monto")
    @Basic
    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    @Column(name = "fecha")
    @Basic
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Column(name = "hora")
    @Basic
    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    @Column(name = "folio", length = 65535)
    @Basic
    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    @Column(name = "creado")
    @Basic
    public Timestamp getCreado() {
        return creado;
    }

    public void setCreado(Timestamp creacion) {
        this.creado = creacion;
    }

    @Column(name = "modificado")
    @Basic
    public Timestamp getModificado() {
        return modificado;
    }

    public void setModificado(Timestamp modificado) {
        this.modificado = modificado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovimientoAlmacen that = (MovimientoAlmacen) o;

        if (id != that.id) return false;
        if (that.monto.compareTo(monto) != 0) return false;
        if (creado != null ? !creado.equals(that.creado) : that.creado != null) return false;
        if (modificado != null ? !modificado.equals(that.modificado) : that.modificado != null) return false;
        if (descripcion != null ? !descripcion.equals(that.descripcion) : that.descripcion != null) return false;
        if (fecha != null ? !fecha.equals(that.fecha) : that.fecha != null) return false;
        if (folio != null ? !folio.equals(that.folio) : that.folio != null) return false;
        if (hora != null ? !hora.equals(that.hora) : that.hora != null) return false;
        if (tipo != null ? !tipo.equals(that.tipo) : that.tipo != null) return false;

        return true;
    }

    private Collection<MovimientoAlmacenDetalle> movimientoAlmacenDetallesById;

    @OneToMany(mappedBy = "movimientoAlmacenByProductoId")
    public Collection<MovimientoAlmacenDetalle> getMovimientoAlmacenDetallesById() {
        return movimientoAlmacenDetallesById;
    }

    public void setMovimientoAlmacenDetallesById(Collection<MovimientoAlmacenDetalle> movimientoAlmacenDetallesById) {
        this.movimientoAlmacenDetallesById = movimientoAlmacenDetallesById;
    }
}
