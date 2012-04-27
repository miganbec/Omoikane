package omoikane.entities;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

/**
 * Proyecto Omoikane: SmartPOS 2.0
 * User: octavioruizcastillo
 * Date: 14/07/11
 * Time: 04:04
 *
 */
//@IdClass(omoikane.entities.MovimientoCortePK.class)
//@Table(name = "movimiento_corte", catalog = "Omoikane")
//@Entity
public class MovimientoCorte {

    public enum Tipo {
            DEPOSITO, RETIRO, DEVOLUCION
    };

                private int corteIdCaja;

                private int corteSucursalId;

    @NotNull    private Tipo tipo;

    @NotNull    private Date fecha;

    @NotNull    private Time hora;

    @Min(0)     private BigDecimal importe;

    @Column(name = "corte_id_caja")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getCorteIdCaja() {
        return corteIdCaja;
    }

    public void setCorteIdCaja(int corteIdCaja) {
        this.corteIdCaja = corteIdCaja;
    }

    @Column(name = "corte_sucursal_id")
    @Id
    public int getCorteSucursalId() {
        return corteSucursalId;
    }

    public void setCorteSucursalId(int corteSucursalId) {
        this.corteSucursalId = corteSucursalId;
    }

    @Column(name = "tipo")
    @Basic
    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
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

    @Column(name = "importe")
    @Basic
    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovimientoCorte that = (MovimientoCorte) o;

        if (corteIdCaja != that.corteIdCaja) return false;
        if (corteSucursalId != that.corteSucursalId) return false;
        if (that.importe.compareTo(importe) != 0) return false;
        if (fecha != null ? !fecha.equals(that.fecha) : that.fecha != null) return false;
        if (hora != null ? !hora.equals(that.hora) : that.hora != null) return false;
        if (tipo != null ? !tipo.equals(that.tipo) : that.tipo != null) return false;

        return true;
    }

    private Usuario usuarioByCajeroId;

    @ManyToOne
    public
    @JoinColumn(name = "cajero_id", referencedColumnName = "id", nullable = false)
    Usuario getUsuarioByCajeroId() {
        return usuarioByCajeroId;
    }

    public void setUsuarioByCajeroId(Usuario usuarioByCajeroId) {
        this.usuarioByCajeroId = usuarioByCajeroId;
    }

    private Usuario usuarioByUsuarioId;

    @ManyToOne
    public
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    Usuario getUsuarioByUsuarioId() {
        return usuarioByUsuarioId;
    }

    public void setUsuarioByUsuarioId(Usuario usuarioByUsuarioId) {
        this.usuarioByUsuarioId = usuarioByUsuarioId;
    }

    private Corte corte;

    @OneToOne
    public
    @JoinColumns({@JoinColumn(name = "corte_id_caja", referencedColumnName = "id_caja", nullable = false), @JoinColumn(name = "corte_sucursal_id", referencedColumnName = "corte_sucursal_id", nullable = false)})
    Corte getCorte() {
        return corte;
    }

    public void setCorte(Corte corte) {
        this.corte = corte;
    }
}
