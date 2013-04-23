/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.nadesicoiLegacy

import groovy.sql.*;

/**
 *
 * @author Adan
 */

class Grupos {

    static def asignarA(serv) {
        serv.getGrupo = getGrupo
        serv.addGrupo = addGrupo
        serv.modGrupo = modGrupo
    }
    static def getGrupo = { ID ->
        def salida = ""
        try {
        def db   = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        def grupo = db.firstRow("SELECT * FROM grupos WHERE id_grupo = $ID")
            db.close()
            salida = grupo
        } catch(e) {throw new Exception("Error al consultar grupo")}
        salida
    }

    static def addGrupo = {descripcion, descuento ->
        def db
        try {
            db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
            try {
                db.connection.autoCommit = false
                def IDGrupo = db.executeInsert("INSERT INTO grupos SET descripcion = ?, descuento = ?",[descripcion, descuento])
                IDGrupo = IDGrupo[0][0]
                db.commit()
                return "Grupo $descripcion agregada."
            } catch(Exception e) {
                db.rollback()
                if(e.message.contains("Duplicate entry")) { return "El Grupo que intenta capturar ya exíste" }
                println "[Excepcion:$e]"
                throw new Exception("Error al enviar a la base de datos. El Grupo no se registró.")
            } finally {
                db.close()
            }
        } catch(e) { throw new Exception("Error en la conexión del servidor con su base de datos") }
    }

    static def modGrupo = { IDGrupo, descripcion, descuento ->
        def db   = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        try {
          db.connection.autoCommit = false
          db.executeUpdate("UPDATE grupos SET descripcion = ?, descuento = ? WHERE id_grupo = ?",[descripcion, descuento, IDGrupo])
          db.commit()
          return "Grupo modificado existosamente"
        } catch(Exception e) {
            db.rollback()
            println "[Excepcion:$e]"
            throw new Exception ("Error al modificar Grupo")
        } finally {
            db.close()
        }
    }

}

