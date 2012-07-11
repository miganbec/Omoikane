package omoikane.sistema.huellas;

import omoikane.entities.Usuario;
import omoikane.repository.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: usuario1
 * Date: 7/07/12
 * Time: 04:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class HuellasCache {

    private ArrayList<Usuario> huellasBD;

    @Autowired
    UsuarioRepo repo;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Caché administrado por Hibernate
     * @return Lista que contiene todas los usuarios y sus features (características de huellas dactilares)
     */
    public ArrayList<Usuario> getHuellasBD() {
        huellasBD = (ArrayList<Usuario>) repo.findAll();

        return huellasBD;
    }


}
