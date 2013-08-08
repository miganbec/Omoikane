package omoikane.sistema.seguridad;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 2/08/13
 * Time: 03:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class AuthException extends Exception {
    public AuthException(Exception e) {
        super("Authentication issue.", e);
    }

    public AuthException(String msj, Exception e) {
        super(msj, e);
    }
}
