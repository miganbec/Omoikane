/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.ppppzzzzpp
 */

package omoikane.principal

import omoikane.principal.*
import omoikane.sistema.*
import groovy.sql.*;
import groovy.swing.*;
import javax.swing.*;
import java.awt.event.WindowListener;
import javax.swing.event.*;
import groovy.inspect.swingui.*;
import javax.swing.table.TableColumn
import java.awt.event.*;
import java.text.SimpleDateFormat
import groovy.swing.*
import static omoikane.sistema.Usuarios.*;
import static omoikane.sistema.Permisos.*;

/**
 *
 * @author Adan
 */
class Cortes {

    static def lastMovID  = -1
    static def IDAlmacen = Principal.IDAlmacen
    static def escritorio = omoikane.principal.Principal.escritorio
    //static def getVenta(where) { new Venta(where)}

    static def lanzarCatalogo()
    {
        if(cerrojo(PMA_ABRIRCORTES)){
            def cat = (new omoikane.formularios.CatalogoCortesCaja())
            cat.setVisible(true);
            escritorio.getPanelEscritorio().add(cat)
            Herramientas.setColumnsWidth(cat.jTable1, [0.2,0.1,0.25,0.25,0.1,0.1]);
            Herramientas.In2ActionX(cat, KeyEvent.VK_ESCAPE, "cerrar"   ) { cat.btnCerrar.doClick()   }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F4    , "detalles" ) { cat.btnDetalles.doClick() }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F8    , "imprimir" ) { cat.btnImprimir.doClick() }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F2    , "filtrar" ) { cat.btnFiltrar.doClick() }
            cat.txtBusqueda.keyReleased = { if(it.keyCode == it.VK_ESCAPE) cat.btnCerrar.doClick() }
            Herramientas.iconificable(cat)
            cat.toFront()
            try { cat.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario catálogo ventas", Herramientas.getStackTraceString(e)) }
            cat.txtBusqueda.requestFocus()
            return cat
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def lanzarVentanaDetalles() {
        if(cerrojo(PMA_DETALLESVENTAS)){
            def form = (new omoikane.formularios.CorteCajaDetalles())
            form.setVisible(true);
            escritorio.getPanelEscritorio().add(form)
            Herramientas.iconificable(form)
            form.toFront()
            try { form.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario nuevo movimiento de almacén", Herramientas.getStackTraceString(e)) }
            return form
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def lanzarDetalles(ID)
    {
        if(cerrojo(PMA_DETALLESCORTES)){
            lastMovID = ID
            def form  = lanzarVentanaDetalles()
            def mov   = Nadesico.conectar().getCorteWhere(" cortes.id_corte=$ID")
            form.setTxtDescuento     (mov.descuentos as String)
            form.setTxtDesde         (mov.desde as String)
            form.setTxtFecha         (mov.fecha_hora as String)
            form.setTxtHasta         (mov.hasta as String)
            form.setTxtIDAlmacen     (mov.id_almacen as String)
            form.setTxtIDCaja        (mov.id_caja as String)
            form.setTxtIDCorte       (mov.id_corte as String)
            form.setTxtImpuesto      (mov.impuestos as String)
            form.setTxtNumeroVenta   (mov.n_ventas as String)
            form.setTxtSubtotal      (mov.subtotal as String)
            form.setTxtTotal         (mov.total as String)
            return form
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def lanzarImprimir(queryMovs)
    {
        def reporte = new Reporte('omoikane/reportes/ReporteCortesCaja.jasper', [QueryTxt:queryMovs]);
        reporte.lanzarPreview()
    }
    static def lanzarVentanaCorteSucursal(resultadoCorte,IDAlmacen, IDCorte) {
        if(cerrojo(PMA_TOTALVENTASUCURSAL)) {
            
            def form = (new omoikane.formularios.CorteSucursalDetalles())
            def rc   = resultadoCorte

            form.setVisible(true);
            escritorio.getPanelEscritorio().add(form)
            Herramientas.iconificable(form)
            form.toFront()
            try { form.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario nuevo movimiento de almacén", Herramientas.getStackTraceString(e)) }
            form.txtNVentas.text   = rc.n_ventas
            form.txtImpuestos.text = rc.impuestos
            form.txtDescuentos.text= rc.descuentos
            form.txtSubtotal.text  = rc.subtotal
            form.txtTotal.text     = rc.total
            form.btnAceptar.actionPerformed  = { form.dispose() }
            form.btnImprimir.actionPerformed = {
                def comprobante = new Comprobantes()
                (comprobante.CorteSucursal(IDAlmacen, IDCorte))//imprimir ticket
                (comprobante.probar())//* Aqui tambien mandar a imprimir*/
                }
            
            return form
            
        } else { Dialogos.lanzarAlerta("Acceso Denegado") }

    }
    static def lanzarCorteSucursal(IDAlmacen, cortar=false)
    {
        if(cerrojo(PMA_TOTALVENTASUCURSAL)) {
            def paso = 1
            switch(paso) {
                case 1:
                    if(Sucursales.existe(IDAlmacen)) { paso = 2 } else { Dialogos.lanzarAlerta("Sucursal inválida, probablemente sea error de configuración!"); break; }
                    //break;
                case 2:
                    def abierta = Sucursales.abierta(IDAlmacen)
                    if(abierta==1) { paso = 3 } else { Dialogos.lanzarAlerta("Sucursal inhabilitada, no se han iniciado ventas o hay un corte pendiente."); break }
                case 3:
                    def cajasCerradas = Sucursales.cajasSucursalCerradas(IDAlmacen)
                    if(cajasCerradas) { paso = 4 } else { Dialogos.lanzarAlerta("No se puede continuar. Hay cajas abiertas, debe cerrarlas para continuar."); break }
                case 4:
                    Sucursales.cerrar(IDAlmacen)
                    def IDCorte        = Sucursales.corte(IDAlmacen)
                    def resultadoCorte = Sucursales.sumaCorte(IDAlmacen, IDCorte)
                    lanzarVentanaCorteSucursal(resultadoCorte,IDAlmacen, IDCorte)
                    // Aquí mandar a imprimir resultadoCorte (también agregar imprimir en la función anterior (lanzarVentanaCorteSucursal))
                break
            }
            /*
            def serv = Nadesico.conectar()
            def res  = serv.getCaja(IDCaja);
            if(res == 0) {Dialogos.lanzarAlerta("No exíste esa caja")} else {
                if(!serv.cajaAbierta(IDCaja)) {Dialogos.lanzarAlerta("La caja ya estaba cerrada")}
                def horas      = serv.getCaja(IDCaja)
                SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy @ hh:mm:ss a ");
                Calendar         fecha= Calendar.getInstance()
                if(serv.getCorteWhere("id_caja = $IDCaja AND desde = '${sdf.format(horas.horaAbierta)}' AND hasta = '${sdf.format(horas.horaCerrada)}'")!=0) {
                    Dialogos.lanzarAlerta("Ya se realizó corte de Caja y no se han hecho ventas desde el último corte de caja")
                } else {
                    if(cortar) { serv.cerrarCaja(IDCaja) }
                    horas = serv.getCaja(IDCaja)
                    def ventas= serv.sumaVentas(IDCaja, sdf.format(horas.horaAbierta), sdf.format(horas.horaCerrada))
                    def form  = Cortes.lanzarVentanaDetalles()
                    def caja  = serv.getCaja(IDCaja)
                    def desde = horas.horaAbierta
                    def hasta = horas.horaCerrada
                    form.setTxtDescuento     (ventas.descuento as String)
                    form.setTxtDesde         (sdf2.format(desde) as String)
                    form.setTxtFecha         (sdf2.format(fecha.getTime()) as String)
                    form.setTxtHasta         (sdf2.format(hasta) as String)
                    form.setTxtIDAlmacen     (caja.id_almacen as String)
                    form.setTxtIDCaja        (IDCaja as String)
                    //form.setTxtIDCorte       (ventas.id_corte as String)
                    form.setTxtImpuesto      (ventas.impuestos as String)
                    form.setTxtNumeroVenta   (ventas.nVentas as String)
                    form.setTxtSubtotal      (ventas.subtotal as String)
                    form.setTxtTotal         (ventas.total as String)
                    if(cortar) { Dialogos.lanzarAlerta(serv.addCorte(IDCaja, caja.id_almacen, ventas.subtotal, ventas.descuento, ventas.impuestos, ventas.total, ventas.nVentas, desde, hasta)) }
                }
            }
            serv.desconectar() */
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }


}

