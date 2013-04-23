/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.nadesicoiLegacy

/**
 *
 * @author Phesus - RNB
 */
import groovy.sql.*;


class Facturas {

    static def asignarA(serv) {
        serv.getFactura = getFactura;
        serv.getArregloVentas = getArregloVentas;
        serv.getLastIDFactura = getLastIDFactura;
        serv.addFactura = addFactura;
        serv.getDetallesVenta = getDetallesVenta;
        serv.getTotalesVenta = getTotalesVenta;
        serv.cancelFactura = cancelFactura;
    }

    static def getArregloVentas = { idFactura ->
        def db = Db.connect()
        def detallesVentas = [];
        // Consola.aviso("getDetallesVenta accedido con idVenta = " + idVenta, "");
        db.eachRow("SELECT id_venta FROM ventas_facturadas WHERE id_factura = ?" , [idFactura] , {
                    detallesVentas << [
                       id_venta:it.id_venta
                    ];
                }
            );
        return detallesVentas;
    }

    static def getFactura = { idFactura ->
        def facturaCompleta;
        try {
            def db = Db.connect()
            def datosFactura = db.firstRow("SELECT * FROM facturas WHERE id_factura = ?", [idFactura] );
            def usuarioExpidio = db.firstRow("SELECT nombre FROM usuarios " +
                "WHERE id_usuario IN ( SELECT usuario_expidio FROM facturas " +
                "WHERE id_factura = ? )", [idFactura] );
            def usuarioCancelo;
            if(datosFactura.cancelada) {
               usuarioCancelo = db.firstRow("SELECT nombre FROM usuarios " +
                    "WHERE id_usuario IN ( SELECT usuario_cancelo FROM facturas " +
                    "WHERE id_factura = ? );", [idFactura] );
            }   
            else
                datosFactura.usuario_cancelo = "";
            def descAlmacen = db.firstRow("SELECT descripcion FROM almacenes " +
                "WHERE id_almacen IN ( SELECT id_almacen FROM facturas " +
                "WHERE id_factura = ? );", [idFactura] );
            def arregloVentasArticulos = [];

            // Consola.aviso("getFactura accedido con idFactura = " + idFactura, "");
            db.eachRow("SELECT id_venta FROM ventas_facturadas " +
                "WHERE id_factura = ? ;",[idFactura], { idVenta ->
                    def detallesVentas = [];
                    db.eachRow("SELECT id_venta, ventas_detalles.id_articulo, descripcion, precio, cantidad, total " +
                        "FROM ventas_detalles, articulos WHERE id_venta = ? " +
                        "AND ventas_detalles.id_articulo = articulos.id_articulo;", [idVenta.id_venta], {
                            detallesVentas << [
                                id_venta:it.id_venta,
                                id_articulo:it.id_articulo,
                                descripcion:it.descripcion,
                                precio:it.precio,
                                cantidad:it.cantidad,
                                total:it.total
                            ];
                            // Consola.aviso("Iteración de detallesVentas", detallesVentas.toString());
                        }
                    );
                    arregloVentasArticulos << detallesVentas;
                    // Consola.aviso("Fila de ventas_facturadas " + idVenta.id_venta.toString(), arregloVentasArticulos.toString());
                }
            );
           
           if(datosFactura.cancelada) // Factura cancelada (incluye usuario que canceló)
               facturaCompleta = [
                    factura:datosFactura,
                    expidio:usuarioExpidio,
                    cancelo:usuarioCancelo,
                    almacen:descAlmacen,
                    articulos:arregloVentasArticulos
               ];
            else    // Factura sin cancelar
               facturaCompleta = [
                    factura:datosFactura,
                    expidio:usuarioExpidio,
                    almacen:descAlmacen,
                    articulos:arregloVentasArticulos
                ];
            // Consola.aviso("Factura completa enviada",facturaCompleta.toString());

        } catch(e) {
            Consola.error("[Error: ${e.message}]",e);
            throw new Exception("Error al consultar la factura");
        }
        return facturaCompleta;
    }

    static def getDetallesVenta = { idVenta ->
        def db = Db.connect()
        def detallesVentas = [];
        // Consola.aviso("getDetallesVenta accedido con idVenta = " + idVenta, "");
        db.eachRow("SELECT id_venta, ventas_detalles.id_articulo, descripcion, precio, cantidad, total " +
            "FROM ventas_detalles, articulos WHERE id_venta = ? " +
                "AND ventas_detalles.id_articulo = articulos.id_articulo " +
                "AND id_venta NOT IN " +
                "(SELECT id_venta FROM ventas WHERE facturada = 1);", [idVenta], {
                    detallesVentas << [
                       id_venta:it.id_venta,
                       id_articulo:it.id_articulo,
                       descripcion:it.descripcion,
                       precio:it.precio,
                       cantidad:it.cantidad,
                       total:it.total
                    ];
                }
            );
        return detallesVentas;
    }

    static def getTotalesVenta = { idVenta ->
        def db = Db.connect()
        def totales = db.firstRow("SELECT subtotal, impuestos, total FROM ventas " +
            "WHERE id_venta = ? ;",[idVenta]);
        return totales;
    }

    //TODO: Obtener descripcion de detalles de venta no facturadas

    static def getLastIDFactura = {
        def folioFactura;
        try {
            def db = Db.connect()
            def resultadoFilas = db.firstRow("SELECT COUNT(*) FROM facturas;");
            if ( resultadoFilas[0] == 0 )
                folioFactura = "0";
            else {
                resultadoFilas = db.firstRow("SELECT MAX(id_factura) FROM facturas;");
                folioFactura = resultadoFilas[0];
            }
            db.close();
        } catch(e) {
            Consola.error("[Error: ${e.message}]",e);
            throw new Exception("Error al consultar las facturas");
        }
        return folioFactura;
    }

    static def addFactura = { factura, ventas -> // Recibe un mapa con los datos de la factura y un arreglo con ids de ventas
        def db = Db.connect()
        try {
            db.connection.autoCommit = false;
            def usuario_exp = db.firstRow("SELECT id_usuario FROM usuarios " +
                "WHERE nombre = ? ;",[factura.expidio]);
            // Consola.aviso("Cargando factura nueva expedida por: $usuario_exp : $factura.expidio - $factura.fecha", factura.toString());
            db.executeInsert("INSERT INTO facturas ( id_factura, id_cliente, " +
                "fecha, usuario_expidio, subtotal, impuestos, total ) " +
                "VALUES ( ?, ?, ?, ?, ?, ?, ? );",
                [ factura.id_factura, factura.cliente,
                    factura.fecha, usuario_exp.id_usuario, factura.subtotal,
                    factura.impuestos, factura.total ]
            );
            ventas.each {
                db.executeInsert("INSERT INTO ventas_facturadas ( id_factura, id_venta ) " +
                    "VALUES ( ?, ? );", [factura.id_factura, it]);
                db.executeUpdate("UPDATE ventas SET facturada = 1, id_cliente = ? " +
                    "WHERE id_venta = ?;", [factura.id_clente, it] );
            }
            db.commit();
            return "Datos de facturacion modificados existosamente";
        } catch(Exception e) {
            db.rollback();
            Consola.error("Excepcion al modificar los datos de facturacion",e);
            throw new Exception ("Error al modificar los datos de facturacion");
        } finally {
            db.close();
        }
    }

    static def cancelFactura = { factura, ventas -> // Recibe un mapa con los datos de la factura y un arreglo con ids de ventas
        def db = Db.connect()
        try {
            db.connection.autoCommit = false;
            def usuario_c = db.firstRow("SELECT id_usuario FROM usuarios " +
                "WHERE nombre = ? ;", [factura.cancelo]);
            // Cancelación de factura
            db.executeUpdate("UPDATE facturas SET cancelada = 1, usuario_cancelo = ? " +
                "WHERE id_factura = ? ;", [usuario_c.id_usuario, factura.id_factura]);
            // Se elimina la asociación factura-venta. CORRECCIÓN: Permanece la asociación por las facturas canceladas
                // db.executeUpdate("DELETE FROM ventas_facturadas WHERE id_factura = ? ;",[factura.id_factura]);
            // Se actualizan las ventas como no facturadas
            ventas.each {
                db.executeUpdate("UPDATE ventas SET facturada = 0 WHERE id_venta = ? ;", [it]);
            }
            db.commit();
            return "Datos de facturacion modificados existosamente";
        } catch(Exception e) {
            db.rollback();
            Consola.error("Excepcion al modificar los datos de facturacion para ventas",e);
            throw new Exception ("Error al modificar los datos de facturacion para ventas");
        } finally {
            db.close();
        }

    }


}

