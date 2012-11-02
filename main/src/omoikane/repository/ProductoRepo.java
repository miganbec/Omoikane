package omoikane.repository;

import omoikane.producto.Articulo;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Query;
import org.synyx.hades.domain.Pageable;

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
    @Query("FROM Articulo a JOIN FETCH a.baseParaPrecio bp WHERE a.descripcion like ?1")
    List<Articulo> findByDescripcionLike(String descripcion, Pageable pageable);
}
