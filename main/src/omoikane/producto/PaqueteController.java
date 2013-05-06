package omoikane.producto;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 16/02/13
 * Time: 13:41
 * To change this template use File | Settings | File Templates.
 */

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import jfxtras.labs.scene.control.BigDecimalField;
import omoikane.entities.Paquete;
import omoikane.repository.ProductoRepo;
import omoikane.sistema.Dialogos;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static omoikane.sistema.Permisos.getPMA_MODIFICARARTICULO;
import static omoikane.sistema.Usuarios.cerrojo;


public class PaqueteController
        implements Initializable {

    public Logger logger = Logger.getLogger(getClass());

    ObservableList<Paquete> paquete;
    Long productoId = 1l;

    @Autowired
    ProductoRepo productoRepo;

    @Autowired
    Validator validator;

    @Autowired
    JpaTransactionManager transactionManager;

    @PersistenceContext
    EntityManager entityManager;

    @FXML //  fx:id="addButton"
    private Button addButton;

    @FXML //  fx:id="contenidoTable"
    private TableView<Paquete> tablaContenido;

    @FXML //  fx:id="paqueteCheckbox"
    private CheckBox paqueteCheckbox;

    @FXML //  fx:id="removeButton"
    private Button removeButton;
    @FXML private TableColumn<Paquete, String> cantidadCol;
    @FXML private TableColumn<Paquete, String> productoCol;

    @FXML private TableColumn<Paquete, String> precioCol;
    @FXML private BigDecimalField txtCantidad;

    @FXML private TextField txtIdProducto;
    @FXML private Label notaCantidad;

    @FXML private Label notaIdProducto;

    @FXML
    private void actionAdd() {
        BigDecimal cantidad             = txtCantidad.getNumber();
        Long       productoContenedorId = 1l;
        Long       productoContenidoId  = Long.parseLong("0" + txtIdProducto.getText());
        Boolean    contenidoExists      = productoRepo.exists(productoContenedorId);

        Paquete renglonPaquete = new Paquete();
        renglonPaquete.setCantidad( cantidad );
        renglonPaquete.setProductoContenedor(productoRepo.readByPrimaryKey( productoContenedorId ));
        renglonPaquete.setProductoContenido(productoRepo.readByPrimaryKey( contenidoExists ? productoContenidoId : null ));

        if(validar(renglonPaquete)) {
            tablaContenido.getItems().add(renglonPaquete);
            guardar(renglonPaquete);
            txtCantidad.setNumber(new BigDecimal(1));
            txtIdProducto.setText("");
        }

    }

    private void guardar(final Paquete renglonPaquete) {
        if(!cerrojo(getPMA_MODIFICARARTICULO())){ Dialogos.lanzarAlerta("Acceso Denegado"); return; }
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        Object result = transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                entityManager.persist(renglonPaquete);
            }
        });

    }

    private boolean validar(Paquete renglonPaquete) {
        DataBinder binder = new DataBinder(renglonPaquete);
        binder.setValidator(validator);
        binder.validate();
        BindingResult bindingResult = binder.getBindingResult();

        if(bindingResult.hasErrors()) {
            for( ObjectError oe : bindingResult.getAllErrors() ) {
                if(oe.getClass() == FieldError.class) {
                    FieldError fe = (FieldError) oe;
                    if(fe.getField().equals("cantidad"))          { notaCantidad.setText(fe.getDefaultMessage());   }
                    if(fe.getField().equals("productoContenido")) { notaIdProducto.setText("Producto no encontrado"); }
                } else {
                    logger.info(oe.getDefaultMessage());
                }
            }
        } else {
            notaCantidad.setText("");
            notaIdProducto.setText("");
            return true;
        }
        return false;
    }

    @FXML
    private void actionRemove() {
        Paquete productoDesprendido = tablaContenido.selectionModelProperty().get().getSelectedItem();
        if(productoDesprendido != null) {
            borrar(productoDesprendido);
        }
    }

    private void borrar(final Paquete productoDesprendido) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        Object result = transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                Paquete p = entityManager.find(Paquete.class, productoDesprendido.getId());
                entityManager.remove(p);
                tablaContenido.getItems().remove(productoDesprendido);
            }
        });
    }

    @FXML
    private void paqueteStateSwitched() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        Object result = transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                Articulo producto = productoRepo.readByPrimaryKey(productoId);
                producto.setEsPaquete(!producto.getEsPaquete());
                entityManager.persist(producto);
                paqueteCheckbox.setSelected(producto.getEsPaquete());
            }
        });
    }
    PaqueteController me;
    public PaqueteController() {
        me = this;
    }

    public PaqueteController me() { return me; }

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert addButton != null : "fx:id=\"addButton\" was not injected: check your FXML file 'PaqueteView.fxml'.";
        assert tablaContenido != null : "fx:id=\"contenidoTable\" was not injected: check your FXML file 'PaqueteView.fxml'.";
        assert paqueteCheckbox != null : "fx:id=\"paqueteCheckbox\" was not injected: check your FXML file 'PaqueteView.fxml'.";
        assert removeButton != null : "fx:id=\"removeButton\" was not injected: check your FXML file 'PaqueteView.fxml'.";
        assert txtCantidad != null : "fx:id=\"cantidadTxt\" was not injected: check your FXML file 'PaqueteView.fxml'.";

        txtIdProducto.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent t) {
                char ar[] = t.getCharacter().toCharArray();
                char ch = ar[t.getCharacter().toCharArray().length - 1];
                if (!(ch >= '0' && ch <= '9')) {
                    t.consume();
                }
            }
        });

        cantidadCol.setCellValueFactory(new PropertyValueFactory<Paquete, String>("cantidad"));
        productoCol.setCellValueFactory(new PropertyValueFactory<Paquete, String>("descripcion"));
        precioCol.setCellValueFactory(new PropertyValueFactory<Paquete, String>("precioString"));

    }

    public void setProducto(Long id) {
        this.productoId = id;
        llenarTabla();

        Articulo producto = productoRepo.readByPrimaryKey(productoId);
        paqueteCheckbox.setSelected( producto.getEsPaquete() );
    }

    public void llenarTabla() {
        /* Éste código también funciona, es otra opción:  */
        /*
        EntityManager em = entityManager.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Articulo a = em.find(Articulo.class, productoId);
        List<Paquete> listPaquete = a.renglonesPaquete;
        paquete = FXCollections.observableArrayList(listPaquete);
        tablaContenido.setItems(paquete);
        em.close();*/

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        Object result = transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                Articulo a = productoRepo.readByPrimaryKey(productoId);
                List<Paquete> listPaquete = a.renglonesPaquete;
                paquete = FXCollections.observableArrayList(listPaquete);
                tablaContenido.setItems(paquete);
            }
        });

    }


}

