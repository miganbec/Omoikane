package omoikane.artemisa;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import omoikane.artemisa.presentation.PacientesController;
import omoikane.etiquetas.ImpresionEtiquetasController;
import omoikane.inventarios.StockLevelsController;
import omoikane.inventarios.TomaInventarioController;
import omoikane.producto.PaqueteController;
import omoikane.proveedores.ProveedoresController;
import omoikane.sistema.SceneOverloaded;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 17/02/13
 * Time: 16:35
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class ArtemisaSpringAnnotatedConfig {
    public Logger logger = Logger.getLogger(getClass());

    @Bean
    @Scope("prototype")
    PacientesController pacientesController() {
        return new PacientesController();
    }

    @Bean
    @Scope("prototype")
    Scene pacientesView() {
        return initView("/omoikane/artemisa/presentation/PacientesView.fxml", pacientesController());
    }

    private SceneOverloaded initView(String fxml, final Initializable controller) {
        FXMLLoader fxmlLoader;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
            fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
                @Override
                public Object call(Class<?> aClass) {
                    return controller;
                }
            });
            AnchorPane page = (AnchorPane) fxmlLoader.load();
            SceneOverloaded scene = new SceneOverloaded(page, controller);
            return scene;
        } catch(IOException exception) {
            logger.error(exception.getMessage(), exception);
            return null;
        }
    }
}
