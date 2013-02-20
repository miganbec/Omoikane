package omoikane.producto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import omoikane.caja.CajaManager;
import omoikane.principal.Principal;
import omoikane.sistema.SpringFxmlLoader;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.net.URL;
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

            Scene scene = initView();

            primaryStage.setScene(scene);
            primaryStage.setTitle("View Test");
            primaryStage.show();

        } catch (Exception ex) {
            logger.error( ex.getMessage(), ex );
        }
    }

    private Scene initView() throws IOException {
        ApplicationContext context = Principal.applicationContext;
        Map<String,String> parametros = getParameters().getNamed();
        URL urlFxml = DummyJFXApp.class.getResource(parametros.get("view"));
        SpringFxmlLoader fxmlLoader = context.getBean(SpringFxmlLoader.class);
        AnchorPane page = (AnchorPane) fxmlLoader.load(urlFxml);

        Scene scene = new Scene(page);
        return scene;
    }
}
