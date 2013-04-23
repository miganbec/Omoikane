/*  
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.nadesicoiLegacy

import java.sql.*;


import groovy.sql.*;

/**
 *
 * @author Usuario
 */


class ArticulosFunciones {
	static def asignarA(serv) {
        serv.selectArticuloCompleto = selectArticuloCompleto
        serv.getArticuloWhere       = getArticuloWhere
        serv.codigo2Articulo        = codigo2Articulo
        serv.getArticulo            = getArticulo
        serv.addArticulo            = addArticulo
        serv.modArticulo            = modArticulo
        serv.getAnotacion           = getAnotacion
        serv.addAnotacion           = addAnotacion
        serv.modAnotacion           = modAnotacion
        serv.modPrecio              = modPrecio
        serv.conexionMySQL          = conexionMySQL
        serv.ejecutaQuery           = ejecutaQuery
        serv.getFromResultSet       = getFromResultSet
        serv.getRowCount            = getRowCount
        serv.desconectarMySQL       = desconectarMySQL
        serv.getRow                 = getRow
        serv.getPrecio              = getPrecio
    }
    static def selectArticuloCompleto  = { IDAlmacen, select, selCampos = [] ->
        def salida = ""
        try {
            def db       = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
            def query    = select
            def articulo = db.firstRow(query, selCampos)
            if(articulo==null) { return null }
            def idArt    = articulo.id_articulo
            def existencia = db.firstRow("SELECT * FROM existencias WHERE id_almacen = $IDAlmacen AND id_articulo = " + idArt)
            def precio     = db.firstRow("SELECT * FROM precios     WHERE id_almacen = $IDAlmacen AND id_articulo = " + idArt)
            db.close()
            salida         = articulo + existencia + precio
            salida.precio  = ArticulosFunciones.getPrecio(idArt, IDAlmacen, 1) // 1 = público en general
        } catch(e) { e.printStackTrace(); throw new Exception("Error al obtener artículo en nadesico: ${e.message}", e) }
        salida
    }
    static def getArticuloWhere= { IDAlmacen, where ->
        nadesicoi.ArticulosFunciones.selectArticuloCompleto(IDAlmacen, "SELECT * FROM articulos WHERE " + where)
    }
    /** Prueba */
    static def codigo2Articulo = { IDAlmacen, codigo ->
        try {
			println "aquí"
            //return nadesicoi.ArticulosFunciones.getArticuloWhere(IDAlmacen, "codigo = \"$codigo\"")
            def ret = nadesicoi.ArticulosFunciones.selectArticuloCompleto(IDAlmacen,
                "select a.* from ramcachearticulos as a, ramcachecodigos as b where (a.codigo = ? or b.codigo = ?) " +
                "and a.id_articulo = b.id_articulo", [codigo,codigo])
			return ret
        } catch(e) {
            Consola.error("Error al obtener un artículo dado un código: ${e.message}", e)
            throw new Exception("Error al obtener un artículo dado un código: ${e.message}", e)
        }
    }
    static def getArticulo = { ID, IDAlmacen ->
        try {
            return nadesicoi.ArticulosFunciones.getArticuloWhere(IDAlmacen, "id_articulo = $ID")
        } catch(e) { 
            Consola.error("Error al obtener artículo por ID en nadesico", e)
            throw new Exception("Error al obtener artículo por ID en nadesico",e)
        }
    }
    static def getPrecio = { IDArt, IDAlmacen, IDCli ->
        def db
        try {
            //!!!!Aquí­ la conexión lleva un argumento extra!!! useOldAliasMetadataBehavior=true
            db       = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=&useOldAliasMetadataBehavior=true", "root", "", "com.mysql.jdbc.Driver")
            return getPrecioConConexion(db , IDArt, IDAlmacen,IDCli)
        } catch(e) { Consola.error("[Excepción al obtener precio: ${e.message}]",e); throw new Exception("Error al obtener precio",e) 
        } finally { if(db!=null) { db.close() } }

    }
    static def addArticulo = { IDAlmacen, IDLinea, IDGrupo, codigo, descripcion, unidad, impuestos, costo, descuento, utilidad, existencias ->
        def db
        try {
            db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
            try {
                db.connection.autoCommit = false
                def IDArticulo = db.executeInsert("INSERT INTO articulos SET codigo = ?, id_linea = ?, id_grupo = ?, descripcion = ?, unidad = ? , impuestos = ? ", [codigo, IDLinea, IDGrupo, descripcion, unidad, impuestos])
                IDArticulo = IDArticulo[0][0]
                db.executeInsert("INSERT INTO precios SET id_almacen = ?, id_articulo = ?, costo = ?, descuento = ?, utilidad = ?", [IDAlmacen, IDArticulo, costo, descuento, utilidad])
                db.executeInsert("INSERT INTO existencias SET id_almacen = ?, id_articulo = ?, cantidad = ?", [IDAlmacen, IDArticulo, existencias])
                db.commit()
                return [mensaje:"Artículo $descripcion agregado.",ID:IDArticulo]
            } catch(Exception e) {
                db.rollback()
                if(e.message.contains("Duplicate entry")) { return "El artículo que intenta capturar ya exíste o hay un producto con el mismo código" }
                println "[Excepcion:$e]"
                Consola.error("Error al enviar a la base de datos. El artículo no se registró", e)
                throw new Exception("Error al enviar a la base de datos. El artículo no se registró.")
            } finally {
                db.close()
            }
        } catch(e) {
            Consola.error("Error en la conexión del servidor con su base de datos", e)
            throw new Exception("Error en la conexión del servidor con su base de datos")
        }
    }
    static def modArticulo = { IDAlmacen, IDArticulo, codigo, IDLinea, IDGrupo, descripcion, unidad, impuestos, costo, utilidad, descuento ->
        def db   = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        try {
          db.connection.autoCommit = false
          db.executeUpdate("UPDATE articulos SET codigo = ?, id_linea = ?, id_grupo = ?, descripcion = ?, unidad = ?, impuestos = ? WHERE id_articulo = ?"
                           , [codigo, IDLinea, IDGrupo, descripcion, unidad, impuestos, IDArticulo])

          ArticulosFunciones.modPrecio(IDAlmacen, IDArticulo, costo:costo as Double, utilidad:utilidad  as Double, descuento:descuento as Double)
          db.commit()
          return "Artículo modificado existosamente"
        } catch(Exception e) {
            db.rollback()
            println "[Excepcion:$e]"
            Consola.error("Error al modificar artículo", e)
            throw new Exception ("Error al modificar artículo")
        } finally {
            db.close()
        }
    }
    static def modPrecio = { Map cambios, IDAlmacen, IDArticulo ->
        def db   = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        db.connection.autoCommit = false
        try {
            cambios.each { clv, val ->
                try {
                    db.executeUpdate("UPDATE precios SET $clv = ? WHERE id_almacen = ? AND id_articulo = ?", [val,IDAlmacen,IDArticulo])
                } catch(Exception e) {
                    throw e
                }
            }
            db.commit()
            return "Precio modificado exitosamente"
        } catch(Exception e) {
            db.rollback()
            println "[Excepcion:$e]"
            Consola.error("Error al cambiar precio", e)
            throw new Exception("Error al cambiar precio")
        } finally {
            db.close()
        }
    }

    static def getAnotacion = {id_almacen, id_articulo ->
        def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        def notas = db.firstRow("SELECT * FROM anotaciones WHERE id_almacen = ? AND id_articulo = ? ", [id_almacen, id_articulo])
        if (notas != null) {
            return notas.texto
        }
        else {
            return "<Escriba su anotación aquí>"
        }
    }

    static def addAnotacion = {id_almacen, id_articulo, texto ->
        def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        db.connection.autoCommit = false
        db.executeInsert("INSERT INTO anotaciones (id_almacen, id_articulo, texto) VALUES(?, ?, ?)", [id_almacen, id_articulo, texto])
        db.commit()
    }

    static def modAnotacion = {id_almacen, id_articulo, texto ->
        def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        db.connection.autoCommit = false
        if ( db.firstRow("SELECT * FROM anotaciones WHERE id_almacen = ? AND id_articulo = ? ", [id_almacen, id_articulo]) != null )
            db.executeUpdate("UPDATE anotaciones SET texto = ? WHERE id_almacen = ? AND id_articulo = ?", [texto, id_almacen, id_articulo])
        else
            db.executeInsert("INSERT INTO anotaciones (id_almacen, id_articulo, texto) VALUES(?, ?, ?)", [id_almacen, id_articulo, texto])
        db.commit()
            // addAnotacion(id_almacen, id_articulo, texto)
    }

    static def conexionMySQL = { UID ->
        if(NadesicoLegacy.conexs.containsKey(UID)) {
            println "conectando a mysql vía nadesicotablemodel :P"
            try {
                def protocol = "jdbc:mysql://localhost/omoikane?user=root&password=&useOldAliasMetadataBehavior=true";

                def conn = DriverManager.getConnection(protocol);
                conn.setAutoCommit(false);
                def UIDBD = Util.generarUID()
                NadesicoLegacy.conexs[UID][UIDBD] = conn
                return UIDBD
            } catch(Exception e) {
                Consola.error("Error al conectar a MySQL", e)
                throw new Exception("Error al conectar a MySQL", e);
            }
        } else {
            Consola.error("Error: Sesión no iniciada en el servidor", e)
            throw new Exception("Sesión no iniciada en el servidor")
        }
    }

    static def limpiarCache = { UID , UIDBD ->
        if(NadesicoLegacy.conexs.containsKey(UID)) {
            try {
                def tempBD = NadesicoLegacy.conexs[UID][UIDBD]
                NadesicoLegacy.conexs.remove(UID)
                NadesicoLegacy.conexs[UID] = [:]
                NadesicoLegacy.conexs[UID][UIDBD] = tempBD
                System.gc()
            } catch(Exception e) {
                Consola.error("Error al conectar a MySQL (limpiarCache)", e)
                throw new Exception("Error al conectar a MySQL: ${e.message}", e);
            }
        } else {
            Consola.error("Error: Sesión no iniciada en el servidor (limpiarCache)", e)
            throw new Exception("Sesión no iniciada en el servidor")
        }
    }

    static def ejecutaQuery = { UID, UIDBD, query ->
        if(NadesicoLegacy.conexs.containsKey(UID)) {
            try {
                if(!NadesicoLegacy.conexs[UID].containsKey(UIDBD)) {
                    Consola.error("Error no se inició la conexión a MySQL en el servidor (ejecutaQuery)", e)
                    throw new Exception("No se inició la conexión a MySQL en el servidor")
                }
                Statement control  = NadesicoLegacy.conexs[UID][UIDBD].createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY); //ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY
                ResultSet rs       = control.executeQuery(query);
                def UIDRS          = Util.generarUID()
                NadesicoLegacy.conexs[UID][UIDRS] = rs
                return UIDRS

            } catch(Exception e) {
                Consola.error("Error al conectar a MySQL (ejecutaQuery)", e)
                throw new Exception("Error al conectar a MySQL: ${e.message}", e);
            }
        } else {
            Consola.error("Error: Sesión no iniciada en el servidor (ejecutaQuery)", e)
            throw new Exception("Sesión no iniciada en el servidor")
        }
    }

    static def getFromResultSet = { UID, UIDRS, row, col ->
        if(!NadesicoLegacy.conexs.containsKey(UID)) {
            Consola.error("Sesión no iniciada en el servidor (getFromResultSet)", e)
            throw new Exception("Sesión no iniciada en el servidor")
        }
        if(!NadesicoLegacy.conexs[UID].containsKey(UIDRS)) {
            Consola.error("No se inició la conexión a MySQL en el servidor (getFromResultSet)", e)
            throw new Exception("No se inició la conexión a MySQL en el servidor")
        }
        try {
            def resultSet = NadesicoLegacy.conexs[UID][UIDRS];
            int rowNdx = row + 1
            int colNdx = col + 1
                resultSet.absolute(rowNdx)
                return resultSet.getObject(colNdx); //Para saltar el ID y no mostrarlo println 14
		} catch (e) {
                println "[Error: ${e.message}]"
                Consola.error("Error obteniendo valor en una celda -de la BD- (getFromResultSet)", e)
                throw new Exception("Error obteniendo valor en la celda: " + row + ", " + col);
        }
    }

    static def desconectarMySQL = { UID, UIDBD ->
        if(NadesicoLegacy.conexs.containsKey(UID)) {
            try {
                if(!NadesicoLegacy.conexs[UID].containsKey(UIDBD)) {
                    Consola.error("Error no se inició la conexión a MySQL en el servidor (desconectarMySQL)", e)
                    throw new Exception("No se inició la conexión a MySQL en el servidor")
                }
                NadesicoLegacy.conexs[UID][UIDBD].close()
            } catch(Exception e) {
                Consola.error("Error al desconectar MySQL (desconectarMySQL)", e)
                throw new Exception("Error al desconectar MySQL", e);
            } finally {
				NadesicoLegacy.conexs.remove(UID)
                                System.gc()
			}
        } else {
            Consola.error("No se inició sesión en el servidor (desconectarMySQL)", e)
            throw new Exception("Sesión no iniciada en el servidor")
        }
    }

    static def getRowCount = { UID, UIDRS ->
        if(!NadesicoLegacy.conexs.containsKey(UID)) {
            Consola.error("Sesión no iniciada en el servidor (getRowCount)", e)
            throw new Exception("Sesión no iniciada en el servidor")
        }
        if(!NadesicoLegacy.conexs[UID].containsKey(UIDRS)) {
            Consola.error("No se inicio la conexion a MySQL en el servidor (getRowCount)", e)
            throw new Exception("No se inició la conexión a MySQL en el servidor")
        }

        try {
            def resultSet = NadesicoLegacy.conexs[UID][UIDRS];
            resultSet.last();
            def rowCount = resultSet.getRow();
            return rowCount;
        } catch(e) {
            Consola.error("Error al obtener tamaño del resultSet (getRowCount)", e)
            throw new Exception("Error al obtener tamaño del resultset",e)
        }
    }

    static def getRow = { UID, UIDRS, row, nCols ->
        if(!NadesicoLegacy.conexs.containsKey(UID)) {
            Consola.error("Sesión no iniciada en el servidor (getRow)", e)
            throw new Exception("Sesión no iniciada en el servidor")
        }
        if(!NadesicoLegacy.conexs[UID].containsKey(UIDRS)) {
            Consola.error("No se inició la conexión a MySQL en el servidor (getRow)", e)
            throw new Exception("No se inició la conexión a MySQL en el servidor")
        }

        try {
            row++
            def salida       = [:], fila = []
            def resultSet    = NadesicoLegacy.conexs[UID][UIDRS];
            resultSet.last();
            def rowCount     = resultSet.getRow();
            def config       = NadesicoLegacy.config.cacheResultSet
                row         -= Integer.valueOf(config.@atras[0])   //3; Cachéa desde 3 posiciones atrás hasta 4 adelante y la que fue pedida, en total 8 posiciones
            def limite       = Integer.valueOf(config.@atras[0]) + Integer.valueOf(config.@adelante[0]) + 1
            def filaDat
            def meta         = resultSet.getMetaData()
            def catArts      = false //¿És este un catálogo de artículos particularmente?
            def CADataPrecio = [:]
            nCols            = meta.getColumnCount()-1

            for(i in 1..limite) {  // 1..8
                if(row<=0||row>rowCount) { row++; continue; }
                resultSet.absolute(row)
                fila = []
                (nCols+1).times {
                    filaDat = resultSet.getObject(it+1); //+1 Porqué comienza el conteo en 1 y no 0 como la matríz y +1 por saltarse el ID

                    if(meta.getColumnName(it+1) == "xIDPrecioCA") { catArts = true; CADataPrecio = [:] }
                    if(catArts) {
                        switch(meta.getColumnName(it+1)) {
                            case "xImpuestosCA": CADataPrecio['impuestos'] = resultSet.getObject(it+1); filaDat = null; break;
                            case "xCostoCA": CADataPrecio['costo'] = resultSet.getObject(it+1); filaDat = null; break;
                            case "xUtilidadCA": CADataPrecio['utilidad'] = resultSet.getObject(it+1); filaDat = null; break;
                            case "xDescuentoCA": CADataPrecio['precioDescuento'] = resultSet.getObject(it+1); filaDat = null; break;
                            case "xLineaDescuentoCA": CADataPrecio['lineaDescuento'] = resultSet.getObject(it+1); filaDat = null; break;
                            case "xClienteDescuentoCA": CADataPrecio['clienteDescuento'] = resultSet.getObject(it+1); filaDat = null; break;
                            case "xGrupoDescuentoCA": CADataPrecio['grupoDescuento'] = resultSet.getObject(it+1);
                                filaDat = null
                                fila[6] = ArticulosFunciones.getPrecioConDatos(CADataPrecio).total
                            break;
                        }
                        /*println "***Precio:"+ArticulosFunciones.getPrecioConDatos(
                            [costo:,clienteDescuento:,lineaDescuento:,precioDescuento:,impuestos:]
                        ) */
                    }
                    if(filaDat != null) {
                        fila << filaDat
                    }
                }

                salida[row-1 as String] = fila
                row++
            }
            return salida
        } catch(e) { 
            println "[Error al extraer fila de resultset: ${e.message}]";
            Consola.error("Error al extraer fila de resultset (getRow): ${e.message}", e)
            throw new Exception("Error al extraer fila de resultset: ${e.message}", e)
        }
    }

    static def getPrecioConConexion(db , IDArt, IDAlmacen,IDCli){
        try{
            def datos    = db.firstRow("SELECT art.id_articulo id_articulo, art.impuestos impuestos, pr.costo costo, pr.descuento precioDescuento" + 
                                       ", pr.utilidad utilidad, lin.descuento lineaDescuento, cli.descuento clienteDescuento, gru.descuento grupoDescuento" +
                                       " FROM articulos as art, precios as pr, lineas as lin, clientes as cli, grupos as gru "+ 
                                       " WHERE art.id_articulo = ? AND gru.id_grupo = art.id_grupo AND " +
                                       "art.id_articulo=pr.id_articulo AND art.id_linea = lin.id_linea AND pr.id_almacen = ? AND cli.id_cliente = ?"
                                    , [IDArt, IDAlmacen, IDCli])
            return getPrecioConDatos(datos)
        } catch(e) { Consola.error("[Excepción al obtener precio: ${e.message}]",e); throw new Exception("Error al obtener precio",e)
        }
    }

    static def descontar(desc, cant) {
        cant-(cant*desc/100)
    }
    static def getPrecioConDatos(datos){
        try {
        def precio = [:]
        
        precio['costo']     = datos.costo
        precio['utilidad']  = (datos.costo*(datos.utilidad/100))
        precio['$']         = precio['costo'] + precio['utilidad']
        precio['descuCli%'] = datos.clienteDescuento
        precio['descuLin%'] = datos.lineaDescuento
        precio['descuArt%'] = datos.precioDescuento
        precio['descuGru%'] = datos.grupoDescuento
        precio['descuento%']= 100-ArticulosFunciones.descontar(datos.precioDescuento, 
                                                               ArticulosFunciones.descontar(datos.lineaDescuento,
                                                                   ArticulosFunciones.descontar(datos.grupoDescuento,
                                                                        ArticulosFunciones.descontar(datos.clienteDescuento,100))))


        precio['subtotal']           = precio['$']
        precio['impuestos$']         = precio['subtotal'] * (datos.impuestos/100)
        precio['impuestos%']         = datos.impuestos
        precio['PrecioConImpuestos'] = (precio['subtotal'] + precio['impuestos$'])
        precio['descuento$']         = (precio['descuento%']/100) * precio['PrecioConImpuestos']
        precio['$rebajado']          = precio['PrecioConImpuestos'] - precio['descuento$']
        precio['total']              = precio['$rebajado']
        precio.each { k,v ->
            precio[k] = Util.redondea(v)
        }
        return precio
    } catch(e) { Consola.error("[Excepción al obtener precio: ${e.message}]",e); throw new Exception("Error al obtener precio",e)}
    }
}
