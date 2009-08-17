
 /* Author Phesus        //////////////////////////////
 *  ORC,ACR             /////////////
 *                     /////////////
 *                    /////////////
 *                   /////////////
 * //////////////////////////////                   */

package omoikane.principal

import omoikane.*;
import omoikane.sistema.*
import omoikane.sistema.Usuarios as SisUsuarios
import javax.swing.JFrame;

class MenuPrincipal {
    def menuPrincipal = new omoikane.formularios.MenuPrincipal()

    void iniciar()
    { if(!SisUsuarios.cerrojo(SisUsuarios.SUPERVISOR)) {
      //Si es cajero no se lanza el menú, ingresa directo a caja
       Caja.lanzar()
    } else {
            Herramientas.panelCatalogo(menuPrincipal);
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
            }
            menuPrincipal.btnArticulos.keyReleased = {
                if(it.keyCode==it.VK_UP)   { menuPrincipal.btnVender.requestFocusInWindow() }
                if(it.keyCode==it.VK_DOWN) { menuPrincipal.btnAlmacenes.requestFocusInWindow() }
            }
            menuPrincipal.btnAlmacenes.keyReleased = {
                if(it.keyCode==it.VK_UP)   { menuPrincipal.btnArticulos.requestFocusInWindow() }
                if(it.keyCode==it.VK_DOWN) { menuPrincipal.btnUsuarios.requestFocusInWindow() }
            }
            menuPrincipal.btnUsuarios.keyReleased = {
                if(it.keyCode==it.VK_UP)   { menuPrincipal.btnAlmacenes.requestFocusInWindow() }
                if(it.keyCode==it.VK_DOWN) { menuPrincipal.btnVender.requestFocusInWindow() }
            }
            menuPrincipal.btnDetallesVentas.keyReleased = {
                if(it.keyCode==it.VK_UP)   { menuPrincipal.btnPreferencias.requestFocusInWindow() }
                if(it.keyCode==it.VK_DOWN) { menuPrincipal.btnLineas.requestFocusInWindow() }
            }
            menuPrincipal.btnLineas.keyReleased = {
                if(it.keyCode==it.VK_UP)   { menuPrincipal.btnDetallesVentas.requestFocusInWindow() }
                if(it.keyCode==it.VK_DOWN) { menuPrincipal.btnMovAlmacen.requestFocusInWindow() }
            }
            menuPrincipal.btnMovAlmacen.keyReleased = {
                if(it.keyCode==it.VK_UP)   { menuPrincipal.btnLineas.requestFocusInWindow() }
                if(it.keyCode==it.VK_DOWN) { menuPrincipal.btnPreferencias.requestFocusInWindow() }
            }
            menuPrincipal.btnPreferencias.keyReleased = {
                if(it.keyCode==it.VK_UP)   { menuPrincipal.btnMovAlmacen.requestFocusInWindow() }
                if(it.keyCode==it.VK_DOWN) { menuPrincipal.btnDetallesVentas.requestFocusInWindow() }
            }
            menuPrincipal.btnCerrar.keyReleased = {
                if(it.keyCode==it.VK_UP)   { menuPrincipal.btnClientes.requestFocusInWindow() }
                if(it.keyCode==it.VK_DOWN) { menuPrincipal.btnCortes.requestFocusInWindow() }
            }
            menuPrincipal.btnCortes.keyReleased = {
                if(it.keyCode==it.VK_UP)   { menuPrincipal.btnCerrar.requestFocusInWindow() }
                if(it.keyCode==it.VK_DOWN) { menuPrincipal.btnGrupos.requestFocusInWindow() }
            }
            menuPrincipal.btnGrupos.keyReleased = {
                if(it.keyCode==it.VK_UP)   { menuPrincipal.btnCortes.requestFocusInWindow() }
                if(it.keyCode==it.VK_DOWN) { menuPrincipal.btnCajas.requestFocusInWindow() }
            }
            menuPrincipal.btnCajas.keyReleased = {
                if(it.keyCode==it.VK_UP)   { menuPrincipal.btnGrupos.requestFocusInWindow() }
                if(it.keyCode==it.VK_DOWN) { menuPrincipal.btnClientes.requestFocusInWindow() }
            }
            menuPrincipal.btnClientes.keyReleased = {
                if(it.keyCode==it.VK_UP)   { menuPrincipal.btnCajas.requestFocusInWindow() }
                if(it.keyCode==it.VK_DOWN) { menuPrincipal.btnCerrar.requestFocusInWindow() }                
            }

            menuPrincipal.btnCerrar.actionPerformed = {
                menuPrincipal.dispose()
                Principal.cerrarSesion()
            }
        }
    }
}

