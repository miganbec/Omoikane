/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package omoikane.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author octavioruizcastillo
 */
@Entity
@Table(name = "ventas")
@XmlRootElement

public class LegacyVenta implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;

    @Column(name = "id_caja")
    private int idCaja;

    @Column(name = "id_almacen")
    private int idAlmacen;

    @Column(name = "id_cliente")
    private Integer idCliente;

    @Basic(optional = false)
    @Column(name = "fecha_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    @Basic(optional = false)
    @Column(name = "uModificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uModificacion;
    @Column(name = "facturada")
    private Integer facturada;
    @Column(name = "completada")
    private Integer completada;
    @Basic(optional = false)
    @Lob
    @Column(name = "eliminar")
    private byte[] eliminar;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "subtotal")
    private Double subtotal;
    @Column(name = "descuento")
    private Double descuento;
    @Column(name = "impuestos")
    private Double impuestos;
    @Column(name = "total")
    private Double total;
    @Basic(optional = false)
    @Column(name = "id_usuario")
    private int idUsuario;
    @Basic(optional = false)
    @Column(name = "efectivo")
    private double efectivo;
    @Basic(optional = false)
    @Column(name = "cambio")
    private double cambio;
    @Basic(optional = false)
    @Column(name = "centecimosredondeados")
    private double centecimosredondeados;
    @Basic(optional = false)
    @Column(name = "folio")
    private long folio;

    public LegacyVenta() {
    }

    @Column(name = "id_venta")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "id_caja")
    public int getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }

    @Column(name = "id_almacen")
    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    @Column(name = "id_cliente")
    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    @Column(name = "fecha_hora")
    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Date getUModificacion() {
        return uModificacion;
    }

    public void setUModificacion(Date uModificacion) {
        this.uModificacion = uModificacion;
    }

    @PrePersist
    protected void onCreate() {
        setUModificacion( new Timestamp(Calendar.getInstance().getTime().getTime()) );
    }

    @PreUpdate
    protected void onUpdate() {
        setUModificacion(new Timestamp(Calendar.getInstance().getTime().getTime()));
    }

    public Integer getFacturada() {
        return facturada;
    }

    public void setFacturada(Integer facturada) {
        this.facturada = facturada;
    }

    public Integer getCompletada() {
        return completada;
    }

    public void setCompletada(Integer completada) {
        this.completada = completada;
    }

    public byte[] getEliminar() {
        return eliminar;
    }

    public void setEliminar(byte[] eliminar) {
        this.eliminar = eliminar;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Double getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(Double impuestos) {
        this.impuestos = impuestos;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Column(name = "id_usuario")
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public double getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(double efectivo) {
        this.efectivo = efectivo;
    }

    public double getCambio() {
        return cambio;
    }

    public void setCambio(double cambio) {
        this.cambio = cambio;
    }

    public double getCentecimosredondeados() {
        return centecimosredondeados;
    }

    public void setCentecimosredondeados(double centecimosredondeados) {
        this.centecimosredondeados = centecimosredondeados;
    }

    public long getFolio() {
        return folio;
    }

    public void setFolio(long folio) {
        this.folio = folio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += id;
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof LegacyVenta)) {
            return false;
        }
        LegacyVenta other = (LegacyVenta) object;
        if (this.id == other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.LegacyVenta[ legacyVentaPK=" + id + " ]";
    }


    
}
