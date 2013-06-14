
 /* Author Phesus        //////////////////////////////
 *  ORC,ACR             /////////////
 *                     /////////////
 *                    /////////////
 *                   /////////////
 * //////////////////////////////                   */

package omoikane.principal

import groovy.swing.*
import omoikane.sistema.Reloj;
import groovy.inspect.swingui.*
 import java.awt.Font
 import java.awt.GraphicsEnvironment
 import omoikane.formularios.FrostedGlassDesktopPane

 class Escritorio {

    omoikane.formularios.Escritorio escritorioFrame = new omoikane.formularios.Escritorio()
    def reloj = new Reloj(escritorioFrame.reloj)

    void iniciar()
    {
        SwingBuilder.build { lookAndFeel('nimbus') }
        ajustarTam()
        escritorioFrame.setVisible(true);
        reloj.start()
    }

    void setNombreUsuario(nombre)
    {
        escritorioFrame.usuario.setText(nombre)
    }

    FrostedGlassDesktopPane getPanelEscritorio()
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
        else if(ancho==1280&&alto==720) {
            escritorioFrame.setSize(new java.awt.Dimension(ancho,alto))
            escritorioFrame.setPreferredSize(new java.awt.Dimension(ancho,alto))
            escritorioFrame.setMinimumSize(new java.awt.Dimension(ancho,alto))
            escritorioFrame.setBounds(0,0,ancho,alto)
            escritorioFrame.lblImagenFondo.setBounds(0,0,ancho,alto)


            InputStream is = Escritorio.getResourceAsStream("/omoikane/Media2/fonts/Roboto-Thin.ttf");
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            Font sizedFont  = font.deriveFont(36f);
            Font sizedFont2 = font.deriveFont(14f);

            escritorioFrame.usuario.setFont(sizedFont2);
            GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
            genv.registerFont(sizedFont);

            InputStream iconis = Escritorio.getResourceAsStream("/omoikane/Media2/icons/linecons.ttf");
            Font iconFont = Font.createFont(Font.TRUETYPE_FONT, (java.io.InputStream) iconis);
            Font sizedIconFont  = iconFont.deriveFont(36f);

            genv.registerFont(sizedIconFont);

            //escritorioFrame.usuario.setLocation(136,162)
            //escritorioFrame.reloj.setLocation(790,146)
        }
    }
}

