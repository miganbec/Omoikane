package omoikane.entities;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Arrays;
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
@Cacheable(true)
// **03/04/2012: Retrocompatibilidad ==(nuevo)usuario --> usuarios(antiguo)==
@Table(name="usuarios")
public class Usuario {

    private Long id;

    @NotNull    private Timestamp fechaHoraAlta;

    @NotEmpty(message = "¡El nombre es importante! No puede estár vacío")
    private String nombre;

    private byte[] huella1;

    private byte[] huella2;

    private byte[] huella3;

    @Range(min = 1000, max = 9999, message = "El nip debe ser un número de 4 dígitos")
    private int nip;

    @NotNull    private Timestamp umodificacion;

    public Usuario() {

    }

    @PrePersist
    protected void onCreate() {
        umodificacion = new Timestamp(Calendar.getInstance().getTime().getTime());
        fechaHoraAlta = new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    @PreUpdate
    protected void onUpdate() {
        setUmodificacion(new Timestamp(Calendar.getInstance().getTime().getTime()));
    }

     // **03/04/2012: Retrocompatibilidad ==(nuevo)id --> id_usuario(antiguo)==
    @Column(name = "id_usuario")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "fecha_hora_alta")
    public Timestamp getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Timestamp fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    @Column(name = "nombre")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Column(name = "huella1", length = 65535)
    public byte[] getHuella1() {
        return huella1;
    }

    public void setHuella1(byte[] huella1) {
        this.huella1 = huella1;
    }

    @Column(name = "huella2", length = 65535)
    public byte[] getHuella2() {
        return huella2;
    }

    public void setHuella2(byte[] huella2) {
        this.huella2 = huella2;
    }

    @Column(name = "huella3", length = 65535)
    public byte[] getHuella3() {
        return huella3;
    }

    public void setHuella3(byte[] huella3) {
        this.huella3 = huella3;
    }

    @Column(name = "nip")
    public int getNip() {
        return nip;
    }

    public void setNip(int nip) {
        this.nip = nip;
    }

    @Column(name = "uModificacion")
    public Timestamp getUmodificacion() {
        return umodificacion;
    }

    public void setUmodificacion(Timestamp umodificacion) {
        this.umodificacion = umodificacion;
    }

    public Usuario(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usuario usuario = (Usuario) o;

        if (id != usuario.id) return false;
        if (nip != usuario.nip) return false;
        if (fechaHoraAlta != null ? !fechaHoraAlta.equals(usuario.fechaHoraAlta) : usuario.fechaHoraAlta != null)
            return false;
        if (!Arrays.equals(huella1, usuario.huella1)) return false;
        if (!Arrays.equals(huella2, usuario.huella2)) return false;
        if (!Arrays.equals(huella3, usuario.huella3)) return false;
        if (nombre != null ? !nombre.equals(usuario.nombre) : usuario.nombre != null) return false;
        if (umodificacion != null ? !umodificacion.equals(usuario.umodificacion) : usuario.umodificacion != null)
            return false;

        return true;
    }

}
