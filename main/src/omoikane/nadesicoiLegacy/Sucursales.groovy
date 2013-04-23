/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.nadesicoiLegacy


import groovy.sql.*;

/**
 *
 * @author Usuario
 */
class Sucursales {
	static def asignarA(serv) {
        serv.sucursalExiste           = sucursalExiste
        serv.abrirSucursal            = abrirSucursal
        serv.sucursalAbierta          = sucursalAbierta
        serv.cerrarSucursal           = cerrarSucursal
        serv.sucursalCortePendiente   = sucursalCortePendiente
        serv.getCajasSucursalCerradas = getCajasSucursalCerradas
        serv.getCajasSucursal         = getCajasSucursal
        serv.corteSucursal            = corteSucursal
    }
    static def corteSucursal = { IDAlmacen ->
        def db        = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        def resultado = db.firstRow("SELECT * FROM sucursales WHERE id_almacen = ?", [IDAlmacen])
        resultado     = db.executeInsert("INSERT INTO cortes_sucursal SET id_almacen = ?, desde = ?, hasta = ?",
                                         [IDAlmacen, resultado.hAbierta, resultado.hCerrada])
        db.close()
        resultado[0][0]
    }
    static def abrirSucursal = { IDAlmacen ->
        def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        def resultado = db.executeUpdate("UPDATE sucursales SET abierta = ?, hAbierta = CURRENT_TIMESTAMP WHERE id_almacen = ?", [1, IDAlmacen]);
        db.close()
        resultado
    }
    static def cerrarSucursal = { IDAlmacen ->
        def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        def resultado = db.executeUpdate("UPDATE sucursales SET abierta = ?, hCerrada = CURRENT_TIMESTAMP WHERE id_almacen = ?", [0, IDAlmacen]);
        db.close()
    }
    static def getCajasSucursalCerradas = { IDAlmacen ->
        def res = true
        this.getCajasSucursal(IDAlmacen).each {
            res = !it.abierta && res
        }
        res
    }

    static def getCajasSucursal = { IDAlmacen ->
        try {
            def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
            def resultado = db.rows("SELECT * FROM cajas WHERE id_almacen = ?", [IDAlmacen]);
            db.close()
            return resultado
        } catch(e) { def msg = "Error al abrir sucursal: ${e.message}"; Consola.error(msg, e); throw new Exception(msg) }
    }
    static def sucursalAbierta = { IDAlmacen ->
        try {
            def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
            def resultado = db.firstRow("SELECT abierta FROM sucursales, almacenes WHERE sucursales.id_almacen = almacenes.id_almacen AND sucursales.id_almacen = ?", [IDAlmacen])
            db.close()
            if(resultado != null && resultado.size()==1) { return resultado.abierta } else { return -1 }
        } catch(e) { def msg = "Error al abrir sucursal: ${e.message}"; Consola.error(msg, e); throw new Exception(msg) }
    }
    static def sucursalCortePendiente = { IDAlmacen ->
        try {
            def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
            def resultado = db.firstRow("SELECT hAbierta, hCerrada FROM sucursales WHERE id_almacen = ?", [IDAlmacen])
            resultado     = db.firstRow("SELECT desde, hasta FROM cortes_sucursal WHERE id_almacen = ? AND desde = ? AND hasta = ?",
                                [IDAlmacen, resultado.hAbierta, resultado.hCerrada])
            db.close()
            if(resultado != null && resultado.size()==1) { return true } else { return false }
        } catch(e) { def msg = "Error al determinar si existen cortes del día pendientes: ${e.message}"; Consola.error(msg, e); throw new Exception(msg) }
    }
    static def sucursalExiste  = { IDAlmacen ->
        try {
            def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
            def resultado = db.firstRow("SELECT abierta FROM sucursales, almacenes WHERE sucursales.id_almacen = almacenes.id_almacen AND sucursales.id_almacen = ?", [IDAlmacen])
        db.close()
        if(resultado != null && resultado.size()==1) { return true } else { return false }
        } catch(e) { Consola.error("Excepción en función sucursalExiste: ${e.message}", e) }
    }
}
