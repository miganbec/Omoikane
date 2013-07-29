package omoikane.artemisa.reports;

import omoikane.artemisa.entity.Paciente
import omoikane.moduloreportes.Reporte

import javax.swing.JFrame
import javax.swing.JPanel

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 29/07/13
 * Time: 11:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class PacientePrint {
    Reporte reporte;

    public PacientePrint(Paciente selectedPaciente) {
        def data = [
                [
                        id:selectedPaciente.id,
                        nombre:selectedPaciente.nombre,
                        habitacion: selectedPaciente.habitacion,
                        edad: selectedPaciente.edad,
                        responsable: selectedPaciente.responsable,
                        entrada: selectedPaciente.entrada
                ]
        ]
        reporte = new Reporte("Plantillas/artemisa_registroPaciente.jrxml", data)
    }

    public show() {
        JFrame frame  = new JFrame();
        JPanel viewer = reporte.getPreviewPanel();

        viewer.setOpaque(true);
        viewer.setVisible(true);

        frame.setTitle("Registro de paciente");
        frame.add(viewer);
        frame.setSize(960,600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
