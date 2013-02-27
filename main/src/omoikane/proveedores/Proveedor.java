package omoikane.proveedores;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 25/02/13
 * Time: 08:32 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Proveedor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size( min = 5, max = 120, message = "Nombre debe tener entre 5 y 120 letras")
    @Column
    private String nombre = "";

    @NotNull
    @Column
    private String notas = "";

    @NotNull
    @Column
    private
    Boolean activo = true;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
