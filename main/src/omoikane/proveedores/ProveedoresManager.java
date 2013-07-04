package omoikane.proveedores;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.stage.Stage;
import omoikane.etiquetas.ImpresionEtiquetasController;
import omoikane.etiquetas.ImpresionEtiquetasModel;
import omoikane.principal.Principal;
import omoikane.sistema.Herramientas;
import omoikane.sistema.Permisos;
import org.springframework.context.ApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 20/04/13
 * Time: 10:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProveedoresManager {

    ProveedoresController controller;
    private JInternalFrame frame;

    private Scene initProveedores() throws IOException {
        Platform.setImplicitExit(false);
        ApplicationContext context = Principal.applicationContext;

        Scene scene = (Scene)context.getBean("proveedoresView");

        return scene;
    }

    public JInternalFrame startJFXProveedores() {
        JInternalFrame frame = null;
        if(omoikane.sistema.Usuarios.cerrojo(Permisos.PMA_PROVEEDORES)) frame = _startJFXProveedores();
        return frame;
    }

    private JInternalFrame _startJFXProveedores() {
        frame = new JInternalFrame("Administrador de proveedores");
        final JFXPanel fxPanel = new JFXPanel();

        frame.setClosable(true);
        frame.add(fxPanel);
        frame.setVisible(true);

        Herramientas.panelCatalogo(frame);
        omoikane.principal.Principal.getEscritorio().getPanelEscritorio().add(frame);
        frame.setSize(706, 518);
        frame.setPreferredSize(new Dimension(706, 518));
        frame.setResizable(true);
        frame.setVisible(true);
        Herramientas.centrarVentana(frame);
        Herramientas.iconificable(frame);
        frame.toFront();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Scene scene = null;
                try {
                    scene = initProveedores();
                    scene.setFill(null);
                    fxPanel.setScene(scene);
                } catch (IOException e) {

                }

            }
        });

        return frame;
    }

}
