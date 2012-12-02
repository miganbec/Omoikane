package omoikane.caja.presentation;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 14/09/12
 * Time: 11:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class Number2DoubleBinding extends DoubleBinding {
    private ObjectProperty<BigDecimal> bigDecimalProperty;

    public Number2DoubleBinding(ObjectProperty<BigDecimal> bigDecimalProperty) {
        this.bigDecimalProperty = bigDecimalProperty;
        super.bind( bigDecimalProperty );
    }

    @Override
    protected double computeValue() {

        return bigDecimalProperty.get().doubleValue();
    }
}
