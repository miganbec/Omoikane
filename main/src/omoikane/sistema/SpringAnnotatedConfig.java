package omoikane.sistema;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import omoikane.etiquetas.ImpresionEtiquetasController;
import omoikane.inventarios.StockLevelsController;
import omoikane.inventarios.TomaInventarioController;
import omoikane.producto.PaqueteController;
import omoikane.proveedores.ProveedoresController;
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
public class SpringAnnotatedConfig {
    public Logger logger = Logger.getLogger(getClass());

    @Bean
    @Scope("prototype")
    PaqueteController paqueteController() {
        return new PaqueteController();
    }

    @Bean
    @Scope("prototype")
    StockLevelsController stockLevelsController() {
        return new StockLevelsController();
    }

    @Bean
    @Scope("prototype")
    ProveedoresController proveedoresController() {
        return new ProveedoresController();
    }

    @Bean
    @Scope("prototype")
    TomaInventarioController tomaInventarioController() {
        return new TomaInventarioController();
    }

    @Bean
    @Scope("prototype")
    ImpresionEtiquetasController impresionEtiquetasController() {
        return new ImpresionEtiquetasController();
    }

    @Bean
    @Scope("prototype")
    Scene stockLevelsView() {
        return initView("/omoikane/inventarios/StockLevelsView.fxml", stockLevelsController());
    }

    @Bean
    @Scope("prototype")
    Scene paqueteView() {
        return initView("/omoikane/producto/PaqueteView.fxml", paqueteController());
    }

    @Bean
    @Scope("prototype")
    Scene proveedoresView() {
        return initView("/omoikane/proveedores/ProveedoresView.fxml", proveedoresController());
    }

    @Bean
    @Scope("prototype")
    Scene tomaInventarioView() {
        return initView("/omoikane/inventarios/TomaInventarioView.fxml", tomaInventarioController());
    }

    @Bean
    @Scope("prototype")
    Scene impresionEtiquetasView() {
        return initView("/omoikane/etiquetas/presentation/ImpresionetiquetasView.fxml", impresionEtiquetasController());
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
