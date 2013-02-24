package omoikane.producto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import omoikane.caja.CajaManager;
import omoikane.principal.Principal;
import omoikane.sistema.SpringFxmlLoader;
import omoikane.sistema.Usuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 16/02/13
 * Time: 12:08
 * To change this template use File | Settings | File Templates.
 */
public class DummyJFXApp extends Application {

    final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DummyJFXApp.class);

    @Override
    public void start(Stage primaryStage) {
        try {
            omoikane.principal.Principal.setConfig( new omoikane.sistema.Config() );

            //Principal.IDCaja = 1;
            Principal.IDAlmacen = 1;
            Usuarios.setIDUsuarioActivo(1);

            HashMap testProperties = (HashMap) Principal.applicationContext.getBean( "testProperties" );
            String beanToTest = (String) testProperties.get("DummyJFXApp.viewBeanToTest");
            Scene scene = (Scene) Principal.applicationContext.getBean(beanToTest);

            primaryStage.setScene(scene);
            primaryStage.setTitle("View Test");
            primaryStage.show();

        } catch (Exception ex) {
            logger.error( ex.getMessage(), ex );
        }
    }

}
