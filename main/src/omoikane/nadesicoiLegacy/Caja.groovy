/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.nadesicoiLegacy

import groovy.sql.*
/**
 *
 *
 *
 *
 *
 *
 *
 *
 *
 * @author Usuario
 */
class Caja {
	/***
	 * Pruebaaaa
	 */
	def prueba() {} 
	static def asignarA(serv) {
        serv.aplicarVenta = aplicarVenta
        serv.doRetiro     = doRetiro
        serv.doDeposito   = doDeposito
        serv.getCaja      = getCaja
        serv.addCaja      = addCaja
        serv.modCaja      = modCaja
        serv.abrirCaja    = abrirCaja
        serv.cajaAbierta  = cajaAbierta
        serv.cerrarCaja   = cerrarCaja
        serv.getDoMovimiento = getDoMovimiento
    }

    static def getDoMovimiento = { ID, tipo ->
        def salida = ""
        try {
        def db   = Db.connect()
        def Caja = db.firstRow("SELECT * FROM movimientos_cortes WHERE id = $ID and tipo= $tipo")
            db.close()
            salida = Caja
        } catch(e) { Consola.error("Error al consultar movimiento (getdoMovimiento)", e); throw new Exception("Error al consultar Cajas") }
        salida
    }

    static def abrirCaja = { IDCaja ->
        def db = Db.connect()
        def resultado = db.executeUpdate("UPDATE cajas SET abierta = ?, horaAbierta = CURRENT_TIMESTAMP WHERE id_caja = ?", [1, IDCaja]);
        db.close()
        resultado
    }
    static def doRetiro = { IDAlmacen, IDCaja, IDCajero, IDUsuario, importe ->
        def db = Db.connect()
        def resultado = db.executeInsert("""INSERT INTO movimientos_cortes (tipo, id_almacen, id_caja, id_cajero, id_usuario, importe)
                                            VALUES (?,?,?,?,?,?) """,
                                          ['retiro', IDAlmacen, IDCaja, IDCajero, IDUsuario, importe]);        
        resultado = resultado[0][0]
        db.close()
        resultado
    }
    static def doDeposito = { IDAlmacen, IDCaja, IDCajero, IDUsuario, importe ->
        def db = Db.connect()
        def resultado = db.executeInsert("""INSERT INTO movimientos_cortes (tipo, id_almacen, id_caja, id_cajero, id_usuario, importe)
                                            VALUES (?,?,?,?,?,?) """,
                                          ['deposito', IDAlmacen, IDCaja, IDCajero, IDUsuario, importe]);
        resultado = resultado[0][0]
        db.close()
        resultado
    }
    static def cerrarCaja = { IDCaja ->
        def db = Db.connect()
        def resultado = db.executeUpdate("UPDATE cajas SET abierta = ?, horaCerrada = CURRENT_TIMESTAMP WHERE id_caja = ?", [0, IDCaja]);
        db.close()
    }
    static def cajaAbierta = { IDCaja ->
        def db = Db.connect()
        def resultado = db.rows("SELECT abierta FROM cajas WHERE id_caja = ?", [IDCaja])
        db.close()
        if(resultado.size()==1) { return resultado[0].abierta } else { return -1 }
    }
    /**
     * Método ejecutado al terminar una venta
     */
    static def aplicarVenta = { IDCaja, IDAlmacen, IDCliente, IDUsuario, subtotal, descuento, impuestos, total, detalles ,efectivo,cambio,centecimos->
        def db, retorna = [], artDetalles

        db = Db.connect()
        try {
            db.connection.autoCommit = false
			def folio   = Ventas.generaFolioSync(IDCaja, db)
			
			def IDVenta = db.executeInsert("INSERT INTO ventas SET id_caja = ?, id_almacen = ?, id_cliente = ?, id_usuario = ?, fecha_hora = NOW() ,"+
                "facturada = ?, completada = ?, eliminar = ?, subtotal = ?, descuento = ?, impuestos = ?, total = ? ,efectivo=?,cambio=?,centecimosredondeados=?,"+
				"folio=?"
                , [IDCaja, IDAlmacen, IDCliente, IDUsuario, 0, 1, 0, subtotal, descuento, impuestos, 
                   total,efectivo,cambio,centecimos, folio])
				
            IDVenta = IDVenta[0][0]
            detalles.each {
                artDetalles = db.firstRow("SELECT id_linea, impuestos FROM ramcachearticulos WHERE id_articulo = ?", [it.IDArticulo])
                db.executeInsert("INSERT INTO ventas_detalles SET id_venta = ?, id_caja = ?, id_almacen = ?, id_articulo = ?, precio = ?, "+
                    "cantidad = ?, tipo_salida = ?, subtotal = ?, impuestos = ?, descuento = ?, total = ?, id_linea = ?"
                , [IDVenta, IDCaja, IDAlmacen, it.IDArticulo, it.precio, it.cantidad, 0, ((it.precio)as Double)*((it.cantidad)as Double), artDetalles['impuestos'],
                   it.descuento, it.total, artDetalles['id_linea']])
                Existencias.cambiar(db, IDAlmacen, it.IDArticulo, "-${it.cantidad.toString()}")
            }
            db.commit()
            return [mensaje:"Venta registrada.", ID: IDVenta]
        } catch(Exception e) {
            db.rollback()
            if(e.message.contains("Duplicate entry")) { return "El artículo que intenta capturar ya exíste o hay un producto con el mismo código" }
            println "[Excepcion:$e]"
            Consola.error("Error al momento de registrar venta (aplicarventa)", e)
            throw new Exception("Error al enviar a la base de datos. La venta no se registró : ${e.message}.", e)
            
        } finally {
            db.close()
        }

    }

    static def getCaja = { ID ->
        def salida = ""
        try {
        def db   = Db.connect()
        def Caja = db.firstRow("SELECT * FROM cajas WHERE id_caja = $ID")
            db.close()
            salida = Caja
        } catch(e) { Consola.error("Error al consultar Caja (getCaja)", e); throw new Exception("Error al consultar Cajas") }
        salida
    }

    static def addCaja = {IDAlmacen,descripcion ->
        def db
        try {
            db = Db.connect()
            try {
                db.connection.autoCommit = false
                def IDCaja = db.executeInsert("INSERT INTO cajas SET id_almacen= ? , descripcion = ?, uModificacion = NOW() ",[IDAlmacen,descripcion])
                IDCaja = IDCaja[0][0]
                db.commit()
                return "Caja $descripcion agregada."
            } catch(Exception e) {
                db.rollback()
                if(e.message.contains("Duplicate entry")) { return "Caja que intenta capturar ya exíste" }
                println "[Excepcion:$e]"
                throw new Exception("Error al enviar a la base de datos. Caja no se registró.")
            } finally {
                db.close()
            }
        } catch(e) { throw new Exception("Error en la conexión del servidor con su base de datos") }
    }

    static def modCaja = { IDCaja,IDAlmacen,descripcion ->
        def db   = Db.connect()
        try {
          db.connection.autoCommit = false
          db.executeUpdate("UPDATE cajas SET descripcion = ? WHERE id_caja = ? and id_almacen=? ",[descripcion, IDCaja, IDAlmacen])
          db.commit()
          return "Caja modificada existosamente"
        } catch(Exception e) {
            db.rollback()
            println "[Excepcion:$e]"
            throw new Exception ("Error al modificar Caja")
        } finally {
            db.close()
        }
    }

}

