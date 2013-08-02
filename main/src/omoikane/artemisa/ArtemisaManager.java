package omoikane.artemisa;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import omoikane.principal.Principal;
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

    public static void main(String[] args) {
        omoikane.principal.Principal.configExceptions();
        Application.launch(ArtemisaManager.class);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            omoikane.principal.Principal.setConfig( new omoikane.sistema.Config() );
            omoikane.principal.Principal.applicationContext = new ClassPathXmlApplicationContext("applicationContext-artemisa.xml");

            //Principal.IDCaja = 1;
            Principal.IDAlmacen = 1;
            Usuarios.setIDUsuarioActivo(1);

            Scene scene = (Scene) Principal.applicationContext.getBean("uiManagerView");

            primaryStage.setScene(scene);
            primaryStage.setTitle("Artemisa");
            primaryStage.show();

        } catch (Exception ex) {
            logger.error( ex.getMessage(), ex );
        }
    }
}
