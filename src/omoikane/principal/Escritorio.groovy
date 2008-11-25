/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.principal

import omoikane.sistema.Reloj;
import groovy.swing.*
import groovy.inspect.swingui.*
/**
 *
 * @author Octavio
 */
class Escritorio {
    def escritorioFrame = new omoikane.formularios.Escritorio()
    def reloj = new Reloj(escritorioFrame.reloj)

    void iniciar()
    {
        SwingBuilder.build { lookAndFeel('nimbus') }
        ajustarTam()
        escritorioFrame.setVisible(true);
        reloj.start()
    }
    javax.swing.JDesktopPane getPanelEscritorio()
    {
        return escritorioFrame.PanelEscritorio
    }
    javax.swing.JFrame getFrameEscritorio()
    {
        return escritorioFrame
    }
    def ajustarTam() {
        def ancho = Principal.config.resolucionPantalla.@ancho[0]
        def alto  = Principal.config.resolucionPantalla.@alto [0]
        ancho = Integer.valueOf (ancho)
        alto  = Integer.valueOf (alto)

        if(ancho==1024&&alto==768) {
            escritorioFrame.setSize(new java.awt.Dimension(ancho,alto))
            escritorioFrame.setPreferredSize(new java.awt.Dimension(ancho,alto))
            escritorioFrame.setMinimumSize(new java.awt.Dimension(ancho,alto))
            escritorioFrame.setBounds(0,0,ancho,alto)
            escritorioFrame.lblImagenFondo.setBounds(0,0,ancho,alto)
            escritorioFrame.usuario.setLocation(136,162)
            escritorioFrame.reloj.setLocation(790,146)
        }
    }
}

