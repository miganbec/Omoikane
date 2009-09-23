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
import javax.swing.table.TableColumn;
import java.awt.event.*;
import groovy.swing.*;
import static omoikane.sistema.Usuarios.*;
import static omoikane.sistema.Permisos.*;

public class Articulos
{
    static def IDAlmacen = Principal.IDAlmacen
    static def escritorio = omoikane.principal.Principal.escritorio

    static def getArticulo(where) { new Articulo(where) }
    static def findByCodigo(busqueda) {
        def res
        PuertoNadesico.workIn() { res = it.RAMCacheCodigos.executeQuery("select id_articulo from nadesicoi.RAMCacheCodigos as rca WHERE rca like '%"+busqueda+"%'") /*it.RAMCacheCodigos.findAllByCodigoLike("%"+busqueda+"%");*/ }
        //println "resu:"+res.dump()
    }
    static def lanzarCatalogo()
    {
        if(cerrojo(PMA_ABRIRARTICULO)){
            def cat = (new omoikane.formularios.CatalogoArticulos())
            cat.setVisible(true);
            escritorio.getPanelEscritorio().add(cat)
            Herramientas.setColumnsWidth(cat.jTable1, [0.16,0.16,0.05,0.4,0.07,0.08,0.08]);
            Herramientas.panelCatalogo(cat)
            Herramientas.In2ActionX(cat, KeyEvent.VK_ESCAPE, "cerrar"   ) { cat.btnCerrar.doClick()   }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F3    , "buscar"   ) { cat.txtBusqueda.requestFocusInWindow()  }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F4    , "detalles" ) { cat.btnDetalles.doClick() }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F5    , "nuevo"    ) { cat.btnNuevo.doClick()    }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F6    , "modificar") { cat.btnModificar.doClick()}
            Herramientas.In2ActionX(cat, KeyEvent.VK_DELETE, "eliminar" ) { cat.btnEliminar.doClick() }
            cat.txtBusqueda.keyReleased = { if(it.keyCode == it.VK_ESCAPE) cat.btnCerrar.doClick() }
            Herramientas.iconificable(cat)
            cat.toFront()
            try { cat.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario catálogo de artículos", Herramientas.getStackTraceString(e)) }
            cat.txtBusqueda.requestFocus()
            return cat
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    public static String lanzarDialogoCatalogo()
    {
        def foco=new Object()
        def cat = lanzarCatalogo()
        cat.setModoDialogo()
        cat.internalFrameClosed = {synchronized(foco){foco.notifyAll()} }
        cat.txtBusqueda.keyPressed = { if(it.keyCode == it.VK_ENTER) { println "aquí"; cat.btnAceptar.doClick(); println "aquí2";  } }
        cat.txtBusqueda.keyReleased = { if(it.keyCode == it.VK_ENTER) {cat.btnAceptar.doClick()} }
        def retorno
        cat.btnAceptar.actionPerformed = { 
            System.out.println ("otro btn aceptar");
            def catTab = cat.jTable1;
            retorno = catTab.getModel().getValueAt(catTab.getSelectedRow(), 0) as String;
            cat.btnCerrar.doClick();
        }
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
        if(cerrojo(PMA_ELIMINARARTICULO)){
        def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        db.execute("DELETE FROM articulos WHERE id_articulo = " + ID)
        db.close()
        Dialogos.lanzarAlerta("Artículo " + ID + " supuestamente eliminado")
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
        */
    }
    static def rellenarCodigosAlternos(ID, form) {

        PuertoNadesico.workIn() {
            def artGorm = it.Articulos.get(ID)

            form.tblCodigos.setModel(new javax.swing.table.DefaultTableModel(0,1))
            form.tblCodigos.getModel().setColumnIdentifiers("Código")
            artGorm.get(ID).codigos.each {
                form.tblCodigos.getModel().addRow(it.codigo)
            }

        }
    }
    static def lanzarDetallesArticulo(ID)
    {
        if(cerrojo(PMA_DETALLESARTICULO)){
            def formArticulo = new omoikane.formularios.Articulo()
            def formateador = new java.text.DecimalFormat("#.00");
            formArticulo.setVisible(true)
            escritorio.getPanelEscritorio().add(formArticulo)
            Herramientas.panelFormulario(formArticulo)
            formArticulo.toFront()
            try { formArticulo.setSelected(true)
            def serv        = Nadesico.conectar()
            def art         = serv.getArticulo(ID,IDAlmacen)
            def lin         = serv.getLinea(art.id_linea)
            def gru         = serv.getGrupo(art.id_grupo)
            def notas       = serv.getAnotacion(IDAlmacen,ID)
            serv.desconectar()
            formArticulo.setTxtIDArticulo    art.id_articulo           as String
            formArticulo.setTxtCodigo        art.codigo
            formArticulo.setTxtIDLinea       art.id_linea              as String
            formArticulo.setTxtIDGrupo       art.id_grupo              as String
            formArticulo.setTxtIDLineaDes    lin.descripcion           as String
            formArticulo.setTxtIDGrupoDes    gru.descripcion           as String
            formArticulo.setTxtDescripcion   art.descripcion
            formArticulo.setTxtUnidad        art.unidad
            formArticulo.setTxtImpuestos     art.impuestos             as String
            formArticulo.setTxtUModificacion art.uModificacion         as String
            formArticulo.setTxtDescuento     art.precio['descuento$']  as String
            formArticulo.setTxtCosto         art.costo                 as String
            formArticulo.setTxtUtilidadPorc  art.utilidad              as String
            formArticulo.setTxtExistencias   art.cantidad              as String
            formArticulo.setTxtPrecio        art.precio.total          as String
            formArticulo.setTxtComentarios   notas                     as String
            formArticulo.getTxtDesctoPorcentaje().text = art.precio['descuento%'] as String
            formArticulo.getTxtDescuento2().text       = art.precio['descuento$'] as String
            formArticulo.getTxtPrecio2().text          = art.precio.total         as String
            formArticulo.getTxtImpuestosPorc().text    = art.impuestos            as String
            formArticulo.getTxtImpuestos().text        = art.precio['impuestos']  as String
            formArticulo.getTxtUtilidad().text         = art.precio['utilidad']   as String
            formArticulo.ID                   = ID
            formArticulo.setModoDetalles();
            formArticulo.btnAddCode.actionPerformed    = {
                new SimpleForm("omoikane.formularios.CodigoArticulo") {
                    def form = it.form
                    form.visible = true
                    form.btnCancelar.actionPerformed = { form.dispose() }
                    form.btnAceptar.actionPerformed  = {
                        PuertoNadesico.workIn() { puerto ->
                            (puerto.Articulos.get(ID)).addToCodigos(puerto.CodigosArticulo.newInstance(codigo:form.txtCodigo.text)).save()
                            rellenarCodigosAlternos(ID, formArticulo)
                            Dialogos.lanzarAlerta("Código agregado")
                            form.dispose()
                        }
                    }
                }
            }
            rellenarCodigosAlternos(ID, formArticulo)
            return formArticulo
            
            } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles artículo", Herramientas.getStackTraceString(e)) }
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def guardar(formArticulo)
    {
        if(cerrojo(PMA_MODIFICARARTICULO)){
            Herramientas.verificaCampos {
                def codigo        = formArticulo.getTxtCodigo()
                def IDLinea       = formArticulo.getTxtIDLinea()
                def IDGrupo       = formArticulo.getTxtIDGrupo()
                def descripcion   = formArticulo.getTxtDescripcion()
                def unidad        = formArticulo.getTxtUnidad()
                def impuestos     = formArticulo.getTxtImpuestosPorc().text
                def costo         = formArticulo.getTxtCosto()
                def descuento     = formArticulo.getTxtDesctoPorcentaje().text
                def utilidad      = formArticulo.getTxtUtilidadPorc().text
                def existencias   = formArticulo.getTxtExistencias()
                def notas   = formArticulo.getTxtComentarios()
                Herramientas.verificaCampo(codigo,Herramientas.texto,"codigo"+Herramientas.error1)
                Herramientas.verificaCampo(IDLinea,Herramientas.numero,"ID linea"+Herramientas.error2)
                Herramientas.verificaCampo(IDGrupo,Herramientas.numero,"ID Grupo"+Herramientas.error2)
                Herramientas.verificaCampo(descripcion,Herramientas.texto,"descripcion"+Herramientas.error1)
                Herramientas.verificaCampo(impuestos,Herramientas.numeroReal,"impuestos"+Herramientas.error3)
                Herramientas.verificaCampo(costo,Herramientas.numeroReal,"costos"+Herramientas.error3)
                Herramientas.verificaCampo(descuento,Herramientas.numeroReal,"descuento"+Herramientas.error3)
                Herramientas.verificaCampo(utilidad,Herramientas.numeroReal,"utilidad"+Herramientas.error3)
                Herramientas.verificaCampo(existencias,Herramientas.numeroReal,"existencias"+Herramientas.error3)
                IDLinea       = java.lang.Integer.valueOf(IDLinea)
                IDGrupo       = java.lang.Integer.valueOf(IDGrupo)
                impuestos     = impuestos as Double
                costo         = costo as Double
                descuento     = descuento as Double
                utilidad      = utilidad as Double
                existencias   = existencias as Double
                try {
                    def serv   = Nadesico.conectar()
                    def datAdd = serv.addArticulo(IDAlmacen, IDLinea, IDGrupo, codigo, descripcion, unidad, impuestos, costo, descuento, utilidad, existencias)
                    def notasAdd = serv.addAnotacion(IDAlmacen, datAdd.ID, notas )

                    Dialogos.lanzarAlerta(datAdd.mensaje)
                    serv.desconectar()
                    PuertoNadesico.workIn() { it.CacheArticulos.actualizar(datAdd.ID) }
                } catch(e) { Dialogos.error("Error al enviar a la base de datos. El artículo no se registró", e) }
                formArticulo.dispose()
            }
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def lanzarFormNuevoArticulo()
    {
        if(cerrojo(PMA_MODIFICARARTICULO)){
            try{
            def form = new omoikane.formularios.Articulo()
            form.setVisible(true)
            Herramientas.panelFormulario(form)
            escritorio.getPanelEscritorio().add(form)
            form.toFront()
            SwingBuilder.build {
                //Al presionar F1: (lanzarCatalogoDialogo)
                form.getCampoID()   .keyReleased = { if(it.keyCode == it.VK_F1) Thread.start {form.txtIDLinea      = Lineas.lanzarCatalogoDialogo() as String; form.getIDLinea().requestFocus()}  }
                form.getCampoGrupo().keyReleased = { if(it.keyCode == it.VK_F1) Thread.start {form.txtIDGrupo      = Grupos.lanzarCatalogoDialogo() as String; form.getIDGrupo().requestFocus()}  }
            }
            try { form.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles artículo", Herramientas.getStackTraceString(e)) }
            form.setEditable(true);
            form.setModoNuevo();
            } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario nuevo artículo", Herramientas.getStackTraceString(e)) }
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def lanzarModificarArticulo(ID)
    {
        def form = lanzarDetallesArticulo(ID)
        //Dialogos.lanzarAlerta("Eliminar codigo viejo de lanzarModificarArticulo")
        SwingBuilder.build {
                //Al presionar F1: (lanzarCatalogoDialogo)
                form.getCampoID()   .keyReleased = { if(it.keyCode == it.VK_F1) Thread.start {form.txtIDLinea      = Lineas.lanzarCatalogoDialogo() as String; form.getIDLinea().requestFocus()}  }
                form.getCampoGrupo().keyReleased = { if(it.keyCode == it.VK_F1) Thread.start {form.txtIDGrupo      = Grupos.lanzarCatalogoDialogo() as String; form.getIDGrupo().requestFocus()}  }
            }
        form.setModoModificar();
        form
    }

    static def modificar(formArticulo)
    {
        if(cerrojo(PMA_MODIFICARARTICULO)){
            def f = formArticulo
            def c = [cod:f.getTxtCodigo(), lin:f.getTxtIDLinea(),gru:f.getTxtIDGrupo(), des:f.getTxtDescripcion(), imp:f.getTxtImpuestosPorc().text, cos:f.getTxtCosto(),
            dto:f.getTxtDesctoPorcentaje().text, uti:f.getTxtUtilidadPorc().text, art:f.getTxtIDArticulo(), uni:f.getTxtUnidad(), notas:f.getTxtComentarios()]
            Herramientas.verificaCampos {
                Herramientas.verificaCampo(c.cod,Herramientas.texto,"codigo"+Herramientas.error1)
                Herramientas.verificaCampo(c.lin,Herramientas.numero,"ID linea"+Herramientas.error2)
                Herramientas.verificaCampo(c.gru,Herramientas.numero,"ID Grupo"+Herramientas.error2)
                Herramientas.verificaCampo(c.des,Herramientas.texto,"descripcion"+Herramientas.error1)
                Herramientas.verificaCampo(c.imp,Herramientas.numeroReal,"impuestos"+Herramientas.error3)
                Herramientas.verificaCampo(c.cos,Herramientas.numeroReal,"costos"+Herramientas.error3)
                Herramientas.verificaCampo(c.dto,Herramientas.numeroReal,"descuento"+Herramientas.error3)
                Herramientas.verificaCampo(c.uti,Herramientas.numeroReal,"utilidad"+Herramientas.error3)
                def serv = Nadesico.conectar()
                Dialogos.lanzarAlerta(serv.modArticulo(IDAlmacen, c.art, c.cod, c.lin,c.gru, c.des, c.uni, c.imp, c.cos, c.uti, c.dto))
                serv.modAnotacion(IDAlmacen, c.art, c.notas)
                serv.desconectar()
                PuertoNadesico.workIn() { it.CacheArticulos.actualizar(c.art) }
            }
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def recalcularCampos(formArticulo) {
        def f = formArticulo
        def c = [ imp:Double.parseDouble(f.getTxtImpuestosPorc().text), cos:Double.parseDouble(f.getTxtCosto()),
                dto:Double.parseDouble(f.getTxtDesctoPorcentaje().text), uti:Double.parseDouble(f.getTxtUtilidadPorc().text)]

        def utilidad = c.uti * c.cos * 0.01;
        def descuento = (utilidad + c.cos) * c.dto * 0.01;
        def impuesto = ( c.cos + utilidad - descuento ) * c.imp * 0.01;
        def precio = (c.cos + utilidad - descuento + impuesto);
        def formateador = new java.text.DecimalFormat("#.00");

        f.setTxtImpuestos4(formateador.format(impuesto));
        f.setTxtDescuento3(formateador.format(descuento));
        f.setTxtDescuento(formateador.format(c.dto));
        f.setTxtUtilidad2(formateador.format(utilidad));
        f.setTxtPrecio3(formateador.format(precio));
        f.setTxtPrecio(formateador.format(precio));

    }

    static def recalcularUtilidad(formArticulo, txtPrecio) {
        def f = formArticulo
        def c = [cos:(f.getTxtCosto()) as Double                  ,pre:(f.getTxtPrecio2().text) as Double,
                 poi:(f.getTxtImpuestosPorc().text) as Double     ,pod:(f.getTxtDesctoPorcentaje().text) as Double]
        def formateador = new java.text.DecimalFormat("#.00");
        c.poi=c.poi/100
        c.pod=c.pod/100

        def porcentajeUtilidad  =   (c.pre/(c.cos*(1+c.poi)*(1-c.pod)))-1
        def utilidad            =   c.cos*porcentajeUtilidad
        def descuento           =   (c.cos+utilidad)*c.pod
        def impuesto            =   (c.cos+utilidad-descuento)*c.poi
        porcentajeUtilidad      =   porcentajeUtilidad*100

        f.setTxtDescuento3(formateador.format(descuento));
        f.setTxtImpuestos4(formateador.format(impuesto));
        f.setTxtUtilidad2(formateador.format(utilidad))
        f.setTxtUtilidadPorcText(formateador.format(porcentajeUtilidad))
        f.setTxtPrecio3(txtPrecio.getText())
        f.setTxtPrecio(txtPrecio.getText())

    }
}
