package omoikane.entities;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
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
//@IdClass(omoikane.entities.CortePK.class)
//@Entity
public class Corte {
    private int idCaja;

    private int corteSucursalId;

    @Min(0) private BigDecimal subtotal;

    @Min(0) private BigDecimal impuestos;

    @Min(0) private BigDecimal descuentos;

    @Min(0) private BigDecimal total;

    @Min(0) private int nVentas;

    @NotNull private Timestamp desde;

    private Timestamp hasta;

    @Min(0) private BigDecimal depositos;

    @Min(0) private BigDecimal retiros;

    private Date fecha;

    private Time hora;

    @NotNull private boolean abierto;

    @PrePersist
    protected void onCreate() {

        fecha = (Date) Calendar.getInstance().getTime();
        hora  = (Time) Calendar.getInstance().getTime();
    }

    @Column(name = "id_caja")
    @Id
    public int getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }

    @Column(name = "corte_sucursal_id")
    @Id
    public int getCorteSucursalId() {
        return corteSucursalId;
    }

    public void setCorteSucursalId(int corteSucursalId) {
        this.corteSucursalId = corteSucursalId;
    }

    @Column(name = "subtotal")
    @Basic
    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    @Column(name = "impuestos")
    @Basic
    public BigDecimal getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(BigDecimal impuestos) {
        this.impuestos = impuestos;
    }

    @Column(name = "descuentos")
    @Basic
    public BigDecimal getDescuentos() {
        return descuentos;
    }

    public void setDescuentos(BigDecimal descuentos) {
        this.descuentos = descuentos;
    }

    @Column(name = "total")
    @Basic
    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Column(name = "n_ventas")
    @Basic
    public int getnVentas() {
        return nVentas;
    }

    public void setnVentas(int nVentas) {
        this.nVentas = nVentas;
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

        Corte corte = (Corte) o;

        if (abierto != corte.abierto) return false;
        if (corteSucursalId != corte.corteSucursalId) return false;
        if (corte.depositos.compareTo(depositos) != 0) return false;
        if (corte.descuentos.compareTo(descuentos) != 0) return false;
        if (idCaja != corte.idCaja) return false;
        if (corte.impuestos.compareTo(impuestos) != 0) return false;
        if (nVentas != corte.nVentas) return false;
        if (corte.retiros.compareTo(retiros) != 0) return false;
        if (corte.subtotal.compareTo(subtotal) != 0) return false;
        if (corte.total.compareTo(total) != 0) return false;
        if (desde != null ? !desde.equals(corte.desde) : corte.desde != null) return false;
        if (fecha != null ? !fecha.equals(corte.fecha) : corte.fecha != null) return false;
        if (hasta != null ? !hasta.equals(corte.hasta) : corte.hasta != null) return false;
        if (hora != null ? !hora.equals(corte.hora) : corte.hora != null) return false;

        return true;
    }

    private Collection<Caja> cajas;

    @OneToMany(mappedBy = "corte")
    public Collection<Caja> getCajas() {
        return cajas;
    }

    public void setCajas(Collection<Caja> cajas) {
        this.cajas = cajas;
    }

    private CorteSucursal corteSucursalByCorteSucursalId;

    @ManyToOne
    public
    @JoinColumn(name = "corte_sucursal_id", referencedColumnName = "id", insertable=false, updatable=false)
    CorteSucursal getCorteSucursalByCorteSucursalId() {
        return corteSucursalByCorteSucursalId;
    }

    public void setCorteSucursalByCorteSucursalId(CorteSucursal corteSucursalByCorteSucursalId) {
        this.corteSucursalByCorteSucursalId = corteSucursalByCorteSucursalId;
    }

    private MovimientoCorte movimientoCorte;

    @OneToOne(mappedBy = "corte")
    public MovimientoCorte getMovimientoCorte() {
        return movimientoCorte;
    }

    public void setMovimientoCorte(MovimientoCorte movimientoCorte) {
        this.movimientoCorte = movimientoCorte;
    }

    private Collection<Venta> ventas;

    @OneToMany(mappedBy = "corte")
    public Collection<Venta> getVentas() {
        return ventas;
    }

    public void setVentas(Collection<Venta> ventas) {
        this.ventas = ventas;
    }

}
