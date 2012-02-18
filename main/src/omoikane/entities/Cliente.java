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
@Entity
public class Cliente {

    @NotNull
    private int clienteId;

    @NotEmpty
    private String nombre;

    @Min(value = 0)
    private BigDecimal descuento;

    @NotNull
    private BigDecimal saldo;

    @NotNull
    private Timestamp actualizacion;

    @NotNull
    private Timestamp creacion;

    @PrePersist
    protected void onCreate() {
        creacion = new Timestamp(Calendar.getInstance().getTime().getTime());
        actualizacion = new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    @PreUpdate
    protected void onUpdate() {
        actualizacion = new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    @Column(name = "cliente_id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    @Column(name = "nombre", length = 65535)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    @Column(name = "descuento")
    @Basic
    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    @Column(name = "saldo")
    @Basic
    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    @Column(name = "actualizacion")
    @Basic
    public Timestamp getActualizacion() {
        return actualizacion;
    }

    public void setActualizacion(Timestamp umodificacion) {
        this.actualizacion = umodificacion;
    }

    @Column(name = "creacion")
    @Basic
    public Timestamp getCreacion() {
        return creacion;
    }

    public void setCreacion(Timestamp creacion) {
        this.creacion = creacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cliente cliente = (Cliente) o;

        if (clienteId != cliente.clienteId) return false;
        if (cliente.descuento.compareTo(descuento) != 0) return false;
        if (cliente.saldo.compareTo(saldo) != 0) return false;
        if (nombre.equals(cliente.getNombre())) return false;
        if (actualizacion != null ? !actualizacion.equals(cliente.actualizacion) : cliente.actualizacion != null)
            return false;
        if (creacion != null ? !creacion.equals(cliente.creacion) : cliente.creacion != null)
                    return false;

        return true;
    }

    private Collection<Venta> ventasByClienteId;

    @OneToMany(mappedBy = "clienteByClienteId")
    public Collection<Venta> getVentasByClienteId() {
        return ventasByClienteId;
    }

    public void setVentasByClienteId(Collection<Venta> ventasByClienteId) {
        this.ventasByClienteId = ventasByClienteId;
    }
}
