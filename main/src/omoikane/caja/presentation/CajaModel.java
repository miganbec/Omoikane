package omoikane.caja.presentation;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 12/09/12
 * Time: 10:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class CajaModel {
    private StringProperty captura;
    private ObservableList<ProductoModel> productos;

    /**
     * Instancía todos los atributos
     */
    public CajaModel() {
        setCaptura(new SimpleStringProperty());

        ObservableList<ProductoModel> list = FXCollections.observableArrayList();
        setProductos(list);
    }

    public StringProperty getCaptura() {
        return captura;
    }

    public void setCaptura(StringProperty captura) {
        this.captura = captura;
    }


    public ObservableList<ProductoModel> getProductos() {
        return productos;
    }

    public void setProductos(ObservableList<ProductoModel> productos) {
        this.productos = productos;
    }

}
