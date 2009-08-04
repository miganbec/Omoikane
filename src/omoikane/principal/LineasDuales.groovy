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
 */

class LineasDuales {

    static def escritorio = omoikane.principal.Principal.escritorio
    static def queryLineas  = ""
    static def cat

    static def lanzarCatalogo()
    {
        if(cerrojo(PMA_DUAL)){
            cat = (new omoikane.formularios.CatalogoDual())
            cat.setVisible(true);
            escritorio.getPanelEscritorio().add(cat)
            Herramientas.In2ActionX(cat, KeyEvent.VK_ESCAPE, "cerrar"   ) { cat.btnCerrar.doClick()   }
            Herramientas.iconificable(cat)
            cat.toFront()
            try { 
                cat.setSelected(true)
            } catch(Exception e) {
                Dialogos.lanzarDialogoError(null, "Error al iniciar formulario catálogo de lineas", Herramientas.getStackTraceString(e))
            }
            poblarLineas()
            return cat
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def poblarLineas()
    {
        def listaNormales = (cat.getListaNormales())
        def listaDuales = (cat.getListaDuales())
        def dataListNormal
        def dataListDual

        dataListNormal = (new DefaultListModel())
        listaNormales.setModel(dataListNormal)

        dataListDual = (new DefaultListModel())
        listaDuales.setModel(dataListDual)
            
         try {
            def serv = Nadesico.conectar()
            def filasTabNormal = serv.getRows(queryLineas =("SELECT lineas.id_linea, lineas.descripcion FROM lineas WHERE lineas.id_linea NOT IN (SELECT lineas_dual.id_linea FROM lineas_dual)"))
            filasTabNormal.each {
                dataListNormal.addElement(it.descripcion)
            }
            
            def filasTabDual = serv.conectar().getRows(queryLineas =("SELECT lineas.id_linea, lineas.descripcion FROM lineas WHERE lineas.id_linea IN (SELECT lineas_dual.id_linea FROM lineas_dual)") )
            filasTabDual.each {
                dataListDual.addElement(it.descripcion)
            }
            
        } catch(Exception e) {
            Dialogos.lanzarDialogoError(null, "Error grave. No hay conexion con la base de datos!", omoikane.sistema.Herramientas.getStackTraceString(e))
        }
    }

    static def cambioLineaNormalADual () {

        def listaNormales = (cat.getListaNormales())
        def listaDuales = (cat.getListaDuales())
        def seleccion = (cat.getSeleccionNormales())

        try {
            def serv = Nadesico.conectar()
           
            (0..<seleccion.length).each {
                serv.addLineaD(String.valueOf(seleccion[it]))
            }
            
            poblarLineas()
            
        } catch(e) {
            Dialogos.error("Error al enviar a la base de datos. La línea no se registró", e)
        }

    }

    static def cambioLineaDualANormal () {
        def listaNormales = (cat.getListaNormales())
        def listaDuales = (cat.getListaDuales())
        def seleccion = (cat.getSeleccionDuales())

        try {
            def serv = Nadesico.conectar()

            (0..<seleccion.length).each {
                serv.delLineaD(String.valueOf(seleccion[it]))
            }

            poblarLineas()
            
        } catch(e) {
            Dialogos.error("Error al enviar a la base de datos. La linea no se registró", e)
        }
    }

}
