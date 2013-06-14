package omoikane.caja.data;

import omoikane.entities.Linea;
import omoikane.producto.Articulo;
import omoikane.producto.PrecioOmoikaneLogic;
import omoikane.producto.Producto;
import omoikane.repository.ProductoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.synyx.hades.domain.PageRequest;
import org.synyx.hades.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List ;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;


/**
 * Ésta clase mapea campos entre la vieja clase Articulo y la nueva Producto, por defecto no mapea las existencias.
 * Ésta clase debe desaparecer el PRO de utilizar directamente la nueva entidad cuandos sea posible.
 * User: octavioruizcastillo
 * Date: 19/09/12
 * Time: 11:15
 */
public class ProductosNadesicoAdapter implements IProductosDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ProductoRepo productoRepo;

    @Override
    /**
     * Busca productos por código primario, si ninguno es encontrado entonces busca por código secundario.
     * Convierte los productos de la entidad Articulo (antigua por comptabilidad) a la entidad Producto (entidad
     * moderna con correcciones)
     */
    public List<Producto> findByCodigo(String codigo) {

        ArrayList<Articulo> articulos = (ArrayList<Articulo>) productoRepo.findByCodigo( codigo );
        if(articulos.size() == 0)
            articulos = (ArrayList<Articulo>) productoRepo.findByCodigoAlterno( codigo );
        ArrayList<Producto> productos = new ArrayList<>();

        for( Articulo articulo : articulos ) {
            final Producto producto = new Producto();
            articuloToProducto(articulo, producto);
            productos.add(producto);
        }

        return productos;

    }

    @Override
    public Producto findById(Long id) {

        Articulo articulo = (Articulo) productoRepo.readByPrimaryKey(id);
        Producto producto = new Producto();

        articuloToProducto(articulo, producto);

        return producto;

    }

    @Override
    public List<Producto> findByDescripcionLike(String descripcion, Pageable pagina) {
        Pageable pageable = new PageRequest(pagina.getPageNumber(), pagina.getPageSize());
        ArrayList<Articulo> articulos = (ArrayList<Articulo>) productoRepo.findByDescripcionLike( descripcion, pageable );
        ArrayList<Producto> productos = new ArrayList<>();
        Producto p;


        for( Articulo a : articulos ) {
            p = new Producto();

            articuloToProducto(a, p);
            productos.add(p);
        }
        return productos;
    }

    private void articuloToProducto(Articulo articulo, Producto producto) {
        producto.setId         ( articulo.getIdArticulo().intValue() );
        producto.setCodigo     ( articulo.getCodigo()                );
        producto.setDescripcion( articulo.getDescripcion()           );
        producto.setUnidad     ( articulo.getUnidad()                );
        producto.setImpuestos  ( new BigDecimal(articulo.getPorcentajeImpuestos()) );
        producto.setCosto      ( new BigDecimal(articulo.getBaseParaPrecio().getCosto()) );
        producto.setDescuento  ( new BigDecimal(articulo.getBaseParaPrecio().getPorcentajeDescuentoProducto()) );
        producto.setUtilidad   ( new BigDecimal(articulo.getBaseParaPrecio().getPorcentajeUtilidad()) );
        producto.setExistencia ( new BigDecimal( -1 ) );
        producto.setPrecio( new PrecioOmoikaneLogic(articulo.getBaseParaPrecio()) );

        Linea linea = new Linea();
        linea.setId( articulo.getIdLinea() );
        producto.setLineaByLineaId( linea );
    }
}
