package omoikane.caja.business;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import omoikane.caja.presentation.CajaModel;
import omoikane.caja.presentation.ProductoModel;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 13/09/12
 * Time: 02:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class CajaLogic implements ICajaLogic {
    @Override
    public void onCaptura(CajaModel model) {

        LineaDeCaptura captura = new LineaDeCaptura(model.getCaptura().get());

        ProductoModel productoModel = new ProductoModel();
        productoModel.setConcepto(new SimpleStringProperty(captura.getCodigo()));
        productoModel.setCantidad(new SimpleObjectProperty<BigDecimal>(captura.getCantidad()));
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

    @Override
    public void onProductListChanged(CajaModel model) {
        BigDecimal subtotal = new BigDecimal(0);
        subtotal.setScale(2, BigDecimal.ROUND_HALF_UP);

        BigDecimal descuentos = new BigDecimal(0);
        descuentos.setScale(2, BigDecimal.ROUND_HALF_UP);

        BigDecimal impuestos = new BigDecimal(0);
        impuestos.setScale(2, BigDecimal.ROUND_HALF_UP);

        BigDecimal total = new BigDecimal(0);
        total.setScale(2, BigDecimal.ROUND_HALF_UP);

        for ( ProductoModel producto : model.getProductos() ) {
            subtotal   = subtotal  .add( producto.getImporte() );
            descuentos = descuentos.add( producto.getDescuentos().get() );
            impuestos  = impuestos .add( producto.getImpuestos().get() );
        }

        total = total.add( subtotal );
        total = total.subtract( descuentos );
        total = total.add( impuestos );

        model.getSubtotal().set( subtotal );
        model.getDescuento().set( descuentos );
        model.getImpuestos().set( impuestos );
        model.getTotal().set( total );
    }
}
