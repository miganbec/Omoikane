package omoikane.entities;

import omoikane.producto.Articulo;
import org.hibernate.engine.jdbc.SerializableClobProxy;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 07/12/12
 * Time: 20:50
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Cancelacion implements Serializable {

    @Column private Long id;

    @Column private Date fechaHora;

    @Column private Articulo articulo;

    @Column private Usuario cajero;

    @Column private Usuario autorizador;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    @PrePersist
    protected void onCreate() {
        setFechaHora(new Timestamp(Calendar.getInstance().getTime().getTime()));
    }

    @OneToOne
    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    @ManyToOne
    public Usuario getCajero() {
        return cajero;
    }

    public void setCajero(Usuario cajero) {
        this.cajero = cajero;
    }

    @ManyToOne
    public Usuario getAutorizador() {
        return autorizador;
    }

    public void setAutorizador(Usuario autorizador) {
        this.autorizador = autorizador;
    }
}
