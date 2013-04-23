/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.nadesicoiLegacy

/**
 *
 * @author Adan
 */


class LineasFunciones {
    static def asignarA(serv) {
        serv.getLinea = getLinea
        serv.addLinea = addLinea
        serv.modLinea = modLinea
    }
    static def getLinea = { ID ->
        def salida = ""
        try {
        def db   = Db.connect()
        def linea = db.firstRow("SELECT * FROM lineas WHERE id_linea = $ID")
            db.close()
            salida = linea
        } catch(e) {throw new Exception("Error al consultar linea")}
        salida
    }

    static def addLinea = {descripcion, descuento ->
        def db
        try {
            db = Db.connect()
            try {
                db.connection.autoCommit = false
                def IDLinea = db.executeInsert("INSERT INTO lineas SET descripcion = ?, descuento = ?",[descripcion, descuento])
                IDLinea = IDLinea[0][0]
                db.commit()
                return "Linea $descripcion agregada."
            } catch(Exception e) {
                db.rollback()
                if(e.message.contains("Duplicate entry")) { return "La Linea que intenta capturar ya exíste" }
                println "[Excepcion:$e]"
                throw new Exception("Error al enviar a la base de datos. La linea no se registró.")
            } finally {
                db.close()
            }
        } catch(e) { throw new Exception("Error en la conexión del servidor con su base de datos") }
    }

    static def modLinea = { IDLinea, descripcion, descuento ->
        def db   = Db.connect()
        try {
          db.connection.autoCommit = false
          db.executeUpdate("UPDATE lineas SET descripcion = ?, descuento = ? WHERE id_linea = ?",[descripcion, descuento, IDLinea])
          db.commit()
          return "Linea modificada existosamente"
        } catch(Exception e) {
            db.rollback()
            println "[Excepcion:$e]"
            throw new Exception ("Error al modificar Linea")
        } finally {
            db.close()
        }
    }

}

