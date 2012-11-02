package omoikane.caja.presentation;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 25/10/12
 * Time: 14:21
 * To change this template use File | Settings | File Templates.
 */
public class BuscarMasDummyProducto extends ProductoModel {
    public BuscarMasDummyProducto() {
        super();
        setConcepto(new SimpleStringProperty("Buscar más productos..."));
    }
}
