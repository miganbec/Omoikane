package omoikane.sistema;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import omoikane.caja.business.ICajaLogic;
import omoikane.caja.presentation.CajaController;
import omoikane.caja.presentation.CajaModel;
import omoikane.principal.Principal;
import omoikane.producto.PaqueteController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 16/02/13
 * Time: 21:30
 * To change this template use File | Settings | File Templates.
 */
public class SpringFxmlLoader {

        @Autowired
        private ApplicationContext context;

        public Object load(URL fxml) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(fxml);
                Object page = null;

                Object controller = context.getBean(PaqueteController.class);
                fxmlLoader.setController(controller);

                page = fxmlLoader.load();


                return page;
            } catch (IOException e) {
                return null;
            }
        }

}
