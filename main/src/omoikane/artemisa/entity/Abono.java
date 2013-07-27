package omoikane.artemisa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 26/07/13
 * Time: 02:47 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Abono extends Transaccion {

    public Abono() {
        setConcepto("Abono a cuenta del paciente");
    }

    public BigDecimal getImporte() {
        return getAbono();
    }

    public void setImporte(BigDecimal importe) {
        this.setAbono(importe);
    }


}
