/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.nadesicoiLegacy

import groovy.sql.*;

/**
 *
 * @author OCTAVIOOO!!
 */
class Cortes {
    static def asignarA(serv) {
        serv.getCorteWhere        = getCorteWhere
        serv.getCorte             = getCorte
        serv.addCorte             = addCorte
        serv.getSumaCorteSucursal = getSumaCorteSucursal
        serv.getCorteSucursal     = getCorteSucursal
    }
    static def addCorte = { IDCaja, IDAlmacen, subtotal, impuestos, descuentos, total, nVentas, desde, hasta , depositos , retiros->
        def db
        try {
            db = Db.connect()
            try {
				def folios  = [ini:0,fin:0]  
				folios.ini  = db.firstRow('SELECT min(folio) as fol FROM ventas WHERE fecha_hora >= ? and fecha_hora <= ? and id_caja = ? and id_almacen = ?', [desde, hasta, IDCaja, IDAlmacen]).fol
				folios.fin  = db.firstRow('SELECT max(folio) as fol FROM ventas WHERE fecha_hora >= ? and fecha_hora <= ? and id_caja = ? and id_almacen = ?', [desde, hasta, IDCaja, IDAlmacen]).fol
				
                db.connection.autoCommit = false
                def IDCorte = db.executeInsert("INSERT INTO cortes SET subtotal = ?, impuestos = ?, descuentos = ?, total = ? "+
                                              ", n_ventas = ?, desde = ?, hasta = ?, id_caja = ?, id_almacen = ?, depositos = ?, retiros = ?, folio_inicial = ?, folio_final = ?"
                							  , [subtotal, impuestos, descuentos, total, nVentas, desde, hasta, IDCaja, IDAlmacen , depositos , retiros, folios.ini, folios.fin])
                IDCorte = IDCorte[0][0]
                db.commit()
                return [IDCorte:IDCorte,mensaje:"Corte hecho."]
            } catch(Exception e) {
                //db.rollback()
                if(e.message.contains("Duplicate entry")) { return "Corte que intenta capturar ya exíste" }
                Consola.error("[Excepcion al addCorte:${e.message}]", e)
                throw new Exception("Error al enviar a la base de datos. Corte no se registró.")
            } finally {
                db.close()
            }
        } catch(e) { Consola.error("[Err addCorte:${e.message}]", e); throw new Exception("Error en la conexión del servidor con su base de datos") }
    }
    static def getCorteWhere = { where ->
        def salida = ""
        try {
        def db   = Db.connect()
        def where2 = "id_caja = 1 AND desde = '2009-01-08 23:35:35' AND hasta = '2009-01-10 09:22:48'"
        String query= "SELECT * FROM cortes WHERE "+where

        def corte= db.firstRow(query)
            db.close()
            salida = corte
        } catch(e) { Consola.error("Error al consultar corte", e); throw new Exception("Error al consultar corte") }
        salida
    }
    static def getCorte = { id ->
        getCorteWhere(" cortes.id_corte = "+id)
    }

    static def getCorteSucursal = { IDAlmacen, IDCorteSucursal ->
        def corteSuc = ""
        try {
            def db       = Db.connect()
            corteSuc = db.firstRow("SELECT desde, hasta FROM cortes_sucursal WHERE id_corte = ? AND id_almacen = ?", [IDCorteSucursal, IDAlmacen])

            if(corteSuc==null) { throw new Exception("Error al consultar la tabla cortes_sucursal, resultado de la consulta: "+corteSuc.inspect()) }
        } catch(e) { Consola.error("Error al consultar corte de sucursal", e); throw new Exception("Error al consultar corte de sucursal") }
        corteSuc
    }

    static def getSumaCorteSucursal = { IDAlmacen, IDCorteSucursal ->
        def salida = ""
        try {
            def db       = Db.connect()
            def corteSuc = db.firstRow("SELECT desde, hasta FROM cortes_sucursal WHERE id_corte = ? AND id_almacen = ?", [IDCorteSucursal, IDAlmacen])
            
            if(corteSuc==null) { throw new Exception("Error al consultar la tabla cortes_sucursal, resultado de la consulta: "+corteSuc.inspect()) }

            salida       = db.firstRow("SELECT sum(subtotal) as subtotal, sum(impuestos) as impuestos, sum(descuentos) as descuentos, sum(total) as total, sum(depositos) as depositos, sum(retiros) as retiros, sum(n_ventas) as n_ventas FROM cortes "+
                                       "WHERE id_almacen = ? AND desde >= ? AND hasta <= ?", [IDAlmacen, corteSuc.desde, corteSuc.hasta])

            db.close()
        } catch(e) { Consola.error("Error al consultar corte de sucursal", e); throw new Exception("Error al consultar corte de sucursal") }
        salida
    }
}

