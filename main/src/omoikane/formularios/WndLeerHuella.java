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
import omoikane.sistema.huellas.ContextoFPSDK;
import omoikane.sistema.huellas.HuellasOneTouchSDK;
import omoikane.sistema.huellas.MiniLeerHuella;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;

/**
 *
 * @author Octavio
 */
public class WndLeerHuella extends JInternalDialog2 {
    
    MiniLeerHuella pnlHuella;
    /** Creates a new instance of WndLeerHuella */
    public WndLeerHuella(Object parent) {
        super(parent, "Leer huella");

        this.getActionMap().put("apagado", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Apagado.apagar();
            }
        });
        this.getInputMap(this.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("F10"), "apagado");
    }
    
    public byte[] getHuella() throws Exception {

        pnlHuella = ContextoFPSDK.instanciar(this);
        super.setContentPane(pnlHuella);
        super.setActivo(true);
        return pnlHuella.byteTemplate;
        //return null;
    }
    
}
