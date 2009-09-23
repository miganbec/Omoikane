
 /* Author Phesus        //////////////////////////////
 *  ORC,ACR             /////////////
 *                     /////////////
 *                    /////////////
 *                   /////////////
 * //////////////////////////////                   */

package omoikane.sistema

import groovy.sql.*;

class Articulo
{
    def articulo;
    def where;
    def cargado = false;

    Articulo (where) { cargar(where); }

    def propertyMissing(nombre, args) 
    {
        if(cargado) { return articulo."$nombre" }
        else { return null; }
    }

    def cargar(where)
    {   if(where=~/select/) {
            cargarConQuery(where)
        } else {
            cargarConQuery("SELECT * FROM articulos WHERE " + where)
        }
    }

    def cargarConQuery(query) {
        def serv = Nadesico.conectar();
        def a = serv.getArticuloAlmacen(query);
        cargado  = a[1]
        articulo = a[0]
        serv.desconectar();
        return a[0]
    }

    static double precio(id,IDAlmacen){
            def serv = Nadesico.conectar();
            def a = serv.getPrecio(id,IDAlmacen,1);
            serv.desconectar();
            return a.total
        }
}