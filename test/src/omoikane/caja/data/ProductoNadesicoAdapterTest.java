package omoikane.caja.data;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import omoikane.caja.data.ProductosNadesicoAdapter;
import omoikane.producto.Articulo;
import omoikane.producto.Producto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.synyx.hades.domain.PageRequest;
import org.synyx.hades.domain.Pageable;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 30/10/12
 * Time: 18:16
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-test.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("../../repository/sampleData.xml")
public class ProductoNadesicoAdapterTest {
    @Autowired
    ProductosNadesicoAdapter productoRepo;

    @Test
    public void testFindByDescripcion() {
        Pageable pagina = new PageRequest(0, 10);
        ArrayList<Producto> productos = (ArrayList<Producto>) productoRepo.findByDescripcionLike("%" + "CONCEPCION" + "%", pagina);
        for(Producto p : productos) {
            System.out.println("-" + p.getDescripcion());
        }
    }

    @Test
    public void testFindByCodigo() {
        ArrayList<Producto> productos = (ArrayList<Producto>) productoRepo.findByCodigo("7501059238305");
        for(Producto a : productos) {
            System.out.println("-" + a.getDescripcion());
        }
    }
}

