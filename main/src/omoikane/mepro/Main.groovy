/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package omoikane.mepro;

import groovy.swing.SwingBuilder
import java.awt.BorderLayout
import groovy.inspect.swingui.*;
import groovy.util.*
import javax.swing.BoxLayout
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.Icon
import javax.swing.JOptionPane

/**
 *
 * @author Octavio
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    def mainMenu
    def swing

    public static void main(String[] args) {
        if(args.length > 0) {
            try {
                def file = new File(args[0])
                ejecutarScript(file)
            } catch(java.io.FileNotFoundException fnfe) {
                println "No exíste el archivo que se quiere ejecutar"
            }
        } else
            new Main()
    }

    Main() {
        swing = SwingBuilder.build {
            lookAndFeel( 'nimbus' )
            mainMenu = new FramePrincipal(title:"Phesus MePro")
            mainMenu.show true
        }
        def frameScript
        mainMenu.pnlScripts.layout = new BoxLayout(mainMenu.pnlScripts, BoxLayout.PAGE_AXIS)
        mainMenu.btnAgregar.actionPerformed = { lanzarNuevo() }
        refrescaLista()

    }
    def refrescaLista() {
        mainMenu.pnlScripts.removeAll()
        def dir = new File('./'), nombre, archivos = [:], archivo

        dir.eachFileMatch(~/(.*\.groovy)/) {
            nombre  = it.name
            archivos[nombre] = it
            archivo = it;

            mainMenu.pnlScripts.add(
                swing.panel() {
                    swing.button(text: nombre, actionCommand:it, actionPerformed: { ejecutarScript(archivos[it.actionCommand]) }, preferredSize:[130,35])
                    swing.button(icon: imageIcon("/omoikane/mepro/media/blog_post_edit.png"), actionCommand:archivo, actionPerformed: { modificarScript(archivo) })
                    swing.button(icon: imageIcon("/omoikane/mepro/media/remove.png"), actionCommand:archivo, actionPerformed: { eliminarScript(archivo) } )
                }
            )
        }
        mainMenu.pack()
    }

    static def ejecutarScript(archivo) { evalScript(archivo.text) }

    def archivo2Nombre(archivo) { (archivo =~ /.*\\(.*)\.groovy/)[0][1] }

    static def evalScript(script) {
        try {
          Eval.me("Omoikane", omoikane, script)
        } catch (Exception e) {
          MeproKit.lanzarDialogoError(null, "Exíste un error en su script", e)  
        }
    }

    def lanzarNuevo() {
        def fS
        SwingBuilder.build {
          fS = new FrameScript(title:"Phesus MePro")
          fS.show true
          fS.btnEjecutar.actionPerformed = { evalScript(fS.txtScript.getText()) }
          fS.btnCerrar  .actionPerformed = { fS.dispose() }
          fS.btnGuardar .actionPerformed = { this.guardarScript(nombre:fS.txtNombre.getText(), script:fS.txtScript.getText()); }
        }
        fS
    }
    def guardarScript(Map p)
    {
        def file = new File("./${p.nombre}")
        file.delete()
        file << p.script
        refrescaLista()
        MeproKit.lanzarAlerta("Script guardado")
    }
    def modificarScript(archivo)
    {
        def fS = lanzarNuevo()
        fS.txtNombre.setText(archivo.name)
        fS.txtScript.setText(archivo.text)
    }
    def eliminarScript(archivo) {
        try {
            if(JOptionPane.showConfirmDialog(null, "¿Realmente desea eliminar el script?")==JOptionPane.OK_OPTION) {
              def file = archivo.delete()
              MeproKit.lanzarAlerta("El script se ha eliminado")
              refrescaLista()
            }
        } catch(Exception e) {
            MeproKit.lanzarDialogoError(null, "Ha ocurrido un error, el script no se ha eliminado", e)
        }
    }
}