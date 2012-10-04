package omoikane.producto;

import javax.persistence.Transient;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 03/10/12
 * Time: 10:09
 * To change this template use File | Settings | File Templates.
 */
public interface IProductoApreciado {
    @Transient
    public IPrecio getPrecio();

    @Transient
    public void setPrecio(IPrecio precio);

    @Transient
    public BaseParaPrecio getBaseParaPrecio();
}
