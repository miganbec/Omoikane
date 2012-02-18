/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.principal

/**
 *
 * @author Octavio
 */
class Splash {
	def splashFrame = new omoikane.formularios.Splash()
        void iniciar()
        {
            splashFrame.setVisible(true)
        }
        void setText(texto)
        {
            splashFrame.getJProgressBar1().setString(texto)
        }
        void detener()
        {
            splashFrame.dispose()

        }
}

