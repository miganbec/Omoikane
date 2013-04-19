/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.mepro

import java.awt.*;
import javax.swing.*
import org.apache.log4j.Logger;


class MeproKit
{
    public static Logger logger = Logger.getLogger(MeproKit.class);
    def static selectFile()
    {
        final JFileChooser fc = new JFileChooser()
        fc.showOpenDialog(null)
        fc.getSelectedFile().path
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
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventanSize = ventana.getPreferredSize();

        ventana.setLocation(screenSize.width/2 - (ventanSize.width/2) as int, screenSize.height/2 - (ventanSize.height/2) as int);
    }
    static def lanzarDialogoError(parent, mensaje, detalles)
    {

        String msjXLineas = "";
        int largo = 50, nLetrasLinea = 0;
        String[] palabras  = mensaje.split(" ");

        for(int i = 0; i < palabras.length; i++)
        {
            if  (nLetrasLinea + palabras[i].length() <= largo)
            {
                nLetrasLinea += palabras[i].length();
                msjXLineas += " " + palabras[i];
            } else {
                nLetrasLinea = palabras[i].length();
                msjXLineas += "<br>" + palabras[i];
            }
        }
        logger.error(msjXLineas, detalles);
    }
    static def lanzarAlerta(mensaje)
    {
        logger.info(mensaje);
    }
}
