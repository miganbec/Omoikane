/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.sistema

import java.io.*;
import groovy.text.GStringTemplateEngine

/**
 *
 * @author Usuario
 */
class Ticket {
    def protocolo = "LPT1"
    def folio = "", fecha = "", cajero = "", caja = "", cliente = "", detalles = [], subtotal = 0.0, impuestos = 0.0, descuentos = 0.0, total = 0.0
    def data
    def IDVenta

    def Ticket(IDAlmacen, IDVenta) {
        this.IDVenta = IDVenta
        def serv = new Nadesico().conectar()
        data = serv.getVenta(IDVenta, IDAlmacen)
        serv.desconectar()
    }
    def generarTxt() {
        def plantilla = getClass().getResourceAsStream("/omoikane/reportes/FormatoTicket.txt").getText('UTF-8') as String
        /*
        plantilla = plantilla.replace('[%fecha%]'    , data.fecha_hora   )
        plantilla = plantilla.replace('[%folio%]'    , folio   )
        plantilla = plantilla.replace('[%contenido%]', data.tabMatriz.toString())
        plantilla = plantilla.replace('[%subtotal%]' , data.subtotal as String)
        plantilla = plantilla.replace('[%descuento%]', data.descuento as String)
        plantilla = plantilla.replace('[%impuestos%]', data.impuestos as String)
        plantilla = plantilla.replace('[%total%]'    , data.total as String)
        */
        def binding = ["fecha":data.fecha_hora, "lastname":"Pullara", "city":"San Francisco", "month":"December", "signed":"Groovy-Dev"]

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
}

