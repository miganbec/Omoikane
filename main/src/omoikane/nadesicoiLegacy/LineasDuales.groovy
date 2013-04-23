/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.nadesicoiLegacy

class LineasDualesFunciones {
    static def asignarA(serv) {
        serv.addLineaD = addLineaD
        serv.delLineaD = delLineaD
    }

    static def addLineaD = { descripcion ->
        def db
        try {
            db = Db.connect()
            try {
                db.connection.autoCommit = false
                def IDLinea = db.executeInsert("INSERT INTO lineas_dual (id_linea) (SELECT id_linea FROM lineas WHERE descripcion = $descripcion)" )
                db.commit()
                return "$descripcion"
            } catch(Exception e) {
                db.rollback()
                if(e.message.contains("Duplicate entry")) { return "La Linea que intenta trasladar ya existe" }
                println "[Excepcion:$e]"
                throw new Exception("Error al enviar a la base de datos. La linea no se registró.")
            } finally {
                db.close()
            }
        } catch(e) { throw new Exception("Error en la conexión del servidor con su base de datos") }
    }

    static def delLineaD = { descripcion ->
        def db   = Db.connect()
        try {
          db.connection.autoCommit = false
          db.executeUpdate("DELETE FROM lineas_dual WHERE lineas_dual.id_linea = (SELECT lineas.id_linea FROM lineas WHERE descripcion = $descripcion )" )
          db.commit()
          // return "Linea borrada existosamente"
        } catch(Exception e) {
            db.rollback()
            println "[Excepcion:$e]"
            throw new Exception ("Error al borrar Linea Dual")
        } finally {
            db.close()
        }
    }

}
