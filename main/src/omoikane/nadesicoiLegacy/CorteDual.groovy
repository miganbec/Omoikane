/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.nadesicoiLegacy

/**
 *
 * @author Adan
 */
import groovy.sql.*;


class CorteDual {

     static def asignarA(serv) {
        serv.sumaDual= sumaDual
        serv.addCorteDual=addCorteDual
        serv.getSumaCorteSucursalDual=getSumaCorteSucursalDual
        serv.getCorteWhereFrom=getCorteWhereFrom
    }
    
    static def sumaDualFunc (IDCaja, desde, hasta) {    sumaDual(IDCaja, desde, hasta)    }

    static def sumaDual = { IDCaja, desde, hasta ->
        def salida = ""
        try {
            def db     = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
            def ventas = db.firstRow("""SELECT count(id_venta) as nVentas, sum(subtotal) as subtotal, sum(impuestos) as impuestos,
                                    sum(descuento) as descuento, sum(total) as total FROM ventas WHERE id_caja = ?
                                    AND fecha_hora >= ? AND fecha_hora <= ?""", [IDCaja, desde, hasta])
            def ventasNoReg = db.firstRow("SELECT sum(ventas_detalles.subtotal) as subtotal,sum(ventas_detalles.impuestos) as impuestos,"+
            "sum(ventas_detalles.descuento) as descuento,sum(ventas_detalles.total) as total FROM ventas,ventas_detalles,"+
            "lineas_dual WHERE ventas.id_caja = ? AND ventas.fecha_hora >= ? AND ventas.fecha_hora <= ?"+
            "and ventas.id_venta = ventas_detalles.id_venta and ventas.id_caja=ventas_detalles.id_caja and lineas_dual.id_linea=ventas_detalles.id_linea", [IDCaja, desde, hasta])
            ventas.total=ventas.total-ventasNoReg.total
            ventas.subtotal=ventas.subtotal-ventasNoReg.subtotal
            ventas.descuento=ventas.descuento-ventasNoReg.descuento
            ventas.impuestos=ventas.impuestos-ventasNoReg.impuestos
            ventas.totalDual=ventasNoReg.total
            ventas.subtotalDual=ventasNoReg.subtotal
            ventas.descuentoDual=ventasNoReg.descuento
            ventas.impuestosDual=ventasNoReg.impuestos
            def depos  = db.firstRow("""SELECT sum(importe) as total FROM movimientos_cortes WHERE id_caja = ? AND momento >= ? AND momento <= ?
                                AND tipo = 'deposito'""",[IDCaja, desde, hasta])

            def retiros= db.firstRow("""SELECT sum(importe) as total FROM movimientos_cortes WHERE id_caja = ? AND momento >= ? AND momento <= ?
                                AND tipo = 'retiro'""",[IDCaja, desde, hasta])
            ventas.depositos = depos.total!=null?depos.total:0.0
            ventas.retiros   = retiros.total!=null?retiros.total:0.0
            if(ventas.total == null) { ventas = 0 }
            salida = ventas
			
			
        } catch(e) { println "[Error]"; Consola.error("[Error: ${e.message}]",e); throw new Exception("Error al consultar la suma de ventas")}
        return salida
    }

    static def addCorteDual = { IDCaja, IDAlmacen, subtotal,subtotalDual, impuestos,impuestoDual, descuentos,descuentoDual,total, totalDual, nVentas, desde, hasta , depositos , retiros ->
        def db
        try {
            db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
            try {
				def folios  = [ini:0,fin:0]  
				folios.ini  = db.firstRow('SELECT min(folio) as fol FROM ventas WHERE fecha_hora >= ? and fecha_hora <= ? and id_caja = ? and id_almacen = ?', [desde, hasta, IDCaja, IDAlmacen]).fol
				folios.fin  = db.firstRow('SELECT max(folio) as fol FROM ventas WHERE fecha_hora >= ? and fecha_hora <= ? and id_caja = ? and id_almacen = ?', [desde, hasta, IDCaja, IDAlmacen]).fol				
				
                db.connection.autoCommit = false
                def IDCorte = db.executeInsert("INSERT INTO cortes SET subtotal = ?, impuestos = ?, descuentos = ?, total = ? "+
                                              ", n_ventas = ?, desde = ?, hasta = ?, id_caja = ?, id_almacen = ?, depositos = ?, retiros = ?, folio_inicial = ?, folio_final = ?"
                							  , [subtotal, impuestos, descuentos, total, nVentas, desde, hasta, IDCaja, IDAlmacen , depositos , retiros, folios.ini, folios.fin])
                def i = 0
                def IDCorteDual = db.executeInsert("INSERT INTO cortes_dual SET subtotal = ?, impuestos = ?, descuentos = ?, total = ? "+
                                              ", n_ventas = ?, desde = ?, hasta = ?, id_caja = ?, id_almacen = ?, depositos = ?, retiros = ?, folio_inicial = ?, folio_final = ?"
                							  , [subtotalDual, impuestoDual, descuentoDual,totalDual,i,desde,hasta,IDCaja, IDAlmacen,i,i, folios.ini, folios.fin])
                IDCorte = IDCorte[0][0]
                IDCorteDual = IDCorteDual[0][0]
                db.commit()
                return [IDCorte:IDCorte,IDCorteDual:IDCorteDual,mensaje:"Corte hecho."]
            } catch(Exception e) {
                db.rollback()
                if(e.message.contains("Duplicate entry")) { return "Corte que intenta capturar ya exíste" }
                Consola.error("[Excepcion al addCorte:${e.message}]", e)
                throw new Exception("Error al enviar a la base de datos. Corte no se registró.")
            } finally {
                db.close()
            }
        } catch(e) { Consola.error("[Error addCorte:${e.message}]", e); throw new Exception("Error en la conexión del servidor con su base de datos") }
    }



    static def getSumaCorteSucursalDual = { IDAlmacen, IDCorteSucursal ->
        def salida = ""
        try {
            def db       = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
            def corteSuc = db.firstRow("SELECT desde, hasta FROM cortes_sucursal WHERE id_corte = ? AND id_almacen = ?", [IDCorteSucursal, IDAlmacen])

            if(corteSuc==null) { throw new Exception("Error al consultar la tabla cortes_sucursal, resultado de la consulta: "+corteSuc.inspect()) }

            salida       = db.firstRow("SELECT sum(subtotal) as subtotal, sum(impuestos) as impuestos, sum(descuentos) as descuentos, sum(total) as total,"+
            "sum(depositos) as depositos, sum(retiros)as retiros, sum(n_ventas) as n_ventas FROM cortes_dual WHERE id_almacen = ? AND desde >= ? AND hasta <= ?", [IDAlmacen, corteSuc.desde, corteSuc.hasta])
            db.close()
        } catch(e) { Consola.error("Error al consultar corte de sucursal", e); throw new Exception("Error al consultar corte de sucursal") }
        salida
    }

    static def getCorteWhereFrom = { where,tabla ->
        def salida = ""
        try {
        def db   = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        String query= "SELECT * FROM $tabla as cortes WHERE "+where

        def corte= db.firstRow(query)
            db.close()
            salida = corte
        } catch(e) { Consola.error("Error al consultar corte", e); throw new Exception("Error al consultar corte") }
        salida
    }


}

