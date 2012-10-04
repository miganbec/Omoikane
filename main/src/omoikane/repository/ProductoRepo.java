package omoikane.repository;

import omoikane.producto.Articulo;
import org.synyx.hades.dao.GenericDao;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 27/07/11
 * Time: 00:58
 * To change this template use File | Settings | File Templates.
 */
public interface ProductoRepo extends GenericDao<Articulo, Long>
{
    List<Articulo> findByCodigo(String codigo);
}
