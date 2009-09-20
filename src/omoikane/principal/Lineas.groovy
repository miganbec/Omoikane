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
import static omoikane.sistema.Usuarios.*;
import static omoikane.sistema.Permisos.*;

/**
 *
 * @author Octavio
 */
class Lineas {
    static def queryLineas  = ""
    static def escritorio = omoikane.principal.Principal.escritorio

      
    static def lanzarCatalogo()
    {
        if(cerrojo(PMA_ABRIRLINEA)){
            def cat = (new omoikane.formularios.CatalogoLineas())
            cat.setVisible(true);
            escritorio.getPanelEscritorio().add(cat)
            Herramientas.In2ActionX(cat, KeyEvent.VK_ESCAPE, "cerrar"   ) { cat.btnCerrar.doClick()   }
            cat.txtBusqueda.keyReleased = { if(it.keyCode == it.VK_ESCAPE) cat.btnCerrar.doClick() }
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
            return cat
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}

    }

    static def lanzarCatalogoDialogo()
    {
        def foco=new Object()
        def cat = lanzarCatalogo()
        cat.setModoDialogo()
        cat.internalFrameClosed = {synchronized(foco){foco.notifyAll()} }
        cat.txtBusqueda.keyReleased = { if(it.keyCode == it.VK_ENTER) cat.btnAceptar.doClick() }
        def retorno
        cat.btnAceptar.actionPerformed = { def catTab = cat.tablaLineas; retorno = catTab.getModel().getValueAt(catTab.getSelectedRow(), 0) as String; cat.btnCerrar.doClick(); }
        synchronized(foco){foco.wait()}
        retorno
    }

    static def lanzarFormNuevoLinea()
    {
        if(cerrojo(PMA_MODIFICARLINEA)){
            def formLinea = new omoikane.formularios.Linea()
            formLinea.setVisible(true)
            escritorio.getPanelEscritorio().add(formLinea)
            Herramientas.iconificable(formLinea)
            formLinea.toFront()
            try { formLinea.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles Linea", Herramientas.getStackTraceString(e)) }
            formLinea.setEditable(true);
            formLinea.setModoNuevo();
            return formLinea
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def guardar(formLinea)
    {
        if(cerrojo(PMA_MODIFICARLINEA)){
            Herramientas.verificaCampos{
                def descripcion = formLinea.getTxtDescripcion()
                def descuento   = formLinea.getTxtDescuento()
                Herramientas.verificaCampo(formLinea.getTxtDescripcion(),Herramientas.texto,"Descripcion"+Herramientas.error1)
                Herramientas.verificaCampo(formLinea.getTxtDescuento(),Herramientas.numeroReal,"Descuento"+Herramientas.error3)
                descuento = descuento as Double
                try {
                    def serv = Nadesico.conectar()
                    Dialogos.lanzarAlerta(serv.addLinea(descripcion, descuento))
                } catch(e) { Dialogos.error("Error al enviar a la base de datos. La linea no se registró", e) }
                formLinea.dispose()
            }
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def lanzarDetallesLinea(ID)
    {
        if(cerrojo(PMA_DETALLESLINEA)){
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
            return formLinea
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def poblarLineas(tablaMovs,txtBusqueda)
    {
        def dataTabMovs = tablaMovs.getModel()
         try {
            def movimientos = Nadesico.conectar().getRows(queryLineas =("SELECT * FROM lineas WHERE (descripcion LIKE '%"+txtBusqueda+"%' OR id_linea LIKE '%"+txtBusqueda+"%')") )
            def filaNva = []
            movimientos.each {
                filaNva = [it.id_linea, it.descripcion,it.descuento]
                dataTabMovs.addRow(filaNva.toArray())
            }
        } catch(Exception e) {
            Dialogos.lanzarDialogoError(null, "Error grave. No hay conexion con la base de datos!", omoikane.sistema.Herramientas.getStackTraceString(e))
        }
    }

    static def lanzarModificarLinea(ID)
    {

        def formLinea = lanzarDetallesLinea(ID)
        formLinea.setModoModificar();
        formLinea
    }

    static def modificar(formLinea)
    {
        if(cerrojo(PMA_MODIFICARLINEA)){
            Herramientas.verificaCampos{
                Herramientas.verificaCampo(formLinea.getTxtDescripcion(),/^([a-zA-Z0-9_\-\s\&\ñ\Ñ\+áéíóúüàèìòùÁÉÍÓÚÀÈÌÒÙÜ\\\%\.\/\"\'\,\;\.\:\#\@]+)$/,"Descripcion solo puede incluir numeros, letras, espacios, acentos, diagonales, coma, comillas y los siguientes caracteres . _ - + % ; : # @ & ")
                Herramientas.verificaCampo(formLinea.getTxtDescuento(),/^([0-9]*[\.]{0,1}[0-9]+)$/,"Descuento sólo puede incluír números reales positivos")
                def serv = Nadesico.conectar()
                Dialogos.lanzarAlerta(serv.modLinea(formLinea.getTxtIDLinea(),formLinea.getTxtDescripcion(),formLinea.getTxtDescuento()))
            }
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def eliminarLinea(ID)
    {
        if(cerrojo(PMA_ELIMINARLINEA)){
            def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
            db.execute("DELETE FROM lineas WHERE id_linea = " + ID)
            db.close()
            Dialogos.lanzarAlerta("Linea " + ID + " supuestamente eliminado")
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
        
    }

    static def lanzarImprimir()
    {
        def reporte = new Reporte('omoikane/reportes/ReporteLineas.jasper', [QueryTxt:queryLineas]);
        reporte.lanzarPreview()
    }
}