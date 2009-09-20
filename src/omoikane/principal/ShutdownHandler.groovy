/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.principal

/**
 *
 * @author SYSTEM
 */
public class ShutdownHandler extends Thread {


    public ShutdownInterceptor() {
    }

    public void run() {
        println "El Sistema esta cerrando buen dia ..."
        def aCerrar = Principal.toFinalizeTracker
        aCerrar.each { clv, val ->
            clv.finalize()
            println ".."
        }
    }
}