
 /* Author Phesus        //////////////////////////////
 *  ORC,ACR             /////////////
 *                     /////////////
 *                    /////////////
 *                   /////////////
 * //////////////////////////////                   */

package omoikane.sistema

import java.net.*;
import java.io.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;
import net.sf.jasperreports.view.*;
import java.sql.*;
import javax.swing.*;
import groovy.sql.*;
import groovy.inspect.swingui.*;
import java.awt.*;
import net.sf.jasperreports.engine.data.JRTableModelDataSource
import net.sf.jasperreports.engine.data.JRTableModelDataSource

class Reporte {
    JasperPrint jp;

    Reporte (String reporteJasper, params = [:])
    {
        try {
            def db     = Sql.newInstance(omoikane.principal.Principal.URLMySQL, "Jasper", "gatogato", "com.mysql.jdbc.Driver")
            //def db     = Sql.newInstance(omoikane.principal.Principal.url, "root", "", "com.mysql.jdbc.Driver")
            def stream = cargarPlantilla(reporteJasper)
            jp         = JasperFillManager.fillReport(stream, params, db.connection);
            db.close()
        } catch(Exception e) {
            omoikane.sistema.Dialogos.lanzarDialogoError(null, "Error generando reporte desde base de datos", omoikane.sistema.Herramientas.getStackTraceString(e))
        }
    }
    Reporte(String reporteJasper, javax.swing.JTable tablaj)
    {
        try {
            def stream = cargarPlantilla(reporteJasper)
            jp = JasperFillManager.fillReport(stream, new java.util.HashMap(), new JRTableModelDataSource(tablaj))
        } catch(Exception e) {
            omoikane.sistema.Dialogos.lanzarDialogoError(null, "Error generando reporte desde JTable", omoikane.sistema.Herramientas.getStackTraceString(e))
        }
    }

    Reporte(String reporteJasper,List matriz)
    {
        try {
            def stream = cargarPlantilla(reporteJasper)
            jp = JasperFillManager.fillReport(stream, new java.util.HashMap(), new JRMapCollectionDataSource(matriz))
        } catch(Exception e) {
            omoikane.sistema.Dialogos.lanzarDialogoError(null, "Error generando reporte desde matriz", omoikane.sistema.Herramientas.getStackTraceString(e))
        }
    }
    def cargarPlantilla(String reporteURL)
    {
        def stream = ClassLoader.getSystemResourceAsStream(reporteURL)
        if(stream == null) { throw new Exception("Plantilla de reporte no encontrada. " + reporteURL); }
        return stream
    }
    def toPDF(salida)
    {
        JasperExportManager.exportReportToPdfFzile(jp, salida);
    }
    def lanzarPreview()
    {
        int ancho = 750
        int alto  = 500
        JFrame fj = new JFrame();
        fj.add(new net.sf.jasperreports.view.JRViewer(jp));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension labelSize = new Dimension(ancho,alto)

        int x = screenSize.width/2 - (labelSize.width/2)
        int y = screenSize.height/2 - (labelSize.height/2)

        fj.setLocation(x+90, y+90);

        fj.pack();
        fj.setSize(ancho,alto)
        fj.setVisible(true);

    }


	
}

