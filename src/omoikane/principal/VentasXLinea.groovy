/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.principal

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
class VentasXLinea {
        def form
        def queryLineas  = ""
        static def escritorio = omoikane.principal.Principal.escritorio

	VentasXLinea(form) {
            this.form = form
            VentasXLinea.escritorio.getPanelEscritorio().add(form)
            form.btnAceptar.actionPerformed = { lanzarReporte() }
            
            form.setVisible true
            poblarLineas()
        }

        def lanzarReporte() {
            def list
            list = (String) (form.getLineas()*.id)
            list=list.replace('[','')
            list=list.replace(']','')
            def reporte = new Reporte('omoikane/reportes/VentasXLinea.jasper', [FDesde:form.getFechaDesde(),FHasta:form.getFechaHasta(),Lineas:list]);
            reporte.lanzarPreview()
        }

        def poblarLineas()
            {
                def listaNormales = (form.listaLineas)
                def dataListNormal

                dataListNormal = (new DefaultListModel())
                listaNormales.setModel(dataListNormal)

                 try {
                    def serv = Nadesico.conectar()
                    def filasTabNormal = serv.getRows(queryLineas =("SELECT lineas.id_linea, lineas.descripcion FROM lineas WHERE lineas.id_linea NOT IN (SELECT lineas_dual.id_linea FROM lineas_dual)"))
                    filasTabNormal.each {
                        dataListNormal.addElement(new ElementoListaLineas(id:it.id_linea,nombre:it.descripcion))
                    }

                } catch(Exception e) {
                    Dialogos.lanzarDialogoError(null, "Error grave. No hay conexion con la base de datos!", omoikane.sistema.Herramientas.getStackTraceString(e))
                }
            }


}

class ElementoListaLineas{
    def id
    def nombre
    String toString(){
        return String.valueOf(nombre)
    }
}

