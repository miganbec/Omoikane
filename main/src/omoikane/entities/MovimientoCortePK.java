package omoikane.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 14/07/11
 * Time: 04:04
 * To change this template use File | Settings | File Templates.
 */
public class MovimientoCortePK implements Serializable {
    private int corteIdCaja;

    private int corteSucursalId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "corte_id_caja")
    public int getCorteIdCaja() {
        return corteIdCaja;
    }

    public void setCorteIdCaja(int corteIdCaja) {
        this.corteIdCaja = corteIdCaja;
    }

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

        MovimientoCortePK that = (MovimientoCortePK) o;

        if (corteIdCaja != that.corteIdCaja) return false;
        if (corteSucursalId != that.corteSucursalId) return false;

        return true;
    }

}
