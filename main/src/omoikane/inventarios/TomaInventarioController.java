package omoikane.inventarios;

import javafx.application.Platform;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
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
import omoikane.repository.ConteoInventarioRepo;
import omoikane.sistema.TextFieldTableCell;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.TransactionManager;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormatSymbols;
import java.util.*;
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

    ConteoInventarioPropWrapper modelo;

    @Autowired
    EhCacheManagerFactoryBean cacheManager;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    ConteoInventarioRepo repo;

    @FXML TableView<ItemConteoPropWrapper> itemsTable;
    @FXML TableColumn<ItemConteoPropWrapper, String> codigoCol;
    @FXML TableColumn<ItemConteoPropWrapper, String> nombreProductoCol;
    @FXML TableColumn<ItemConteoPropWrapper, BigDecimal> conteoCol;
    @FXML Label idLabel;
    @FXML Label fechaLabel;
    @FXML TextField codigoTextField;

    @FXML public void guardarAction(ActionEvent actionEvent) {
        modelo.setCompletado(true);
        Task<Void> persistTask = persistModel();
        persistTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                initModel();
                logger.info("Captura de conteo guardada");
            }
        });
        new Thread(persistTask).start();
    }

    @FXML public void onEliminarAction(ActionEvent actionEvent) {
        Task<Void> deleteTask = deleteModel();
        deleteTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                initModel();
                logger.info("Captura eliminada");
            }
        });
        new Thread(deleteTask).start();
    }

    Character decimalSeparator = getDecimalSeparator();

    private Character getDecimalSeparator() {
        DecimalFormatSymbols custom=new DecimalFormatSymbols();
        return custom.getDecimalSeparator();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        codigoCol        .setCellValueFactory(new PropertyValueFactory<ItemConteoPropWrapper, String>("codigo"));
        codigoCol        .setCellFactory(new ImprovedCellFactory<ItemConteoPropWrapper>(ItemConteoPropWrapper.class));

        nombreProductoCol.setCellValueFactory(new PropertyValueFactory<ItemConteoPropWrapper, String>("nombre"));
        nombreProductoCol.setCellFactory(new ImprovedCellFactory<ItemConteoPropWrapper>(ItemConteoPropWrapper.class));

        conteoCol        .setCellValueFactory(new PropertyValueFactory<ItemConteoPropWrapper, BigDecimal>("conteo"));
        conteoCol        .setCellFactory(new NumericCellFactory<ItemConteoPropWrapper>(ItemConteoPropWrapper.class));

        initModel();

        codigoTextField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER) {
                    ItemConteoInventario newItemBean = new ItemConteoInventario(codigoTextField.getText(), "prueba", new BigDecimal("10"));
                    ItemConteoPropWrapper newItem = new ItemConteoPropWrapper(newItemBean);
                    modelo.addItem(newItem);
                }
            }
        });


    }

    private void initModel() {
        modelo = loadOrCreateModel();
        itemsTable.setItems(modelo.getItems());
        fechaLabel.textProperty().bind(Bindings.convert(modelo.getDate()));
        idLabel.textProperty().bind(Bindings.convert(modelo.getId()));

        modelo.getItems().addListener(new MyListChangeListener());
    }

    @Transactional
    private ConteoInventarioPropWrapper loadOrCreateModel() {
        ConteoInventario conteoInventario = repo.findByCompletado(false);

        if(conteoInventario != null) {
            conteoInventario = repo.findByCompletado(false);
        } else {
            conteoInventario = new ConteoInventario();
            conteoInventario.setFecha( new Date() );
            conteoInventario = repo.saveAndFlush(conteoInventario);
        }

        ConteoInventarioPropWrapper conteoInventarioPropWrapper = new ConteoInventarioPropWrapper(conteoInventario);

        return conteoInventarioPropWrapper;
    }

    private class MyListChangeListener implements ListChangeListener<ItemConteoPropWrapper> {

        @Override
        public void onChanged(Change<? extends ItemConteoPropWrapper> change) {
            change.next();
            new Thread(
                    persistModel()
            ).start();
        }
    }

    /**
     * Ésta función almacena todo el modelo de esta vista.
     * Nota: Éste método utiliza Spring e Hibernate directamente, TransactionTemplate para la transacción
     * y entityManager para persistir a diferencia del método deleteModel. Simplemente por experimentación.
     * @return
     */

    private Task<Void> persistModel() {
        Task persistTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
                transactionTemplate.execute(new TransactionCallbackWithoutResult() {

                    protected void doInTransactionWithoutResult(TransactionStatus status) {
                        entityManager.merge(modelo._conteoInventario);
                        entityManager.flush();
                    }
                });

                itemsTable.edit(itemsTable.getSelectionModel().getSelectedIndex() + 1, itemsTable.getColumns().get(0));
                return null;
            }
        };
        return persistTask;
    }

    /**
     * Elimina el modelo de la presentación.
     * Nota: Utiliza Hades/Spring DAO para la eliminación a diferencia del método persistModel, claramente
     * el código es más simple y legible.
     * @return
     */
    private Task<Void> deleteModel() {
        Task deleteTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                repo.delete(modelo._conteoInventario);
                return null;
            }
        };
        return deleteTask;
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
                        persistModel();

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
