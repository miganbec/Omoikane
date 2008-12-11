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
import javax.swing.table.TableColumn
import java.awt.event.*;
import groovy.swing.*

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
        
        Herramientas.setColumnsWidth(cat.jTable1, [0.14,0.2,0.4,0.06,0.1,0.1]);
        Herramientas.In2ActionX(cat, KeyEvent.VK_ESCAPE, "cerrar"   ) { cat.btnCerrar.doClick()   }
        Herramientas.In2ActionX(cat, KeyEvent.VK_F4    , "detalles" ) { cat.btnDetalles.doClick() }
        Herramientas.In2ActionX(cat, KeyEvent.VK_F5    , "nuevo"    ) { cat.btnNuevo.doClick()    }
        Herramientas.In2ActionX(cat, KeyEvent.VK_F6    , "modificar") { cat.btnModificar.doClick()}
        Herramientas.In2ActionX(cat, KeyEvent.VK_DELETE, "eliminar" ) { cat.btnEliminar.doClick() }

        Herramientas.iconificable(cat)

        cat.toFront()
        try { cat.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario catálogo de artículos", Herramientas.getStackTraceString(e)) }
        cat.txtBusqueda.requestFocus()
        cat
    }

    public static String lanzarDialogoCatalogo()
    {
        def foco=new Object()
        def cat = lanzarCatalogo()
        cat.setModoDialogo()
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
        Dialogos.lanzarAlerta("Función desactivada!")
        /*
        def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        db.execute("DELETE FROM articulos WHERE id_articulo = " + ID)
        db.close()
        Dialogos.lanzarAlerta("Artículo " + ID + " supuestamente eliminado")
        */
    }
    static def lanzarDetallesArticulo(ID)
    {
        def formArticulo = new omoikane.formularios.Articulo()
        formArticulo.setVisible(true)
        escritorio.getPanelEscritorio().add(formArticulo)
        formArticulo.toFront()
        try { formArticulo.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles artículo", Herramientas.getStackTraceString(e)) }

        def art         = Nadesico.conectar().getArticulo(ID,IDAlmacen)

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
        formArticulo.ID                   = ID
        formArticulo.setModoDetalles();
        formArticulo
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

        try {
            def serv = Nadesico.conectar()
            Dialogos.lanzarAlerta(serv.addArticulo(IDAlmacen, IDLinea, codigo, descripcion, unidad, impuestos, costo, descuento, utilidad, existencias))
            serv.desconectar()
        } catch(e) { Dialogos.error("Error al enviar a la base de datos. El artículo no se registró", e) }

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
        def formArticulo = lanzarDetallesArticulo(ID)
        Dialogos.lanzarAlerta("Eliminar codigo viejo de lanzarModificarArticulo")
        formArticulo.setModoModificar();
    }
    static def modificar(formArticulo)
    {
        def f = formArticulo
        def c = [cod:f.getTxtCodigo(), lin:f.getTxtIDLinea(), des:f.getTxtDescripcion(), imp:f.getTxtImpuestos(), cos:f.getTxtCosto(),
                 dto:f.getTxtDescuento(), uti:f.getTxtUtilidad(), art:f.getTxtIDArticulo(), uni:f.getTxtUnidad()]
        Herramientas.verificaCampos {
            Herramientas.verificaCampo(c.cod, /^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+]+)$/,"Codigo sólo puede incluír números, letras, espacios, *, -,_ y +.")
            Herramientas.verificaCampo(c.lin, /^([0-9]+)$/,"ID Linea sólo puede incluír números enteros.")
            Herramientas.verificaCampo(c.des, /^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+áéíóúü]+)$/,"Descripcion sólo puede incluír números, letras, espacios, á, é, í, ó, ú, ü, _, -, * y +.")
            Herramientas.verificaCampo(c.imp, /^([0-9]*[\.]{0,1}[0-9]+)$/,"Impuestos sólo puede incluír números reales positivos")
            Herramientas.verificaCampo(c.cos, /^([0-9]*[\.]{0,1}[0-9]+)$/,"Costo sólo puede incluír números reales positivos")
            Herramientas.verificaCampo(c.dto, /^([0-9]*[\.]{0,1}[0-9]+)$/,"Descuento sólo puede incluír números reales positivos")
            Herramientas.verificaCampo(c.uti, /^([0-9]*[\.]{0,1}[0-9]+)$/,"Utilidad sólo puede incluír números reales positivos")

            def serv = Nadesico.conectar()
            Dialogos.lanzarAlerta(serv.modArticulo(IDAlmacen, c.art, c.cod, c.lin, c.des, c.uni, c.imp, c.cos, c.uti, c.dto))
            }
        }
}