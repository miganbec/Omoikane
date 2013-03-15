package omoikane.inventarios;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.util.converter.DefaultStringConverter;
import net.sf.ehcache.Element;
import omoikane.sistema.TextFieldTableCell;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;

import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormatSymbols;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 27/02/13
 * Time: 05:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class TomaInventarioController implements Initializable {

    public Logger logger = Logger.getLogger(getClass());

    ObservableList<ItemTomaInventario> items;

    @Autowired
    EhCacheManagerFactoryBean cacheManager;

    @FXML TableView<ItemTomaInventario> itemsTable;
    @FXML TableColumn<ItemTomaInventario, String> codigoCol;
    @FXML TableColumn<ItemTomaInventario, String> nombreProductoCol;
    @FXML TableColumn<ItemTomaInventario, BigDecimal> conteoCol;
    @FXML Label idLabel;
    @FXML TextField codigoTextField;
    Character decimalSeparator = getDecimalSeparator();

    private Character getDecimalSeparator() {
        DecimalFormatSymbols custom=new DecimalFormatSymbols();
        return custom.getDecimalSeparator();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        codigoCol        .setCellValueFactory(new PropertyValueFactory<ItemTomaInventario, String>("codigo"));
        codigoCol        .setCellFactory(new ImprovedCellFactory<ItemTomaInventario>(ItemTomaInventario.class));

        nombreProductoCol.setCellValueFactory(new PropertyValueFactory<ItemTomaInventario, String>("nombre"));
        nombreProductoCol.setCellFactory(new ImprovedCellFactory<ItemTomaInventario>(ItemTomaInventario.class));

        conteoCol        .setCellValueFactory(new PropertyValueFactory<ItemTomaInventario, BigDecimal>("conteo"));
        conteoCol        .setCellFactory(new NumericCellFactory<ItemTomaInventario>(ItemTomaInventario.class));

        codigoTextField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER) {
                    items.add(new ItemTomaInventario(codigoTextField.getText(), "", new BigDecimal("0")));
                }
            }
        });

        items = (ObservableList<ItemTomaInventario>) cacheManager.getObject().getCache("test").get("prueba").getObjectValue();
        get ( prueba ) es nulo
        if(items == null)
            items = FXCollections.observableArrayList();

        ItemTomaInventario item = new ItemTomaInventario();
        item.setNombre(new SimpleStringProperty("prueba"));
        item.setCodigo(new SimpleStringProperty("0001"));
        items.add(item);
        itemsTable.setItems(items);

        items.addListener(new ListChangeListener<ItemTomaInventario>() {
            @Override
            public void onChanged(final Change<? extends ItemTomaInventario> change) {

                change.next();
                if(change.wasAdded()) {
                    cacheManager.getObject().getCache("test").put(new Element("prueba", items));
                    //itemsTable.getFocusModel().focus(itemsTable.getSelectionModel().getSelectedIndex(), itemsTable.getColumns().get(0));

                    //itemsTable.getFocusModel().focus(0, codigoCol);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            itemsTable.edit(itemsTable.getSelectionModel().getSelectedIndex() + 1, itemsTable.getColumns().get(0));
                        }
                    });

                }

            }
        });
    }
    public class ImprovedCellFactory<S> implements Callback<TableColumn<S, String>, TableCell<S, String>> {

        Class<S> impl;
        public ImprovedCellFactory(Class<S> impl) { this.impl = impl;  }
        @Override
        public TableCell<S, String> call(TableColumn<S, String> stTableColumn) {
            return new ImprovedTableCell<S, String>(impl);
        }

    }

    public class NumericCellFactory<S> implements Callback<TableColumn<S, BigDecimal>, TableCell<S, BigDecimal>> {

        Class<S> impl;
        public NumericCellFactory(Class<S> impl) { this.impl = impl;  }
        @Override
        public TableCell<S, BigDecimal> call(TableColumn<S, BigDecimal> stTableColumn) {
            return new NumericTableCell<S, BigDecimal>(impl);
        }

    }

    public class NumericTableCell<S, T> extends ImprovedTableCell<S, BigDecimal> {

        public NumericTableCell(Class<S> impl) {
            super(impl, new BigDecimalStringConverter());
        }

        @Override
        public void postCreateTextField(TextField t) {
            super.postCreateTextField(t);
            textField.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
                public void handle(KeyEvent t) {
                    Integer pos = textField.caretPositionProperty().get();
                    String newVal = textField.getText().substring(0, pos)
                            + t.getCharacter()
                            + textField.getText().substring(pos);


                    Pattern pattern = Pattern.compile("^\\d*(\\"+decimalSeparator+"?\\d{0,2})$");
                    Matcher matcher = pattern.matcher(newVal);
                    if (!matcher.find()) {
                        t.consume();
                    }

                }
            });

        }
    }

    public class ImprovedTableCell<S, T> extends TextFieldTableCell<S, T> {

        Class<S> impl;
        public ImprovedTableCell(Class<S> impl) {
            this(impl, (StringConverter<T>) new DefaultStringConverter());
        }
        public ImprovedTableCell(Class<S> impl, StringConverter<T> sc) {
            super(sc);
            this.impl = impl;
        }

        @Override
        public void postCreateTextField(TextField t) {

            t.setOnKeyReleased(new EventHandler<KeyEvent>() {
                @Override
                public void handle(final KeyEvent t) {
                            toRunLater(t);
                }

                private void toRunLater(KeyEvent t) {
                    if (t.getCode() == KeyCode.ENTER) {
                        t.consume();
                        if (getConverter() == null) {
                            throw new IllegalStateException(
                                    "Attempting to convert text input into Object, but provided "
                                            + "StringConverter is null. Be sure to set a StringConverter "
                                            + "in your cell factory.");
                        }
                        commitEdit(getConverter().fromString(textField.getText()));

                        TablePosition position = getTableView().getFocusModel().getFocusedCell();
                        int nextCol = position.getColumn()+1;
                        int nextRow = position.getRow();

                        if(getTableView().getColumns().size()-1 < nextCol)
                        {
                            //nextRow = position.getRow() + 1;
                            /*
                            if(getTableView().getItems().size()-1 < nextRow) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            getTableView().getItems().add(impl.newInstance());
                                        } catch (InstantiationException e) {
                                            logger.error(e.getMessage(), e);
                                        } catch (IllegalAccessException e) {
                                            logger.error(e.getMessage(), e);
                                        }
                                    }
                                });

                            }*/
                            nextCol = 0;
                        }

                        final int finalNextRow = nextRow;
                        final int finalNextCol = nextCol;
                        getTableView().edit(finalNextRow, getTableView().getColumns().get(finalNextCol));
                        getTableView().getFocusModel().focus(finalNextRow, getTableView().getColumns().get(finalNextCol));


                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                }
            });
        }

        @Override
        public void startEdit() {

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ImprovedTableCell.super.startEdit();
                    textField.requestFocus();

                }
            });
        }
    }


}
