package omoikane.artemisa;


import omoikane.artemisa.entity.Paciente;
import omoikane.proveedores.Proveedor;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Query;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 25/02/13
 * Time: 09:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PacienteRepo extends GenericDao<Paciente, Integer> {
    @Query("FROM Paciente p WHERE p.liquidado != 1")
    List<Paciente> findAllActive();

    @Query("FROM Paciente p WHERE (p.liquidado = false OR p.liquidado = ?1) AND nombre like ?2")
    List<Paciente> findByLiquidadoAndNombreLike(Boolean activo, String like);
}
