package omoikane.repository;

import omoikane.entities.Usuario;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Query;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 25/07/11
 * Time: 18:38
 * To change this template use File | Settings | File Templates.
 */


public interface UsuarioRepo extends GenericDao<Usuario, Long> {
    Usuario save(Usuario usuario);

    @Query("FROM Usuario u")
    List<Usuario>  findAll();

}
