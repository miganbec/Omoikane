/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.sistema

import omoikane.formularios.*;

class Dialogos
{
    static def lanzarDialogoError(parent, mensaje, detalles)
    {
        String msjXLineas = "";
        int largo = 50, nLetrasLinea = 0;
        String[] palabras  = mensaje.split(" ");

        for(int i = 0; i < palabras.length; i++)
        {
            if(nLetrasLinea + palabras[i].length() <= largo)
            {
                nLetrasLinea += palabras[i].length();
                msjXLineas += " " + palabras[i];
            } else {
                nLetrasLinea = palabras[i].length();
                msjXLineas += "<br>" + palabras[i];
            }
        }

        def pm = new omoikane.formularios.Error(parent, true);
        pm.setMensaje("<html>"+msjXLineas+"</html>");
        pm.setTxtDetalles(detalles)
        pm.setVisible(true)
    }
    static def lanzarAlerta(mensaje)
    {
        def alerta = new omoikane.formularios.Alerta(null, true)
        alerta.setMensaje(mensaje)
        alerta.setVisible(true)
    }
}
