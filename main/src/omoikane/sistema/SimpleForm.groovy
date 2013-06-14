
 /* Author Phesus        //////////////////////////////
 *  ORC,ACR             /////////////
 *                     /////////////
 *                    /////////////
 *                   /////////////
 * //////////////////////////////                   */

package omoikane.sistema

import static omoikane.sistema.Usuarios.cerrojo as CERROJO;
import static omoikane.sistema.Permisos.*;
import javax.swing.*
import java.awt.*
import java.awt.image.BufferedImage

class SimpleForm {

    def form;
    def cerrojo     = 0
    def conNadesico = true
    def alertas     = true
    def prepararForm= true
    def fondo       = null
	
	static def getInstance() {
		
	}
	
    SimpleForm(form = "omoikane.formularios.SimpleFormDefault", Closure cls) {
        def serv = null;

        this.form = form
        prepForm()

        try {
            if(conNadesico) { serv = Nadesico.conectar() }
            if(CERROJO(cerrojo)) {
                cls([data:serv, form:this.form])
            }
        } catch(e) {
            if(alertas) {
                Dialogos.error("Error: ${e.message}", e)
            } else {
                throw e
            }
        } finally {
            if(conNadesico) { serv.desconectar() }
        }
    }

    static def escritorio = omoikane.principal.Principal.escritorio

    def prepForm() {
        if(prepararForm) {
            //Eval.me("${form}.metaClass.fondo << new Object()");
            def clase = Eval.me("$form")
            clase.metaClass.generarFondo   << { Component componente ->
                  Rectangle areaDibujo = form.getBounds();
                  if(areaDibujo.height<= 0) { areaDibujo.height = 1 }
                  if(areaDibujo.width <= 0) { areaDibujo.width  = 1 }
                  BufferedImage tmp;
                  GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
                  tmp = gc.createCompatibleImage(areaDibujo.width as int, areaDibujo.height as int,BufferedImage.TRANSLUCENT);
                  Graphics2D g2d = (Graphics2D) tmp.getGraphics();
                  g2d.setColor(new Color(0,0,0,165));
                  g2d.fillRect(0,0,areaDibujo.width as int, areaDibujo.height as int);
                  form.fondo = tmp;
                }
            form = Eval.me("return new ${form}()")

            form.setOpaque(false);
            ((JPanel)form   .getContentPane()).setOpaque(false);
            form.getLayeredPane().setOpaque(false);
            form.getRootPane().setOpaque(false);
            form.generarFondo(form);
            Herramientas.centrarVentana(form);
            escritorio.getPanelEscritorio().add(form)
            Herramientas.iconificable(form)
            form.toFront()
            form.setSelected(true)

        }
    }
}

