package omoikane.inventarios;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import jfxtras.labs.scene.control.BigDecimalField;
import omoikane.producto.Articulo;
import omoikane.repository.ProductoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 23/02/13
 * Time: 03:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class StockLevelsController implements Initializable {

    @Autowired
    ProductoRepo productoRepo;

    @Autowired
    JpaTransactionManager transactionManager;

    @PersistenceContext
    EntityManager entityManager;

    Long idProducto = 1l;

    @FXML BigDecimalField stockTienda;
    @FXML BigDecimalField stockBodega;
    @FXML BigDecimalField stockMin;
    @FXML BigDecimalField stockMax;
    @FXML TextArea ubicacion;
    @FXML ToggleGroup clasificacion;
    @FXML RadioButton radioClaseA;
    @FXML RadioButton radioClaseB;
    @FXML RadioButton radioClaseC;

    Articulo producto;
    Stock stock;

    @FXML
    private void accionGuardar(ActionEvent actionEvent) {
        /*
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        Object result = transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                stock.setUbicacion( ubicacion.getText() );
                entityManager.merge(stock);
            }
        });*/
        stock.setUbicacion( ubicacion.getText() );
        productoRepo.saveAndFlush(producto);
    }

    @FXML
    private void accionCerrar(ActionEvent actionEvent) {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        producto = productoRepo.readByPrimaryKey(idProducto);
        stock = producto.stock;

        stockTienda.setNumber( stock.getEnTienda() );
        stockBodega.setNumber(stock.getEnBodega());
        stockMin.setNumber( stock.getMinimo() );
        stockMax.setNumber( stock.getMaximo() );
        ubicacion.setText( stock.getUbicacion() );

        switch( stock.getClasificacion() ) {
            case 'a':
            case 'A':
                clasificacion.selectToggle( radioClaseA );
                break;
            case 'b':
            case 'B':
                clasificacion.selectToggle( radioClaseB );
                break;
            case 'c':
            case 'C':
                clasificacion.selectToggle( radioClaseC );
                break;
        }

    }
}
