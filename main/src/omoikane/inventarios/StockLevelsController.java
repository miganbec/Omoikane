package omoikane.inventarios;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import jfxtras.labs.scene.control.BigDecimalField;
import omoikane.producto.Articulo;
import omoikane.repository.ProductoRepo;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.*;

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

    @Autowired
    Validator validator;

    @PersistenceContext
    EntityManager entityManager;

    Long idProducto = 500l;

    public Logger logger = Logger.getLogger(getClass());

    @FXML BigDecimalField stockTienda;
    @FXML BigDecimalField stockBodega;
    @FXML BigDecimalField stockMin;
    @FXML BigDecimalField stockMax;
    @FXML TextArea ubicacion;
    @FXML ToggleGroup clasificacion;
    @FXML RadioButton radioClaseA;
    @FXML RadioButton radioClaseB;
    @FXML RadioButton radioClaseC;
    @FXML Label notaStockMin;
    @FXML Label notaStockMax;
    @FXML Label notaUbicacion;

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
        if(radioClaseA.isSelected())
            stock.setClasificacion('A');
        else if (radioClaseB.isSelected())
            stock.setClasificacion('B');
        else
            stock.setClasificacion('C');
        stock.setMaximo( stockMax.getNumber() );
        stock.setMinimo(stockMin.getNumber());
        stock.setUbicacion( ubicacion.getText() );
        if(validar(producto.stock))
        {
            productoRepo.saveAndFlush(producto);
            logger.info("Información de stock del producto actualizada");
        }
    }

    @FXML
    private void accionCerrar(ActionEvent actionEvent) {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        producto = productoRepo.findByIdIncludeStock(idProducto);
        stock = producto.getStock();

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

    private boolean validar(Stock stock) {
        DataBinder binder = new DataBinder(stock);
        binder.setValidator(validator);
        binder.validate();
        BindingResult bindingResult = binder.getBindingResult();
        notaStockMax.setText("");
        notaStockMin.setText("");
        notaUbicacion.setText("");


        if(bindingResult.hasErrors()) {
            for( ObjectError oe : bindingResult.getAllErrors() ) {
                if(oe.getClass() == FieldError.class) {
                    FieldError fe = (FieldError) oe;
                    if(fe.getField().equals("minimo"))    { notaStockMin .setText(fe.getDefaultMessage());  }
                    if(fe.getField().equals("maximo"))    { notaStockMax .setText(fe.getDefaultMessage());  }
                    if(fe.getField().equals("ubicacion")){ notaUbicacion.setText(fe.getDefaultMessage());  }
                } else {
                    logger.info(oe.getDefaultMessage());
                }
            }
        } else {
            return true;
        }
        return false;
    }
}
