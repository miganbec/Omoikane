package omoikane.entities;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 14/07/11
 * Time: 04:04
 * To change this template use File | Settings | File Templates.
 */
@Table(name = "venta_facturada", catalog = "Omoikane")
@Entity
public class VentaFacturada {
    private int facturaId;

    @Column(name = "factura_id")
    @Basic
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(int facturaId) {
        this.facturaId = facturaId;
    }

    private int ventaId;

    @Column(name = "venta_id")
    @Id
    public int getVentaId() {
        return ventaId;
    }

    public void setVentaId(int ventaId) {
        this.ventaId = ventaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VentaFacturada that = (VentaFacturada) o;

        if (facturaId != that.facturaId) return false;
        if (ventaId != that.ventaId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = facturaId;
        result = 31 * result + ventaId;
        return result;
    }

    private Venta ventaByVentaId;

    @OneToOne
    public
    @JoinColumn(name = "venta_id", referencedColumnName = "id", nullable = false)
    Venta getVentaByVentaId() {
        return ventaByVentaId;
    }

    public void setVentaByVentaId(Venta ventaByVentaId) {
        this.ventaByVentaId = ventaByVentaId;
    }
}
