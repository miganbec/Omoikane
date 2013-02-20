package omoikane.configuracion;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import omoikane.sistema.Herramientas;
import phesus.configuratron.ConfiguratorApp;
import phesus.configuratron.ConfiguratorController;

import javax.swing.*;
import java.io.IOException;

import omoikane.sistema.Permisos;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 10/01/13
 * Time: 13:32
 * To change this template use File | Settings | File Templates.
 */
public class ConfiguratorAppManager {
    final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ConfiguratorApp.class);
    private JInternalFrame frame;

    public JInternalFrame startJFXConfigurator() {
        JInternalFrame frame = null;
        if(omoikane.sistema.Usuarios.autentifica(Permisos.PMA_CONFIGURACION)) frame = _startJFXConfigurator();
        return frame;
    }

    private JInternalFrame _startJFXConfigurator() {
        frame = new JInternalFrame("FX");
        final JFXPanel fxPanel = new JFXPanel();

        frame.add(fxPanel);
        frame.setVisible(true);

        Herramientas.panelCatalogo(frame);
        omoikane.principal.Principal.getEscritorio().getPanelEscritorio().add(frame);
        frame.setBounds(10,10,499,595);
        frame.setVisible(true);
        Herramientas.centrarVentana(frame);
        Herramientas.iconificable(frame);
        frame.toFront();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Scene scene = null;
                try {
                    ConfiguratorApp configGUI = new ConfiguratorApp();
                    scene = configGUI.initConfigurator();
                    fxPanel.setScene(scene);
                    ConfiguratorController controller = configGUI.getFxmlLoader().getController();
                    controller.getCerrarBtn().setOnAction(new CerrarBtnHandler());
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }

            }
        });

        return frame;
    }

    private class CerrarBtnHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            ConfiguratorAppManager.this.frame.setVisible(false);
            ConfiguratorAppManager.this.frame.dispose();
        }
    }
}
