package omoikane.caja;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import javafx.application.Application;
import omoikane.principal.Principal;
import omoikane.sistema.Usuarios;
import org.junit.Test;
import org.junit.runner.RunWith;
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

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 01/11/12
 * Time: 20:48
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-test.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("../repository/sampleData.xml")
public class CajaManagerTest {

    @Test
    public void cajaTest() {
        omoikane.principal.Principal.setConfig( new omoikane.sistema.Config() );
        omoikane.principal.Principal.applicationContext = new ClassPathXmlApplicationContext("applicationContext-test.xml");

        //Principal.IDCaja = 1;
        Principal.IDAlmacen = 1;
        Usuarios.setIDUsuarioActivo( 1 );

        Application.launch(CajaManager.class, (java.lang.String[]) null);

    }
    @Test
    public void cajaSwingTest() throws InvocationTargetException, InterruptedException {

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                omoikane.principal.Principal.setConfig(new omoikane.sistema.Config());
                omoikane.principal.Principal.applicationContext = new ClassPathXmlApplicationContext("applicationContext-test.xml");

                CajaManager manager = new CajaManager();
                JInternalFrame frame = manager.startJFXCaja();
                JFrame jFrame = new JFrame("Caja");
                jFrame.setContentPane(frame);
                jFrame.setVisible(true);

            }
        });

        while(true) {
            Thread.sleep(10000);
        }
    }
}
