package omoikane.configuracion;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import javafx.application.Application;
import omoikane.caja.CajaManager;
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
import java.lang.reflect.InvocationTargetException;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 20/04/13
 * Time: 11:33 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-test.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
public class ConfiguratorAppManagerTest {

    @Test
    public void cofiguratorSwingTest() throws InvocationTargetException, InterruptedException {

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                omoikane.principal.Principal.setConfig(new omoikane.sistema.Config());
                omoikane.principal.Principal.applicationContext = new ClassPathXmlApplicationContext("applicationContext-test.xml");

                ConfiguratorAppManager manager = new ConfiguratorAppManager();
                JInternalFrame frame = manager.startJFXConfigurator();
                JFrame jFrame = new JFrame("Configurator");
                jFrame.setContentPane(frame);
                jFrame.setVisible(true);

            }
        });

        while(true) {
            Thread.sleep(10000);
        }
    }
}
