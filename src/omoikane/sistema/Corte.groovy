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
class Corte {
    def protocolo = "LPT1"
    def tick

    def imprimir() {
        try {
            FileOutputStream os = new FileOutputStream("$protocolo:");
            PrintStream ps = new PrintStream(os);
            ps.println(generarCorte());
            //for(int i = 0; i < 4; i++) { ps.println("\n"); }
            ps.close();
        } catch (FileNotFoundException fnf) { consola.out.echo("Error al imprimir al puerto lpt1"); }
    }


    def Corte(ID) {
        def serv = new Nadesico().conectar()
        try {
            tick         = serv.getCorteWhere(" cortes.id_corte=$ID")
            tick.caja    = serv.getCaja(tick.id_caja)
            println tick
        } catch(e) {
            serv.desconectar()
            throw e
        }
    }

    def generarCorte() {
        def plantilla = getClass().getResourceAsStream("/omoikane/reportes/FormatoCorte.txt").getText('UTF-8') as String
        def sdfFecha = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a")
        def sdfHora  = new SimpleDateFormat("hh:mm a")
        def sdfDia  = new SimpleDateFormat("EEEEEEEEEE dd-MMM-yyyy  ")
        def binding = tick

        binding.fecha  = sdfFecha.format(tick.fecha_hora)
        binding.descripcion=tick.caja.descripcion
        binding.dia    = sdfDia.format(tick.desde)
        binding.desde  = sdfHora.format(tick.desde)
        binding.hasta  = sdfHora.format(tick.hasta)
        binding.folio  = "${tick.id_almacen}-${tick.id_caja}-${tick.n_ventas}"
        binding.devoluciones= 4.5
        binding.ingresos=4.5
        binding.retiros=4.5
        
        def engine = new GStringTemplateEngine()
        def template = engine.createTemplate(plantilla).make(binding)

        template.toString()
    }

    def prueba() {
        //println generarCorte()
        imprimir()
    }
}

