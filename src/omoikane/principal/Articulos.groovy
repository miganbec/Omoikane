/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.principal

import omoikane.principal.*
import omoikane.sistema.*
import groovy.sql.*;
import groovy.swing.*;
import javax.swing.*;
import java.awt.event.WindowListener;
import javax.swing.event.*;
import groovy.inspect.swingui.*;

public class Articulos
{
    static def IDAlmacen = 1
    static def escritorio = omoikane.principal.Principal.escritorio

    static def getArticulo(where) { new Articulo(where) }

    static def lanzarCatalogo()
    {
        def cat = (new omoikane.formularios.CatalogoArticulos())
        cat.setVisible(true);
        escritorio.getPanelEscritorio().add(cat)
        cat.toFront()
        try { cat.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario catálogo de artículos", Herramientas.getStackTraceString(e)) }
        cat.txtBusqueda.requestFocus()
    }

    public static String lanzarDialogoCatalogo()
    {
        def foco=new Object()
        def cat = (new omoikane.formularios.CatalogoArticulos())
        cat.setModoDialogo()
        cat.setVisible(true);
        escritorio.getPanelEscritorio().add(cat)
        cat.toFront()
        try { cat.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario catÃ¡logo de Articulos", Herramientas.getStackTraceString(e)) }
        cat.txtBusqueda.requestFocus()
        cat.internalFrameClosed = {synchronized(foco){foco.notifyAll()} }
        cat.txtBusqueda.keyPressed = { if(it.keyCode == it.VK_ENTER) cat.btnAceptar.doClick() }
        def retorno
        cat.btnAceptar.actionPerformed = { def catTab = cat.jTable1; retorno = catTab.getModel().getValueAt(catTab.getSelectedRow(), 0) as String; cat.btnCerrar.doClick(); }
        synchronized(foco){foco.wait()}
        retorno
    }

    static def lanzarImprimir(form)
    {
        def reporte = new Reporte('omoikane/reportes/ArticulosTodos.jasper',[txtQuery:form.txtQuery]);
        reporte.lanzarPreview()
    }
    static def eliminarArticulo(ID)
    {
        def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        db.execute("DELETE FROM articulos WHERE id_articulo = " + ID)
        db.close()
        Dialogos.lanzarAlerta("Artículo " + ID + " supuestamente eliminado")
    }
    static def lanzarDetallesArticulo(ID)
    {
        def formArticulo = new omoikane.formularios.Articulo()
        formArticulo.setVisible(true)
        escritorio.getPanelEscritorio().add(formArticulo)
        formArticulo.toFront()
        try { formArticulo.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles artículo", Herramientas.getStackTraceString(e)) }

        def db   = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        def articulo   = db.rows("SELECT * FROM articulos WHERE id_articulo = $ID")
        def existencia = db.firstRow("SELECT * FROM existencias WHERE id_articulo = $ID AND id_almacen = $IDAlmacen")
        def precio     = db.firstRow("SELECT * FROM precios     WHERE id_articulo = $ID AND id_almacen = $IDAlmacen")

        formArticulo.setTxtIDArticulo    ((String)articulo[0].id_articulo)
        formArticulo.setTxtCodigo        (articulo[0].codigo)
        formArticulo.setTxtIDLinea       (articulo[0].id_linea as String)
        formArticulo.setTxtDescripcion   (articulo[0].descripcion)
        formArticulo.setTxtUnidad        (articulo[0].unidad)
        formArticulo.setTxtImpuestos     ((Double)articulo[0].impuestos as String)
        formArticulo.setTxtUModificacion (articulo[0].uModificacion as String)
        formArticulo.setTxtDescuento     (precio.descuento as String)
        formArticulo.setTxtCosto         (precio.costo as String)
        formArticulo.setTxtUtilidad      (precio.utilidad as String)
        formArticulo.setTxtExistencias   (existencia.cantidad as String)
        formArticulo.setModoDetalles();

        db.close()
    }
    static def guardar(formArticulo)
    {
        Herramientas.verificaCampos {
        def codigo        = formArticulo.getTxtCodigo()
        def IDLinea       = formArticulo.getTxtIDLinea()
        def descripcion   = formArticulo.getTxtDescripcion()
        def unidad        = formArticulo.getTxtUnidad()
        def impuestos     = formArticulo.getTxtImpuestos()
        def costo         = formArticulo.getTxtCosto()
        def descuento     = formArticulo.getTxtDescuento()
        def utilidad      = formArticulo.getTxtUtilidad()
        def existencias   = formArticulo.getTxtExistencias()
        
        Herramientas.verificaCampo(codigo,/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+]+)$/,"Codigo sólo puede incluír números, letras, espacios, *, -,_ y +.")
        Herramientas.verificaCampo(IDLinea,/^([0-9]+)$/,"IDLinea sólo puede incluír números enteros.")
        Herramientas.verificaCampo(descripcion,/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+áéíóúü]+)$/,"Descripcion sólo puede incluír números, letras, espacios, á, é, í, ó, ú, ü, _, -, * y +.")
        Herramientas.verificaCampo(impuestos,/^([0-9]*[\.]{0,1}[0-9]+)$/,"Impuestos sólo puede incluír números reales positivos")
        Herramientas.verificaCampo(costo,/^([0-9]*[\.]{0,1}[0-9]+)$/,"Costo sólo puede incluír números reales positivos")
        Herramientas.verificaCampo(descuento,/^([0-9]*[\.]{0,1}[0-9]+)$/,"Descuento sólo puede incluír números reales positivos")
        Herramientas.verificaCampo(utilidad,/^([0-9]*[\.]{0,1}[0-9]+)$/,"Utilidad sólo puede incluír números reales positivos")
        Herramientas.verificaCampo(existencias,/^([0-9]*[\.]{0,1}[0-9]+)$/,"Existencias sólo puede incluír números reales positivos")
       
        IDLinea       = java.lang.Integer.valueOf(IDLinea)
        impuestos     = impuestos as Double
        costo         = costo as Double
        descuento     = descuento as Double
        utilidad      = utilidad as Double
        existencias   = existencias as Double

        def db

        try {
            db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
            def IDArticulo = db.executeInsert("INSERT INTO articulos SET codigo = ?, id_linea = ?, descripcion = ?, unidad = ? , impuestos = ?", [codigo, IDLinea, descripcion, unidad, impuestos])
            IDArticulo = IDArticulo[0][0]
            db.connection.autoCommit = false
            db.executeInsert("INSERT INTO precios SET id_almacen = ?, id_articulo = ?, costo = ?, descuento = ?, utilidad = ?", [IDAlmacen, IDArticulo, costo, descuento, utilidad])
            db.executeInsert("INSERT INTO existencias SET id_almacen = ?, id_articulo = ?, cantidad = ?", [IDAlmacen, IDArticulo, existencias])
            db.commit()
            Dialogos.lanzarAlerta("Artículo $descripcion agregado.")
        } catch(Exception e) {
            db.rollback()
            Dialogos.lanzarDialogoError(null, "Error al enviar a la base de datos. El artículo no se registró.", omoikane.sistema.Herramientas.getStackTraceString(e))
        }
    
        finally {
            db.close()
        }
        formArticulo.dispose()
        }
    }
    static def lanzarFormNuevoArticulo()
    {
        def form = new omoikane.formularios.Articulo()
        form.setVisible(true)
        escritorio.getPanelEscritorio().add(form)
        form.toFront()
        SwingBuilder.build {
          //Al presionar F2: (lanzarCatalogoDialogo)

          form.getCampoID().keyPressed = { if(it.keyCode == it.VK_F2) Thread.start {form.txtIDLinea = Lineas.lanzarCatalogoDialogo() as String; form.getIDLinea().requestFocus()}  }
        }
        try { form.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles artículo", Herramientas.getStackTraceString(e)) }
        form.setEditable(true);
        form.setModoNuevo();
    }
    static def lanzarModificarArticulo(ID)
    {
        def formArticulo = new omoikane.formularios.Articulo()
        formArticulo.setVisible(true)
        escritorio.getPanelEscritorio().add(formArticulo)
        formArticulo.toFront()
        try { formArticulo.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles artículo", Herramientas.getStackTraceString(e)) }

        def db   = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        def articulo = db.rows("SELECT * FROM articulos WHERE id_articulo = $ID")
        def existencia = db.firstRow("SELECT * FROM existencias WHERE id_articulo = $ID AND id_almacen = $IDAlmacen")
        def precio     = db.firstRow("SELECT * FROM precios     WHERE id_articulo = $ID AND id_almacen = $IDAlmacen")
        db.close()

        formArticulo.setTxtIDArticulo    ((String)articulo[0].id_articulo)
        formArticulo.setTxtCodigo        (articulo[0].codigo)
        formArticulo.setTxtIDLinea       (articulo[0].id_linea as String)
        formArticulo.setTxtDescripcion   (articulo[0].descripcion)
        formArticulo.setTxtUnidad        (articulo[0].unidad)
        formArticulo.setTxtImpuestos     ((Double)articulo[0].impuestos as String)
        formArticulo.setTxtUModificacion (articulo[0].uModificacion as String)
        formArticulo.setTxtDescuento     (precio.descuento as String)
        formArticulo.setTxtCosto         (precio.costo as String)
        formArticulo.setTxtUtilidad      (precio.utilidad as String)
        formArticulo.setTxtExistencias   (existencia.cantidad as String)
        formArticulo.ID                   = ID
        formArticulo.setModoModificar();
    }
    static def modificar(formArticulo)
    {
        Herramientas.verificaCampos{
        Herramientas.verificaCampo(formArticulo.getTxtCodigo(),/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+]+)$/,"Codigo sólo puede incluír números, letras, espacios, *, -,_ y +.")
        Herramientas.verificaCampo(formArticulo.getTxtIDLinea(),/^([0-9]+)$/,"IDLinea sólo puede incluír números enteros.")
        Herramientas.verificaCampo(formArticulo.getTxtDescripcion(),/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+áéíóúü]+)$/,"Descripcion sólo puede incluír números, letras, espacios, á, é, í, ó, ú, ü, _, -, * y +.")
        Herramientas.verificaCampo(formArticulo.getTxtImpuestos(),/^([0-9]*[\.]{0,1}[0-9]+)$/,"Impuestos sólo puede incluír números reales positivos")
        Herramientas.verificaCampo(formArticulo.getTxtCosto(),/^([0-9]*[\.]{0,1}[0-9]+)$/,"Costo sólo puede incluír números reales positivos")
        Herramientas.verificaCampo(formArticulo.getTxtDescuento(),/^([0-9]*[\.]{0,1}[0-9]+)$/,"Descuento sólo puede incluír números reales positivos")
        Herramientas.verificaCampo(formArticulo.getTxtUtilidad(),/^([0-9]*[\.]{0,1}[0-9]+)$/,"Utilidad sólo puede incluír números reales positivos")
        
        def db   = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        try {
          db.connection.autoCommit = false
          db.executeUpdate("UPDATE articulos SET codigo = ?, id_linea = ?, descripcion = ?, unidad = ?, impuestos = ? WHERE id_articulo = ?"
            , [
                formArticulo.getTxtCodigo(),
                formArticulo.getTxtIDLinea(),
                formArticulo.getTxtDescripcion(),
                formArticulo.getTxtUnidad(),
                formArticulo.getTxtImpuestos(),
                formArticulo.getTxtIDArticulo()
            ])
          Precios.modificar(db, IDAlmacen, formArticulo.ID, costo:    formArticulo.getTxtCosto()    as Double,
                                                            utilidad: formArticulo.getTxtUtilidad() as Double,
                                                            descuento:formArticulo.getTxtDescuento()as Double)
          db.commit()
        } catch(Exception e) {
            db.rollback()
        } finally {
            db.close()
        }
        Dialogos.lanzarAlerta("Artículo modificado con éxito!")
        }
        }
}