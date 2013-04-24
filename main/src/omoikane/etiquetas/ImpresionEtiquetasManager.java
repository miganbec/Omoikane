package omoikane.etiquetas;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import omoikane.caja.business.ICajaLogic;
import omoikane.caja.presentation.CajaController;
import omoikane.caja.presentation.CajaModel;
import omoikane.principal.Principal;
import omoikane.producto.Producto;
import omoikane.repository.ProductoRepo;
import omoikane.sistema.Herramientas;
import omoikane.sistema.Permisos;
import omoikane.sistema.SpringAnnotatedConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created with IntelliJ IDEA.
 * User: Pedro
 * Date: 12/03/13
 * Time: 01:41
 * To change this template use File | Settings | File Templates.
 */
public class    ImpresionEtiquetasManager  {

    ImpresionEtiquetasController controller;
    ImpresionEtiquetasModel model;
    private JInternalFrame frame;

    private Scene initImpresionEtiquetas() throws IOException {
        Platform.setImplicitExit(false);
        ApplicationContext context = Principal.applicationContext;

        Scene scene = (Scene)context.getBean("impresionEtiquetasView");

        return scene;
    }

    public JInternalFrame startJFXEtiqueta() {
        JInternalFrame frame = null;
        if(omoikane.sistema.Usuarios.autentifica(Permisos.PMA_ETIQUETAS)) frame = _startJFXEtiqueta();
        return frame;
    }

    public JInternalFrame _startJFXEtiqueta() {
        frame = new JInternalFrame("Impresión de etiquetas");
        final JFXPanel fxPanel = new JFXPanel();

        frame.setClosable(true);
        frame.add(fxPanel);
        frame.setVisible(true);

        Herramientas.panelCatalogo(frame);
        omoikane.principal.Principal.getEscritorio().getPanelEscritorio().add(frame);
        frame.setSize(606, 418);
        frame.setPreferredSize(new Dimension(606, 418));
        frame.setVisible(true);
        Herramientas.centrarVentana(frame);
        Herramientas.iconificable(frame);
        frame.toFront();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Scene scene = null;
                try {
                    scene = initImpresionEtiquetas();
                    scene.setFill(null);
                    fxPanel.setScene(scene);
                } catch (IOException e) {

                }

            }
        });

        return frame;
    }

}
