package omoikane.moduloreportes

/*
 * @author Phesus-Lab
 */

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
import java.util.logging.Logger
import java.util.Scanner
import org.apache.log4j.Logger;

class Reporte {
    JasperPrint jp;

    public static Logger logger = Logger.getLogger(Reporte.class);
    Reporte (String reporteJasper, params = [:])
    {
        try {
            def conn
            conn = moduloreportes.Comandos.Enlace(conn);
            //def stream = cargarPlantilla(reporteJasper)
            def stream = cargarYCompilarJXML(reporteJasper)
            jp         = JasperFillManager.fillReport(stream, params, conn);
            conn.close()
        } catch(Exception e) {
            logger.error("Error leyendo plantilla de reporte", e);
        }
    }

    Reporte(String reporteJasper, javax.swing.JTable tablaj)
    {
        try {
            //def stream = cargarPlantilla(reporteJasper)
            def stream = cargarYCompilarJXML(reporteJasper)
            jp = JasperFillManager.fillReport(stream, new java.util.HashMap(), new JRTableModelDataSource(tablaj))
        } catch(Exception e) {
            logger.error("Error leyendo plantilla de reporte", e);
        }
    }

    Reporte(String reporteJasper,java.util.List matriz)
    {
        try {
            def stream = cargarYCompilarJXML(reporteJasper)
            jp = JasperFillManager.fillReport(stream, new java.util.HashMap(), new JRMapCollectionDataSource(matriz))
        } catch(Exception e) {
            logger.error("Error leyendo plantilla de reporte", e);
        }
    }

    def cargarPlantilla(String reporteURL)
    {
        //def stream = ClassLoader.getSystemResourceAsStream(reporteURL)
        FileInputStream stream = readFile(reporteURL)
        if(stream == null) { throw new Exception("Plantilla de reporte no encontrada. " + reporteURL); }
        return stream
    }

    def cargarYCompilarJXML(String reporteURL)
    {
       def stream = cargarPlantilla(reporteURL)
       JasperReport report = JasperCompileManager.compileReport(reporteURL);
       return report;
    }

    def toPDF(salida)
    {
        JasperExportManager.exportReportToPdfFzile(jp, salida);
    }

    def lanzarPreview(form)
    {
        form.panel.removeAll();
        form.panel.add(new net.sf.jasperreports.view.JRViewer(jp));
        form.panel.updateUI()
    }
    
    private FileInputStream readFile(String filename) throws IOException {
        File file = new File( filename );
        if ( !file.exists() ) {
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));
            out.close();
        }
        FileInputStream fis = new FileInputStream(file);
        return fis;
    }



}


