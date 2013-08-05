package omoikane.artemisa;

import com.sun.webpane.webkit.Timer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import omoikane.principal.Principal;
import omoikane.sistema.Permisos;
import omoikane.sistema.Usuarios;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 31/07/13
 * Time: 03:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class ArtemisaManager extends Application {
    final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ArtemisaManager.class);

    public static void main(String[] args) throws Exception {

        omoikane.principal.Principal.configExceptions();
        omoikane.principal.Principal.setConfig( new omoikane.sistema.Config() );
        omoikane.principal.Principal.applicationContext = new ClassPathXmlApplicationContext("applicationContext-artemisa.xml");
        //Usuarios.identificaPersona();

        Application.launch(ArtemisaManager.class);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            //Principal.IDCaja = 1;
            //Principal.IDAlmacen = 1;
            //Usuarios.setIDUsuarioActivo(1);

            Scene scene = (Scene) Principal.applicationContext.getBean("uiManagerView");
            exitOnClose(primaryStage);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Artemisa");
            primaryStage.show();

        } catch (Exception ex) {
            logger.error( ex.getMessage(), ex );
        }
    }

    private void exitOnClose(Stage primaryStage) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent windowEvent) {
                Platform.exit();
                System.exit(0);
            }
        });
    }
}
