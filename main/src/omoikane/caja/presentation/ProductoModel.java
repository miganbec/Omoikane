package omoikane.caja.presentation;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import omoikane.producto.Producto;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 13/09/12
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductoModel {
    private LongProperty id;
    private StringProperty concepto;
    private StringProperty codigo;
    private ObjectProperty<BigDecimal> cantidad;
    private ObjectProperty<BigDecimal> precio;
    private ObjectProperty<BigDecimal> precioBase;
    private ObjectProperty<BigDecimal> impuestosBase;
    private ObjectProperty<BigDecimal> impuestos;
    private ObjectProperty<BigDecimal> descuentosBase;
    private StringProperty importeString;
    private Producto productoData;

    public ProductoModel() {
        id         = new SimpleLongProperty(0l);
        concepto   = new SimpleStringProperty("Concepto vacío");
        setCodigo   ( new SimpleStringProperty(null) );
        setCantidad ( new SimpleObjectProperty<BigDecimal>(new BigDecimal(0)) );
        setPrecio   ( new SimpleObjectProperty<BigDecimal>(new BigDecimal(0)) );
        precioBase = new SimpleObjectProperty<BigDecimal>(new BigDecimal(0));
        impuestosBase = new SimpleObjectProperty<BigDecimal>(new BigDecimal(0));
        descuentosBase = new SimpleObjectProperty<BigDecimal>(new BigDecimal(0));
        importeString = new SimpleStringProperty("");

    }

    /**
     *
     * @return String del importe formateado con el tipo de moneda predeterminado
     */
    //public String getImporteString() {
    //    NumberFormat nf = NumberFormat.getCurrencyInstance();
    //    BigDecimal importe = getImporte();
    //    return nf.format( importe );
    //}

    public BigDecimal getImporte() {
        BigDecimal importe = cantidad.get().multiply( precio.get() );
        return importe;
    }

    private void updateImporteStringProperty() {
        NumberFormat nf      = NumberFormat.getCurrencyInstance();
        BigDecimal   importe = getImporte();
        importeStringProperty().set( nf.format( importe ) );
    }

    public StringProperty importeStringProperty() {
        return importeString;
    }

    public LongProperty getId() {
        return id;
    }

    public Long getLongId() {
        return id.get();
    }


    public String getConceptoString() {
        return concepto.get();
    }

    public StringProperty conceptoProperty() {
        return concepto;
    }

    private void setConcepto(StringProperty concepto) {
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

    /**
     *
     * @return Quantity sold
     */
    public BigDecimal getCantidad() {
        return cantidad.get();
    }

    public ObjectProperty<BigDecimal> cantidadProperty() {
        return cantidad;
    }

    /**
     * Asigna la cantidad de producto en el renglón y establece una escala de máximo de 3 dígitos decimales.
     * Redondeo BigDecimal.ROUND_HALF_UP
     * @param cantidad
     */

    private void setCantidad(ObjectProperty<BigDecimal> cantidad) {
        cantidad.get().setScale(3, BigDecimal.ROUND_HALF_UP);
        this.cantidad = cantidad;

        cantidad.addListener(new ChangeListener<BigDecimal>() {
            @Override
            public void changed(ObservableValue<? extends BigDecimal> observableValue, BigDecimal bigDecimal, BigDecimal bigDecimal1) {
                updateImporteStringProperty();
            }
        });
    }

    public ObjectProperty<BigDecimal> precioProperty() {
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
     */
    private void setPrecio(ObjectProperty<BigDecimal> precio) {
        precio.get().setScale(2, BigDecimal.ROUND_HALF_UP);
        this.precio = precio;

        precio.addListener(new ChangeListener<BigDecimal>() {
            @Override
            public void changed(ObservableValue<? extends BigDecimal> observableValue, BigDecimal bigDecimal, BigDecimal bigDecimal1) {
                updateImporteStringProperty();
            }
        });

    }

    public BigDecimal getImpuestos() {
        return impuestosBase.get().multiply(cantidad.get());
    }

    public ObjectProperty<BigDecimal> impuestosBaseProperty() {
        return impuestosBase;
    }

    private void setImpuestosBase(ObjectProperty<BigDecimal> impuestosBase) {
        impuestosBase.get().setScale(2, BigDecimal.ROUND_HALF_UP);
        this.impuestosBase = impuestosBase;
    }

    public String getSubtotalString() {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        return nf.format(getSubtotal());
    }

    public BigDecimal getSubtotal() {
        BigDecimal subtotal = cantidad.get().multiply( precioBase.get() );
        return subtotal;
    }


    public String getImpuestosString() {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        return nf.format(getImpuestos());
    }

    public BigDecimal getDescuentos() {
        return descuentosBase.get().multiply(cantidad.get());
    }

    public ObjectProperty<BigDecimal> descuentosBaseProperty() {
        return descuentosBase;
    }

    private void setDescuentosBase(ObjectProperty<BigDecimal> descuentosBase) {
        descuentosBase.get().setScale(2, BigDecimal.ROUND_HALF_UP);
        this.descuentosBase = descuentosBase;
    }

    public String getDescuentosString() {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        return nf.format(descuentosBase.get());
    }

    private void setPrecioBase(ObjectProperty<BigDecimal> precioBase) {
        this.precioBase = precioBase;
    }

    public ObjectProperty<BigDecimal> precioBaseProperty() {
        return precioBase;
    }

    public StringProperty codigoProperty() {
        return codigo;
    }

    private void setCodigo(StringProperty codigo) {
        this.codigo = codigo;
    }

    public Producto getProductoData() {
        return productoData;
    }

    public void setProductoData(Producto productoData) {
        this.productoData = productoData;
    }
}
