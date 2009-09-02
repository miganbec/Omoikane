/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.sistema

import java.sql.*;
import java.util.*;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.*;
import java.awt.event.*;

/**
 * ///////////////////////////////////// //////////////////////////////
 * ///////////////////////////////////// //////////////////////////////
 * ///////////////////////////////////// //////////////////////////////
 *
 * 
 * 
 * 
 * @author Usuario //
 */

public class NadesicoTableModel extends AbstractTableModel {
	java.util.List	colNames	= null;
	int             rowCount        = -1;
	java.util.List  colClasses      = null;
        String          queryAct        = null;
    public NadesicoX       port = null;
    java.util.Hashtable<String,java.util.List> cacheFila = new Hashtable();

    public NadesicoTableModel(java.util.List colNames, ArrayList colClases) {
        this.port = new NadesicoX();
        port.conectar();
        this.colNames   = new ArrayList(colNames);
        this.colClasses = colClases;
	}
    public void refrescar() {
        setQuery(queryAct)
    }
    public void setQuery(String query) {
        try
        {
            queryAct = query
            cacheFila = new Hashtable()
            port.setQuery(query);
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
		if (this.rowCount == -1) {
			try {
                this.rowCount = port.getRowCount();
			} catch (Exception e) {
                omoikane.sistema.Dialogos.lanzarDialogoError(null, "Error al pasar a la última fila", omoikane.sistema.Herramientas.getStackTraceString(e));
            }
		}

		return rowCount;
	}

	/**
	 * <p>Returns the value for the cell at columnIndex
	 * and rowIndex</p>
	 * @param rowIndex The row whose value is to be queried
	 * @param columnIndex The column whose value is to be queried
	 * @return The value Object at the specified cell
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
            if(!this.cacheFila.containsKey(String.valueOf(rowIndex))) {
                cacheFila = port.getRow(rowIndex, getColumnCount()); //return port.getAt(rowIndex, columnIndex+1);
                //Prueba del caché:  System.out.println("Requesting... [fila:"+rowIndex+"] contenido: ["+cacheFila.toString());
            }
            return cacheFila.get(String.valueOf(rowIndex)).get(columnIndex+1);
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
        try { this.port.desconectar(); } catch(Exception e) { Dialogos.error("No se pudo cerrar la conexión con el servidor", e); }
    }
}