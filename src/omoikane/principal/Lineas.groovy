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
        cat.toFront()
        try { cat.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario catálogo de lineas", Herramientas.getStackTraceString(e)) }
        cat.txtBusqueda.requestFocus()
        poblarLineas(cat.getTablaLineas(),"")

    }
    static def lanzarCatalogoDialogo()
    {
        def cat = new omoikane.formularios.CatalogoLineas(), dial
        cat.btnAceptar.setVisible true
        def swb = SwingBuilder.build {
            dial = dialog(modal:true, pack:true, undecorated:true, location:Herramientas.centrarVentana(cat), preferredSize:cat.preferredSize,
              windowOpened:{
                  try { cat.setSelected(true); } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario catálogo de líneas", Herramientas.getStackTraceString(e)); }
                  cat.txtBusqueda.requestFocus();
              }) {

              widget(cat)
              cat.show true
            }
        }
        cat.internalFrameClosed = { dial.dispose() }
        def retorno
        cat.btnAceptar.actionPerformed = { def catTab = cat.tablaLineas; retorno = catTab.getModel().getValueAt(catTab.getSelectedRow(), 0) as int; cat.btnCerrar.doClick(); }
        poblarLineas(cat.getTablaLineas(),"")
        dial.setVisible(true)  //Aquí se activa el diálogo modal y no proseguirá el script hasta que se cierre
        retorno
    }

    static def lanzarFormNuevoLinea()
    {
        def formLinea = new omoikane.formularios.Linea()
        formLinea.setVisible(true)
        escritorio.getPanelEscritorio().add(formLinea)
        formLinea.toFront()
        try { formLinea.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles Linea", Herramientas.getStackTraceString(e)) }
        formLinea.setEditable(true);
        formLinea.setModoNuevo();
        formLinea
    }

        static def guardar(formLinea)
    {
        def descripcion = formLinea.getTxtDescripcion()

        def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        def tablaLineas = db.dataSet('lineas')
        tablaLineas.add(descripcion:descripcion)
        db.close()
        Dialogos.lanzarAlerta("Linea $descripcion agregado.")
        formLinea.dispose()
    }

    static def lanzarDetallesLinea(ID)
    {
        def formLinea = new omoikane.formularios.Linea()
        formLinea.setVisible(true)
        escritorio.getPanelEscritorio().add(formLinea)
        formLinea.toFront()
        try { formLinea.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles linea", Herramientas.getStackTraceString(e)) }

        def db   = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        def linea = db.rows("SELECT * FROM lineas WHERE id_linea = $ID")

        formLinea.setTxtIDLinea    ((String)linea[0].id_linea)
        formLinea.setTxtDescripcion   (linea[0].descripcion)
        formLinea.setTxtDescuento     ((Double)linea[0].descuento as String)
        formLinea.setTxtUModificacion (linea[0].uModificacion as String)
        formLinea.setModoDetalles();

        db.close()
        formLinea
    }

    static def poblarLineas(tablaMovs,txtBusqueda)
    {

        def dataTabMovs = tablaMovs.getModel()
         try {
            def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
            def movimientos = db.rows(queryLineas =("SELECT * FROM lineas WHERE (descripcion LIKE '%"+txtBusqueda+"%' OR id_linea LIKE '%"+txtBusqueda+"%')") )
            db.close()
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
        formLinea.toFront()
        try { formLinea.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles Linea", Herramientas.getStackTraceString(e)) }

        def db   = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        def linea = db.rows("SELECT * FROM lineas WHERE id_linea = $ID")
        db.close()

        formLinea.setTxtIDLinea    ((String)linea[0].id_linea)
        formLinea.setTxtDescripcion   (linea[0].descripcion)
        formLinea.setTxtDescuento    ((Double)linea[0].descuento as String)
        formLinea.setTxtUModificacion (linea[0].uModificacion as String)
        formLinea.setModoModificar();
        formLinea
    }
    static def modificar(formLinea)
    {
        def db   = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        db.executeUpdate("UPDATE lineas SET descripcion = ? , descuento = ? WHERE id_linea = ?"
            , [
                formLinea.getTxtDescripcion(),
                formLinea.getTxtDescuento(),
                formLinea.getTxtIDLinea()
            ])
        Dialogos.lanzarAlerta("Linea modificado con éxito!")
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

