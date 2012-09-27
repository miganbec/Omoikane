package omoikane.entities;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
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
 * To change this template use File | Settings | File Templates.
 */
//@Entity
public class Producto {
                private int id;

    @NotEmpty   private String codigo;

    @NotEmpty   private String descripcion;

    @NotEmpty   private String unidad;

    @Min(0)     private BigDecimal impuestos;

    @NotNull    private Timestamp modificado;

    @NotEmpty   private Timestamp creado;

    @Min(0)     private BigDecimal costo;

    @Min(0)     private BigDecimal descuento;

    @Min(0)     private BigDecimal utilidad;

    @Min(0)     private BigDecimal existencia;

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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "codigo", length = 64)
    @Basic
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Column(name = "descripcion", length = 64)
    @Basic
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Column(name = "unidad", length = 8)
    @Basic
    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    @Column(name = "impuestos")
    @Basic
    public BigDecimal getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(BigDecimal impuestos) {
        this.impuestos = impuestos;
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

    @Column(name = "costo")
    @Basic
    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    @Column(name = "descuento")
    @Basic
    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    @Column(name = "utilidad")
    @Basic
    public BigDecimal getUtilidad() {
        return utilidad;
    }

    public void setUtilidad(BigDecimal utilidad) {
        this.utilidad = utilidad;
    }

    @Column(name = "existencia")
    @Basic
    public BigDecimal getExistencia() {
        return existencia;
    }

    public void setExistencia(BigDecimal existencia) {
        this.existencia = existencia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Producto producto = (Producto) o;

        if (producto.costo.compareTo(costo) != 0) return false;
        if (producto.descuento.compareTo(descuento) != 0) return false;
        if (producto.existencia.compareTo(existencia) != 0) return false;
        if (id != producto.id) return false;
        if (producto.impuestos.compareTo(impuestos) != 0) return false;
        if (producto.utilidad.compareTo(utilidad) != 0) return false;
        if (codigo != null ? !codigo.equals(producto.codigo) : producto.codigo != null) return false;
        if (descripcion != null ? !descripcion.equals(producto.descripcion) : producto.descripcion != null)
            return false;
        if (modificado != null ? !modificado.equals(producto.modificado) : producto.modificado != null)
            return false;
        if (unidad != null ? !unidad.equals(producto.unidad) : producto.unidad != null) return false;

        return true;
    }

    private Anotacion anotacionById;

    @OneToOne(mappedBy = "productoByProductoId")
    public Anotacion getAnotacionById() {
        return anotacionById;
    }

    public void setAnotacionById(Anotacion anotacionById) {
        this.anotacionById = anotacionById;
    }

    private Collection<CodigoProducto> codigosAlternos;

    @OneToMany(mappedBy = "producto")
    public Collection<CodigoProducto> getCodigosAlternos() {
        return codigosAlternos;
    }

    public void setCodigosAlternos(Collection<CodigoProducto> codigosAlternos) {
        this.codigosAlternos = codigosAlternos;
    }

    /** Aún no implementado por ORM
    private Collection<MovimientoAlmacenDetalle> movimientoAlmacenDetallesById;

    @OneToMany(mappedBy = "productoByProductoId")
    public Collection<MovimientoAlmacenDetalle> getMovimientoAlmacenDetallesById() {
        return movimientoAlmacenDetallesById;
    }

    public void setMovimientoAlmacenDetallesById(Collection<MovimientoAlmacenDetalle> movimientoAlmacenDetallesById) {
        this.movimientoAlmacenDetallesById = movimientoAlmacenDetallesById;
    }

    private Collection<Paquete> paquetesById;

    @OneToMany(mappedBy = "productoByProductoId")
    public Collection<Paquete> getPaquetesById() {
        return paquetesById;
    }

    public void setPaquetesById(Collection<Paquete> paquetesById) {
        this.paquetesById = paquetesById;
    }
    */

    private Linea lineaByLineaId;

    @ManyToOne
    public
    @JoinColumn(name = "linea_id", referencedColumnName = "id")
    Linea getLineaByLineaId() {
        return lineaByLineaId;
    }

    public void setLineaByLineaId(Linea lineaByLineaId) {
        this.lineaByLineaId = lineaByLineaId;
    }

    private Grupo grupoByGrupoId;

    @ManyToOne
    public
    @JoinColumn(name = "grupo_id", referencedColumnName = "id")
    Grupo getGrupoByGrupoId() {
        return grupoByGrupoId;
    }

    public void setGrupoByGrupoId(Grupo grupoByGrupoId) {
        this.grupoByGrupoId = grupoByGrupoId;
    }

    /** Aún no implementado por ORM
    private Collection<VentaDetalle> ventaDetallesById;

    @OneToMany(mappedBy = "productoByProductoId")
    public Collection<VentaDetalle> getVentaDetallesById() {
        return ventaDetallesById;
    }

    public void setVentaDetallesById(Collection<VentaDetalle> ventaDetallesById) {
        this.ventaDetallesById = ventaDetallesById;
    }
    */
}
