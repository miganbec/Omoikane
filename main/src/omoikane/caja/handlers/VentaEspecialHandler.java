package omoikane.caja.handlers;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import omoikane.caja.presentation.CajaController;
import omoikane.caja.presentation.ProductoModel;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 09/12/12
 * Time: 20:49
 * To change this template use File | Settings | File Templates.
 */
public class VentaEspecialHandler extends ICajaEventHandler {
    public static Logger logger = Logger.getLogger(CancelarVenta.class);
    VentaKBHandler ventaKBHandler;

    public VentaEspecialHandler(CajaController controller) {
        super(controller);
        ventaKBHandler = new VentaKBHandler();
        getController().getPrecioVentaColumn().setOnEditCommit( new PrecioCellEditHandler() );
        Callback<TableColumn, TableCell> cellFactory =
                new Callback<TableColumn, TableCell>() {
                    public TableCell call(TableColumn p) {
                        return new EditingCell();
                    }
                };
        getController().getPrecioVentaColumn().setCellFactory( cellFactory );
    }

    @Override
    public void handle(Event event) {
        modoEspecial();
        getController().getVentaTableView().setEditable(true);
    }

    public void modoEspecial() {
        getController().setCapturaPaneDisable(true);
        getController().setMainToolBarDisable(true);
        getController().showHud("Ahora puede cambiar los precios\n[Esc] o click aquí para terminar modificaciones");
        getController().getVentaTableView().requestFocus();
        getController().getVentaTableView().onKeyReleasedProperty().set(ventaKBHandler);

    }

    public void modoNormal() {

        getController().setCapturaPaneDisable(false);
        getController().setMainToolBarDisable(false);
        getController().hideHud();
        getController().getVentaTableView().onKeyReleasedProperty().set(null);
        getController().getCapturaTextField().requestFocus();
    }

    public class PrecioCellEditHandler implements EventHandler<TableColumn.CellEditEvent<ProductoModel, String>> {
        public void handle(TableColumn.CellEditEvent<ProductoModel, String> t) {

            DecimalFormat df = (DecimalFormat) NumberFormat.getCurrencyInstance();
            df.setParseBigDecimal(true);
            BigDecimal bd = new BigDecimal(0);

            try {
                bd = (BigDecimal) df.parse(t.getNewValue());
                ProductoModel pm = t.getTableView().getSelectionModel().getSelectedItem();
                pm.impuestosProperty().set(pm.getProductoData().getImpuestos().divide(new BigDecimal(100)).multiply(bd));
                pm.descuentoProperty().set(pm.getProductoData().getDescuento().divide(new BigDecimal(100)).multiply(bd));
                pm.precioBaseProperty().set(bd.subtract(pm.impuestosProperty().get()).add(pm.descuentoProperty().get()));
                pm.precioProperty().set(bd);

                getController().getCajaLogic().onVentaListChanged(getController().getModel());
            } catch (ParseException e) {
                logger.error("Precio mal escrito");
            }

        }
    }

    public class VentaKBHandler implements EventHandler<KeyEvent> {

        @Override
        public void handle(KeyEvent event) {
            CajaController controller = VentaEspecialHandler.this.getController();

            if(event.getCode() == KeyCode.ESCAPE) {
                VentaEspecialHandler.this.modoNormal();
            }
        }
    }

    class EditingCell extends TableCell<ProductoModel, String> {

        private TextField textField;

        public EditingCell() {}

        @Override
        public void startEdit() {
            super.startEdit();

            if (textField == null) {
                createTextField();
            }

            setGraphic(textField);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            textField.selectAll();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(String.valueOf(getItem()));
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setGraphic(textField);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                } else {
                    setText(getString());
                    setContentDisplay(ContentDisplay.TEXT_ONLY);
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()*2);
            textField.setOnKeyPressed(new EventHandler<KeyEvent>() {

                @Override
                public void handle(KeyEvent t) {
                    if (t.getCode() == KeyCode.ENTER) {
                        commitEdit(textField.getText());
                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }

}
