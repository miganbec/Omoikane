
 /* Author Phesus        //////////////////////////////
 *  ORC,ACR             /////////////
 *                     /////////////
 *                    /////////////
 *                   /////////////
 * //////////////////////////////                   */

package omoikane.sistema

import java.io.*;
import groovy.text.GStringTemplateEngine
import java.text.SimpleDateFormat
import omoikane.sistema.*
import groovy.inspect.swingui.*

class Comprobantes {

    def data
    def generado
    def impresora = omoikane.principal.Principal.impresoraActiva
    def protocolo = omoikane.principal.Principal.puertoImpresion

    def ticket(IDAlmacen, IDVenta) {
        def serv = new Nadesico().conectar()
        try {
            data         = serv.getVenta(IDVenta, IDAlmacen)
            data.caja    = serv.getCaja(data.id_caja)
            data.usuario = serv.getUsuario(data.id_usuario, IDAlmacen)
            generado     = generarTicket()
        } catch(e) {
            throw e
        } finally {
            serv.desconectar()
        }
    }

    def Corte(ID) {
        return Corte(ID, "cortes")
    }
    def Corte(ID, tabla) {
        def serv = new Nadesico().conectar()
        try {
            data         = serv.getCorteWhereFrom(" cortes.id_corte=$ID", tabla)
            data.caja    = serv.getCaja(data.id_caja)
            data.leyenda= "C O R T E   D E   C A J A"
            generado     = generarCorte()
        } catch(e) {
            throw e
        } finally {
        serv.desconectar()
        }
    }

    def CorteSucursal(IDAlmacen, IDCorte) {
        def serv = new Nadesico().conectar()
        try {
            data   = serv.getSumaCorteSucursal(IDAlmacen, IDCorte)
            data   += serv.getCorteSucursal(IDAlmacen, IDCorte)
            return CorteSucursalAvanzado(data)
        } catch(e) {
            omoikane.sistema.Dialogos.error("Ha ocurrido un error al realizar comprobante de corte de sucursal", e)
        }finally {
            serv.desconectar()
        }
    }

    def CorteSucursalAvanzado(data) {
        try {
            data.descripcion = "CORTE DEL DIA"
            data.leyenda= "C O R T E   D E   S U C U R S A L"
            this.data = data
            
            generado = generarCorteSucursal()

        } catch(Exception e) {
            omoikane.sistema.Dialogos.error("Ha ocurrido un error al realizar comprobante de corte de sucursal", e)
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
        def plantilla = new File("Plantillas/FormatoTicket.txt").getText('UTF-8') as String
        //def plantilla   = getClass().getResourceAsStream("/omoikane/reportes/FormatoTicket.txt").getText('UTF-8') as String
        def sdfFecha    = new SimpleDateFormat("dd-MM-yyyy")
        def sdfHora     = new SimpleDateFormat("hh:mm a")
        def binding     = data
        binding.fecha   = sdfFecha.format(data.date)
        binding.hora    = sdfHora.format(data.date)
        binding.folio   = "${data.id_almacen}-${data.id_caja}-${data.folio}"
		binding.idFolio = "${data.id_almacen}-${data.id_caja}-${data.id_venta}"
        binding.cajero= data.usuario.nombre
        def engine = new GStringTemplateEngine()
        def template = engine.createTemplate(plantilla).make(binding)
        template.toString()
    }

    def generarCorte() {
        def plantilla = new File("Plantillas/FormatoCorte.txt").getText('UTF-8') as String
        //def plantilla = getClass().getResourceAsStream("/omoikane/reportes/FormatoCorte.txt").getText('UTF-8') as String
        def sdfFecha = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a")
        def sdfHora  = new SimpleDateFormat("hh:mm a")
        def sdfDia  = new SimpleDateFormat("EEEEEEEEEE dd-MMM-yyyy  ")
        def binding = data
        binding.fecha        = sdfFecha.format(data.fecha_hora)
        binding.descripcion  = data.caja.descripcion
        binding.dia          = sdfDia.format(data.desde)
        binding.desde        = sdfHora.format(data.desde)
        binding.hasta        = sdfHora.format(data.hasta)
		binding.id_caja      = data.id_caja
		binding.id_almacen  = data.id_almacen
		binding.prefijoFolio = data.id_almacen+"-"+data.id_caja
		binding.folioInicial = binding.prefijoFolio + "-" + data.folio_inicial
		binding.folioFinal   = binding.prefijoFolio + "-" + data.folio_final
		binding.folios       = "Folios desde ${binding.folioInicial} hasta ${binding.folioFinal}"
                binding.depositos = 0.0f
                binding.retiros = 0.0f

        binding.devoluciones = 0.0f
        def engine = new GStringTemplateEngine()
        def template = engine.createTemplate(plantilla).make(binding)
        template.toString()
    }

    def generarCorteSucursal() {
        def plantilla = new File("Plantillas/FormatoCorte.txt").getText('UTF-8') as String
        //def plantilla = getClass().getResourceAsStream("/omoikane/reportes/FormatoCorte.txt").getText('UTF-8') as String
        def sdfFecha = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a")
        def sdfHora  = new SimpleDateFormat("hh:mm a")
        def sdfDia  = new SimpleDateFormat("EEEEEEEEEE dd-MMM-yyyy  ")
        def binding = data
        binding.fecha  = sdfFecha.format(data.hasta)
        binding.dia    = sdfDia.format(data.desde)
        binding.desde  = sdfHora.format(data.desde)
        binding.hasta  = sdfHora.format(data.hasta)
        binding.devoluciones = 0.0f
        binding.depositos = 0.0f
        binding.retiros = 0.0f

		binding.folios       = ""
        def engine = new GStringTemplateEngine()
        def template = engine.createTemplate(plantilla).make(binding)
        template.toString()
    }

    def generarMovimiento() {
        def plantilla = new File("Plantillas/FormatoMovimiento.txt").getText('UTF-8') as String
        //def plantilla = getClass().getResourceAsStream("/omoikane/reportes/FormatoMovimiento.txt").getText('UTF-8') as String
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
	/* 
	 * Imprime el comprobante generado, alias de probar() 
	 */
    def imprimir() { return probar() }
    def probar() {
        if (impresora)
        {
        try {
            FileOutputStream os = new FileOutputStream("$protocolo");
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
  
