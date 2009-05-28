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
    def impresora = omoikane.principal.Principal.impresoraActiva
    def generado
    def cajon = omoikane.principal.Principal.cajon

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

    def movimiento(ID,tipo) {
        def serv = new Nadesico().conectar()
        def temp
        try {
            data         = serv.getDoMovimiento(ID,tipo)
            temp         = serv.getCaja(data.id_caja)
            data.caja    =temp.descripcion
            temp         = serv.getUsuario(data.id_cajero,data.id_almacen)
            data.cajero = temp.nombre
            temp         = serv.getUsuario(data.id_usuario,data.id_almacen)
            data.usuario = temp.nombre
            generado = generarMovimiento()

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
        binding.cajon = cajon
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
        binding.cajon = cajon
        binding.fecha  = sdfFecha.format(data.fecha_hora)
        binding.descripcion=data.caja.descripcion
        binding.dia    = sdfDia.format(data.desde)
        binding.desde  = sdfHora.format(data.desde)
        binding.hasta  = sdfHora.format(data.hasta)
        binding.devoluciones = 0.0f
        binding.ingresos = 0.0f
        binding.retiros = 0.0f

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
        binding.cajon = cajon
        binding.fecha  = sdfFecha.format(new Date())
        binding.dia    = sdfDia.format(data.desde)
        binding.desde  = sdfHora.format(data.desde)
        binding.hasta  = sdfHora.format(data.hasta)
        binding.devoluciones = 0.0f
        binding.ingresos = 0.0f
        binding.retiros = 0.0f

        def engine = new GStringTemplateEngine()
        def template = engine.createTemplate(plantilla).make(binding)
        template.toString()
    }

    def generarMovimiento() {
        def plantilla = getClass().getResourceAsStream("/omoikane/reportes/FormatoMovimiento.txt").getText('UTF-8') as String
        def sdfFecha = new SimpleDateFormat("dd-MM-yyyy")
        def sdfHora  = new SimpleDateFormat("hh:mm a")
        def binding = data
        binding.fecha = sdfFecha.format(data.momento)
        binding.hora  = sdfHora.format(data.momento)
        binding.folio = "${data.id_almacen}-${data.id_caja}-${data.id}"
        def engine = new GStringTemplateEngine()
        def template = engine.createTemplate(plantilla).make(binding)

        template.toString()
    }

    def probar() {
        if (impresora)
        {
        try {
            FileOutputStream os = new FileOutputStream("$protocolo:");
            PrintStream ps = new PrintStream(os);
            ps.println(generado);
            ps.close();
        } catch (FileNotFoundException fnf) { Dialogos.lanzarAlerta("Error al imprimir al puerto lpt1"); }
        }
        else
        {
            try {
            println generado
            } catch (e) { Dialogos.lanzarAlerta("Error al mandar a al consola"); }
        }
    }

}
  
