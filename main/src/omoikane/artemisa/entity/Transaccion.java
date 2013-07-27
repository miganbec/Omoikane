package omoikane.artemisa.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 26/07/13
 * Time: 02:06 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private
    Date fecha;

    @Column
    private
    String concepto;

    @Column private BigDecimal cargo;
    @Column private BigDecimal abono;

    @ManyToOne
    private
    Paciente paciente;

    public Transaccion() {
        cargo = new BigDecimal(0);
        abono = new BigDecimal(0);
    }

    @PrePersist
    protected void onCreate() {
        setFecha( new Timestamp(Calendar.getInstance().getTime().getTime()) );
    }

    public Integer getId() {
        return id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public BigDecimal getCargo() {
        return cargo;
    }

    public void setCargo(BigDecimal cargo) {
        this.cargo = cargo;
    }

    public BigDecimal getAbono() {
        return abono;
    }

    public void setAbono(BigDecimal abono) {
        this.abono = abono;
    }
}
