package omoikane.repository;

import omoikane.entities.Producto;
import omoikane.entities.Usuario;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Query;
import org.synyx.hades.dao.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 27/07/11
 * Time: 00:58
 * To change this template use File | Settings | File Templates.
 */
public interface ProductoRepo extends GenericDao<Producto, Long>
{
    @Query("select p from Producto p, CodigoProducto cp where p = cp.producto and ( c.codigo = ?1 or cp.codigo = ?1)")
    public List<Producto> findByCodigo(String codigo);
}
