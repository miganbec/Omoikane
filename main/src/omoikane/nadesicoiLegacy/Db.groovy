/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.nadesicoiLegacy

import groovy.sql.*;


import omoikane.principal.Principal
/**
 *
 * @author Adan
 */
class Db {

    static def asignarA(serv) {
        serv.getRows = getRows
    }

    static def getRows = { query ->
        def salida = ""
        try {

            def db   = Sql.newInstance(Principal.URLMySQL, Principal.loginJasper, Principal.passJasper, "com.mysql.jdbc.Driver")
            def rows = db.rows(query)
            db.close()
            salida = rows
        } catch(e) { Consola.error( "[Error: ${e.message}]",e); throw new Exception("Error al Poblar Tabla") }
        salida
    }

    static def connect() {
        return Sql.newInstance(Principal.URLMySQL, Principal.loginJasper, Principal.passJasper, "com.mysql.jdbc.Driver");
    }
}

