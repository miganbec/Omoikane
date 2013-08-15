/* Author Phesus        //////////////////////////////
 *  ORC,ACR             /////////////
 *                     /////////////
 *                    /////////////
 *                   /////////////
 * //////////////////////////////
 */

package omoikane.sistema

import java.sql.*;
import java.util.*;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.*;
import java.awt.event.*;
import omoikane.principal.*;
import omoikane.sistema.*;

public class ScrollableTableModel extends AbstractTableModel {
		java.util.List	colNames		= null;
		int             rowCount        = -1;
		java.util.List  colClasses      = null;
        String          queryAct        = null;
        def 			conn
        Statement 		control
        ResultSet 		rs
        java.util.Hashtable<String,java.util.List> cacheFila = new Hashtable();

    public ScrollableTableModel(java.util.List colNames, ArrayList colClases)
    {
        def protocol = omoikane.principal.Principal.URLMySQL +
                        "?user="     + omoikane.principal.Principal.loginJasper +
                        "&password=" + omoikane.principal.Principal.passJasper  +
                        "&useOldAliasMetadataBehavior=true&useCompression=true";
        conn = DriverManager.getConnection(protocol);
        conn.setAutoCommit(false);

        this.colNames   = new ArrayList(colNames);
        this.colClasses = colClases;

        control   = conn.createStatement();
    }
    public void refrescar() {
        setQuery(queryAct)
    }
    public void setQuery(String query) {
        try
        {
            queryAct  = query
            cacheFila = new Hashtable()

            //rs        = control.executeQuery(query);

            resetData();
            fireTableDataChanged();

        }
        catch (Exception e) { omoikane.sistema.Dialogos.lanzarDialogoError(null, "Error al conectar a MySQL", omoikane.sistema.Herramientas.getStackTraceString(e)); }
    }
    public void resetData() {
        this.rowCount = -1;
    }
	/**
	 * <p>Fills the <code>colNames</code> property from the
	 * <code>resultSet</code> if this property is null
	 * using the current <code>resultSet</code>.</p>
	 * @see ScrollableTableModel#fillColNames(ResultSet)
	 */
	/**
	 * @return The number of columns in this model.
	 */
	public int getColumnCount() {
		return colNames.size();
	}

	/**
	 * @return The number of rows in this model.
	 */
       public int getRowCount() {

            if(rowCount == -1) {
                //def tmpQ = queryAct?.toLowerCase()
                def tmpQ = queryAct;
                //println "->>"+tmpQ
                def replaced = tmpQ.replaceAll(/(?i)(select (distinct)|select) ([a-zA-Z0-9\._]+)(.*?) (from .*?)(group by.*|$)(order by.*|$)/, 'SELECT COUNT(*) FROM ($1 $2 $3 $5 $6) as t1')
                def stmt      = conn.createStatement()
                def res       = stmt.executeQuery(replaced);
                res.next()
                this.rowCount = res.getObject(1)
            }
            return this.rowCount

       }
	/**
	 * <p>Returns the value for the cell at columnIndex
	 * and rowIndex</p>
	 * @param rowIndex The row whose value is to be queried
	 * @param columnIndex The column whose value is to be queried
	 * @return The value Object at the specified cell
	 */
        public getRow( row, nCols ) {
			
			def timeTotal = 0
			def time1 = System.currentTimeMillis()
			
            row++
            def salida       = [:], fila = []
            def resultSet    = rs;
            //rs.last();
            def rowCount     = getRowCount()

                row         -= Principal.CacheSTableAtras   //3; Cachéa desde 3 posiciones atrás hasta 4 adelante y la que fue pedida, en total 8 posiciones
            def limite       = Principal.CacheSTableAtras + Principal.CacheSTableAdelante
            def filaDat
            def catArts      = false //¿És este un catálogo de artículos particularmente?
            def CADataPrecio = [:]
            
            if(row<0) { row = 0 }

            def query        = queryAct + " limit $limite offset $row"
            control = conn.createStatement()
            rs = control.executeQuery(query)            
            
            def  meta        = rs.getMetaData()
            nCols            = meta.getColumnCount()-1

            //for(i in 1..limite-1) {  // 1..8
            while(rs.next()) {
                if(row<0||row>rowCount) { row++; continue; }
                //rs.absolute(row)
                //println "Des->"+rs.getObject(5)
                //rs.next()
                fila = []

                (nCols+1).times {
                    
                    filaDat = rs.getObject(it+1); //+1 Porqué comienza el conteo en 1 y no 0 como la matríz y +1 por saltarse el ID

                    /*
                    if(meta.getColumnName(it+1) == "xIDPrecioCA") { catArts = true; CADataPrecio = [:] }
                    if(catArts) {
                        switch(meta.getColumnName(it+1)) {
                            case "xImpuestosCA": CADataPrecio['impuestosBase'] = resultSet.getObject(it+1); filaDat = null; break;
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
                          //println "***Precio:"+ArticulosFunciones.getPrecioConDatos(
                          //  [costo:,clienteDescuento:,lineaDescuento:,precioDescuento:,impuestosBase:]
                        ) //
                    } */

                    if(filaDat != null) {
                        fila << filaDat
						
                    }
                }

                salida[row as String] = fila
                row++
                
            } 
            rs.close()
            control.close()
			
              //def time2 = System.currentTimeMillis() - time1
			//timeTotal += time2				
			//println "Time total: $time2 ms"
			
            return salida
        }
	public Object getValueAt(int rowIndex, int columnIndex) {
            
            if(!this.cacheFila.containsKey(String.valueOf(rowIndex))) {
                //println "cachando"
                cacheFila = getRow(rowIndex, getColumnCount()); //return port.getAt(rowIndex, columnIndex+1);
                //Prueba del caché:  System.out.println("Requesting... [fila:"+rowIndex+"] contenido: ["+cacheFila.toString());
            }
            //println cacheFila.get(String.valueOf(rowIndex)).dump()

            if(cacheFila.get(String.valueOf(rowIndex)) == null) {
              return "";
            } else {
              return cacheFila.get(String.valueOf(rowIndex)).get(columnIndex+1);
            }
        }

        public int getIDArticuloFila(int rowIndex)
        {
            if(rowIndex == -1)
            { Dialogos.lanzarAlerta("Ningúna fila ha sido seleccionada."); }
            else
            { return (Integer)getValueAt(rowIndex, -1); }
            return -1;

        }
        public String getDescripcion(int rowIndex)
        {
            return (String)getValueAt(rowIndex, 1);
        }

	/**
	 * @return The column name
	 */
	public String getColumnName(int column) {
		return (String)colNames.get(column);
	}

	public Class getColumnClass(int columnIndex) {

		Class c = (Class)colClasses.get(columnIndex);

		return c;
	}
	public void destroy() {
        try {
            //println "destruyendo modelo"
            conn.close()
            control.close()
            rs?.close()
            System.gc();

        } catch(Exception e) { Dialogos.error("No se pudo cerrar la conexión con el servidor", e); }
    }
}