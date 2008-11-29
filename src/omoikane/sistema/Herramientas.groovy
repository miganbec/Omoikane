/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.sistema

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import groovy.inspect.swingui.*

class Herramientas
{
    def static void verificaCampo(txt,expresion,error)
    {
        if(!(txt==~ expresion))
        {throw new Alerta(error)}
    }

    def static void verificaCampos(c)
    {
        try { c() }
        catch (Alerta e){Dialogos.lanzarAlerta(e.message)}
        catch (e) {Dialogos.lanzarDialogoError(null,e.message,getStackTraceString(e))}
    }

    def static String getStackTraceString(java.lang.Exception exc)
    {
        String salida = exc.toString() + "\n";
        java.lang.StackTraceElement[] elementos = exc.getStackTrace();
        for(int i = 0; i < elementos.length; i++)
        {
            salida += elementos[i].toString() + " at \n" ;
        }
        return salida;
    }
    def static centrarVentana(ventana)
    {
        def compensaY = 0
        def config    = omoikane.principal.Principal.config.resolucionPantalla
        
        if(((config.@ancho[0] as Double)/4)==((config.@alto[0] as Double)/3)) { compensaY = 100 } //Compensaciòn particular para 1024768
        Dimension screenSize = omoikane.principal.Principal.escritorio.escritorioFrame.getSize();
        Dimension ventanSize = ventana.getPreferredSize();

        def posX = screenSize.width/2 - (ventanSize.width/2) as int
        def posY = screenSize.height/2 - (ventanSize.height/2) as int
        ventana.setLocation(posX,posY+compensaY);
        [x:posX,y:posY]
    }
    def static centrarVentanaAPantalla(ventana) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventanSize = ventana.getPreferredSize();

        def posX = screenSize.width/2 - (ventanSize.width/2) as int
        def posY = screenSize.height/2 - (ventanSize.height/2) as int
        ventana.setLocation(posX,posY);
        [x:posX,y:posY]
    }
    def static Input2Action(JComponent componente, int tecla, String nombre, Action claseAccion)
    {
        componente.getInputMap(JInternalFrame.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(tecla, 0), nombre);
        componente.getActionMap().put(nombre, claseAccion);
        componente.getInputMap(JInternalFrame.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(tecla, 0), nombre);
        componente.getActionMap().put(nombre, claseAccion);
    }

}