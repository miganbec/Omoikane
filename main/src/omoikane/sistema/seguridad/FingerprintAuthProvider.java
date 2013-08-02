package omoikane.sistema.seguridad;

import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
import omoikane.entities.Usuario;
import omoikane.sistema.Nadesico;
import omoikane.sistema.huellas.MiniLeerHuella;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 2/08/13
 * Time: 03:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class FingerprintAuthProvider implements AuthProvider {
    MiniLeerHuella fingerPrint;
    JFrame escritorio;

    public FingerprintAuthProvider() {
        escritorio   = omoikane.principal.Principal.getEscritorio().getFrameEscritorio();
    }

    @Override
    public Usuario authenticate() throws AuthException {
        while(true) {
            try {
                fingerPrint  = new omoikane.formularios.WndLeerHuella(escritorio).getMiniLeerHuella();
            } catch (Exception e) {
                throw new AuthException(e);
            }
            if (fingerPrint.verifyFeatureSet != null && fingerPrint.verifyFeatureSet.length > 0) { break; }
        }

        //Aquí sucede la identificación del usuario
        try {
            Usuario usuario = fingerPrint.identify();
            return usuario;
        } catch (DPFPImageQualityException e) {
            throw new AuthException(e);
        }
    }
}
