/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.moduloreportes;

import com.phesus.omoikaneapi.Menus.MenuBean;
import omoikane.sistema.Permisos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Octavio
 */
public class MenuOmoikane extends MenuBean {
    public MenuOmoikane() {
        super.setDisplayName("Reportes");
        super.setID("MOD-Reportes");
    }
    public void launch() {
        if(omoikane.sistema.Usuarios.cerrojo(Permisos.PMA_REPORTES)) {
            final VentanaPrincipal  vp = new VentanaPrincipal();
            panelReportes pvl= new panelReportes();
            vp.getContentPane().add(pvl);
            vp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            vp.setTitle("Reportes avanzados");
            vp.setVisible(true);
            pvl.getSalir().addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    vp.dispose();
                }
            });
        }
    }

}

