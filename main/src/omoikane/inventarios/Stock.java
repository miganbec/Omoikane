package omoikane.inventarios;

import omoikane.producto.Articulo;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 22/02/13
 * Time: 02:00 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Stock {
    @Id
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "idArticulo")
    public
    Articulo producto;

    @NotNull
    private BigDecimal enTienda;
    @NotNull
    private BigDecimal enBodega;
    @NotNull @DecimalMin(value = "0")
    private BigDecimal minimo;
    @NotNull @DecimalMax(value = "0")
    private BigDecimal maximo;
    @NotNull
    private Character clasificacion;
    @NotNull
    private Timestamp modificado;

    @PrePersist
    protected void onCreate() {
        modificado = new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    @PreUpdate
    protected void onUpdate() {
        modificado = new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    @Column
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column
    public Articulo getProducto() {
        return producto;
    }

    public void setProducto(Articulo producto) {
        this.producto = producto;
    }

    @Column
    public BigDecimal getEnTienda() {
        return enTienda;
    }

    public void setEnTienda(BigDecimal enTienda) {
        this.enTienda = enTienda;
    }

    @Column
    public BigDecimal getEnBodega() {
        return enBodega;
    }

    public void setEnBodega(BigDecimal enBodega) {
        this.enBodega = enBodega;
    }

    @Column
    public BigDecimal getMinimo() {
        return minimo;
    }

    public void setMinimo(BigDecimal minimo) {
        this.minimo = minimo;
    }

    @Column
    public BigDecimal getMaximo() {
        return maximo;
    }

    public void setMaximo(BigDecimal maximo) {
        this.maximo = maximo;
    }

    @Column
    public Character getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(Character clasificacion) {
        this.clasificacion = clasificacion;
    }

    @Column
    public Timestamp getModificado() {
        return modificado;
    }

    public void setModificado(Timestamp modificado) {
        this.modificado = modificado;
    }
}
