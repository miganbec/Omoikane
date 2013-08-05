package omoikane.artemisa.presentation;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import omoikane.artemisa.PacienteRepo;
import omoikane.artemisa.entity.Cargo;
import omoikane.artemisa.entity.Paciente;
import omoikane.artemisa.reports.CargoPrint;
import omoikane.producto.Articulo;
import omoikane.producto.Producto;
import omoikane.repository.ProductoRepo;
import omoikane.sistema.Permisos;
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
    private ListView<Articulo> productoList; // Value injected by FXMLLoader

    @FXML //  fx:id="registrarButton"
    private Button registrarButton; // Value injected by FXMLLoader

    @FXML private TextField txtBuscarProducto;

    private ObservableList<Articulo> productos;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

        assert cantidadField != null : "fx:id=\"cantidadField\" was not injected: check your FXML file 'ConsumoView.fxml'.";
        assert pacienteField != null : "fx:id=\"pacienteField\" was not injected: check your FXML file 'ConsumoView.fxml'.";
        assert productoList != null : "fx:id=\"productoField\" was not injected: check your FXML file 'ConsumoView.fxml'.";
        assert registrarButton != null : "fx:id=\"registrarButton\" was not injected: check your FXML file 'ConsumoView.fxml'.";

        productos = FXCollections.observableArrayList();
        productoList.setItems(productos);
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
                    cargo.setProducto(productoList.getSelectionModel().getSelectedItem());
                    entityManager.persist(cargo);

                    resetUI();
                    CargoPrint cargoPrint = new CargoPrint(cargo);
                    cargoPrint.print();
                    logger.info("Registrado");
                } catch(Exception e) {
                    logger.error("No registrado por causa de un error.", e);
                }
            }
        });

    }

    private boolean validar() {
        if(pacienteField.getValue() == null) { logger.info("Seleccione un paciente"); return false; }
        if(productoList.getSelectionModel().getSelectedItem() == null) { logger.info("Seleccione un producto o servicio"); return false; }
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
        cantidadField.setText("");
        txtBuscarProducto.setText("");

        fillProductosList();
        pacienteField.getSelectionModel().clearSelection();
        productoList.getSelectionModel().clearSelection();
    }

    private void fillProductosList() {
        List p = productoRepo.findByDescripcionLikeOrCodigoLike("%" + txtBuscarProducto.getText() + "%");
        ObservableList observableList1 = FXCollections.observableArrayList(p);
        productos.clear();
        productos.addAll(observableList1);
    }

    private TimerBusqueda timerBusqueda;
    @FXML
    private void onBusquedaKey(KeyEvent event) {
        String txtBusqueda = txtBuscarProducto.getText();
        if ( txtBusqueda != null && !txtBusqueda.isEmpty() ) {
            if(timerBusqueda != null && timerBusqueda.isAlive()) { timerBusqueda.cancelar(); }
            this.timerBusqueda = new TimerBusqueda();
            timerBusqueda.start();
        }
    }

    @FXML
    private void onBusquedaKeyReleased(KeyEvent event) {
        if(event.getCode() == KeyCode.DOWN) { productoList.getSelectionModel().selectNext(); }
        if(event.getCode() == KeyCode.UP) { productoList.getSelectionModel().selectPrevious(); }
    }

    class TimerBusqueda extends Thread
    {
        boolean busquedaActiva = true;

        public void run()
        {
            synchronized(this)
            {
                busquedaActiva = true;
                try { this.wait(500); } catch(Exception e) { logger.error("Error en el timer de búsqueda automática", e); }
                if(busquedaActiva) {
                    Task task =  new Task() {
                        @Override
                        protected Void call() throws Exception {
                            ConsumoController.this.fillProductosList();
                            if(productos.size() > 0) { productoList.getSelectionModel().selectFirst(); }
                            return null;
                        }
                    };
                    Platform.runLater(task);
                }
            }
        }
        void cancelar()
        {
            busquedaActiva = false;
            try { this.notify(); } catch(Exception e) {}
        }
    }

}
