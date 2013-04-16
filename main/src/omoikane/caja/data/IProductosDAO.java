package omoikane.caja.data;

import omoikane.producto.Producto;
import org.synyx.hades.domain.Pageable;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 19/09/12
 * Time: 11:19
 * To change this template use File | Settings | File Templates.
 */
public interface IProductosDAO {

    public List<Producto> findByCodigo(String codigo);
    public List<Producto> findByDescripcionLike(String descripcion, Pageable pagina);

    Producto findById(Long id);
}
