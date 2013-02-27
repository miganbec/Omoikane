package omoikane.proveedores;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import omoikane.entities.Paquete;
import omoikane.repository.ProveedorRepo;
import omoikane.sistema.Dialogos;
import omoikane.sistema.Herramientas;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.*;
import org.synyx.hades.domain.PageRequest;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 25/02/13
 * Time: 08:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProveedoresController implements Initializable {
    @FXML TextField txtBuscar;
    @FXML TextField txtId;
    @FXML TextField txtNombre;

    @FXML TableView<Proveedor> proveedoresTable;
    @FXML TextArea txtNotas;

    @FXML Label notaNombre;
    @FXML Label notaNota;

    @FXML TableColumn idCol;
    @FXML TableColumn nombreCol;

    @FXML CheckBox chkIncluirInactivos;

    @Autowired
    ProveedorRepo proveedorRepo;

    @Autowired
    Validator validator;

    public Logger logger = Logger.getLogger(getClass());

    ObservableList<Proveedor> proveedores;

    Proveedor selectedProveedor;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        idCol.setCellValueFactory(new PropertyValueFactory<Proveedor, Long>("id"));
        nombreCol.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("nombre"));

        proveedores = FXCollections.observableArrayList();
        proveedoresTable.setItems(proveedores);

        llenarTabla();
        proveedoresTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Proveedor>() {
            @Override
            public void changed(ObservableValue<? extends Proveedor> observableValue, Proveedor proveedor, Proveedor proveedor2) {
                if(proveedor2 != null) {
                    selectedProveedor = proveedor2;
                    notaNota.setText("");
                    notaNombre.setText("");
                    txtNombre.textProperty().set(proveedor2.getNombre());
                    txtNotas.textProperty().set(proveedor2.getNotas());
                    if(proveedor2.getId() != null)
                        txtId.textProperty().set(proveedor2.getId().toString());
                    else
                        txtId.textProperty().set("");
                }
            }
        });

    }

    public void llenarTabla() {
        Boolean soloActivos = !chkIncluirInactivos.isSelected();
        List<Proveedor> proovs = proveedorRepo.findByActivoAndNombreLike(soloActivos, "%"+txtBuscar.getText()+"%");
        proveedores.clear();
        proveedores.addAll(proovs);
    }

    @FXML
    public void agregarAction(ActionEvent event) {
        Proveedor proveedor = new Proveedor();
        selectedProveedor = proveedor;
        proveedores.add(proveedor);
        proveedoresTable.getSelectionModel().select(proveedor);
    }

    @FXML
    /**
     * Ésta acción en realidad desactiva al proveedor
     */
    public void eliminarAction(ActionEvent event) {
        Proveedor proveedor = selectedProveedor;
        if(selectedProveedor != null && selectedProveedor.getId() != null && proveedorRepo.exists(selectedProveedor.getId())) {
            //proveedorRepo.delete(proveedor);
            proveedor.setActivo(false);
            proveedorRepo.saveAndFlush(proveedor);
            llenarTabla();
            borrarCampos();
            logger.info("Proveedor inhabilitado!");
        }
    }

    private void borrarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtNotas.setText("");
        notaNota.setText("");
        notaNombre.setText("");
        selectedProveedor = null;
    }

    @FXML
    public void guardarAction(ActionEvent event) {
        Proveedor proveedor = selectedProveedor != null ? selectedProveedor : new Proveedor();
        proveedor.setNombre( txtNombre.getText() );
        proveedor.setNotas( txtNotas.getText() );

        if(validar(proveedor)) {
            proveedorRepo.saveAndFlush(proveedor);
            llenarTabla();
            proveedoresTable.getSelectionModel().select(proveedor);
            logger.info("Proveedor guardado!");
        }
    }

    private TimerBusqueda timerBusqueda;
    @FXML
    private void onBusquedaKey(KeyEvent event) {
        String txtBusqueda = txtBuscar.getText();
        if ( txtBusqueda != null && !txtBusqueda.isEmpty() ) {
            if(timerBusqueda != null && timerBusqueda.isAlive()) { timerBusqueda.cancelar(); }
            this.timerBusqueda = new TimerBusqueda();
            timerBusqueda.start();
        }
    }

    @FXML
    private void mostrarInactivosAction(ActionEvent event) {
        llenarTabla();
    }

    private boolean validar(Proveedor proveedor) {
        DataBinder binder = new DataBinder(proveedor);
        binder.setValidator(validator);
        binder.validate();
        BindingResult bindingResult = binder.getBindingResult();

        if(bindingResult.hasErrors()) {
            for( ObjectError oe : bindingResult.getAllErrors() ) {
                if(oe.getClass() == FieldError.class) {
                    FieldError fe = (FieldError) oe;
                    if(fe.getField().equals("nombre"))  { notaNombre.setText(fe.getDefaultMessage());   }
                    if(fe.getField().equals("nota"))    { notaNota.setText(fe.getDefaultMessage()); }
                } else {
                    logger.info(oe.getDefaultMessage());
                }
            }
        } else {
            notaNombre.setText("");
            notaNota.setText("");
            return true;
        }
        return false;
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
                    ProveedoresController.this.llenarTabla();
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
