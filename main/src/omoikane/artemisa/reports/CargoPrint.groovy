package omoikane.artemisa.reports

import omoikane.artemisa.entity.Cargo
import omoikane.sistema.Comprobantes

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 30/07/13
 * Time: 12:35 PM
 * To change this template use File | Settings | File Templates.
 */
class CargoPrint {
    Map data;
    Comprobantes comprobante;

    public CargoPrint(Cargo cargo) {
        data = [:];
        data.paciente = cargo.paciente;
        data.cantidad = cargo.cantidad;
        data.producto = cargo.producto;
        data.importe  = cargo.total;
        data.fecha    = cargo.fecha;

        comprobante = new Comprobantes();
        comprobante.setComprobante("Plantillas/artemisa-FormatoCargo.txt", data);
    }

    public print() {
        comprobante.imprimir();
    }
}
