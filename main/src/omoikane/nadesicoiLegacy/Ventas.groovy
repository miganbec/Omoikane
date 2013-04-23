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
class Ventas {
    static def asignarA(serv) {
        serv.getVenta  = getVenta
        serv.modVenta  = modVenta
        serv.sumaVentas= sumaVentas
        serv.addVentaEspecial=addVentaEspecial
    }
	/**
	 * Obtiene y guarda el siguiente folio disponible para la caja, sin rollback
	 */
	static def generaFolio ( IDCaja ) {
		def db     = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
		def folio = generaFolioSync(IDCaja, db)
		((Sql)db).close()
		return folio
	}
	/**
	 * Obtiene y guarda el siguiente folio, disponible para rollbacks
	 */
	static def generaFolioSync( IDCaja, db ) {		
		def folioActual = db.firstRow('SELECT uFolio from cajas where id_caja = ?', [IDCaja])
		folioActual.uFolio++
		db.executeUpdate "UPDATE cajas SET uFolio = ? where id_caja = ?", [folioActual.uFolio, IDCaja]
		return folioActual.uFolio
	}
    static def sumaVentas = { IDCaja, desde, hasta ->
        def salida = ""
        try {
            def db     = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
            def ventas = db.firstRow("""SELECT count(id_venta) as nVentas, sum(subtotal) as subtotal, sum(impuestos) as impuestos,
                                    sum(descuento) as descuento, sum(total) as total FROM ventas WHERE id_caja = ?
                                    AND fecha_hora >= ? AND fecha_hora <= ?""", [IDCaja, desde, hasta])
            def depos  = db.firstRow("""SELECT sum(importe) as total FROM movimientos_cortes WHERE id_caja = ? AND momento >= ? AND momento <= ?
                                AND tipo = 'deposito'""",[IDCaja, desde, hasta])
            def retiros= db.firstRow("""SELECT sum(importe) as total FROM movimientos_cortes WHERE id_caja = ? AND momento >= ? AND momento <= ?
                                AND tipo = 'retiro'""",[IDCaja, desde, hasta])
            ventas.depositos = depos.total!=null?depos.total:0.0
            ventas.retiros   = retiros.total!=null?retiros.total:0.0
            if(ventas.total == null) { ventas = 0 }
            salida = ventas
            
        } catch(e) { Consola.error("[Error: ${e.message}]",e); throw new Exception("Error al consultar la suma de ventas")}
        salida
    }
    static def getVenta = { ID ,IDAlmacen->
        def salida = ""
        try {
        def db   = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        def Venta = db.firstRow("SELECT * FROM ventas WHERE id_almacen = $IDAlmacen AND id_venta = $ID")
        def Cliente=db.firstRow("SELECT razonSocial FROM clientes WHERE id_cliente=$Venta.id_cliente")
        def Almacen=db.firstRow("SELECT descripcion FROM almacenes WHERE id_almacen=$Venta.id_almacen")
        Venta.date       = Venta.fecha_hora
        Venta.fecha_hora = Venta.fecha_hora as String
        Venta.nombreCliente=Cliente.razonSocial
        Venta.nombreAlmacen=Almacen.descripcion


        def articulo
        def tabMatriz = []
        def detalles  = []
        db.eachRow("SELECT * FROM ventas_detalles WHERE id_almacen = $IDAlmacen AND id_venta = $ID")
        {
            articulo = db.firstRow("SELECT codigo, descripcion from articulos WHERE id_articulo = ?", [it.id_articulo])
            tabMatriz << [articulo.codigo, articulo.descripcion, it.precio, it.cantidad, it.cantidad*it.precio]
            detalles  << [codigo:articulo.codigo     , descripcion:articulo.descripcion, precio: it.precio      ,
                          cantidad:it.cantidad       , total:it.cantidad*it.precio     ]
        }
            db.close()
            Venta.tabMatriz = tabMatriz
            Venta.detalles  = detalles
            salida = Venta
        } catch(e) { Consola.error("[Error: ${e.message}]",e); throw new Exception("Error al consultar la venta")}
        salida
    }

    static def addVentaEspecial={IDVenta,IDAutorizador->
        def db
        try {
            db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
            try {
                db.connection.autoCommit = false
                def IDVentaEspecial = db.executeInsert("INSERT INTO ventasprecioespecial SET id_venta = ?, id_autorizador = ?",[IDVenta, IDAutorizador])
                IDVentaEspecial = IDVentaEspecial[0][0]
                db.commit()
                return "Venta Especial agregada."
            } catch(Exception e) {
                db.rollback()
                if(e.message.contains("Duplicate entry")) { return "La Venta Especial que intenta capturar ya exíste" }
                println "[Excepcion:$e]"
                throw new Exception("Error al enviar a la base de datos. La Venta Especial no se registró.")
            } finally {
                db.close()
            }
        } catch(e) { throw new Exception("Error al registrar venta especial") }

    }

    static def modVenta = { IDVenta,IDCliente ->
        def db   = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        try {
          db.connection.autoCommit = false
          db.executeUpdate("UPDATE ventas SET id_cliente=?,facturada=? WHERE id_venta = ?"
                           , [IDCliente,1,IDVenta])
          db.commit()
          return "datos de facturacion modificados existosamente"
        } catch(Exception e) {
            db.rollback()
            Consola.error("Excepcion al modificar los datos de facturacion para ventas",e)
            throw new Exception ("Error al modificar los datos de facturacion para ventas")
        } finally {
            db.close()
        }
    }

}

