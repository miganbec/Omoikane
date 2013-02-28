package omoikane.inventarios;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 27/02/13
 * Time: 05:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class TomaInventarioController implements Initializable {

    ObservableList<ItemTomaInventario> items;

    @FXML TableView<ItemTomaInventario> itemsTable;
    @FXML TableColumn<ItemTomaInventario, String> codigoCol;
    @FXML TableColumn<ItemTomaInventario, String> nombreProductoCol;
    @FXML TableColumn<ItemTomaInventario, BigDecimal> conteoCol;
    @FXML Label idLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        codigoCol        .setCellValueFactory(new PropertyValueFactory<ItemTomaInventario, String>("codigo"));
        codigoCol        .setCellFactory(TextFieldTableCell.<ItemTomaInventario>forTableColumn());

        nombreProductoCol.setCellValueFactory(new PropertyValueFactory<ItemTomaInventario, String>("nombre"));
        Callback<TableColumn, TableCell> miCellFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn tableColumn) {
                return new BigDecimalTableCell();
            }
        };
        /*
        nombreProductoCol.setCellFactory(new Callback<TableColumn<Object, String>, TableCell<Object, String>>() {
            @Override
            public TableCell call(TableColumn itemTomaInventarioStringTableColumn) {
                return new BigDecimalTableCell();
            }
        });
        */
        //conteoCol        .setCellValueFactory(new PropertyValueFactory<ItemTomaInventario, BigDecimal>("conteo"));

        items = FXCollections.observableArrayList();
        ItemTomaInventario item = new ItemTomaInventario();
        item.setNombre(new SimpleStringProperty("prueba"));
        item.setCodigo(new SimpleStringProperty("0001"));
        items.add(item);
        itemsTable.setItems(items);

    }

    public class BigDecimalTableCell extends TableCell<Object, String> {
        public BigDecimalTableCell() {
            setOnKeyReleased(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent t) {
                    if (t.getCode() == KeyCode.ENTER) {
                        commitEdit(BigDecimalTableCell.this.getText());
                        getTableView().getFocusModel().focusRightCell();
                        getTableView().edit(getTableRow().getIndex(), getTableView().getFocusModel().getFocusedCell().getTableColumn());
                    }
                }
            });
        }
    }
}
