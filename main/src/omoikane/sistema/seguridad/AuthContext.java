package omoikane.sistema.seguridad;

import omoikane.principal.Principal;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 03/08/13
 * Time: 23:51
 * To change this template use File | Settings | File Templates.
 */
public class AuthContext {
    public enum AuthType { FINGERPRINT, NIP }

    public static AuthProvider instanciar() {
        AuthType authType = (AuthType) Principal.authType;
        if(authType == AuthType.FINGERPRINT)
            return new FingerprintAuthProvider();
        else
            return new NipAuthProvider();
    }

    public static AuthType valueOf(String a) throws AuthException {
        try {
            return AuthType.valueOf(a);
        } catch(IllegalArgumentException exc) {
            throw new AuthException("Se especificó un valor inválido en el valor authType de la configuración.", exc);
        }
    }
}
