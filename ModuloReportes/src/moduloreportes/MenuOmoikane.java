/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moduloreportes;

import com.phesus.omoikaneapi.Menus.MenuBean;
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
        VentanaPrincipal  vp = new VentanaPrincipal();
        panelReportes pvl= new panelReportes();
        vp.getContentPane().add(pvl);
        vp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        vp.setVisible(true);
    }

}

