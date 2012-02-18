package omoikane.entities;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 14/07/11
 * Time: 04:04
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Venta {
                    private int id;

    @NotNull        private int usuarioId;

    @NotNull        private Timestamp modificado;

    @NotNull        private Timestamp creado;

    @Min(0) @Max(1) private int facturada;

    @Min(0) @Max(1) private int completada;

                    private byte[] eliminar;

    @Min(0)         private BigDecimal subtotal;

    @Min(0)         private BigDecimal descuento;

    @Min(0)         private BigDecimal impuestos;

    @Min(0)         private BigDecimal total;

    @Min(0)         private BigDecimal efectivo;

    @Min(0)         private BigDecimal cambio;

    @Min(0)         private long folio;

    @NotNull        private Date fecha;

    @NotNull        private Time hora;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "usuario_id")
    @Basic
    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
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

    @Column(name = "facturada")
    @Basic
    public int getFacturada() {
        return facturada;
    }

    public void setFacturada(int facturada) {
        this.facturada = facturada;
    }

    @Column(name = "completada")
    @Basic
    public int getCompletada() {
        return completada;
    }

    public void setCompletada(int completada) {
        this.completada = completada;
    }

    @Column(name = "eliminar")
    @Basic
    public byte[] getEliminar() {
        return eliminar;
    }

    public void setEliminar(byte[] eliminar) {
        this.eliminar = eliminar;
    }

    @Column(name = "subtotal")
    @Basic
    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    @Column(name = "descuento")
    @Basic
    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    @Column(name = "impuestos")
    @Basic
    public BigDecimal getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(BigDecimal impuestos) {
        this.impuestos = impuestos;
    }

    @Column(name = "total")
    @Basic
    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Column(name = "efectivo")
    @Basic
    public BigDecimal getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(BigDecimal efectivo) {
        this.efectivo = efectivo;
    }

    @Column(name = "cambio")
    @Basic
    public BigDecimal getCambio() {
        return cambio;
    }

    public void setCambio(BigDecimal cambio) {
        this.cambio = cambio;
    }

    @Column(name = "folio")
    @Basic
    public long getFolio() {
        return folio;
    }

    public void setFolio(long folio) {
        this.folio = folio;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Venta venta = (Venta) o;

        if (venta.cambio.compareTo(cambio) != 0) return false;
        if (completada != venta.completada) return false;
        if (venta.descuento.compareTo(descuento) != 0) return false;
        if (venta.efectivo.compareTo(efectivo) != 0) return false;
        if (facturada != venta.facturada) return false;
        if (folio != venta.folio) return false;
        if (venta.impuestos.compareTo(impuestos) != 0) return false;
        if (venta.subtotal.compareTo(subtotal) != 0) return false;
        if (venta.total.compareTo(total) != 0) return false;
        if (usuarioId != venta.usuarioId) return false;
        if (id != venta.id) return false;
        if (!Arrays.equals(eliminar, venta.eliminar)) return false;
        if (fecha != null ? !fecha.equals(venta.fecha) : venta.fecha != null) return false;
        if (hora != null ? !hora.equals(venta.hora) : venta.hora != null) return false;
        if (modificado != null ? !modificado.equals(venta.modificado) : venta.modificado != null)
            return false;

        return true;
    }

    private Caja cajaByCajaId;

    @ManyToOne
    public
    @JoinColumn(name = "caja_id", referencedColumnName = "id", insertable=false, updatable=false)
    Caja getCajaByCajaId() {
        return cajaByCajaId;
    }

    public void setCajaByCajaId(Caja cajaByCajaId) {
        this.cajaByCajaId = cajaByCajaId;
    }

    private Cliente clienteByClienteId;

    @ManyToOne
    public
    @JoinColumn(name = "cliente_id", referencedColumnName = "cliente_id")
    Cliente getClienteByClienteId() {
        return clienteByClienteId;
    }

    public void setClienteByClienteId(Cliente clienteByClienteId) {
        this.clienteByClienteId = clienteByClienteId;
    }

    private Corte corte;

    @ManyToOne
    public
    @JoinColumns({@JoinColumn(name = "caja_id", referencedColumnName = "id_caja"),
                  @JoinColumn(name = "corte_sucursal_id", referencedColumnName = "corte_sucursal_id")})
    Corte getCorte() {
        return corte;
    }

    public void setCorte(Corte corte) {
        this.corte = corte;
    }

    private Collection<VentaDetalle> ventaDetallesByVentaId;

    @OneToMany(mappedBy = "ventaByVentaId")
    public Collection<VentaDetalle> getVentaDetallesByVentaId() {
        return ventaDetallesByVentaId;
    }

    public void setVentaDetallesByVentaId(Collection<VentaDetalle> ventaDetallesByVentaId) {
        this.ventaDetallesByVentaId = ventaDetallesByVentaId;
    }

    private Collection<VentaEspecial> ventaEspecialsByVentaId;

    @OneToMany(mappedBy = "ventaByIdVenta")
    public Collection<VentaEspecial> getVentaEspecialsByVentaId() {
        return ventaEspecialsByVentaId;
    }

    public void setVentaEspecialsByVentaId(Collection<VentaEspecial> ventaEspecialsByVentaId) {
        this.ventaEspecialsByVentaId = ventaEspecialsByVentaId;
    }

    private VentaFacturada ventaFacturadaByVentaId;

    @OneToOne(mappedBy = "ventaByVentaId")
    public VentaFacturada getVentaFacturadaByVentaId() {
        return ventaFacturadaByVentaId;
    }

    public void setVentaFacturadaByVentaId(VentaFacturada ventaFacturadaByVentaId) {
        this.ventaFacturadaByVentaId = ventaFacturadaByVentaId;
    }
}
