/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.principal

import omoikane.sistema.*

/**
 *
 * @author Usuario
 */
class Caja {
    static def IDAlmacen = 1
    static def escritorio = omoikane.principal.Principal.escritorio

	static def lanzar() {
        def form = new omoikane.formularios.Caja()
        escritorio.getPanelEscritorio().add(form)
        Herramientas.centrarVentana(form);
        form.setVisible(true);
        Herramientas.iconificable(form)

        form.toFront()
        try { form.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario caja", Herramientas.getStackTraceString(e)) }
        form.txtCaptura.requestFocus()

        def serv = Nadesico.conectar()
        def addArtic = { codigo ->
            try {
                def art = serv.codigo2Articulo(IDAlmacen, codigo)
                form.modelo.addRow([art,61,71,18,19].toArray())
            } catch(e) { Dialogos.error("Error al obtener información de nadesico!: ${e.message}", e) }
        }
        form.txtCaptura.keyPressed = { e -> if(e.keyCode==e.VK_ENTER) { addArtic(form.txtCaptura.text) } }
    }
}
