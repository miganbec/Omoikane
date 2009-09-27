/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.principal

import omoikane.principal.*
import omoikane.sistema.*
import groovy.swing.*;
import javax.swing.*;
import java.awt.*;
import omoikane.sistema.*;
import javax.swing.event.*;
import java.awt.event.*;

/**
 *
 * @author RNB
 */
class Calendario {
    
    static def escritorio = omoikane.principal.Principal.escritorio
    static def cal

    static def lanzarCalendario(invocador) {
            cal = (new omoikane.formularios.Calendario(invocador))
            cal.setVisible(true);
            escritorio.getPanelEscritorio().add(cal)
            escritorio.getFrameEscritorio().setCalendarioAbierto(true)

            Herramientas.In2ActionX(cal.jxmvCalendario, KeyEvent.VK_ESCAPE, "cierre"   ) {
                cal.btnCerrar.doClick()
            }
            cal.internalFrameClosed = {
                escritorio.getFrameEscritorio().setCalendarioAbierto(false);
                invocador.requestFocus();
                invocador.requestFocusInWindow();
            }

            cal.toFront()
            cal.setSelected(true);
            cal.requestFocus()
            Herramientas.iconificable(cal)
            cal.jxmvCalendario.requestFocus()
            return cal
    }
}

