/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.sistema

import java.io.*;
import groovy.text.GStringTemplateEngine
import java.text.SimpleDateFormat
import omoikane.sistema.*
import groovy.inspect.swingui.*

class Comprobantes {

    def protocolo = omoikane.principal.Principal.puertoImpresion
    def data
    def generado

    def ticket(IDAlmacen, IDVenta) {
        def serv = new Nadesico().conectar()
        try {
            data         = serv.getVenta(IDVenta, IDAlmacen)
            data.caja    = serv.getCaja(data.id_caja)
            data.usuario = serv.getUsuario(data.id_usuario, IDAlmacen)
            generado     = generarTicket()
        } catch(e) {
            //data.usuario = serv.getUsuario(data.id_usuario)
            throw e
        } finally {
        serv.desconectar()
        }
    }

    def Corte(ID) {
        def serv = new Nadesico().conectar()
        try {
            data         = serv.getCorteWhere(" cortes.id_corte=$ID")
            data.caja    = serv.getCaja(data.id_caja)
            generado     = generarCorte()
        } catch(e) {
            throw e
        }finally {
        serv.desconectar()
        }
    }

    def CorteSucursal(IDAlmacen, IDCorte) {
        def serv = new Nadesico().conectar()
        try {
            data   = serv.getSumaCorteSucursal(IDAlmacen, IDCorte)
            data   += serv.getCorteSucursal(IDAlmacen, IDCorte)
            data.descripcion = "CORTE DEL DIA"
            generado = generarCorteSucursal()
        } catch(e) {
            throw e
        }finally {
        serv.desconectar()
        }
    }

    def generarTicket() {
        def plantilla = getClass().getResourceAsStream("/omoikane/reportes/FormatoTicket.txt").getText('UTF-8') as String
        def sdfFecha = new SimpleDateFormat("dd-MM-yyyy")
        def sdfHora  = new SimpleDateFormat("hh:mm a")
        def binding = data

        binding.fecha = sdfFecha.format(data.date)
        binding.hora  = sdfHora.format(data.date)
        binding.folio = "${data.id_almacen}-${data.id_caja}-${data.id_venta}"
        binding.cajero= data.usuario.nombre

        def engine = new GStringTemplateEngine()
        def template = engine.createTemplate(plantilla).make(binding)

        template.toString()
    }

    def generarCorte() {
        def plantilla = getClass().getResourceAsStream("/omoikane/reportes/FormatoCorte.txt").getText('UTF-8') as String
        def sdfFecha = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a")
        def sdfHora  = new SimpleDateFormat("hh:mm a")
        def sdfDia  = new SimpleDateFormat("EEEEEEEEEE dd-MMM-yyyy  ")
        def binding = data

        binding.fecha  = sdfFecha.format(data.fecha_hora)
        binding.descripcion=data.caja.descripcion
        binding.dia    = sdfDia.format(data.desde)
        binding.desde  = sdfHora.format(data.desde)
        binding.hasta  = sdfHora.format(data.hasta)
        binding.folio  = "${data.id_almacen}-${data.id_caja}-${data.n_ventas}"
        binding.devoluciones= 4.5
        binding.ingresos=4.5
        binding.retiros=4.5

        def engine = new GStringTemplateEngine()
        def template = engine.createTemplate(plantilla).make(binding)

        template.toString()
    }

    def generarCorteSucursal() {
        def plantilla = getClass().getResourceAsStream("/omoikane/reportes/FormatoCorte.txt").getText('UTF-8') as String
        def sdfFecha = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a")
        def sdfHora  = new SimpleDateFormat("hh:mm a")
        def sdfDia  = new SimpleDateFormat("EEEEEEEEEE dd-MMM-yyyy  ")
        def binding = data

        binding.fecha  = sdfFecha.format(new Date())
        binding.dia    = sdfDia.format(data.desde)
        binding.desde  = sdfHora.format(data.desde)
        binding.hasta  = sdfHora.format(data.hasta)
        binding.folio  = "${data.id_almacen}-${data.n_ventas}"
        binding.devoluciones = 10.0
        binding.ingresos = 10.0
        binding.retiros= 10.0

        def engine = new GStringTemplateEngine()
        def template = engine.createTemplate(plantilla).make(binding)
        template.toString()
    }

    def probar() {
        try {
            FileOutputStream os = new FileOutputStream("$protocolo:");
            PrintStream ps = new PrintStream(os);
            ps.println(generado);
            ps.close();
        } catch (FileNotFoundException fnf) { consola.out.echo("Error al imprimir al puerto lpt1"); }
    }
    
    def imprimir(){
        try {
            println generado
        } catch (e) { consola.out.echo("Error al mandar a al consola"); }
    }


}