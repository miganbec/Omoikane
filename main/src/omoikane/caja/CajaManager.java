package omoikane.caja;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import omoikane.caja.business.CajaLogicImpl;
import omoikane.caja.business.ICajaLogic;
import omoikane.caja.presentation.CajaController;
import omoikane.caja.presentation.CajaModel;
import omoikane.principal.Principal;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 12/09/12
 * Time: 11:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class CajaManager extends Application {
    CajaModel model;
    CajaController controller;
    final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CajaManager.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(CajaManager.class, (java.lang.String[])null);
    }

    @Override
    public void start(Stage primaryStage) {
        try {

            omoikane.principal.Principal.setConfig( new omoikane.sistema.Config() );
            omoikane.principal.Principal.applicationContext = new ClassPathXmlApplicationContext("applicationContext-test.xml");

            /* PRuebas */

            //ProductoRepo productoRepo = Principal.applicationContext.getBean(ProductoRepo.class);
            //Articulo art = productoRepo.readByPrimaryKey(1000l);
            //System.out.print("pausa");

            /* end of pruebas */

            FXMLLoader fxmlLoader = new FXMLLoader(CajaManager.class.getResource("presentation/Caja.fxml"));
            AnchorPane page = (AnchorPane) fxmlLoader.load();

            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Caja 3.0 alfa");
            primaryStage.show();

            model      = new CajaModel();
            controller = fxmlLoader.getController();
            controller.setModel(model);

            ICajaLogic cajaLogic = (ICajaLogic) Principal.applicationContext.getBean("cajaLogic");
            controller.setCajaLogic( cajaLogic );

        } catch (Exception ex) {
            logger.error( ex.getMessage(), ex );
        }
    }
}
