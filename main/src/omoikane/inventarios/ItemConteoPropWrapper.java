package omoikane.inventarios;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanObjectProperty;
import javafx.beans.property.adapter.JavaBeanObjectPropertyBuilder;
import javafx.beans.property.adapter.JavaBeanStringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 05/04/13
 * Time: 03:29
 * To change this template use File | Settings | File Templates.
 */
public class ItemConteoPropWrapper {

    private JavaBeanStringProperty codigo;
    private JavaBeanStringProperty nombre;
    private JavaBeanObjectProperty<BigDecimal> conteo;

    private ItemConteoInventario _itemConteo;
    final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ItemConteoPropWrapper.class);

    public ItemConteoPropWrapper(ItemConteoInventario itemConteo) {
        _itemConteo = itemConteo;

        try {
            JavaBeanStringPropertyBuilder builder = JavaBeanStringPropertyBuilder.create();
            builder.bean(itemConteo);
            builder.name("codigo");
            codigo = builder.build();

            builder = JavaBeanStringPropertyBuilder.create();
            builder.bean(itemConteo);
            builder.name("nombre");
            nombre = builder.build();

            JavaBeanObjectPropertyBuilder<BigDecimal> builder1 = JavaBeanObjectPropertyBuilder.create();
            builder1.bean(itemConteo);
            builder1.name("conteo");
            conteo = builder1.build();
        } catch (NoSuchMethodException e) {
            logger.error("Invalid method to wrap", e);
        }
    }

    public ItemConteoInventario getBean() {
        return _itemConteo;
    }

    public StringProperty codigoProperty() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo.set(codigo);
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public ObjectProperty<BigDecimal> conteoProperty() {
        return conteo;
    }

    public void setConteo(BigDecimal conteo) {
        this.conteo.set(conteo);
    }

}
