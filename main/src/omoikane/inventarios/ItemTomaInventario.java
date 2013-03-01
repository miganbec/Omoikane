package omoikane.inventarios;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 27/02/13
 * Time: 05:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class ItemTomaInventario {
    private StringProperty codigo;
    private StringProperty nombre;
    private ObjectProperty<BigDecimal> conteo;

    public ItemTomaInventario() {
        this("", "", new BigDecimal("0.00"));
    }

    public ItemTomaInventario(String codigo, String nombre, BigDecimal conteo) {
            setCodigo( new SimpleStringProperty( codigo ) );
            setNombre( new SimpleStringProperty( nombre ) );
            setConteo( new SimpleObjectProperty<BigDecimal>( conteo ) );
    }

    public StringProperty codigoProperty() {
        return codigo;
    }

    public void setCodigo(StringProperty codigo) {
        this.codigo = codigo;
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public void setNombre(StringProperty nombre) {
        this.nombre = nombre;
    }

    public ObjectProperty<BigDecimal> conteoProperty() {
        return conteo;
    }

    public void setConteo(ObjectProperty<BigDecimal> conteo) {
        this.conteo = conteo;
    }
}
