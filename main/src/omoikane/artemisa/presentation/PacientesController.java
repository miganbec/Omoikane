package omoikane.artemisa.presentation;

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
import omoikane.artemisa.entity.Paciente;
import omoikane.proveedores.Proveedor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import omoikane.artemisa.PacienteRepo;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 25/02/13
 * Time: 08:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class PacientesController implements Initializable {
    @FXML TextField txtBuscar;
    @FXML TextField txtId;
    @FXML TextField txtNombre;

    @FXML TableView<Paciente> pacientesTable;
    @FXML TextField txtHabitacion;

    @FXML Label notaNombre;
    @FXML Label notaHabitacion;

    @FXML TableColumn idCol;
    @FXML TableColumn nombreCol;

    @FXML CheckBox chkIncluirInactivos;

    @Autowired
    PacienteRepo pacienteRepo;

    @Autowired
    Validator validator;

    public Logger logger = Logger.getLogger(getClass());

    ObservableList<Paciente> pacientes;

    Paciente selectedPaciente;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        idCol.setCellValueFactory(new PropertyValueFactory<Proveedor, Long>("id"));
        nombreCol.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("nombre"));

        pacientes = FXCollections.observableArrayList();
        pacientesTable.setItems(pacientes);

        llenarTabla();
        pacientesTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Paciente>() {
            @Override
            public void changed(ObservableValue<? extends Paciente> observableValue, Paciente paciente, Paciente paciente2) {
                if(paciente2 != null) {
                    selectedPaciente = paciente2;
                    notaHabitacion.setText("");
                    notaNombre.setText("");
                    txtNombre.textProperty().set(paciente2.getNombre());
                    txtHabitacion.textProperty().set(paciente2.getHabitacion());
                    if(paciente2.getId() != null)
                        txtId.textProperty().set(paciente2.getId().toString());
                    else
                        txtId.textProperty().set("");
                }
            }
        });

    }

    public void llenarTabla() {
        Boolean soloActivos = chkIncluirInactivos.isSelected();
        List<Paciente> pacs = pacienteRepo.findByLiquidadoAndNombreLike(soloActivos, "%" + txtBuscar.getText() + "%");
        pacientes.clear();
        pacientes.addAll(pacs);
    }

    @FXML
    public void agregarAction(ActionEvent event) {
        Paciente paciente = new Paciente();
        selectedPaciente = paciente;
        pacientes.add(paciente);
        pacientesTable.getSelectionModel().select(paciente);
    }

    @FXML
    /**
     * Ésta acción en realidad desactiva al proveedor
     */
    public void eliminarAction(ActionEvent event) {
        Paciente paciente = selectedPaciente;
        if(selectedPaciente != null && selectedPaciente.getId() != null && pacienteRepo.exists(selectedPaciente.getId())) {
            paciente.setLiquidado(true);
            pacienteRepo.saveAndFlush(paciente);
            llenarTabla();
            borrarCampos();
            logger.info("Paciente inhabilitado!");
        }
    }

    private void borrarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtHabitacion.setText("");
        notaHabitacion.setText("");
        notaNombre.setText("");
        selectedPaciente = null;
    }

    @FXML
    public void guardarAction(ActionEvent event) {
        Paciente paciente = selectedPaciente != null ? selectedPaciente : new Paciente();
        paciente.setNombre( txtNombre.getText() );
        paciente.setHabitacion(txtHabitacion.getText());

        if(validar(paciente)) {
            pacienteRepo.saveAndFlush(paciente);
            llenarTabla();
            pacientesTable.getSelectionModel().select(paciente);
            logger.info("Paciente guardado");
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

    private boolean validar(Paciente paciente) {
        DataBinder binder = new DataBinder(paciente);
        binder.setValidator(validator);
        binder.validate();
        BindingResult bindingResult = binder.getBindingResult();

        if(bindingResult.hasErrors()) {
            for( ObjectError oe : bindingResult.getAllErrors() ) {
                if(oe.getClass() == FieldError.class) {
                    FieldError fe = (FieldError) oe;
                    if(fe.getField().equals("nombre"))        { notaNombre.setText(fe.getDefaultMessage());   }
                    if(fe.getField().equals("habitacion"))    { notaHabitacion.setText(fe.getDefaultMessage()); }
                } else {
                    logger.info(oe.getDefaultMessage());
                }
            }
        } else {
            notaNombre.setText("");
            notaHabitacion.setText("");
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
                    PacientesController.this.llenarTabla();
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
