package omoikane.sistema.seguridad;

import omoikane.entities.Usuario;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 2/08/13
 * Time: 03:34 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AuthProvider {
    Usuario authenticate() throws AuthException;

}
