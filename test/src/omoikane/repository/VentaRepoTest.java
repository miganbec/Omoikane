package omoikane.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import omoikane.entities.LegacyVenta;
import omoikane.entities.LegacyVentaDetalle;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 06/11/12
 * Time: 13:43
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-test.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("sampleData.xml")
public class VentaRepoTest {
    @Autowired
    VentaRepo ventaRepo;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    @Transactional
    public void testCrearVentas() {
        LegacyVenta venta = new LegacyVenta();
        venta.setCambio(0f);
        venta.setCentecimosredondeados(0f);
        venta.setCompletada(1);
        venta.setDescuento(0d);
        venta.setEfectivo(0d);
        venta.setEliminar(null);
        venta.setFacturada(0);
        venta.setFolio(0);
        venta.setIdCliente(1);
        venta.setIdUsuario(1);
        venta.setImpuestos(0d);
        venta.setTotal(0d);

        ventaRepo.saveAndFlush(venta);

        LegacyVentaDetalle lvd = new LegacyVentaDetalle();
        lvd.setIdVenta(venta.getId());
        lvd.setIdAlmacen(1);
        lvd.setIdArticulo(500);
        lvd.setIdLinea(1);
        lvd.setIdCaja(1);
        lvd.setCantidad(1d);
        lvd.setDescuento(0d);
        lvd.setImpuestos(0d);
        lvd.setPrecio(100d);
        lvd.setSubtotal(100d);
        lvd.setTipoSalida("");
        lvd.setTotal(100d);

        entityManager.persist( lvd );
        entityManager.flush();

        System.out.println("Venta ID: "+venta.getId());
        System.out.println("Venta detalle ID: "+lvd.getIdRenglon());
        Assert.assertTrue( ventaRepo.count() == 51);
        Assert.assertTrue( entityManager.find(LegacyVentaDetalle.class, lvd.getIdRenglon()).getTotal() == 100d );
    }

    @Test
    public void findAllVentas() {
        List<LegacyVenta> ventas = ventaRepo.readAll();
        Assert.assertTrue(ventaRepo.count() == 50);
    }
}
