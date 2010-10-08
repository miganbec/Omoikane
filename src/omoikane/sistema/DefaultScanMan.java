/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.sistema;

import java.text.*;
import java.awt.event.*;
/**
 *
 * @author Octavio
 */
public class DefaultScanMan extends ScanMan {
    public void handleScan(String scanned) {
        System.out.println ("iniciando handler antes de each");
        CharacterIterator it = new StringCharacterIterator(scanned);
        for (char ch=it.first(); ch != CharacterIterator.DONE; ch=it.next()) {
            if(((int)ch)==13) { break; }
            System.out.println ("caracter:"+ch);
            super.robot.keyPress((int) ch);
        }
        
        System.out.println("después de each");

        try {
            System.out.println ("supuesto enter");
            super.robot.keyPress(KeyEvent.VK_ENTER);
            super.robot.keyRelease(KeyEvent.VK_ENTER);
            System.out.println ("fin supuesto enter");
        } catch(Exception exc) { Dialogos.error("Error capturar desde escáner de códigos de barras", exc); }
    }
}
