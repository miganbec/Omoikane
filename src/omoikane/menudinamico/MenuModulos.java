/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.menudinamico;

import com.phesus.omoikaneapi.Menus.MenuBean;

import java.awt.event.*;
import javax.swing.*;

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
        
        final JFrame frame = new JFrame();
        frame.setBounds(20,20,400,300);
        panel.getBtnCerrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                frame.dispose();
            }
        });
        Action cerrar = new AbstractAction() { public void actionPerformed(ActionEvent e) { panel.getBtnCerrar().doClick(); } };
        panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cerrar");
        panel.getActionMap().put("cerrar"                 , cerrar  );

        frame.getContentPane().add(panel);
        frame.setVisible(true);
        frame.setSize(401,301);
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
