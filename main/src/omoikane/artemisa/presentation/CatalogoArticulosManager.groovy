package omoikane.artemisa.presentation;

import omoikane.principal.Articulos;
import omoikane.principal.Escritorio;

import javax.swing.*
import omoikane.sistema.Permisos;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 31/07/13
 * Time: 11:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class CatalogoArticulosManager {
    public void show() {

        if(omoikane.principal.Principal.escritorio == null) {
            omoikane.principal.Principal.escritorio = new Escritorio();
            omoikane.principal.Principal.escritorio.frameEscritorio.setUndecorated(false)
            omoikane.principal.Principal.escritorio.frameEscritorio.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            omoikane.principal.Principal.escritorio.iniciar()
            omoikane.principal.Principal.iniciarSesion()
            omoikane.principal.Principal.escritorio.setNombreUsuario("Artemisa")
            omoikane.principal.Principal.escritorio.frameEscritorio.setTitle("Artemisa")
        } else {
            omoikane.principal.Principal.escritorio.frameEscritorio.setVisible(true)
        }

        Articulos.lanzarCatalogo();
    }
}
