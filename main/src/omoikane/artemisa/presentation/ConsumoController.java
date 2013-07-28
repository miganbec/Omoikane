package omoikane.artemisa.presentation;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import omoikane.artemisa.PacienteRepo;
import omoikane.artemisa.entity.Cargo;
import omoikane.artemisa.entity.Paciente;
import omoikane.producto.Articulo;
import omoikane.repository.ProductoRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.swing.*;


public class ConsumoController implements Initializable {

    public static Logger logger = Logger.getLogger(ConsumoController.class);

    @Autowired
    PacienteRepo pacienteRepo;

    @Autowired
    ProductoRepo productoRepo;

    @Autowired
    JpaTransactionManager transactionManager;

    @PersistenceContext
    EntityManager entityManager;

    @FXML //  fx:id="cantidadField"
    private TextField cantidadField; // Value injected by FXMLLoader

    @FXML //  fx:id="pacienteField"
    private ComboBox<Paciente> pacienteField; // Value injected by FXMLLoader

    @FXML //  fx:id="productoField"
    private ComboBox<Articulo> productoField; // Value injected by FXMLLoader

    @FXML //  fx:id="registrarButton"
    private Button registrarButton; // Value injected by FXMLLoader

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert cantidadField != null : "fx:id=\"cantidadField\" was not injected: check your FXML file 'ConsumoView.fxml'.";
        assert pacienteField != null : "fx:id=\"pacienteField\" was not injected: check your FXML file 'ConsumoView.fxml'.";
        assert productoField != null : "fx:id=\"productoField\" was not injected: check your FXML file 'ConsumoView.fxml'.";
        assert registrarButton != null : "fx:id=\"registrarButton\" was not injected: check your FXML file 'ConsumoView.fxml'.";

        resetUI();
    }

    @FXML
    public void registrar(ActionEvent event) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        Object result = transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    if(!validar()) return;
                    Paciente paciente = pacienteRepo.readByPrimaryKey(pacienteField.getValue().getId());
                    Cargo cargo = new Cargo();
                    cargo.setCantidad(new BigDecimal(cantidadField.getText()));
                    cargo.setPaciente( paciente );
                    cargo.setProducto( productoField.getValue() );
                    entityManager.persist(cargo);

                    resetUI();
                    logger.info("Registrado");
                } catch(Exception e) {
                    logger.error("No registrado por causa de un error.", e);
                }
            }
        });

    }

    private boolean validar() {
        if(pacienteField.getValue() == null) { logger.info("Seleccione un paciente"); return false; }
        if(productoField.getValue() == null) { logger.info("Seleccione un producto o servicio"); return false; }
        try {
            BigDecimal a = new BigDecimal(cantidadField.getText());
        } catch (Exception e) {
            logger.info("Cantidad mal escrita, debe ser un número");
            return false;
        }
        return true;
    }

    // TODO Optimizar selects, hace un select por cada artículo X_X
    private void resetUI() {
        List pacientesActivos = pacienteRepo.findAllActive();
        ObservableList observableList = FXCollections.observableArrayList(pacientesActivos);
        pacienteField.setItems( observableList );

        List productos = productoRepo.findAll();
        ObservableList observableList1 = FXCollections.observableArrayList(productos);
        productoField.setItems( observableList1 );

        cantidadField.setText("");
        productoField.getSelectionModel().clearSelection();
        pacienteField.getSelectionModel().clearSelection();
    }

}
