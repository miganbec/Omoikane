package omoikane.artemisa.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.PrePersist
import java.sql.Timestamp

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 27/07/13
 * Time: 01:09 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
class CancelacionTransaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    Paciente paciente;

    @PrePersist
    protected void onCreate() {
        setFecha( new Timestamp(Calendar.getInstance().getTime().getTime()) );
    }

    @Column
    String concepto;

    @Column
    BigDecimal importe;

    @Column private Date fecha;

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
