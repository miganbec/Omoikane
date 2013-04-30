/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.nadesicoiLegacy

class Existencias
{

    /** Cambia las existencias de un artículo en determinado almacén
     *  @param id_almacen  (int) El id del almacén al que se le modificarán existencias, 1 para tienda y 2 para bodega
     *  @param id_articulo (int) El id del artículo al que se le modificarán las existencias
     *  @param val         Cadena que especifíca que cambio se quiere hacer, hay 3 opciones: agregar, quitar o establecer
     *                      ejemplos (respectivamente) "+3", "-5", "2"
     *  */
    static def cambiar(db, id_almacen, id_articulo, val)
    {
        try {
            try {

                def almacenes = [1: "enTienda", 2: "enBodega"]
                def almacen = almacenes[id_almacen]
                def op   = val
                op       = val.replace('+', almacen+'+')
                op       = op.replace('-', almacen+'-')

                def IDMov = db.executeUpdate("UPDATE Stock SET ${almacen} = $op WHERE idArticulo = ?"
                    , [
                      id_articulo
                    ])
                if(IDMov == 0) { throw new Exception("Error actualizando stock. (Almacén: $id_almacen, Producto: $id_articulo)"); }
            } catch(Exception e) {
                throw e;
            } 
        } catch(Exception e) { throw e }
    }

}