package omoikane.producto;



import net.sf.ehcache.hibernate.HibernateUtil;
import omoikane.entities.Anotacion;
import omoikane.entities.Paquete;
import omoikane.inventarios.Stock;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cascade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 01/10/12
 * Time: 17:54
 * @author octavioruizcastillo
 * Legacy entity. Must be replaced for Producto entity
 */

@Entity
@Table(name = "articulos")

public class Articulo implements Serializable, IProductoApreciado {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_articulo")
    private Long idArticulo;
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "id_linea")
    private Integer idLinea;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "unidad")
    private String unidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "impuestos")
    private Double porcentajeImpuestos;
    @Basic(optional = false)
    @Column(name = "uModificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uModificacion;
    @Column(name = "version")
    private Integer version;
    @Basic(optional = false)
    @Column(name = "id_grupo")
    private int idGrupo;

    @Transient private Boolean esPaqueteDefaultValue = false;
    private Boolean esPaquete = esPaqueteDefaultValue;

    @Transient
    PrecioOmoikaneLogic precio;

    public Articulo() {

    }

    public Articulo(Long idArticulo) {
        this.idArticulo = idArticulo;
    }

    public Articulo(Long idArticulo, Date uModificacion, int idGrupo) {
        this.idArticulo = idArticulo;
        this.uModificacion = uModificacion;
        this.idGrupo = idGrupo;
    }

    public Long getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Long idArticulo) {
        this.idArticulo = idArticulo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(Integer idLinea) {
        this.idLinea = idLinea;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }



    public Double getPorcentajeImpuestos() {
        return porcentajeImpuestos;
    }

    public void setPorcentajeImpuestos(Double porcentajeImpuestos) {
        this.porcentajeImpuestos = porcentajeImpuestos;
    }

    public Date getUModificacion() {
        return uModificacion;
    }

    public void setUModificacion(Date uModificacion) {
        this.uModificacion = uModificacion;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_articulo")
    private BaseParaPrecio baseParaPrecio;

    @OneToMany(mappedBy = "productoContenedor")
    public List<Paquete> renglonesPaquete;

    @Transactional
    public List<Paquete> getRenglonesPaquete() {
        List<Paquete> p = renglonesPaquete;
        Hibernate.initialize(p);
        return p;
    }


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    public Stock stock;

    @Transactional
    public Stock getStock() {
        Stock s = stock;
        Hibernate.initialize(s);
        return s;
    }

    public void setStock(Stock s) {
        this.stock = s;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idArticulo != null ? idArticulo.hashCode() : 0);
        return hash;
    }

    @Transient
    public PrecioOmoikaneLogic getPrecio() {
        if(precio == null) { precio = new PrecioOmoikaneLogic( getBaseParaPrecio() ); }
        return precio;
    }

    @Override
    public void setPrecio(IPrecio precio) {
        this.precio = (PrecioOmoikaneLogic) precio;
    }

    @Override
    public BaseParaPrecio getBaseParaPrecio() {
        return this.baseParaPrecio;
    }


    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Articulo)) {
            return false;
        }
        Articulo other = (Articulo) object;
        if ((this.idArticulo == null && other.idArticulo != null) || (this.idArticulo != null && !this.idArticulo.equals(other.idArticulo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.Articulos[ idArticulo=" + idArticulo + " ]";
    }

    public Boolean getEsPaquete() {
        Boolean r = esPaquete != null ? esPaquete : esPaqueteDefaultValue;
        return r;
    }

    public void setEsPaquete(Boolean esGrupo) {
        this.esPaquete = esGrupo;
    }
}

