/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.nadesicoiLegacy

/**
 *
 * @author Adan
 */

class ClientesFunciones {
    static def asignarA(serv) {
        serv.getCliente = getCliente
        serv.addCliente = addCliente
        serv.modCliente = modCliente
        
    }
    static def getCliente = { ID ->
        def salida = ""
        try {
        def db   = Db.connect()
        def almacen = db.firstRow("SELECT * FROM clientes WHERE id_cliente = $ID")
            db.close()
            salida = almacen
        } catch(e) {throw new Exception("Error al consultar Clientes")}
        salida
    }

    static def addCliente = { RFC,direccion,telefono,RazonSocial,Saldo,CP,descuento ->
        def db
        try {
            Db.connect()
            try {
                db.connection.autoCommit = false
                def IDCliente = db.executeInsert("INSERT INTO clientes SET RFC=?,direccion=?,telefono=?,razonSocial=?,saldo=?,CP=?,descuento=?", [RFC,direccion,telefono,RazonSocial,Saldo,CP,descuento])
                IDCliente = IDCliente[0][0]
                db.commit()
                return "Cliente $RazonSocial agregado."
            } catch(Exception e) {
                db.rollback()
                if(e.message.contains("Duplicate entry")) { return "El Cliente que intenta capturar ya exíste" }
                println "[Excepcion:$e]"
                throw new Exception("Error al enviar a la base de datos. El cliente no se registró.")
            } finally {
                db.close()
            }
        } catch(e) { throw new Exception("Error en la conexión del servidor con su base de datos") }
    }

    static def modCliente = { RFC,direccion,telefono,RazonSocial,Saldo,CP,descuento,IDCliente ->
        def db   = Db.connect()
        try {
          db.connection.autoCommit = false
          db.executeUpdate("UPDATE clientes SET RFC=?,direccion=?,telefono=?,razonSocial=?,saldo=?,CP=?,descuento=? WHERE id_cliente = ?"
                           , [RFC,direccion,telefono,RazonSocial,Saldo,CP,descuento,IDCliente])

          db.commit()
          return "Cliente modificado existosamente"
        } catch(Exception e) {
            db.rollback()
            println "[Excepcion:$e]"
            throw new Exception ("Error al modificar Cliente")
        } finally {
            db.close()
        }
    }


}

