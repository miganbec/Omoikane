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
import static omoikane.sistema.Usuarios.*;
import static omoikane.sistema.Permisos.*;

/**
 *
 * @author Adan
 */
class Ventas {

    static def lastMovID  = -1
    static def IDAlmacen = Principal.IDAlmacen
    static def escritorio = omoikane.principal.Principal.escritorio
    //static def getVenta(where) { new Venta(where)}

    static def lanzarCatalogo()
    {
        if(cerrojo(PMA_ABRIRVENTAS)){
            def cat = (new omoikane.formularios.CatalogoVentas())
            cat.setVisible(true);
            escritorio.getPanelEscritorio().add(cat)
            Herramientas.setColumnsWidth(cat.jTable1, [0.2,0.1,0.1,0.25,0.25,0.1]);
            Herramientas.In2ActionX(cat, KeyEvent.VK_ESCAPE, "cerrar"   ) { cat.btnCerrar.doClick()   }
            cat.txtBusqueda.keyReleased = { if(it.keyCode == it.VK_ESCAPE) cat.btnCerrar.doClick() }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F4    , "detalles" ) { cat.btnDetalles.doClick() }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F8    , "imprimir" ) { cat.btnImprimir.doClick() }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F2    , "filtrar" ) { cat.btnFiltrar.doClick() }
            Herramientas.iconificable(cat)
            cat.toFront()
            try { cat.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario catálogo ventas", Herramientas.getStackTraceString(e)) }
            cat.txtBusqueda.requestFocus()
            return cat
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def lanzarDetalles(ID)
    {
        if(cerrojo(PMA_DETALLESVENTAS)){
            lastMovID = ID
            def form = (new omoikane.formularios.VentasDetalles())
            form.setVisible(true);
            escritorio.getPanelEscritorio().add(form)
            Herramientas.setColumnsWidth(form.jTable1, [0.2,0.5,0.1,0.1,0.1])
            Herramientas.In2ActionX(form, KeyEvent.VK_ESCAPE, "cerrar"   ) { form.btnCerrar.doClick()   }
            Herramientas.In2ActionX(form, KeyEvent.VK_F8    , "imprimir" ) { form.btnImprimir.doClick() }
            Herramientas.iconificable(form)
            form.toFront()
            try { form.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario nuevo movimiento de almacén", Herramientas.getStackTraceString(e)) }
            def mov         = Nadesico.conectar().getVenta(ID,IDAlmacen)
            form.setCliente(mov.nombreCliente as String)
            form.setDescuento(mov.descuento as String)
            form.setImpuesto(mov.impuestos as String)
            //form.setTipoSalida(mov.tabMatriz as String)
            form.setSubtotal(mov.subtotal as String)
            form.setTotal(mov.total as String)
            form.setAlmacen(mov.nombreAlmacen as String)
            form.setFecha(mov.fecha_hora as String)
            form.setTablaPrincipal(mov.tabMatriz as List)
            return form
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def lanzarImprimir(queryMovs)
    {
        def reporte = new Reporte('omoikane/reportes/ReporteVentas.jasper', [QueryTxt:queryMovs]);
        reporte.lanzarPreview()
    }

    static def lanzarImprimirVenta(form)
    {
        def reporte = new Reporte('omoikane/reportes/VentaEncabezado.jasper',[SUBREPORT_DIR:"omoikane/reportes/",IDMov:lastMovID as String]);
        reporte.lanzarPreview()
    }

    static def lanzarImprimirFactura(form)
    {
        def reporte = new Reporte('omoikane/reportes/FacturaEncabezado.jasper',[SUBREPORT_DIR:"omoikane/reportes/",IDVenta:lastMovID as String]);
        reporte.lanzarPreview()
    }

}

