/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package omoikane.entities;

import omoikane.inventarios.ItemConteoInventario;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

    @Column(name = "id_venta")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
    private Boolean completada;

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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "venta")
    List<LegacyVentaDetalle> items;

    public void addItem(LegacyVentaDetalle item) {
        if(getItems() == null) {
            setItems(new ArrayList<LegacyVentaDetalle>());
        }
        items.add(item);
        item.setVenta(this);
    }

    public LegacyVenta() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

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

    public Boolean getCompletada() {
        return completada;
    }

    public void setCompletada(Boolean completada) {
        this.completada = completada;
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

    public List<LegacyVentaDetalle> getItems() { return items; }

    public void setItems(List<LegacyVentaDetalle> lvd) { items = lvd; }

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
