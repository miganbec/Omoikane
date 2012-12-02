package omoikane.caja.business;

import groovy.util.GroovyTestCase;
import javafx.beans.property.SimpleStringProperty;
import omoikane.caja.data.IProductosDAO;
import omoikane.caja.presentation.CajaModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import omoikane.sistema.Config
import omoikane.caja.presentation.ProductoModel
import org.apache.log4j.Logger
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.support.DirtiesContextTestExecutionListener
import org.springframework.test.context.transaction.TransactionalTestExecutionListener
import com.github.springtestdbunit.DbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DatabaseSetup
import javax.persistence.PersistenceContext
import javax.persistence.EntityManager
import omoikane.entities.LegacyVenta
import omoikane.repository.VentaRepo
import org.springframework.test.context.TestExecutionListeners;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 18/10/12
 * Time: 02:14
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( listeners = [ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class ])
@DatabaseSetup("../repository/sampleData20121117.xml")
public class CajaLogicImplTest extends GroovyTestCase {

    @Autowired
    IProductosDAO productosDAO;

    @Autowired
    ICajaLogic cajaLogic;

    @PersistenceContext
    EntityManager entiyManager;

    @Autowired
    VentaRepo ventaRepo;

    CajaModel cajaModel;

    @Before
    public void setUp() {
        new Config();
        cajaModel = new CajaModel();
    }

    @After
    public void tearDown() {
    }
    @Test
    public void buscarTest() {
        new Config();
        cajaModel.setCaptura(new SimpleStringProperty("gerber"));
        cajaLogic.buscar( cajaModel );

        for (ProductoModel productoModel : cajaModel.getProductos()) {
            System.out.println("Producto: " + productoModel.conceptoString);
            Logger.rootLogger.trace("Producto: " + productoModel.conceptoString);
        }
    }

    @Test
    public void imprimirVentaTest() {
        LegacyVenta venta = ventaRepo.readByPrimaryKey( 7270222 );
        cajaLogic.imprimirVenta ( venta );
    }


}
