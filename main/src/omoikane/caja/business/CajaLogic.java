package omoikane.caja.business;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import omoikane.caja.presentation.CajaModel;
import omoikane.caja.presentation.ProductoModel;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 13/09/12
 * Time: 02:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class CajaLogic implements ICajaLogic {
    @Override
    public void captura(CajaModel model) {

        ProductoModel productoModel = new ProductoModel();
        productoModel.setConcepto(new SimpleStringProperty(model.getCaptura().get()));
        productoModel.setCantidad(new SimpleObjectProperty<BigDecimal>(new BigDecimal("1.25")));
        productoModel.setPrecio(new SimpleObjectProperty<BigDecimal>(new BigDecimal("1048.32")));

        model.getProductos().add(productoModel);
    }

    @Override
    public void calcularCambio(CajaModel model )
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void terminarVenta(CajaModel model) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
