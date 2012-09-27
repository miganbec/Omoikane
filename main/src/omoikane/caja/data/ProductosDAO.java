package omoikane.caja.data;

import omoikane.entities.Producto;
import org.synyx.hades.dao.Query;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 19/09/12
 * Time: 11:19
 * To change this template use File | Settings | File Templates.
 */
public interface ProductosDAO {

    public List<Producto> findByCodigo(String codigo);
}
