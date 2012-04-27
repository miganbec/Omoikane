package omoikane.entities;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 14/07/11
 * Time: 04:04
 * To change this template use File | Settings | File Templates.
 */
//@IdClass(omoikane.entities.VentaEspecialPK.class)
//@Table(name = "venta_especial", catalog = "Omoikane")
//@Entity
public class VentaEspecial {
    private int id;

    private int usuariosIdUsuario;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "usuarios_id_usuario")
    @Id
    public int getUsuariosIdUsuario() {
        return usuariosIdUsuario;
    }

    public void setUsuariosIdUsuario(int usuariosIdUsuario) {
        this.usuariosIdUsuario = usuariosIdUsuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VentaEspecial that = (VentaEspecial) o;

        if (id != that.id) return false;
        if (usuariosIdUsuario != that.usuariosIdUsuario) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + usuariosIdUsuario;
        return result;
    }

    private Venta ventaByIdVenta;

    @ManyToOne
    public
    @JoinColumn(name = "id_venta", referencedColumnName = "id", nullable = false)
    Venta getVentaByIdVenta() {
        return ventaByIdVenta;
    }

    public void setVentaByIdVenta(Venta ventaByIdVenta) {
        this.ventaByIdVenta = ventaByIdVenta;
    }

    private Usuario usuarioByIdAutorizador;

    @ManyToOne
    public
    @JoinColumn(name = "id_autorizador", referencedColumnName = "id", nullable = false)
    Usuario getUsuarioByIdAutorizador() {
        return usuarioByIdAutorizador;
    }

    public void setUsuarioByIdAutorizador(Usuario usuarioByIdAutorizador) {
        this.usuarioByIdAutorizador = usuarioByIdAutorizador;
    }
}
