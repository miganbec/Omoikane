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
class Grupos {
    static def queryGrupos  = ""
    static def escritorio = omoikane.principal.Principal.escritorio

      
    static def lanzarCatalogo()
    {
        if(cerrojo(PMA_ABRIRGRUPO)){
            def cat = (new omoikane.formularios.CatalogoGrupos())
            cat.setVisible(true);
            cat.jProgressBar1.setIndeterminate(true);
            escritorio.getPanelEscritorio().add(cat)
            cat.txtBusqueda.keyReleased = { if(it.keyCode == it.VK_ESCAPE) cat.btnCerrar.doClick() }
            Herramientas.iconificable(cat)
            cat.toFront()
            try { cat.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario catálogo de Grupos", Herramientas.getStackTraceString(e)) }
            cat.txtBusqueda.requestFocus()

            Thread.start {
                poblarGrupos(cat.getTablaGrupos(),"")
                cat.jProgressBar1.setIndeterminate(false);
            }
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
        cat.btnAceptar.actionPerformed = { def catTab = cat.tablaGrupos; retorno = catTab.getModel().getValueAt(catTab.getSelectedRow(), 0) as String; cat.btnCerrar.doClick(); }
        synchronized(foco){foco.wait()}
        retorno
    }

    static def lanzarFormNuevoGrupo()
    {
        if(cerrojo(PMA_MODIFICARGRUPO)){
            def formGrupo = new omoikane.formularios.Grupo()
            formGrupo.setVisible(true)
            escritorio.getPanelEscritorio().add(formGrupo)
            Herramientas.iconificable(formGrupo)
            formGrupo.toFront()
            try { formGrupo.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles Grupo", Herramientas.getStackTraceString(e)) }
            formGrupo.setEditable(true);
            formGrupo.setModoNuevo();
            return formGrupo
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def guardar(formGrupo)
    {
        if(cerrojo(PMA_MODIFICARGRUPO)){
            Herramientas.verificaCampos{
                def descripcion = formGrupo.getTxtDescripcion()
                def descuento   = formGrupo.getTxtDescuento()
                Herramientas.verificaCampo(formGrupo.getTxtDescripcion(),/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+áéíóúü]+)$/,"Descripcion sólo puede incluír números, letras, espacios, á, é, í, ó, ú, ü, _, -, * y +.")
                Herramientas.verificaCampo(formGrupo.getTxtDescuento(),/^([0-9]*[\.]{0,1}[0-9]+)$/,"Descuento sólo puede incluír números reales positivos")
                descuento = descuento as Double
                try {
                    def serv = Nadesico.conectar()
                    Dialogos.lanzarAlerta(serv.addGrupo(descripcion, descuento))
                } catch(e) { Dialogos.error("Error al enviar a la base de datos. El grupo no se registró", e) }
                formGrupo.dispose()
            }
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def lanzarDetallesGrupo(ID)
    {
        if(cerrojo(PMA_DETALLESGRUPO)){
            def formGrupo = new omoikane.formularios.Grupo()
            formGrupo.setVisible(true)
            escritorio.getPanelEscritorio().add(formGrupo)
            Herramientas.iconificable(formGrupo)
            formGrupo.toFront()
            try { formGrupo.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles Grupo", Herramientas.getStackTraceString(e)) }
            def gru         = Nadesico.conectar().getGrupo(ID)
            formGrupo.setTxtIDGrupo        gru.id_grupo      as String
            formGrupo.setTxtDescripcion    gru.descripcion
            formGrupo.setTxtDescuento      gru.descuento     as String
            formGrupo.setTxtUModificacion  gru.uModificacion as String
            formGrupo.setModoDetalles();
            return formGrupo
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def poblarGrupos(tablaMovs,txtBusqueda)
    {

        def dataTabMovs = tablaMovs.getModel()
         try {
            def movimientos = Nadesico.conectar().getRows(queryGrupos =("SELECT * FROM grupos WHERE (descripcion LIKE '%"+txtBusqueda+"%' OR id_grupo LIKE '%"+txtBusqueda+"%')") )
            def filaNva = []

            movimientos.each {
                filaNva = [it.id_grupo, it.descripcion,it.descuento]
                dataTabMovs.addRow(filaNva.toArray())
            }
        } catch(Exception e) {
            Dialogos.lanzarDialogoError(null, "Error grave. No hay conexion con la base de datos!", omoikane.sistema.Herramientas.getStackTraceString(e))
        }
    }

    static def lanzarModificarGrupo(ID)
    {
        def formGrupo = lanzarDetallesGrupo(ID)
        formGrupo.setModoModificar();
        formGrupo
    }

    static def modificar(formGrupo)
    {
        if(cerrojo(PMA_MODIFICARGRUPO)){
            Herramientas.verificaCampos{
                Herramientas.verificaCampo(formGrupo.getTxtDescripcion(),/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+áéíóúü]+)$/,"Descripcion sólo puede incluír números, letras, espacios, á, é, í, ó, ú, ü, _, -, * y +.")
                Herramientas.verificaCampo(formGrupo.getTxtDescuento(),/^([0-9]*[\.]{0,1}[0-9]+)$/,"Descuento sólo puede incluír números reales positivos")
                def serv = Nadesico.conectar()
                Dialogos.lanzarAlerta(serv.modGrupo(formGrupo.getTxtIDGrupo(),formGrupo.getTxtDescripcion(),formGrupo.getTxtDescuento()))
            }
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def eliminarGrupo(ID)
    {
        if(cerrojo(PMA_ELIMINARGRUPO)){
            def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
            db.execute("DELETE FROM grupos WHERE id_grupo = " + ID)
            db.close()
            Dialogos.lanzarAlerta("Grupo " + ID + " supuestamente eliminado")
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
        
    }

        static def lanzarImprimir()
    {
        def reporte = new Reporte('omoikane/reportes/ReporteGrupos.jasper', [QueryTxt:queryGrupos]);
        reporte.lanzarPreview()
    }
}