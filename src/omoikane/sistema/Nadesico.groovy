/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.sistema

import groovy.net.xmlrpc.*
import java.net.ServerSocket
import omoikane.principal.*

/**
 *
 * @author Usuario
 */
class Nadesico {
    def conn = null
    def sesion = null
    Nadesico() { iniciar() }

	def iniciar() {
        try {
            conn = new XMLRPCServerProxy(Principal.config.URLServidor[0].text())
            sesion = conn.login()
        } catch(Exception e) { Dialogos.error("Error al conectar con el servidor", e) }
        conn
    }

    static def conectar() {
        new Nadesico()
    }

    Object methodMissing(String name, Object args) {
        //try {
            return conn.invokeMethod("$name", args)
        //} catch(e) {
          //  throw new Exception("Error en la llamada al procedimiento remoto: ${e.message}", e) }
    }
    def desconectar() {
        conn.logout(sesion)
    }
    protected void finalize() throws Throwable {
        try {
            desconectar()
        } finally {
            super.finalize()
        }
    }

}

