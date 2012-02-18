package omoikane.entities;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 14/07/11
 * Time: 04:04
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Preferencia {
                private int id;

    @NotEmpty   private String clave;

    @NotEmpty   private String valor;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "clave", length = 65535)
    @Basic
    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    @Column(name = "valor", length = 65535)
    @Basic
    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Preferencia that = (Preferencia) o;

        if (id != that.id) return false;
        if (clave != null ? !clave.equals(that.clave) : that.clave != null) return false;
        if (valor != null ? !valor.equals(that.valor) : that.valor != null) return false;

        return true;
    }

}
