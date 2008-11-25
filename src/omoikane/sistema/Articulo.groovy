/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.sistema

import groovy.sql.*;

/**
 *
 * @author Octavio
 */
class Articulo
{
    def articulo;
    def where;
    def cargado = false;
    Articulo (where) { cargar(where); }

    def propertyMissing(nombre, args) { if(cargado) { return articulo."$nombre" } else { return null; } }
    def cargar(where)
    {   if(where=~/select/) {
            cargarConQuery(where)
        } else {
            cargarConQuery("SELECT * FROM articulos WHERE " + where)
        }
    }
    def cargarConQuery(query) {
        def db   = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        def art  = db.rows(query)

        db.close()
        articulo = art[0]
        if(articulo==null) { cargado = false; } else { cargado = true; }
        articulo
    }

}

