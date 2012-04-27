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
public class EstrategiaEstandar extends EstrategiasCorte {

    public def obtenerSumaCaja(IDCaja, horaAbierta, horaCerrada) {
        try {
            def serv  = Nadesico.conectar()
            def ventas= serv.sumaVentas(IDCaja, horaAbierta, horaCerrada)
            serv.desconectar()
            return ventas

        } catch(Exception e) {
            omoikane.sistema.Dialogos.error("Error al obtener suma para corte de caja", e)
        }

    }
    public def hacerCorteCaja(IDCaja, IDAlmacen, subtotal, impuestos, descuento, total,
                               nVentas, desde, hasta, depositos, retiros) 
    {
        try {
            def serv = Nadesico.conectar()

            def newCorte = serv.addCorte(IDCaja, IDAlmacen, subtotal, impuestos, descuento, total,
                                   nVentas, desde, hasta, depositos, retiros)

            serv.desconectar()

            omoikane.sistema.Dialogos.lanzarAlerta(newCorte.mensaje)
            def comprobante = new Comprobantes()
            comprobante.Corte(newCorte.IDCorte)//imprimir ticket
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
            def serv  = Nadesico.conectar()
            def salida= serv.getSumaCorteSucursal(IDAlmacen, IDCorte)
            serv.desconectar()
            return salida
        } catch(Exception e) {
            omoikane.sistema.Dialogos.error("Error al obtener suma de corte de sucursal", e)
        }
    }

    public def imprimirCorteSucursal(IDAlmacen, IDCorte) {
        try {
            def comprobante = new Comprobantes()
            (comprobante.CorteSucursal(IDAlmacen, IDCorte))//imprimir ticket
            (comprobante.probar())//* Aqui tambien mandar a imprimir*/
        } catch(Exception e) {
            omoikane.sistema.Dialogos.error("Error al imprimir corte de sucursal", e)
        }
    }

    def saluda() { println "Hola desde estrategia estándar"}
    //public void hacerCorteSucursal() { }
}
