/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moduloreportes;

/*
 * @author Phesus-Lab
 */
public class Main {

    /*
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        VentanaPrincipal  vp = new VentanaPrincipal();
        panelReportes pvl= new panelReportes();
        vp.getContentPane().add(pvl);
        vp.setVisible(true);
    }

}
