package omoikane.artemisa;

import omoikane.artemisa.entity.CancelacionTransaccion;
import omoikane.artemisa.entity.Paciente;
import omoikane.artemisa.entity.Transaccion;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Query;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 27/07/13
 * Time: 01:11 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CancelacionTransaccionDAO extends GenericDao<CancelacionTransaccion, Integer> {
    @Query("FROM CancelacionTransaccion t WHERE DATE(t.fecha) >= ?1 AND DATE(t.fecha) <= ?2")
    List<CancelacionTransaccion> findCancelaciones(Date desde, Date hasta);
}
