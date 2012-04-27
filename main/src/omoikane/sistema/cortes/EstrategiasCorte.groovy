/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.sistema.cortes;

/**
 *
 * @author Octavio
 */
public abstract class EstrategiasCorte {
    abstract def hacerCorteCaja(IDCaja, IDAlmacen, subtotal, impuestos, descuento, total,
                                     nVentas, desde, hasta, depositos, retiros);
    abstract def obtenerSumaCaja(IDCaja, horaAbierta, horaCerrada);
    abstract def hacerCorteSucursal(IDAlmacen);
    abstract def obtenerSumaSucursal(IDAlmacen, IDCorte);
    abstract def imprimirCorteSucursal(IDAlmacen, IDCorte);

    public Boolean isCajaAbierta(Integer idCaja) {
      throw new UnsupportedOperationException("No implementado");
    }

    public Boolean abrirCaja(Integer idCaja) {
      throw new UnsupportedOperationException("No implementado");
    }

    public Boolean cerrarCaja(Integer idCaja) {
      throw new UnsupportedOperationException("No implementado");
    }
}