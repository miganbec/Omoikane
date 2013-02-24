package omoikane.producto;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import omoikane.caja.CajaManager;
import omoikane.caja.business.ICajaLogic;
import omoikane.caja.presentation.CajaController;
import omoikane.caja.presentation.CajaModel;
import omoikane.principal.Principal;
import omoikane.sistema.Usuarios;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 16/02/13
 * Time: 11:40
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-test.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("../repository/sampleDataLight.xml")
public class PaqueteTest {

    final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PaqueteTest.class);

    @Test
    public void paqueteViewTest() {
        omoikane.principal.Principal.applicationContext = new ClassPathXmlApplicationContext("applicationContext-test.xml");
        HashMap testProperties = (HashMap) Principal.applicationContext.getBean( "testProperties" );
        testProperties.put("DummyJFXApp.viewBeanToTest", "paqueteView");
        Application.launch(DummyJFXApp.class);
    }

}
