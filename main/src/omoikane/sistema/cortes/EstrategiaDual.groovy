/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.sistema.cortes;

import omoikane.sistema.Nadesico;
import omoikane.sistema.Comprobantes;
/**
 *
 * @author Octavio
 */
public class EstrategiaDual extends EstrategiasCorte {

    public def yaSumado = false
    public def laSuma   = null
    public def obtenerSumaCaja(IDCaja, horaAbierta, horaCerrada) {
        throw new UnsupportedOperationException("No soportado desde SmartPos 2");
    }
    public def hacerCorteCaja(IDCaja, IDAlmacen, subtotal, impuestos, descuento, total,
                               nVentas, desde, hasta, depositos, retiros)
    {
        throw new UnsupportedOperationException("No soportado desde SmartPos 2");
    }
    public def hacerCorteSucursal(IDAlmacen) {
        throw new UnsupportedOperationException("No soportado desde SmartPos 2");
    }

    public def obtenerSumaSucursal(IDAlmacen, IDCorte) {
        throw new UnsupportedOperationException("No soportado desde SmartPos 2");
    }

    public def imprimirCorteSucursal(IDAlmacen, IDCorte) {
        throw new UnsupportedOperationException("No soportado desde SmartPos 2");
    }

    def saluda() { println "Hola desde estrategia estándar"}
    //public void hacerCorteSucursal() { }
}
