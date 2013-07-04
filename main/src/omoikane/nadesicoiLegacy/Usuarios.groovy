/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.nadesicoiLegacy

import groovy.sql.*

/**
 *
 * @author Usuario
 */
class Usuarios {
	static def asignarA(serv) {
        serv.getUsuario       = getUsuario
        serv.addUsuario       = addUsuario
        serv.modUsuario       = modUsuario
    }

    static def getUsuario = { ID,IDAlmacen ->
        def salida = ""
        try {
        def db   = Db.connect()
        def usr  = db.firstRow("SELECT * FROM usuarios WHERE id_usuario = $ID")
        if(usr==null) { return null }
        def idUsr    = usr.id_usuario
        def usra = db.firstRow("SELECT * FROM usr_sucursal WHERE id_almacen = $IDAlmacen AND id_usuario = "+ idUsr)
            db.close()
            salida = usra + usr
            salida.uModificacion = usr.uModificacion
        } catch(e) { Consola.error("Error al consultar usuario (getUsuario)", e); throw new Exception("Error al consultar usuario") }
        salida
    }

    static def addUsuario = { Nombre,Huella1,Huella2,Huella3,NIP,Perfil,IDAlmacen ->
        def db
        try {
            db = Db.connect()
            try {
                db.connection.autoCommit = false
                Huella1 = Sql.BLOB(Huella1)
                Huella2 = Sql.BLOB(Huella2)
                Huella3 = Sql.BLOB(Huella3)
                def idUsuario = db.executeInsert("INSERT INTO usuarios SET fecha_hora_alta=NOW(),nombre=?,huella1=?,huella2=?,huella3=?,nip=?,uModificacion=NOW()", [Nombre,Huella1,Huella2,Huella3,NIP])
                idUsuario = idUsuario[0][0]
                db.executeInsert("INSERT INTO usr_sucursal SET id_almacen = ?, id_usuario= ?, perfil = ?",[IDAlmacen, idUsuario, Perfil])
                db.commit()
                return idUsuario
            } catch(Exception e) {
                db.rollback()
                if(e.message.contains("Duplicate entry")) { return "El usuario que intenta capturar ya exíste" }
                Consola.error("Excepcion al modificar usuario",e)
                throw new Exception("Error al enviar a la base de datos. El Usuario no se registró.")
            } finally {
                db.close()
            }
        } catch(e) { throw new Exception("Error en la conexión del servidor con su base de datos") }
    }

    static def modUsuario = { Nombre,NIP,Perfil,IDAlmacen,IDUSR ->
        def db   = Db.connect()
        try {
          db.connection.autoCommit = false
          db.executeUpdate("UPDATE usuarios SET nombre=?,nip=? WHERE id_usuario = ?"
                           , [Nombre,NIP,IDUSR])
          db.executeUpdate("UPDATE usr_sucursal SET perfil=? WHERE id_almacen =? AND id_usuario = ?",  [Perfil,IDAlmacen,IDUSR])
          db.commit()
          return "Usuario modificado existosamente"
        } catch(Exception e) {
            db.rollback()
            Consola.error("Excepcion al modificar usuario",e)
            throw new Exception ("Error al modificar usuario")
        } finally {
            db.close()
        }
    }

}

