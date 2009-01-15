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
import groovy.swing.*

/**
 *
 * @author Adan
 */
class Cortes {

    static def lastMovID  = -1
    static def IDAlmacen = 1
    static def escritorio = omoikane.principal.Principal.escritorio
    //static def getVenta(where) { new Venta(where)}

    static def lanzarCatalogo()
    {
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
    }

    static def lanzarVentanaDetalles() {
        def form = (new omoikane.formularios.CorteCajaDetalles())
        form.setVisible(true);
        escritorio.getPanelEscritorio().add(form)
        Herramientas.iconificable(form)

        form.toFront()
        try { form.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario nuevo movimiento de almacén", Herramientas.getStackTraceString(e)) }
        form
    }
    static def lanzarDetalles(ID)
    {
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

        form
    }

    static def lanzarImprimir(queryMovs)
    {
        def reporte = new Reporte('omoikane/reportes/ReporteCortesCaja.jasper', [QueryTxt:queryMovs]);
        reporte.lanzarPreview()
    }

}

