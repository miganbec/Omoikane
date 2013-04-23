/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.nadesicoiLegacy

class Existencias
{

    /** Cambia las existencias de un artículo en determinado almacén
     *  @param id_almacen  (int) El id del almacén al que se le modificarán existencias
     *  @param id_articulo (int) El id del artículo al que se le modificarán las existencias
     *  @param val         Cadena que especifíca que cambio se quiere hacer, hay 3 opciones: agregar, quitar o establecer
     *                      ejemplos (respectivamente) "+3", "-5", "2"
     *  */
    static def cambiar(db, id_almacen, id_articulo, val)
    {
        try {
            try {
                def op   = val
                op       = val.replace('+', 'cantidad+')
                op       = op.replace('-', 'cantidad-')
                def IDMov = db.executeUpdate("UPDATE existencias SET cantidad = $op WHERE id_almacen = ? AND id_articulo = ?"
                    , [
                      id_almacen,
                      id_articulo
                    ])
                if(IDMov == 0) { throw new Exception("No existe la tupla id_articulo-id_almacen especificada. ($id_almacen,$id_articulo)"); }
            } catch(Exception e) {
                throw e;
            } 
        } catch(Exception e) { throw e }
    }

}