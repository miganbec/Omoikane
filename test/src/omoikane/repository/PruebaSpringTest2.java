/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.repository;

import omoikane.entities.CorteSucursal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
@TransactionConfiguration(defaultRollback=false)
public class PruebaSpringTest2 extends AbstractTransactionalJUnit4SpringContextTests
{


	@Autowired
    CorteSucursalRepo pr;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {

    }
    @Test
    public void springTest() {
        CorteSucursal corte = new CorteSucursal();
        Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
        corte.setCreacion(timestamp);
        corte.setDepositos(new BigDecimal(134d));
        corte.setDesde(timestamp);
        corte.setRetiros(new BigDecimal(9237d));
        corte.setHasta(timestamp);

        pr.save(corte);
    }

}