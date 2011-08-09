/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.sistema.cortes;

/**
 *
 * @author Octavio
 */
public interface EstrategiasCorte {
    public def hacerCorteCaja(IDCaja, IDAlmacen, subtotal, impuestos, descuento, total,
                                     nVentas, desde, hasta, depositos, retiros);
    public def obtenerSumaCaja(IDCaja, horaAbierta, horaCerrada);
    public def hacerCorteSucursal(IDAlmacen);
    public def obtenerSumaSucursal(IDAlmacen, IDCorte);
    public def imprimirCorteSucursal(IDAlmacen, IDCorte);
}