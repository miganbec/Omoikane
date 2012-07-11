package omoikane.repository;

import omoikane.entities.Usuario;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Query;
import org.synyx.hades.dao.QueryHints;

import javax.persistence.Cacheable;
import javax.persistence.QueryHint;
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

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true")
    , @QueryHint(name = "org.hibernate.cacheRegion", value = "omoikane.entities.Usuario") } )
    @Query("FROM Usuario u")
    List<Usuario>  findAll();

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true")
    , @QueryHint(name = "org.hibernate.cacheRegion", value = "omoikane.entities.Usuario") } )
    @Query("SELECT COUNT(*) as count FROM Usuario u")
    Long countIt();

}
