package omoikane.caja.presentation;

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
public class Number2StringBinding extends StringBinding {
    private ObjectProperty<BigDecimal> bigDecimalProperty;
    private NumberFormat numberFormat;

    public Number2StringBinding(ObjectProperty<BigDecimal> bigDecimalProperty, NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
        this.bigDecimalProperty = bigDecimalProperty;
        super.bind( bigDecimalProperty );
    }

    @Override
    protected String computeValue() {
        return numberFormat.format( bigDecimalProperty.get() );
    }
}
