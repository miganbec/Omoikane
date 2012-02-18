package omoikane.repository;

import omoikane.entities.CorteSucursal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 20/07/11
 * Time: 19:31
 * To change this template use File | Settings | File Templates.
 */
@Repository
@Transactional
public class PruebaSpring {

        @PersistenceContext
        private EntityManager em;

        @Autowired
        private CorteSucursalRepo repo;

        @Transactional
        public void pr() {
            //SessionFactory sess = ( SessionFactory )context.getBean( "sessionFactory" );

            //Configuración de hibernate
            //Configuration cfg = new Configuration()
            //    .addResource("hibernate.cfg.xml");
            //cfg.configure();
            //SessionFactory sess = cfg.buildSessionFactory();
            //Session ses = sess.openSession();
            //ses.beginTransaction();


            CorteSucursal corte = new CorteSucursal();
            Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
            corte.setCreacion(timestamp);
            corte.setDepositos(new BigDecimal(134));
            corte.setDesde(timestamp);
            corte.setRetiros(new BigDecimal(9237));
            corte.setHasta(timestamp);

            repo.save(corte);

            System.out.print("saved. N:"+repo.count());

            //corte.setUmodificacion(timestamp);

            /*
            ses.save(corte);

            ses.getTransaction().commit();
            ses.close();
            */
        }

}
