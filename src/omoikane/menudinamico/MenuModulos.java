/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.menudinamico;

import com.phesus.omoikaneapi.Menus.MenuBean;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;



/**
 *
 * @author Octavio
 */
public class MenuModulos {
    PanelMenu panel;
    JFrame frame;
    MenuSPI menuSPI;

    public MenuModulos() {
        menuSPI = new MenuSPI();
        panel = new PanelMenu();
        panel.getPnlBotones().setLayout(new MigLayout("wrap 1"));
        
        JFrame frame = new JFrame();
        frame.getContentPane().add(panel);
        frame.setVisible(true);
        JButton boton;
        for (final MenuBean menuBean : menuSPI.getModulos()) {
            boton = addItem(menuBean.getDisplayName());
            boton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    menuBean.launch();
                }
            }
            );
        }

    }
    public JButton addItem(String nombre) {
        JButton item = new JButton(nombre);
        panel.getPnlBotones().add(item);
        return item;
    }
}
