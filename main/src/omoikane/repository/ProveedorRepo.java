package omoikane.repository;


import omoikane.proveedores.Proveedor;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Query;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 25/02/13
 * Time: 09:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProveedorRepo extends GenericDao<Proveedor, Long> {
    @Query("FROM Proveedor p WHERE p.activo = 1")
    List<Proveedor> findAllActive();

    @Query("FROM Proveedor p WHERE (p.activo = true OR p.activo = ?1) AND nombre like ?2")
    List<Proveedor> findByActivoAndNombreLike(Boolean activo, String like);
}
