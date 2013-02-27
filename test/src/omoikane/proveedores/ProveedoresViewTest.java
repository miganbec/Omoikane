package omoikane.proveedores;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import javafx.application.Application;
import omoikane.principal.Principal;
import omoikane.producto.DummyJFXApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 25/02/13
 * Time: 10:26 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-test.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("../repository/sampleDataLight.xml")
public class ProveedoresViewTest {
    final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ProveedoresViewTest.class);

     @Test
    public void proveedoresViewTest() {
         omoikane.principal.Principal.applicationContext = new ClassPathXmlApplicationContext("applicationContext-test.xml");
         HashMap testProperties = (HashMap) Principal.applicationContext.getBean( "testProperties" );
         testProperties.put("DummyJFXApp.viewBeanToTest", "proveedoresView");
         Application.launch(DummyJFXApp.class);
    }
}
