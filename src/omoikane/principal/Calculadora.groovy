/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.principal

import omoikane.principal.*
import omoikane.sistema.*
import groovy.swing.*
import javax.swing.*
import java.awt.*
import omoikane.sistema.*
import javax.swing.event.*
import java.awt.event.*

/**
 *
 * @author RNB
 */
class Calculadora {
    static def escritorio = omoikane.principal.Principal.escritorio
    static def cal

    static def lanzarCalculadora( clipboard, invocador ) {
            cal = (new omoikane.formularios.Calculadora(clipboard, invocador))
            cal.setVisible(true);
            escritorio.getPanelEscritorio().add(cal)
            // Herramientas.In2ActionX(cal, KeyEvent.VK_ESCAPE, "cerrar"   ) { cal.btnCerrar.doClick()   }
            Herramientas.In2ActionX(cal.jcalCalculadora, KeyEvent.VK_ESCAPE, "cierre"   ) { cal.btnCerrar.doClick()   }
            cal.toFront()
            cal.setSelected(true)
            cal.requestFocus()
            Herramientas.iconificable(cal)
            // cal.jcalCalculadora.requestFocus()
            return cal
    }
}

