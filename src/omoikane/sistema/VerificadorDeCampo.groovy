/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.sistema

import javax.swing.*;
import java.awt.*;
import java.util.regex.*;
import java.util.*;
import omoikane.sistema.*;

class VerificadorDeCampo extends InputVerifier
{
    String     patron;
    String     msg;

    VerificadorDeCampo(String patron, String msg)
    {
        this.patron = patron;
        this.msg    = msg;
    }

    public boolean verify(JComponent input)
    {
        boolean resultado = (((JTextField) input).getText() ==~ patron);
        if(!resultado) {

            Dialogos.lanzarAlerta(msg);
            input.grabFocus();

        }
        return resultado;
    }
}