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
 * @author ssssss
 */
class MenuPrincipal {
    def menuPrincipal = new omoikane.formularios.MenuPrincipal()

    void iniciar()
    {
//  if(!SisUsuarios.cerrojo(SisUsuarios.CAJERO+1)) {
  //Si es cajero no se lanza el menú, se va directo a caja
//       Caja.lanzar()
 //  } else {
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

            menuPrincipal.btnVender.keyReleased = {
                if(it.keyCode==it.VK_UP)   { menuPrincipal.btnUsuarios.requestFocusInWindow() }
                if(it.keyCode==it.VK_DOWN) { menuPrincipal.btnArticulos.requestFocusInWindow() }
                if(it.keyCode == it.VK_ENTER){menuPrincipal.btnVender.doClick() }
            }
            menuPrincipal.btnArticulos.keyReleased = {
                if(it.keyCode==it.VK_UP)   { menuPrincipal.btnVender.requestFocusInWindow() }
                if(it.keyCode==it.VK_DOWN) { menuPrincipal.btnAlmacenes.requestFocusInWindow() }
                if(it.keyCode == it.VK_ENTER){menuPrincipal.btnArticulos.doClick() }
            }
            menuPrincipal.btnAlmacenes.keyReleased = {
                if(it.keyCode==it.VK_UP)   { menuPrincipal.btnArticulos.requestFocusInWindow() }
                if(it.keyCode==it.VK_DOWN) { menuPrincipal.btnUsuarios.requestFocusInWindow() }
                if(it.keyCode == it.VK_ENTER){menuPrincipal.btnAlmacenes.doClick() }
            }
            menuPrincipal.btnUsuarios.keyReleased = {
                if(it.keyCode==it.VK_UP)   { menuPrincipal.btnAlmacenes.requestFocusInWindow() }
                if(it.keyCode==it.VK_DOWN) { menuPrincipal.btnVender.requestFocusInWindow() }
                if(it.keyCode == it.VK_ENTER){menuPrincipal.btnUsuarios.doClick() }
            }
            menuPrincipal.btnDetallesVentas.keyReleased = {
                if(it.keyCode==it.VK_UP)   { menuPrincipal.btnPreferencias.requestFocusInWindow() }
                if(it.keyCode==it.VK_DOWN) { menuPrincipal.btnLineas.requestFocusInWindow() }
                if(it.keyCode == it.VK_ENTER){menuPrincipal.btnDetallesVentas.doClick() }
            }
            menuPrincipal.btnLineas.keyReleased = {
                if(it.keyCode==it.VK_UP)   { menuPrincipal.btnDetallesVentas.requestFocusInWindow() }
                if(it.keyCode==it.VK_DOWN) { menuPrincipal.btnMovAlmacen.requestFocusInWindow() }
                if(it.keyCode == it.VK_ENTER){menuPrincipal.btnLineas.doClick() }
            }
            menuPrincipal.btnMovAlmacen.keyReleased = {
                if(it.keyCode==it.VK_UP)   { menuPrincipal.btnLineas.requestFocusInWindow() }
                if(it.keyCode==it.VK_DOWN) { menuPrincipal.btnPreferencias.requestFocusInWindow() }
                if(it.keyCode == it.VK_ENTER){menuPrincipal.btnMovAlmacen.doClick() }
            }
            menuPrincipal.btnPreferencias.keyReleased = {
                if(it.keyCode==it.VK_UP)   { menuPrincipal.btnMovAlmacen.requestFocusInWindow() }
                if(it.keyCode==it.VK_DOWN) { menuPrincipal.btnDetallesVentas.requestFocusInWindow() }
                if(it.keyCode == it.VK_ENTER){menuPrincipal.btnPreferencias.doClick() }
            }
            menuPrincipal.btnCerrar.keyReleased = {
                if(it.keyCode==it.VK_UP)   { menuPrincipal.btnClientes.requestFocusInWindow() }
                if(it.keyCode==it.VK_DOWN) { menuPrincipal.btnCortes.requestFocusInWindow() }
                if(it.keyCode == it.VK_ENTER){menuPrincipal.btnCerrar.doClick() }
            }
            menuPrincipal.btnCortes.keyReleased = {
                if(it.keyCode==it.VK_UP)   { menuPrincipal.btnCerrar.requestFocusInWindow() }
                if(it.keyCode==it.VK_DOWN) { menuPrincipal.btnGrupos.requestFocusInWindow() }
                if(it.keyCode == it.VK_ENTER){menuPrincipal.btnCortes.doClick() }
            }
            menuPrincipal.btnGrupos.keyReleased = {
                if(it.keyCode==it.VK_UP)   { menuPrincipal.btnCortes.requestFocusInWindow() }
                if(it.keyCode==it.VK_DOWN) { menuPrincipal.btnCajas.requestFocusInWindow() }
                if(it.keyCode == it.VK_ENTER){menuPrincipal.btnGrupos.doClick() }
            }
            menuPrincipal.btnCajas.keyReleased = {
                if(it.keyCode==it.VK_UP)   { menuPrincipal.btnGrupos.requestFocusInWindow() }
                if(it.keyCode==it.VK_DOWN) { menuPrincipal.btnClientes.requestFocusInWindow() }
                if(it.keyCode == it.VK_ENTER){menuPrincipal.btnCajas.doClick() }
            }
            menuPrincipal.btnClientes.keyReleased = {
                if(it.keyCode==it.VK_UP)   { menuPrincipal.btnCajas.requestFocusInWindow() }
                if(it.keyCode==it.VK_DOWN) { menuPrincipal.btnCerrar.requestFocusInWindow() }
                if(it.keyCode == it.VK_ENTER){menuPrincipal.btnClientes.doClick() }
            }
        }
  //  }
}

