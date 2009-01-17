/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.principal

import omoikane.*;
import omoikane.sistema.*
import omoikane.sistema.Usuarios as SisUsuarios
import javax.swing.JFrame;

/**
 *asdasdqw
 * @author Octaviosssssssssssssssss
 */
class MenuPrincipal {
    def menuPrincipal = new omoikane.formularios.MenuPrincipal()

    void iniciar()
    {
  if(!SisUsuarios.cerrojo(SisUsuarios.CAJERO+1)) {
  //Si es cajero no se lanza el menú, se va directo a caja
       Caja.lanzar()
   } else {
            Herramientas.centrarVentana menuPrincipal
            menuPrincipal.setVisible(true)
            Principal.escritorio.getPanelEscritorio().add(menuPrincipal,javax.swing.JLayeredPane.PALETTE_LAYER)
            menuPrincipal.toFront()
            menuPrincipal.requestFocusInWindow()
            Herramientas.iconificable(menuPrincipal)

            try {
                menuPrincipal.setSelected(true)
            } catch(Exception e)
            {
                sistema.Dialogos.lanzarDialogoError(null, "Error al iniciar menu principal", Herramientas.getStackTraceString(e))
            }
        }
    }
}

