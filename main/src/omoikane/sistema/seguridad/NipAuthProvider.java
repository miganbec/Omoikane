package omoikane.sistema.seguridad;

import jfxtras.labs.dialogs.MonologFXBuilder;
import omoikane.entities.Usuario;
import omoikane.principal.Principal;
import omoikane.repository.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 2/08/13
 * Time: 04:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class NipAuthProvider implements AuthProvider {

    UsuarioRepo usuarioRepo;

    @Override
    public Usuario authenticate() throws AuthException {
        usuarioRepo = (UsuarioRepo) Principal.getContext().getBean("usuarioRepo");

        while(true) {
            String nip = JOptionPane.showInputDialog(null, "NIP?");
            if(nip == null || nip.isEmpty()) continue;

            Integer nipInt;
            try { nipInt = Integer.valueOf(nip); } catch(NumberFormatException n) { continue; }


            List<Usuario> usuarios = usuarioRepo.readAll();
            for(Usuario u : usuarios) {
                if( nipInt.equals( u.getNip() ) ) return u;
            }
        }

    }
}
