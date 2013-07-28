package omoikane.artemisa.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

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

    @Column
    String concepto;

    @Column
    BigDecimal importe;
}
