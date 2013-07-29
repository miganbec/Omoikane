package omoikane.artemisa.reports

import omoikane.artemisa.entity.Abono
import omoikane.artemisa.entity.Paciente
import omoikane.moduloreportes.Reporte

import javax.swing.JFrame
import javax.swing.JPanel

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 29/07/13
 * Time: 03:48 PM
 * To change this template use File | Settings | File Templates.
 */
class AbonoPrint {
    Reporte reporte;

    public AbonoPrint(Abono a) {
        def data = [
                [
                        fecha: a.getFecha(),
                        paciente: a.getPaciente().nombre,
                        concepto: a.getConcepto(),
                        importe: a.getImporte()
                ]
        ]
        reporte = new Reporte("Plantillas/artemisa_reciboAbono.jrxml", data)
    }

    public show() {
        JFrame frame  = new JFrame();
        JPanel viewer = reporte.getPreviewPanel();

        viewer.setOpaque(true);
        viewer.setVisible(true);

        frame.setTitle("Recibo de abono");
        frame.add(viewer);
        frame.setSize(960,600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
