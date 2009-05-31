/*
 * WndLeerHuella.java
 *
 * Created on 20 de septiembre de 2007, 11:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package omoikane.formularios;

import omoikane.sistema.*;

/**
 *
 * @author Octavio
 */
public class WndLeerHuella extends JInternalDialog2 {
    
    Huellas2 pnlHuella;
    /** Creates a new instance of WndLeerHuella */
    public WndLeerHuella(Object parent) {
        super(parent, "Leer huella");
    }
    
    public byte[] getHuella()
    {
        pnlHuella = new Huellas2(this);
        super.setContentPane(pnlHuella);
        super.setActivo(true);
        return pnlHuella.byteTemplate;
        //return null;
    }
    
}
