package omoikane.artemisa.presentation;


import java.awt.*;
import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import omoikane.artemisa.PacienteRepo;
import omoikane.artemisa.entity.Abono;
import omoikane.artemisa.entity.Cargo;
import omoikane.artemisa.entity.Paciente;
import omoikane.artemisa.entity.Transaccion;
import omoikane.proveedores.Proveedor;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;


public class CajaClinicaController
        implements Initializable {

    public static Logger logger = Logger.getLogger(CajaClinicaController.class);

    @Autowired
    PacienteRepo pacienteRepo;

    @Autowired
    JpaTransactionManager transactionManager;

    @PersistenceContext
    EntityManager entityManager;

    @FXML //  fx:id="abonoTxt"
    private TextField abonoTxt; // Value injected by FXMLLoader

    @FXML //  fx:id="abonosCol"
    private TableColumn<Transaccion, BigDecimal> abonosCol; // Value injected by FXMLLoader

    @FXML //  fx:id="cargosCol"
    private TableColumn<Transaccion, BigDecimal> cargosCol; // Value injected by FXMLLoader

    @FXML //  fx:id="conceptoCol"
    private TableColumn<Transaccion, String> conceptoCol; // Value injected by FXMLLoader

    @FXML //  fx:id="condicionLiquidacionLbl"
    private Label condicionLiquidacionLbl; // Value injected by FXMLLoader

    @FXML //  fx:id="fechaCol"
    private TableColumn<Transaccion, Date> fechaCol; // Value injected by FXMLLoader

    @FXML //  fx:id="pacienteLbl"
    private Label pacienteLbl; // Value injected by FXMLLoader

    @FXML //  fx:id="pacientesList"
    private ListView<Paciente> pacientesList; // Value injected by FXMLLoader

    @FXML //  fx:id="tabEdoCuenta"
    private TableView<Transaccion> tabEdoCuenta; // Value injected by FXMLLoader

    @FXML
    private Label saldoTxt;


    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert abonoTxt != null : "fx:id=\"abonoTxt\" was not injected: check your FXML file 'CajaClinicaView.fxml'.";
        assert abonosCol != null : "fx:id=\"abonosCol\" was not injected: check your FXML file 'CajaClinicaView.fxml'.";
        assert cargosCol != null : "fx:id=\"cargosCol\" was not injected: check your FXML file 'CajaClinicaView.fxml'.";
        assert conceptoCol != null : "fx:id=\"conceptoCol\" was not injected: check your FXML file 'CajaClinicaView.fxml'.";
        assert condicionLiquidacionLbl != null : "fx:id=\"condicionLiquidacionLbl\" was not injected: check your FXML file 'CajaClinicaView.fxml'.";
        assert fechaCol != null : "fx:id=\"fechaCol\" was not injected: check your FXML file 'CajaClinicaView.fxml'.";
        assert pacienteLbl != null : "fx:id=\"pacienteLbl\" was not injected: check your FXML file 'CajaClinicaView.fxml'.";
        assert pacientesList != null : "fx:id=\"pacientesList\" was not injected: check your FXML file 'CajaClinicaView.fxml'.";
        assert tabEdoCuenta != null : "fx:id=\"tabEdoCuenta\" was not injected: check your FXML file 'CajaClinicaView.fxml'.";
        assert saldoTxt != null : "fx:id=\"saldoTxt\" was not injected: check your FXML file 'CajaClinicaView.fxml'.";

        fechaCol.setCellValueFactory(new PropertyValueFactory<Transaccion, Date>("fecha"));
        cargosCol.setCellValueFactory(new PropertyValueFactory<Transaccion, BigDecimal>("cargo"));
        abonosCol.setCellValueFactory(new PropertyValueFactory<Transaccion, BigDecimal>("abono"));
        conceptoCol.setCellValueFactory(new PropertyValueFactory<Transaccion, String>("concepto"));

        List<Paciente> pacienteList = pacienteRepo.findAllActive();
        ObservableList<Paciente> pacienteObservableList = FXCollections.observableList(pacienteList);
        pacientesList.setItems(pacienteObservableList);

        pacientesList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Paciente>() {
            @Override
            public void changed(ObservableValue<? extends Paciente> observableValue, Paciente paciente, Paciente paciente2) {
                selectPaciente(paciente2);
            }
        });

    }

    private void selectPaciente(final Paciente paciente) {

        pacienteLbl.setText(paciente.getNombre());
        List<Transaccion> transacciones = pacienteRepo.findTransaccionesOf(paciente);
        ObservableList<Transaccion> transaccions = FXCollections.observableArrayList( transacciones );
        tabEdoCuenta.setItems(transaccions);

        saldoTxt.setText(NumberFormat.getCurrencyInstance().format( pacienteRepo.getSaldo(paciente) ));
    }

    public void onAbonar(ActionEvent event) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        Object result = transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                abonar();
            }

        });
    }

    public void onImprimir() {

        TextColumnBuilder cargoColumn, abonoColumn;

        StyleBuilder boldStyle         = stl.style().bold();
        StyleBuilder boldCenteredStyle = stl.style(boldStyle)
                .setHorizontalAlignment(HorizontalAlignment.CENTER);
        StyleBuilder columnTitleStyle  = stl.style(boldCenteredStyle)
                .setBorder(stl.pen1Point())
                .setBackgroundColor(Color.LIGHT_GRAY);
        StyleBuilder titleStyle = stl.style(boldCenteredStyle)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setFontSize(15);


        try {
            report()
                    .columns(
                            col.column("Fecha / Hora", "fecha", type.dateType()),
                            col.column("Concepto", "concepto", type.stringType()),
                            cargoColumn = col.column("Cargo", "cargo", type.bigDecimalType()),
                            abonoColumn = col.column("Abono", "abono", type.bigDecimalType())
                    )
                    .setColumnTitleStyle(columnTitleStyle)
                    .highlightDetailEvenRows()
                    .title(
                            cmp.horizontalList()
                                    .add(
                                            cmp.image(getClass().getResourceAsStream("/omoikane/Media2/icons/PNG/512/banknote.png")).setFixedDimension(80, 80),
                                            cmp.text("Estado de cuenta").setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.LEFT),
                                            cmp.text("Hospital Ángel").setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT))
                                    .newRow()
                                    .add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen2Point())).setFixedHeight(10)))
                    .pageFooter(cmp.pageXofY().setStyle(boldCenteredStyle))
                    .subtotalsAtSummary(
                            sbt.sum(cargoColumn),
                            sbt.sum(abonoColumn))
                    .setDataSource(tabEdoCuenta.getItems())
                    .show(false);
        } catch (DRException e) {
            logger.error("Error al imprimir", e);
        }
    }

    private void abonar() {
        Abono nvoAbono = new Abono();
        Paciente paciente = pacientesList.getSelectionModel().getSelectedItem();
        nvoAbono.setImporte(new BigDecimal(abonoTxt.getText()));
        nvoAbono.setPaciente(paciente);

        entityManager.persist(nvoAbono);
        logger.info("Abono registrado");
        abonoTxt.setText("");
        selectPaciente(paciente);
    }
}

