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

            def reporte = new Reporte('omoikane/reportes/VentasXLinea3.jasper', [desde:"sdsada2222"]);

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
                        dataListNormal.addElement(it.descripcion)
                    }

                } catch(Exception e) {
                    Dialogos.lanzarDialogoError(null, "Error grave. No hay conexion con la base de datos!", omoikane.sistema.Herramientas.getStackTraceString(e))
                }
            }
}

