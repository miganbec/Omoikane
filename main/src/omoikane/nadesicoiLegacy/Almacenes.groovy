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
class Almacenes {
    static def asignarA(serv) {
        serv.getAlmacen = getAlmacen
        serv.addAlmacen = addAlmacen
        serv.modAlmacen = modAlmacen
        serv.getMovimiento = getMovimiento
        serv.addMovimiento = addMovimiento
        serv.getArticuloAlmacen = getArticuloAlmacen
        //serv.modMovimiento = modMovimiento
        

    }

    static def getAlmacen = { ID ->
        def salida = ""
        try {
        def db   = Db.connect()
        def almacen = db.firstRow("SELECT * FROM almacenes WHERE id_almacen = $ID")
            db.close()
            salida = almacen
        } catch(e) {throw new Exception("Error al consultar Almacenes")}
        salida
    }

    static def addAlmacen = {descripcion ->
        def db
        try {
            db = Db.connect()
            try {
                db.connection.autoCommit = false
                def IDAlmacen = db.executeInsert("INSERT INTO almacenes SET descripcion = ?",[descripcion])
                IDAlmacen = IDAlmacen[0][0]
                db.commit()
                return "Almacen $descripcion agregada."
            } catch(Exception e) {
                db.rollback()
                if(e.message.contains("Duplicate entry")) { return "El Almacen que intenta capturar ya exíste" }
                println "[Excepcion:$e]"
                throw new Exception("Error al enviar a la base de datos. El Almacen no se registró.")
            } finally {
                db.close()
            }
        } catch(e) { throw new Exception("Error en la conexión del servidor con su base de datos") }
    }

    static def modAlmacen = { IDAlmacen, descripcion ->
        def db   = Db.connect()
        try {
          db.connection.autoCommit = false
          db.executeUpdate("UPDATE almacenes SET descripcion = ? WHERE id_almacen = ?",[descripcion, IDAlmacen])
          db.commit()
          return "Almacen modificado existosamente"
        } catch(Exception e) {
            db.rollback()
            println "[Excepcion:$e]"
            throw new Exception ("Error al modificar Almacen")
        } finally {
            db.close()
        }
    }


//movimientos

    static def getMovimiento = { ID ->
        def salida = ""
        try {
        def db   = Db.connect()
        def Movimiento = db.firstRow("SELECT * FROM movimientos_almacen WHERE id_movimiento = $ID")
        Movimiento.fecha = Movimiento.fecha as String
        def articulo
        def tabMatriz = []
        db.eachRow("SELECT * FROM movimientos_almacen_detalles WHERE id_movimiento = $ID")
        {
            articulo = db.firstRow("SELECT * from articulos WHERE id_articulo = ?", [it.id_articulo])
            tabMatriz << [articulo.codigo, articulo.descripcion, it.costo, it.cantidad, it.cantidad*it.costo]
            
        }
            db.close()
            Movimiento.tabMatriz = tabMatriz
            salida = Movimiento
        } catch(e) { println "[Error: ${e.message}]"; throw new Exception("Error al consultar el Movimiento")}
        salida
    }

        static def addMovimiento = {movimiento, tabPrincipalArray ->
        def db                = null
        def IDMov             = -1
        def articulo
        
                try {

                db = Db.connect()
                db.connection.autoCommit = false

                IDMov   = db.executeInsert("INSERT INTO movimientos_almacen SET id_almacen = ?, fecha = ?, descripcion = ?, tipo = ?, monto = ?, folio = ?"
                    , [movimiento.almacen, movimiento.fecha, movimiento.descripcion, movimiento.tipo, movimiento.granTotal, movimiento.folio])

                IDMov = IDMov[0][0]
                def movData = []


                tabPrincipalArray.each { row ->
                    movData << [codigo:row[0], descripcion:row[1], costo:row[2], cantidad:row[3], total:row[4]]
                    //aquí guardar
                    articulo = db.firstRow("SELECT * from articulos WHERE codigo = ?", [row[0]])
                    db.executeUpdate("INSERT INTO movimientos_almacen_detalles SET id_movimiento = ?, id_articulo = ?, cantidad = ?, costo = ?, id_almacen = ?",
                    [IDMov, articulo.id_articulo, row[3], row[2], movimiento.almacen])
                }


                def id_art, cant, cost;

                tabPrincipalArray.each { row ->
                    id_art = db.rows("SELECT id_articulo FROM articulos WHERE codigo = '" + row[0]+"'")[0].id_articulo
                    cost   = row[2]
                    cant   = row[3]
                    switch(movimiento.tipo) {
                        case "Entrada al almacén":
                            cant = "+$cant"
                        break
                        case "Salida del almacén":
                            cant = "-$cant"
                        break
                        case "Ajuste": break
                        default:
                            throw new Exception("Tipo de movimiento erróneo (pj. entrada, salida, ajuste)")
                        break
                    }
                    Existencias.cambiar(db, movimiento.almacen, id_art, cant)
                    //PreciosFunciones.modificar(db, movimiento.almacen, id_art, costo:cost)
                }

                db.commit()
               return ("Movimiento \"$movimiento.descripcion\" hecho al almacén.")
            } catch(Exception e) {
                db.rollback()
                println "[Excepcion:$e]"
                throw new Exception ("Error al modificar Almacen: ${e.message}",e)
            } finally {
                db.connection.autoCommit = true
            }
        }

    static def getArticuloAlmacen = { query ->
        def db   = Db.connect()
        def art  = db.rows(query)

        db.close()
        def articulo = art[0]
        def cargado
        if(articulo==null) { cargado = false; } else { cargado = true; }
        return [articulo,cargado]
    }
    
}

