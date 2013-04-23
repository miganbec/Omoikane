package omoikane.caja;

import groovy.util.Eval;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import omoikane.caja.business.CajaLogicImpl;
import omoikane.caja.business.ICajaLogic;
import omoikane.caja.presentation.CajaController;
import omoikane.caja.presentation.CajaModel;
import omoikane.principal.Principal;
import omoikane.principal.Sucursales;
import omoikane.sistema.Dialogos;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 12/09/12
 * Time: 11:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class CajaManager extends Application {
    CajaModel model;
    CajaController controller;
    final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CajaManager.class);

    /**
     * Ésto se debe pasar a un test
     * @param args the command line arguments
     */
    /*
    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {

                omoikane.principal.Principal.setConfig(new omoikane.sistema.Config());
                omoikane.principal.Principal.applicationContext = new ClassPathXmlApplicationContext("applicationContext-test.xml");

                CajaManager manager = new CajaManager();
                manager.startJFXCaja();

            }
        });
    }
    */

    @Override
    public void start(Stage primaryStage) {
        try {
            if(!cajaAbierta()) return;
            Scene scene = initCaja();

            primaryStage.setScene(scene);
            primaryStage.setTitle("Caja 3.0 alfa");
            primaryStage.show();

        } catch (Exception ex) {
            logger.error( ex.getMessage(), ex );
        }
    }

    private Scene initCaja() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(CajaManager.class.getResource("presentation/Caja.fxml"));
        AnchorPane page = (AnchorPane) fxmlLoader.load();

        Scene scene = new Scene(page);
        scene.getStylesheets().add(CajaController.class.getResource("Caja.css").toExternalForm());

        //model      = new CajaModel();
        controller = fxmlLoader.getController();
        //controller.setModel(model);

        ICajaLogic cajaLogic = (ICajaLogic) Principal.applicationContext.getBean("cajaLogic");
        controller.setCajaLogic( cajaLogic );
        cajaLogic.nuevaVenta();

        return scene;

    }

    public JInternalFrame startJFXCaja() {
        final JInternalFrame frame = new JInternalFrame("Caja");
        final JFXPanel fxPanel = new JFXPanel();

        frame.add(fxPanel);
        frame.setVisible(true);

        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Scene scene = null;
                try {
                    scene = initCaja();
                    scene.setFill(null);
                    fxPanel.setScene(scene);
                    controller.getCerrarButton().setOnAction(new SwingCerrarHandler(frame));
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }

            }
        });

        return frame;
    }

    private class SwingCerrarHandler implements EventHandler<ActionEvent> {
        JInternalFrame frame;
        public SwingCerrarHandler(JInternalFrame frame) {
            this.frame = frame;
        }

        @Override
        public void handle(ActionEvent event) {
            frame.setVisible(false);
            frame.dispose();
        }
    }


    /**
     * El mecanismo de este método es atrasado, utiliza "nadesico" en lugar de hibernate.
     * Cambiar a la brevedad.
     * @return
     */
    @Deprecated
    public Boolean cajaAbierta() {
        Integer abierta = (Integer) Sucursales.abierta(Principal.IDAlmacen);

        switch(abierta) {
            case -1: Dialogos.lanzarAlerta("Configuración de sucursal-almacen errónea."); break;
            case  0: abierta = (Integer) Sucursales.abrirSucursal(Principal.IDAlmacen);  //Sin break para continuar
            case  1:

                if(abierta!=1) { break; }
                Object cajaAbierta = null;
                cajaAbierta = Eval.me("cajaAbierta", cajaAbierta,
                        "def serv = omoikane.sistema.Nadesico.conectar();" +
                                "cajaAbierta = serv.cajaAbierta(omoikane.principal.Principal.IDCaja);" +
                                "serv.desconectar();");

                cajaAbierta = ((Boolean)cajaAbierta) ?true: omoikane.principal.Caja.abrirCaja();
                if((Boolean) cajaAbierta) { return true; }
                break;
        }
        return false;
    }

}