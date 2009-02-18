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

/**
 *
 * @author Usuario
 */
class Ticket {
    def protocolo = "LPT1"
    def data
    def tick

    def Ticket(IDAlmacen, IDVenta) {
        def serv = new Nadesico().conectar()
        try {
            data         = serv.getVenta(IDVenta, IDAlmacen)
            data.caja    = serv.getCaja(data.id_caja)
            data.usuario = serv.getUsuario(data.id_usuario, IDAlmacen)
        } catch(e) {data.usuario = serv.getUsuario(data.id_usuario)
            serv.desconectar()
            throw e
        }
    }

    def generarTxt() {
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

    def imprimir() {
        try {
            FileOutputStream os = new FileOutputStream("$protocolo:");
            PrintStream ps = new PrintStream(os);
            ps.println(generarTxt);
            //for(int i = 0; i < 4; i++) { ps.println("\n"); }
            ps.close();
        } catch (FileNotFoundException fnf) { consola.out.echo("Error al imprimir al puerto lpt1"); }
    }

    def probar() {
        println generarTxt()
    }

    def Corte(IDCorte) {
        def serv = new Nadesico().conectar()
        try {
            tick         = serv.getCorte(IDCorte)
            tick.caja    = serv.getCaja(IDCorte)
        } catch(e) {
            serv.desconectar()
            throw e
        }
    }

    def generarCorte() {
        def plantilla = getClass().getResourceAsStream("/omoikane/reportes/FormatoCorte.txt").getText('UTF-8') as String
        Corte(1)
        def sdfFecha = new SimpleDateFormat("dd-MM-yyyy")
        def sdfHora  = new SimpleDateFormat("hh:mm a")
        def binding = tick

        binding.fecha = sdfFecha.format(tick.fecha_hora)
        binding.desde  = sdfHora.format(tick.desde)
        binding.hasta  = sdfHora.format(tick.hasta)
        binding.folio = "${tick.id_almacen}-${tick.id_caja}-${tick.nventas}"
        
        def engine = new GStringTemplateEngine()
        def template = engine.createTemplate(plantilla).make(binding)

        template.toString()
    }

    def prueba() {
        println generarCorte()
    }
}

