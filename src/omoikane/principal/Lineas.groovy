/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.principal

import omoikane.principal.*
import omoikane.sistema.*
import groovy.sql.*;
import groovy.swing.*;
import javax.swing.*;
import java.awt.*;
import omoikane.sistema.*;
import javax.swing.event.*;
import java.awt.event.*;

/**
 *
 * @author Octavio
 */
class Lineas {
    static def queryLineas  = ""
    static def escritorio = omoikane.principal.Principal.escritorio

      
    static def lanzarCatalogo()
    {
        def cat = (new omoikane.formularios.CatalogoLineas())
        cat.setVisible(true);
        escritorio.getPanelEscritorio().add(cat)

        Herramientas.In2ActionX(cat, KeyEvent.VK_ESCAPE, "cerrar"   ) { cat.btnCerrar.doClick()   }
        Herramientas.In2ActionX(cat, KeyEvent.VK_DELETE, "eliminar" ) { cat.btnEliminas.doClick() }
        Herramientas.In2ActionX(cat, KeyEvent.VK_F4    , "detalles" ) { cat.btnDetalles.doClick() }
        Herramientas.In2ActionX(cat, KeyEvent.VK_F5    , "nuevo"    ) { cat.btnNuevo.doClick() }
        Herramientas.In2ActionX(cat, KeyEvent.VK_F6    , "modificar") { cat.btnModificar.doClick() }
        Herramientas.In2ActionX(cat, KeyEvent.VK_F7    , "imprimir" ) { cat.btnImprimir.doClick() }
        Herramientas.iconificable(cat)

        cat.toFront()
        try { cat.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario catálogo de lineas", Herramientas.getStackTraceString(e)) }
        cat.txtBusqueda.requestFocus()
        poblarLineas(cat.getTablaLineas(),"")

    }
    static def lanzarCatalogoDialogo()
    {
       def foco=new Object()
        def cat = (new omoikane.formularios.CatalogoLineas())
        cat.setVisible(true);
        escritorio.getPanelEscritorio().add(cat)
        Herramientas.In2ActionX(cat, KeyEvent.VK_ESCAPE, "cerrar"   ) { cat.btnCerrar.doClick()   }
        Herramientas.In2ActionX(cat, KeyEvent.VK_DELETE, "eliminar" ) { cat.btnEliminas.doClick() }
        Herramientas.In2ActionX(cat, KeyEvent.VK_F4    , "detalles" ) { cat.btnDetalles.doClick() }
        Herramientas.In2ActionX(cat, KeyEvent.VK_F5    , "nuevo"    ) { cat.btnNuevo.doClick() }
        Herramientas.In2ActionX(cat, KeyEvent.VK_F6    , "modificar") { cat.btnModificar.doClick() }
        Herramientas.In2ActionX(cat, KeyEvent.VK_F7    , "imprimir" ) { cat.btnImprimir.doClick() }
        Herramientas.iconificable(cat)
        cat.toFront()
        try { cat.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario catalogo de grupos", Herramientas.getStackTraceString(e)) }
        cat.txtBusqueda.requestFocus()
        cat.internalFrameClosed = {synchronized(foco){foco.notifyAll()} }
        cat.txtBusqueda.keyPressed = { if(it.keyCode == it.VK_ENTER) cat.btnAceptar.doClick() }
        def retorno
        cat.btnAceptar.actionPerformed = { def catTab = cat.tablaLineas; retorno = catTab.getModel().getValueAt(catTab.getSelectedRow(), 0) as int; cat.btnCerrar.doClick(); }
        poblarLineas(cat.getTablaLineas(),"")
        synchronized(foco){foco.wait()}
        retorno
    }

    static def lanzarFormNuevoLinea()
    {
        def formLinea = new omoikane.formularios.Linea()
        formLinea.setVisible(true)
        escritorio.getPanelEscritorio().add(formLinea)
        Herramientas.iconificable(formLinea)
        formLinea.toFront()
        try { formLinea.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles Linea", Herramientas.getStackTraceString(e)) }
        formLinea.setEditable(true);
        formLinea.setModoNuevo();
        formLinea
    }

        static def guardar(formLinea)
    {
        Herramientas.verificaCampos{
        def descripcion = formLinea.getTxtDescripcion()
        def descuento   = formLinea.getTxtDescuento()

        Herramientas.verificaCampo(formLinea.getTxtDescripcion(),/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+áéíóúü]+)$/,"Descripcion sólo puede incluír números, letras, espacios, á, é, í, ó, ú, ü, _, -, * y +.")
        Herramientas.verificaCampo(formLinea.getTxtDescuento(),/^([0-9]*[\.]{0,1}[0-9]+)$/,"Descuento sólo puede incluír números reales positivos")

        descuento = descuento as Double

        try {
            def serv = Nadesico.conectar()
            Dialogos.lanzarAlerta(serv.addLinea(descripcion, descuento))
        } catch(e) { Dialogos.error("Error al enviar a la base de datos. La linea no se registró", e) }

        formLinea.dispose()
        }
    }

    static def lanzarDetallesLinea(ID)
    {
        def formLinea = new omoikane.formularios.Linea()
        formLinea.setVisible(true)
        escritorio.getPanelEscritorio().add(formLinea)
        Herramientas.iconificable(formLinea)
        formLinea.toFront()
        try { formLinea.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles linea", Herramientas.getStackTraceString(e)) }

        def lin         = Nadesico.conectar().getLinea(ID)
        
        formLinea.setTxtIDLinea        lin.id_linea      as String
        formLinea.setTxtDescripcion    lin.descripcion
        formLinea.setTxtDescuento      lin.descuento     as String
        formLinea.setTxtUModificacion  lin.uModificacion as String
        formLinea.setModoDetalles();
        formLinea
    }

    static def poblarLineas(tablaMovs,txtBusqueda)
    {

        def dataTabMovs = tablaMovs.getModel()
         try {
            def movimientos = Nadesico.conectar().getRows(queryLineas =("SELECT * FROM lineas WHERE (descripcion LIKE '%"+txtBusqueda+"%' OR id_linea LIKE '%"+txtBusqueda+"%')") )
            def filaNva = []

            movimientos.each {
                filaNva = [it.id_linea, it.descripcion]
                dataTabMovs.addRow(filaNva.toArray())
            }
        } catch(Exception e) {
            Dialogos.lanzarDialogoError(null, "Error grave. No hay conexion con la base de datos!", omoikane.sistema.Herramientas.getStackTraceString(e))
        }
    }

    static def lanzarModificarLinea(ID)
    {
        def formLinea = new omoikane.formularios.Linea()
        formLinea.setVisible(true)
        escritorio.getPanelEscritorio().add(formLinea)
        Herramientas.iconificable(formLinea)
        formLinea.toFront()
        try { formLinea.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles Linea", Herramientas.getStackTraceString(e)) }

        def lin         = Nadesico.conectar().getLinea(ID)

        formLinea.setTxtIDLinea        lin.id_linea      as String
        formLinea.setTxtDescripcion    lin.descripcion
        formLinea.setTxtDescuento      lin.descuento     as String
        formLinea.setTxtUModificacion  lin.uModificacion as String
        formLinea.setModoModificar();
        formLinea
    }

    static def modificar(formLinea)
    {
        Herramientas.verificaCampos{
        Herramientas.verificaCampo(formLinea.getTxtDescripcion(),/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+áéíóúü]+)$/,"Descripcion sólo puede incluír números, letras, espacios, á, é, í, ó, ú, ü, _, -, * y +.")
        Herramientas.verificaCampo(formLinea.getTxtDescuento(),/^([0-9]*[\.]{0,1}[0-9]+)$/,"Descuento sólo puede incluír números reales positivos")
        
        def serv = Nadesico.conectar()
            Dialogos.lanzarAlerta(serv.modLinea(formLinea.getTxtIDLinea(),formLinea.getTxtDescripcion(),formLinea.getTxtDescuento()))
        }
    }

        static def eliminarLinea(ID)
    {
        def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        db.execute("DELETE FROM lineas WHERE id_linea = " + ID)
        db.close()
        Dialogos.lanzarAlerta("Linea " + ID + " supuestamente eliminado")
        
    }

        static def lanzarImprimir()
    {
        def reporte = new Reporte('omoikane/reportes/ReporteLineas.jasper', [QueryTxt:queryLineas]);
        reporte.lanzarPreview()
    }
}

