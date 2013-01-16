package omoikane.configuracion;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import phesus.configuratron.ConfiguratorApp;

import javax.swing.*;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 10/01/13
 * Time: 13:32
 * To change this template use File | Settings | File Templates.
 */
public class ConfiguratorAppManager {
    final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ConfiguratorApp.class);

    public JInternalFrame startJFXConfigurator() {
        JInternalFrame frame = new JInternalFrame("FX");
        final JFXPanel fxPanel = new JFXPanel();

        frame.add(fxPanel);
        frame.setVisible(true);


        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Scene scene = null;
                try {
                    ConfiguratorApp configGUI = new ConfiguratorApp();
                    scene = configGUI.initConfigurator();
                    fxPanel.setScene(scene);
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }

            }
        });

        return frame;
    }
}
