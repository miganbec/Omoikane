package omoikane.artemisa.presentation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import jfxtras.labs.scene.control.CalendarTextField;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import omoikane.artemisa.CancelacionTransaccionDAO;
import omoikane.artemisa.TransaccionDAO;
import omoikane.artemisa.entity.CancelacionTransaccion;
import omoikane.artemisa.entity.Transaccion;
import omoikane.sistema.Permisos;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.net.URL;
import java.text.DateFormat;
import java.util.List;
import java.util.ResourceBundle;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import static net.sf.dynamicreports.report.builder.DynamicReports.sbt;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 29/07/13
 * Time: 04:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportesController implements Initializable {

    // TX stands for transactions
    @FXML CalendarTextField txDesde;
    @FXML CalendarTextField txHasta;
    @FXML Button txGenerarButton;

    @Autowired TransaccionDAO transaccionDAO;

    // canc stands for "transacciones canceladas"
    @FXML CalendarTextField cancDesde;
    @FXML CalendarTextField cancHasta;

    @Autowired CancelacionTransaccionDAO cancelacionTransaccionDAO;

    public Logger logger = Logger.getLogger(getClass());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void onTxGenerar(ActionEvent action) {
        List<Transaccion> transacciones = transaccionDAO.findTransacciones(txDesde.getValue().getTime(), txHasta.getValue().getTime());
        imprimirTx(transacciones);
    }

    public void imprimirTx(List<Transaccion> data) {

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
                                            cmp.text("Reporte de cargos y abonos").setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.LEFT),
                                            cmp.text("Hospital Ángel").setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT))
                                    .newRow()
                                    .add(
                                            cmp.text("Desde "+ DateFormat.getDateInstance().format( txDesde.getValue().getTime() ) + ", hasta " + DateFormat.getDateInstance().format( txHasta.getValue().getTime() )).setStyle(titleStyle))
                                    .newRow()
                                    .add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen2Point())).setFixedHeight(10)))
                    .pageFooter(cmp.pageXofY().setStyle(boldCenteredStyle))
                    .subtotalsAtSummary(
                            sbt.sum(cargoColumn),
                            sbt.sum(abonoColumn))
                    .setDataSource(data)
                    .show(false);
        } catch (DRException e) {
            logger.error("Error al imprimir", e);
        }
    }

    public void onCancGenerar(ActionEvent event) {
        List<CancelacionTransaccion> cancelaciones = cancelacionTransaccionDAO.findCancelaciones(cancDesde.getValue().getTime(), cancHasta.getValue().getTime());
        imprimirCanc(cancelaciones);
    }

    private void imprimirCanc(List<CancelacionTransaccion> cancelaciones) {
        TextColumnBuilder cargoColumn;

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
                            cargoColumn = col.column("Importe", "importe", type.bigDecimalType())
                    )
                    .setColumnTitleStyle(columnTitleStyle)
                    .highlightDetailEvenRows()
                    .title(
                            cmp.horizontalList()
                                    .add(
                                            cmp.image(getClass().getResourceAsStream("/omoikane/Media2/icons/PNG/512/banknote.png")).setFixedDimension(80, 80),
                                            cmp.text("Reporte de cancelaciones").setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.LEFT),
                                            cmp.text("Hospital Ángel").setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT))
                                    .newRow()
                                    .add(
                                            cmp.text("Desde "+ DateFormat.getDateInstance().format( cancDesde.getValue().getTime() ) + ", hasta " + DateFormat.getDateInstance().format( cancHasta.getValue().getTime() )).setStyle(titleStyle))
                                    .newRow()
                                    .add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen2Point())).setFixedHeight(10)))
                    .pageFooter(cmp.pageXofY().setStyle(boldCenteredStyle))
                    .subtotalsAtSummary(
                            sbt.sum(cargoColumn))
                    .setDataSource(cancelaciones)
                    .show(false);
        } catch (DRException e) {
            logger.error("Error al imprimir", e);
        }
    }
}
