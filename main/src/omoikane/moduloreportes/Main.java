/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.moduloreportes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

/*
 * @author Phesus-Lab
 */
public class Main {

    /*
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
       
        try
        {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch (Exception e)
        {
        e.printStackTrace();
        }

        final VentanaPrincipal  vp = new VentanaPrincipal();
        vp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panelReportes pvl= new panelReportes();
        vp.getContentPane().add(pvl);
        pvl.getSalir().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                vp.dispose();
            }
        });
        vp.setVisible(true);
    }
    public static void lanzarVentanaEstandar() {
        VentanaPrincipal  vp = new VentanaPrincipal();
        panelReportes pvl= new panelReportes();
        vp.getContentPane().add(pvl);
        vp.setVisible(true);
    }
}
