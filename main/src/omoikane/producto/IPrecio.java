package omoikane.producto;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 01/10/12
 * Time: 18:01
 * To change this template use File | Settings | File Templates.
 */
public interface IPrecio {
    public BigDecimal getPorcentajeDescuentoTotal();
    public BigDecimal getDescuento();
    public BigDecimal getUtilidad();
    public BigDecimal getImpuestos();
    public BigDecimal getPrecio();
    public BigDecimal getPrecioBase();
}
