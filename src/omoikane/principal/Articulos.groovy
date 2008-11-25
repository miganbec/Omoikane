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
import groovy.net.xmlrpc.*
import java.net.ServerSocket

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
        JDialog dialogo = new JDialog(omoikane.principal.Principal.escritorio.getFrameEscritorio(), "Seleccione un artículo", true);
        def cat = new omoikane.formularios.CatalogoArticulos()
        cat.setModoDialogo();
        dialogo.getContentPane().add(cat);
        dialogo.setUndecorated true
        cat.setVisible(true);
        dialogo.setBounds(0,0,cat.getBounds().width as int,cat.getBounds().height as int)
        Herramientas.centrarVentana(dialogo)

        def clousures = [
            windowOpened: {
                try { cat.setSelected(true); } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario catálogo de artículos", Herramientas.getStackTraceString(e)); }
                cat.txtBusqueda.requestFocus();
            },
            internalFrameClosed: { dialogo.dispose() }
          ]
        def interfaces= [WindowListener, InternalFrameListener]
        def listener  = ProxyGenerator.instantiateAggregate(clousures, interfaces)
        dialogo.addWindowListener(listener)
        cat.addInternalFrameListener(listener)

        dialogo.setVisible(true);
        cat.codigoSeleccionado
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

        def serv        = new XMLRPCServerProxy("http://localhost:5542")
        def art         = serv.getArticulo(ID,IDAlmacen)

        formArticulo.setTxtIDArticulo    art.id_articulo   as String 
        formArticulo.setTxtCodigo        art.codigo                  
        formArticulo.setTxtIDLinea       art.id_linea      as String 
        formArticulo.setTxtDescripcion   art.descripcion             
        formArticulo.setTxtUnidad        art.unidad                  
        formArticulo.setTxtImpuestos     art.impuestos     as String 
        formArticulo.setTxtUModificacion art.uModificacion as String 
        formArticulo.setTxtDescuento     art.descuento     as String 
        formArticulo.setTxtCosto         art.costo         as String 
        formArticulo.setTxtUtilidad      art.utilidad      as String 
        formArticulo.setTxtExistencias   art.cantidad      as String 
        formArticulo.setModoDetalles();

    }
    static def guardar(formArticulo)
    {
        def codigo        = formArticulo.getTxtCodigo()
        def IDLinea       = java.lang.Integer.valueOf(formArticulo.getTxtIDLinea())
        def descripcion   = formArticulo.getTxtDescripcion()
        def unidad        = formArticulo.getTxtUnidad()
        def impuestos     = formArticulo.getTxtImpuestos() as Double
        def costo         = formArticulo.getTxtCosto() as Double
        def descuento     = formArticulo.getTxtDescuento() as Double
        def utilidad      = formArticulo.getTxtUtilidad() as Double
        def existencias   = formArticulo.getTxtExistencias() as Double

        try {
            def serv = new XMLRPCServerProxy(Principal.config.URLServidor[0].text())
            Dialogos.lanzarAlerta(serv.addArticulo(IDAlmacen, IDLinea, codigo, descripcion, unidad, impuestos, costo, descuento, utilidad, existencias))

        } catch(java.net.ConnectException e) { Dialogos.error("Error al conectar con el servidor", e)

        } catch(e) { Dialogos.error("Error al enviar a la base de datos. El artículo no se registró", e) }
    }
    static def lanzarFormNuevoArticulo()
    {
        def form = new omoikane.formularios.Articulo()
        form.setVisible(true)
        escritorio.getPanelEscritorio().add(form)
        form.toFront()
        SwingBuilder.build {
          //Al presionar F2: (lanzarCatalogoDialogo)
          form.getCampoID().keyPressed = { if(it.keyCode == it.VK_F2) form.txtIDLinea = Lineas.lanzarCatalogoDialogo() as String }
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

        def serv        = new XMLRPCServerProxy("http://localhost:5542")
        def art         = serv.getArticulo(ID,IDAlmacen)

        formArticulo.setTxtIDArticulo    art.id_articulo   as String
        formArticulo.setTxtCodigo        art.codigo
        formArticulo.setTxtIDLinea       art.id_linea      as String
        formArticulo.setTxtDescripcion   art.descripcion
        formArticulo.setTxtUnidad        art.unidad
        formArticulo.setTxtImpuestos     art.impuestos     as String
        formArticulo.setTxtUModificacion art.uModificacion as String
        formArticulo.setTxtDescuento     art.descuento     as String
        formArticulo.setTxtCosto         art.costo         as String
        formArticulo.setTxtUtilidad      art.utilidad      as String
        formArticulo.setTxtExistencias   art.cantidad      as String
        formArticulo.ID                  = ID
        formArticulo.setModoModificar();
    }
    static def modificar(formArticulo)
    {
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