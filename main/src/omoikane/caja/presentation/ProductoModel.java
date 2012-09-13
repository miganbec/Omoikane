package omoikane.caja.presentation;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 13/09/12
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductoModel {
    private StringProperty concepto;
    private ObjectProperty<BigDecimal> cantidad;
    private ObjectProperty<BigDecimal> precio;


    /**
     *
     * @return String del importe formateado con el tipo de moneda predeterminado
     */
    public String getImporteString() {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        BigDecimal importe = getImporte();
        return nf.format( importe );
    }

    public BigDecimal getImporte() {
        BigDecimal importe = cantidad.get().multiply( precio.get() );
        return importe;
    }


    public String getConceptoString() {
        return concepto.get();
    }

    public StringProperty getConcepto() {
        return concepto;
    }

    public void setConcepto(StringProperty concepto) {
        this.concepto = concepto;
    }

    /**
     *
     * @return String de la cantidad formateada con un máximo y mínimo de 3 dígitos decimales
     */
    public String getCantidadString() {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(3);
        nf.setMaximumFractionDigits(3);
        return nf.format(cantidad.get());
    }

    public ObjectProperty<BigDecimal> getCantidad() {
        return cantidad;
    }

    /**
     * Asigna la cantidad de producto en el renglón y establece una escala de máximo de 3 dígitos decimales.
     * Redondeo BigDecimal.ROUND_HALF_UP
     * @param cantidad
     */
    public void setCantidad(ObjectProperty<BigDecimal> cantidad) {
        cantidad.get().setScale(3, BigDecimal.ROUND_HALF_UP);
        this.cantidad = cantidad;
    }

    public ObjectProperty<BigDecimal> getPrecio() {
        return precio;
    }

    /**
     *
     * @return String del precio formateado con el tipo de moneda predeterminado
     */
    public String getPrecioString() {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        return nf.format(precio.get());
    }

    /**
     * Asigna el precio y establece la precisión a dos dígitos decimales y redondeo BigDecimal.ROUND_HALF_UP
     * @param precio
     */
    public void setPrecio(ObjectProperty<BigDecimal> precio) {
        precio.get().setScale(2, BigDecimal.ROUND_HALF_UP);
        this.precio = precio;
    }
}
