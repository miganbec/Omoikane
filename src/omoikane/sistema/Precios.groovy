/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.sistema

import groovy.sql.*
/**
 *
 * @author Octavio
 */
class Precios {
    static def modificar(Map cambios, db, IDAlmacen, IDArticulo) {
        cambios.each { clv, val ->
            try {
                db.executeUpdate("UPDATE precios SET $clv = ? WHERE id_almacen = ? AND id_articulo = ?", [val,IDAlmacen,IDArticulo])
            } catch(Exception e) {
                throw e
            }
        }

    }
}

