package omoikane.entities;


import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * Un registro sucursal comienza con el atributo abierto igual a true
 * Created by IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 14/07/11
 * Time: 04:04
 */
//@Table(name = "corte_sucursal", catalog = "Omoikane")
//@Entity
public class CorteSucursal  {
    private Long id;

    private Timestamp desde;

    private Timestamp hasta;

    @Min(0) private BigDecimal depositos;

    @Min(0) private BigDecimal retiros;

    private Date creacion;

    private Date umodificacion;

    @NotNull
    private boolean abierto;

    @PrePersist
    protected void onCreate() {
        umodificacion = new Date();
        creacion      = new Date();
        abierto       = true;
        depositos     = new BigDecimal(0);
        retiros       = new BigDecimal(0);
    }

    @PreUpdate
    protected void onUpdate() {
        umodificacion = new Date();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "uModificacion")
    public Date getUmodificacion() {
        return umodificacion;
    }

    public void setUmodificacion(Date umodificacion) {
        this.umodificacion = umodificacion;
    }


    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creacion")
    public Date getCreacion() {
        return creacion;
    }

    public void setCreacion(Date creacion) {
        this.creacion = creacion;
    }

    @Column(name = "desde")
    @Basic
    public Timestamp getDesde() {
        return desde;
    }

    public void setDesde(Timestamp desde) {
        this.desde = desde;
    }

    @Column(name = "hasta")
    @Basic
    public Timestamp getHasta() {
        return hasta;
    }

    public void setHasta(Timestamp hasta) {
        this.hasta = hasta;
    }

    @Column(name = "depositos")
    @Basic
    public BigDecimal getDepositos() {
        return depositos;
    }

    public void setDepositos(BigDecimal depositos) {
        this.depositos = depositos;
    }

    @Column(name = "retiros")
    @Basic
    public BigDecimal getRetiros() {
        return retiros;
    }

    public void setRetiros(BigDecimal retiros) {
        this.retiros = retiros;
    }

    @Column(name = "abierto")
    @Basic
    public boolean isAbierto() {
        return abierto;
    }

    public void setAbierto(boolean abierto) {
        this.abierto = abierto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CorteSucursal that = (CorteSucursal) o;

        if (that.depositos.compareTo(depositos) != 0) return false;
        if (id != that.id) return false;
        if (that.retiros.compareTo(retiros) != 0) return false;
        if (creacion != null ? !creacion.equals(that.creacion) : that.creacion != null) return false;
        if (desde != null ? !desde.equals(that.desde) : that.desde != null) return false;
        if (hasta != null ? !hasta.equals(that.hasta) : that.hasta != null) return false;
        if (umodificacion != null ? !umodificacion.equals(that.umodificacion) : that.umodificacion != null)
            return false;

        return true;
    }

    private Collection<Corte> cortesById;

    @OneToMany(mappedBy = "corteSucursalByCorteSucursalId")
    public Collection<Corte> getCortesById() {
        return cortesById;
    }
    public void setCortesById(Collection<Corte> cortesById) {
        this.cortesById = cortesById;
    }

}
