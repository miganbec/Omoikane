package omoikane.principal;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 14/02/13
 * Time: 01:15
 * To change this template use File | Settings | File Templates.
 */
/**
 * Sample Skeleton for "MainGUIView.fxml" Controller Class
 * You can copy and paste this code into your favorite IDE
 **/

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import omoikane.caja.CajaManager;


public class MainGUIController
        implements Initializable {

    @FXML //  fx:id="workArea"
    private TabPane workArea; // Value injected by FXMLLoader

    @FXML
    private void viewPaquetes(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(CajaManager.class.getResource("producto/PaquetesView.fxml"));
        AnchorPane page = null;
        try {
            page = (AnchorPane) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Tab tab = new Tab();
        tab.setContent(page);
        workArea.getTabs().add(tab);
    }

    @FXML
    private void viewProveedores(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(CajaManager.class.getResource("producto/ProveedoresView.fxml"));
        AnchorPane page = null;
        try {
            page = (AnchorPane) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Tab tab = new Tab();
        tab.setContent(page);
        workArea.getTabs().add(tab);
    }


    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert workArea != null : "fx:id=\"workArea\" was not injected: check your FXML file 'MainGUIView.fxml'.";

        // initialize your logic here: all @FXML variables will have been injected

    }

}
