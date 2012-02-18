package omoikane.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 14/07/11
 * Time: 04:04
 * To change this template use File | Settings | File Templates.
 */
public class CortePK implements Serializable {
    private int idCaja;

    @Id
    @Column(name = "id_caja")
    public int getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }

    private int corteSucursalId;

    @Id
    @Column(name = "corte_sucursal_id")
    public int getCorteSucursalId() {
        return corteSucursalId;
    }

    public void setCorteSucursalId(int corteSucursalId) {
        this.corteSucursalId = corteSucursalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CortePK cortePK = (CortePK) o;

        if (corteSucursalId != cortePK.corteSucursalId) return false;
        if (idCaja != cortePK.idCaja) return false;

        return true;
    }

}
