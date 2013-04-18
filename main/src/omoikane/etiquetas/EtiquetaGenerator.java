package omoikane.etiquetas;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import omoikane.principal.Articulos;
import omoikane.producto.Articulo;

import java.awt.*;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Pedro
 * Date: 13/03/13
 * Time: 11:24
 * To change this template use File | Settings | File Templates.
 */
public class EtiquetaGenerator {
    JasperPrint jp;

    public void generate(String reportURL,Collection<Articulo> articulos)
    {
        try {
            InputStream is = loadReport(reportURL);
            JasperDesign design = JRXmlLoader.load(is);
            JasperReport report = JasperCompileManager.compileReport(design);
            jp = JasperFillManager.fillReport(report,new HashMap(),new JRBeanCollectionDataSource(articulos));

            JasperViewer jasperViewer = new JasperViewer(jp, false);
            jasperViewer.setDefaultCloseOperation(JasperViewer.DISPOSE_ON_CLOSE);
            jasperViewer.setTitle("Etiquetas");
            jasperViewer.setZoomRatio((float) 1.25);
            jasperViewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
            jasperViewer.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
            jasperViewer.requestFocus();
            jasperViewer.setVisible(true);
        }catch (Exception e) {
            omoikane.sistema.Dialogos.lanzarDialogoError(null, "Error al generar etiquetas", omoikane.sistema.Herramientas.getStackTraceString(e));
        }

    }

    private InputStream loadReport (String reportURL) throws Exception{
        InputStream is = ClassLoader.getSystemResourceAsStream(reportURL);
        if(is == null) { throw new Exception("Plantilla de reporte no encontrada. " + reportURL); }
        return is;
    }
}
