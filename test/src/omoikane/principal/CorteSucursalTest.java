package omoikane.principal;

import omoikane.entities.CorteSucursal;
import org.apache.log4j.Level;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;
/**
 * Proyecto Omoikane: SmartPOS 2.0
 * User: octavioruizcastillo
 * Date: 02/11/11
 * Time: 17:17
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class CorteSucursalTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    Sucursales sucursales;

    @Test
    public void opsSucursal() {
        Logger logger = LoggerFactory.getLogger(CorteSucursal.class);

        assertFalse (sucursales.isAbierta());
        logger.warn("Caja efectivamente cerrada");

        assertTrue( sucursales.abrirSucursal() == Sucursales.ESTADO.HABILITADA );
        logger.warn("Abrimos la caja con éxito");

        assertTrue( sucursales.isAbierta() );
        CorteSucursal corteSucursal = sucursales.getSesionActual();
        logger.warn("Comprobado la caja está abierta. Fecha de creación: "+corteSucursal.getCreacion().toString());

        sucursales.cerrar();
        logger.warn("Se cierra la caja con éxito");

        assertFalse( sucursales.isAbierta() );
        logger.warn("Comprobado la caja está cerrada");

        //Segunda apertura de sucursal

        assertTrue( sucursales.abrirSucursal() == Sucursales.ESTADO.HABILITADA );
        logger.warn("Abrimos la caja con éxito");

        sucursales.cerrar();
        logger.warn("Se cierra la caja con éxito");
    }
}
