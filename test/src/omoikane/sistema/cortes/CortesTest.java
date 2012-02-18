package omoikane.sistema.cortes;

import omoikane.entities.Caja;
import omoikane.entities.CorteSucursal;
import omoikane.principal.Sucursales;
import omoikane.repository.CajaRepo;
import omoikane.repository.CorteSucursalRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Proyecto Omoikane: SmartPOS 2.0
 * User: octavioruizcastillo
 * Date: 05/11/11
 * Time: 17:43
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class CortesTest {
    @Autowired
    EstrategiaEstandar cortes;

    @Autowired
    CajaRepo cajaRepo;

    @Autowired
    CorteSucursalRepo corteSucursalRepo;

    @Autowired
    Sucursales sucursales;

    @Test
    public void opsCaja() {
        Logger logger = LoggerFactory.getLogger(CortesTest.class);

        sucursales.abrirSucursal();
        CorteSucursal corteSucursal = corteSucursalRepo.getLastCorteSucursal();
        logger.info("Corte sucursal creado");

        Caja caja = new Caja();
        caja.setDescripcion("Caja 1");
        cajaRepo.save(caja);
        logger.info("Caja creada");

        cortes.abrirCaja(caja.getId());
        logger.info("Caja abierta");

        cortes.cerrarCaja(caja.getId());
        logger.info("Caja cerrada (sin sumar ventas)");

        sucursales.cerrar();
        logger.info("Cerrar sucursal (sin sumar ventas)");

    }
}
