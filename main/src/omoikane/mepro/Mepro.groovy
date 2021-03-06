/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package omoikane.mepro;

import groovy.swing.SwingBuilder
import groovy.util.*
import omoikane.sistema.Permisos

import javax.swing.BoxLayout
import javax.swing.JInternalFrame
import javax.swing.JOptionPane
import groovy.ui.Console;

/**
 *
 * @author Octavio
 */
public class Mepro {

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
            new Mepro()
    }

    public def Mepro() {
        if(omoikane.sistema.Usuarios.cerrojo(Permisos.PMA_MEPRO)) _Mepro();
    }

    private def _Mepro() {
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
                    swing.button(text: "GC", actionCommand:archivo, actionPerformed: { lanzarGC(archivo) })
                    swing.button(icon: imageIcon("/omoikane/mepro/media/blog_post_edit.png"), actionCommand:archivo, actionPerformed: { modificarScript(archivo) })
                    swing.button(icon: imageIcon("/omoikane/mepro/media/remove.png"), actionCommand:archivo, actionPerformed: { eliminarScript(archivo) } )
                }
            )
        }
        mainMenu.pack()
    }

    static def ejecutarScript(archivo) { evalScript(archivo.text) }

    def archivo2Nombre(archivo) { return (archivo.nombre =~ /(.+?)(\.[^.]*$|$)/)[0][1]; }

    def lanzarGC(archivo) {
        Console console = new Console();
        console.set
        console.run();
        console.loadScriptFile(archivo);
    }

    static def evalScript(script) {
        try {
          Eval.me(script)
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
        def nombre = archivo2Nombre(p);
        def file = new File("./${nombre}.groovy")
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