package omoikane.artemisa;

import omoikane.artemisa.entity.Paciente;
import omoikane.artemisa.entity.Transaccion;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Query;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 29/07/13
 * Time: 04:49 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TransaccionDAO extends GenericDao<Transaccion, Integer> {
    @Query("FROM Transaccion t WHERE DATE(t.fecha) >= ?1 AND DATE(t.fecha) <= ?2")
    List<Transaccion> findTransacciones(Date desde, Date hasta);
}
