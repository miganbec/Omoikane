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
import omoikane.sistema.SpringAnnotatedConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created with IntelliJ IDEA.
 * User: Pedro
 * Date: 12/03/13
 * Time: 01:41
 * To change this template use File | Settings | File Templates.
 */
public class ImpresionEtiquetasManager extends Application {

    ImpresionEtiquetasController controller;
    ImpresionEtiquetasModel model;

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {

                Principal.setConfig(new omoikane.sistema.Config());

                ImpresionEtiquetasManager manager = new ImpresionEtiquetasManager();
                JInternalFrame frame = manager.startJFXEtiqueta();

                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setResizable(false);
                frame.setBounds(10,10,600,420);
                frame.setVisible(true);

            }
        });
    }

    public void start(Stage primaryStage) {
        try {
            Scene scene = initImpressionEtiquetas();

            primaryStage.setScene(scene);
            primaryStage.setTitle("Impression Etiquetas");
            primaryStage.show();

        } catch (Exception ex) {

        }
    }

    private Scene initImpressionEtiquetas() throws IOException {

        ApplicationContext context = new AnnotationConfigApplicationContext(SpringAnnotatedConfig.class);

        Scene scene = (Scene)context.getBean("impresionEtiquetasView");

        return scene;
    }

    public JInternalFrame startJFXEtiqueta() {
        final JInternalFrame frame = new JInternalFrame("Impresión de etiquetas");
        final JFXPanel fxPanel = new JFXPanel();

        frame.setClosable(true);

        frame.add(fxPanel);
        frame.setVisible(true);

        Herramientas.panelCatalogo(frame);
        omoikane.principal.Principal.getEscritorio().getPanelEscritorio().add(frame);
        frame.setBounds(10,10,606,418);
        frame.setVisible(true);
        Herramientas.centrarVentana(frame);
        Herramientas.iconificable(frame);
        frame.toFront();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Scene scene = null;
                try {
                    scene = initImpressionEtiquetas();
                    scene.setFill(null);
                    fxPanel.setScene(scene);
                } catch (IOException e) {

                }

            }
        });

        return frame;
    }

}
