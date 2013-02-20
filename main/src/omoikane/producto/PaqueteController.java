package omoikane.producto;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 16/02/13
 * Time: 13:41
 * To change this template use File | Settings | File Templates.
 */

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import omoikane.entities.Paquete;
import omoikane.repository.ProductoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;


public class PaqueteController
        implements Initializable {

    ObservableList<Paquete> paquete;
    Long productoId = 1l;

    @Autowired
    ApplicationContext context;

    @FXML //  fx:id="addButton"
    private Button addButton;

    @FXML //  fx:id="contenidoTable"
    private TableView<Paquete> tablaContenido;

    @FXML //  fx:id="paqueteCheckbox"
    private CheckBox paqueteCheckbox;

    @FXML //  fx:id="removeButton"
    private Button removeButton;

    @FXML
    private void actionAdd() {
        Paquete renglonPaquete = new Paquete();
        //renglonPaquete.setCantidad(1);
        //renglonPaquete.setProductoContenedor();
    }

    @FXML
    private void actionRemove() {

    }

    @FXML
    private void paqueteStateSwitched() {

    }


    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert addButton != null : "fx:id=\"addButton\" was not injected: check your FXML file 'PaqueteView.fxml'.";
        assert tablaContenido != null : "fx:id=\"contenidoTable\" was not injected: check your FXML file 'PaqueteView.fxml'.";
        assert paqueteCheckbox != null : "fx:id=\"paqueteCheckbox\" was not injected: check your FXML file 'PaqueteView.fxml'.";
        assert removeButton != null : "fx:id=\"removeButton\" was not injected: check your FXML file 'PaqueteView.fxml'.";

        // initialize your logic here: all @FXML variables will have been injected
        paquete = FXCollections.emptyObservableList();
        tablaContenido.setItems(paquete);

        System.out.print(context);
        /*
        Articulo art = productoRepo.readByPrimaryKey(productoId);
        for(Paquete p : art.renglonesPaquete) {
            System.out.println(p);
        } */

    }

}

