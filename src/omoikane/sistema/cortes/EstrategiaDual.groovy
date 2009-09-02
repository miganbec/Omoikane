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
public class EstrategiaDual implements EstrategiasCorte {

    public def yaSumado = false
    public def laSuma   = null
    public def obtenerSumaCaja(IDCaja, horaAbierta, horaCerrada) {
        try {
            def serv  = Nadesico.conectar()
            def ventas= serv.sumaDual(IDCaja, horaAbierta, horaCerrada)
            serv.desconectar()
            yaSumado = true
            laSuma   = ventas
            return ventas

        } catch(Exception e) {
            omoikane.sistema.Dialogos.error("Error al obtener suma para corte de caja", e)
        }

    }
    public def hacerCorteCaja(IDCaja, IDAlmacen, subtotal, impuestos, descuento, total,
                               nVentas, desde, hasta, depositos, retiros)
    {
        try {
            if(!yaSumado) { throw new Exception("Error. No se realizó \"obtenerSumaCaja\"") }
            def serv = Nadesico.conectar()
            
            def newCorte = serv.addCorteDual(IDCaja, IDAlmacen, subtotal, laSuma.subtotalDual, impuestos,
                                             laSuma.impuestosDual, descuento, laSuma.descuentoDual, total,
                                             laSuma.totalDual, nVentas, desde, hasta, depositos, retiros)
            serv.desconectar()

            omoikane.sistema.Dialogos.lanzarAlerta(newCorte.mensaje)
            def comprobante = new Comprobantes()
            comprobante.Corte(newCorte.IDCorte)                    //imprimir ticket
            comprobante.probar()
            comprobante.Corte(newCorte.IDCorteDual, "cortes_dual") //imprimir corte dual
            comprobante.probar()//imprimir ticket

            return newCorte

        } catch(Exception e) {
            omoikane.sistema.Dialogos.error("Error al generar corte de caja", e)
        }
    }
    public def hacerCorteSucursal(IDAlmacen) {
        try {
            def serv  = Nadesico.conectar()
            def salida= serv.corteSucursal(IDAlmacen)
            serv.desconectar()
            return salida
        } catch(Exception e) {
            omoikane.sistema.Dialogos.error("Error al generar corte de sucursal", e)
        }
    }

    public def obtenerSumaSucursal(IDAlmacen, IDCorte) {
        try {
            def serv    = Nadesico.conectar()
            def salida  = serv.getSumaCorteSucursal(IDAlmacen, IDCorte)
            salida.dual = serv.getSumaCorteSucursalDual(IDAlmacen, IDCorte)

            serv.desconectar()
            return salida
        } catch(Exception e) {
            omoikane.sistema.Dialogos.error("Error al obtener suma de corte de sucursal", e)
        }
    }

    public def imprimirCorteSucursal(IDAlmacen, IDCorte) {
        try {
            def comprobante = new Comprobantes()
            
            def serv    = Nadesico.conectar()
            def data   = serv.getSumaCorteSucursalDual(IDAlmacen, IDCorte)
            data      += serv.getCorteSucursal(IDAlmacen, IDCorte)
            serv.desconectar()

            (comprobante.CorteSucursal(IDAlmacen, IDCorte)) //imprimir ticket
            (comprobante.probar())                          //* Aqui tambien mandar a imprimir*/
            
            (comprobante.CorteSucursalAvanzado(data))       //imprimir ticket
            (comprobante.probar())                          //* Aqui tambien mandar a imprimir*/
        } catch(Exception e) {
            omoikane.sistema.Dialogos.error("Error al imprimir corte de sucursal", e)
        }
    }

    def saluda() { println "Hola desde estrategia estándar"}
    //public void hacerCorteSucursal() { }
}
