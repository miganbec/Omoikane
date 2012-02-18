/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class PruebaSpringTest //extends AbstractTransactionalJUnit4SpringContextTests
{

	@Autowired
    PruebaSpring pr;

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }
    @Test
    public void springTest() {
        pr.pr();
    }

}