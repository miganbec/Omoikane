/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.principal

import omoikane.sistema.*
import java.text.*;
import groovy.sql.*;
import omoikane.sistema.*;
import javax.swing.event.*;
import java.awt.event.*;
/**
 *
 * @author Usuario
 */
class Caja {
    static def IDCaja    = 1
    static def IDAlmacen = 1
    static def IDCliente = 0
    static def queryCaja  = ""
    static def escritorio = omoikane.principal.Principal.escritorio

	static def lanzar() {
        def form = new omoikane.formularios.Caja()
        escritorio.getPanelEscritorio().add(form)
        Herramientas.centrarVentana(form);
        form.setVisible(true);
        Herramientas.iconificable(form)
        Herramientas.setColumnsWidth(form.tablaVenta, [0.03,0.48,0.12,0.12,0.12,0.13]);

        form.toFront()
        try { form.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario caja", Herramientas.getStackTraceString(e)) }
        form.txtCaptura.requestFocus()
        def date = Calendar.getInstance()
        form.txtFecha.text = date.get(date.DAY_OF_MONTH) + "-" + (date.get(date.MONTH)+1) + "-" + date.get(date.YEAR)

        def serv = Nadesico.conectar()
        def round = { cant -> return (Math.round(cant*100)/100) }
        def cifra = { cant -> return String.format("\$%,.2f", cant) }
        def aDoble= { cant -> return cant.replace("\$", '').replace(",", '') as Double }
        def sumarTodo = {
            def dat   = form.modelo.getDataVector()
            def sumas = [0.0,0.0,0.0]
            dat.each { linea ->
                sumas[0] += linea[2] as Double; sumas[1] += linea[4] as Double; sumas[2] += aDoble(linea[5])
            }
            form.txtNArticulos.text = sumas[0]
            form.txtSubtotal.text   = cifra (sumas[2])
            form.txtTotal.text      = cifra (sumas[2])
            form.txtDescuento.text  = cifra (sumas[1])
        }
        def addArtic = { codigo ->
            try {
                def captura = form.txtCaptura.text.split("\\*")
                def cantidad= captura.size()==1?1:captura[0..captura.size()-2].inject(1) { acum, i -> acum*(i as Double) }
                def art     = serv.codigo2Articulo(IDAlmacen, captura[captura.size()-1])
                if(art == null || art == 0) { Dialogos.lanzarAlerta("Artículo no encontrado!!"); } else {
                    def precio  = serv.getPrecio(art.id_articulo, IDAlmacen, IDCliente)
                    def total   = cifra(cantidad * precio.total)
                    form.txtCaptura.text = ""
                    form.modelo.addRow([art.id_articulo,art.descripcion,cantidad,precio.total,precio['descuento$'],total].toArray())
                    sumarTodo()
                    form.repaint()
                }
            } catch(e) { Dialogos.error("Error al obtener información de nadesico!", e) }
        }
        form.txtCaptura.keyPressed = {   e ->
            if(e.keyCode==e.VK_ENTER) addArtic(form.txtCaptura.text)
            //Al presionar   F2: (lanzarCatalogoDialogo)
            if(e.keyCode == e.VK_F2) { form.btnCatalogo.doClick() }

          }
        def catArticulos = { def retorno = Articulos.lanzarDialogoCatalogo() as String; return retorno==null?"":retorno }
        form.btnCatalogo.actionPerformed = { e -> Thread.start { form.txtCaptura.text = form.txtCaptura.text + catArticulos(); form.txtCaptura.requestFocus() } }
        form.btnTerminar.actionPerformed = { e ->
            try {
            def detalles = []
            form.modelo.getDataVector().each {
                detalles << [IDArticulo:it[0], cantidad:it[2], precio:it[3], descuento:it[4], total:aDoble(it[5])]
            }
            def salida = serv.conectar().aplicarVenta(IDCaja, IDAlmacen, IDCliente, aDoble(form.txtSubtotal.text), aDoble(form.txtDescuento.text), 0, aDoble(form.txtTotal.text), detalles)
            serv.desconectar()
            (new Ticket(IDAlmacen, salida.ID)).probar()
            Dialogos.lanzarAlerta(salida.mensaje)
            form.dispose()
            lanzar()
            } catch(err) { Dialogos.error("Error: La venta no se pudo registrar", err) }
        }
    }

    static def lanzarCatalogo()
    {
        def cat = (new omoikane.formularios.CatalogoCajas())
        cat.setVisible(true);
        escritorio.getPanelEscritorio().add(cat)
        Herramientas.In2ActionX(cat, KeyEvent.VK_ESCAPE, "cerrar"   ) { cat.btnCerrar.doClick()   }
        Herramientas.In2ActionX(cat, KeyEvent.VK_DELETE, "eliminar" ) { cat.btnEliminas.doClick() }
        Herramientas.In2ActionX(cat, KeyEvent.VK_F4    , "detalles" ) { cat.btnDetalles.doClick() }
        Herramientas.In2ActionX(cat, KeyEvent.VK_F5    , "nuevo"    ) { cat.btnNuevo.doClick() }
        Herramientas.In2ActionX(cat, KeyEvent.VK_F6    , "modificar") { cat.btnModificar.doClick() }
        Herramientas.In2ActionX(cat, KeyEvent.VK_F7    , "imprimir" ) { cat.btnImprimir.doClick() }
        Herramientas.iconificable(cat)
        cat.toFront()
        try { cat.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario catalogo de cajas", Herramientas.getStackTraceString(e)) }
        cat.txtBusqueda.requestFocus()
        poblarCajas(cat.getTablaCajas(),"")
    }

    static def lanzarCatalogoDialogo()
    {
        def foco=new Object()
        def cat = (new omoikane.formularios.CatalogoCajas())
        cat.setVisible(true);
        escritorio.getPanelEscritorio().add(cat)
        Herramientas.In2ActionX(cat, KeyEvent.VK_ESCAPE, "cerrar"   ) { cat.btnCerrar.doClick()   }
        Herramientas.In2ActionX(cat, KeyEvent.VK_DELETE, "eliminar" ) { cat.btnEliminas.doClick() }
        Herramientas.In2ActionX(cat, KeyEvent.VK_F4    , "detalles" ) { cat.btnDetalles.doClick() }
        Herramientas.In2ActionX(cat, KeyEvent.VK_F5    , "nuevo"    ) { cat.btnNuevo.doClick() }
        Herramientas.In2ActionX(cat, KeyEvent.VK_F6    , "modificar") { cat.btnModificar.doClick() }
        Herramientas.In2ActionX(cat, KeyEvent.VK_F7    , "imprimir" ) { cat.btnImprimir.doClick() }
        Herramientas.iconificable(cat)
        cat.toFront()
        try { cat.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario catÃ¡logo de cajas", Herramientas.getStackTraceString(e)) }
        cat.txtBusqueda.requestFocus()
        cat.internalFrameClosed = {synchronized(foco){foco.notifyAll()} }
        cat.txtBusqueda.keyPressed = { if(it.keyCode == it.VK_ENTER) cat.btnAceptar.doClick() }
        def retorno
        cat.btnAceptar.actionPerformed = { def catTab = cat.tablaCajas; retorno = catTab.getModel().getValueAt(catTab.getSelectedRow(), 0) as int; cat.btnCerrar.doClick(); }
        poblarCajas(cat.getTablaCajas(),"")
        synchronized(foco){foco.wait()}
        retorno
    }

    static def lanzarFormNuevoCaja()
    {
        def formCaja = new omoikane.formularios.CajaDetalles()
        formCaja.setVisible(true)
        escritorio.getPanelEscritorio().add(formCaja)
        Herramientas.iconificable(formCaja)
        formCaja.toFront()
        try { formCaja.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles Caja", Herramientas.getStackTraceString(e)) }
        formCaja.setEditable(true);
        formCaja.setModoNuevo();
        formCaja
    }
        static def guardar(formCaja)
    {
        Herramientas.verificaCampos {
        def descripcion = formCaja.getTxtDescripcion()
        Herramientas.verificaCampo(descripcion,/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+áéíóúü]+)$/,"Descripcion sólo puede incluír números, letras, espacios, á, é, í, ó, ú, ü, _, -, * y +.")

        try {
            def serv = Nadesico.conectar()
            Dialogos.lanzarAlerta(serv.addCaja(descripcion))
        } catch(e) { Dialogos.error("Error al enviar a la base de datos. El grupo no se registró", e) }
        formCaja.dispose()
        }
    }

    static def lanzarDetallesCaja(ID)
    {
        def formCaja = new omoikane.formularios.CajaDetalles()
        formCaja.setVisible(true)
        escritorio.getPanelEscritorio().add(formCaja)
        Herramientas.iconificable(formCaja)
        formCaja.toFront()
        try { formCaja.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles Caja", Herramientas.getStackTraceString(e)) }

        def alm         = Nadesico.conectar().getCaja(ID)

        formCaja.setTxtIDCaja         alm.id_caja       as String
        formCaja.setTxtDescripcion    alm.descripcion
        formCaja.setTxtCreado         alm.creado        as String
        formCaja.setTxtUModificacion  alm.uModificacion as String
        formCaja.setModoDetalles();
        formCaja
    }

    static def poblarCajas(tablaMovs,txtBusqueda)
    {

        def dataTabMovs = tablaMovs.getModel()
         try {
            def movimientos = Nadesico.conectar().getRows(queryCaja =("SELECT * FROM cajas WHERE (descripcion LIKE '%"+txtBusqueda+"%' OR id_caja LIKE '%"+txtBusqueda+"%')") )
            def filaNva = []

            movimientos.each {
                SimpleDateFormat sdf  = new SimpleDateFormat("dd-MM-yyyy");
                def fecha = sdf.format(it.creado);
                filaNva = [it.id_caja, it.descripcion,fecha]
                dataTabMovs.addRow(filaNva.toArray())
            }
        } catch(Exception e) {
            Dialogos.lanzarDialogoError(null, "Error grave. No hay conexion con la base de datos!", omoikane.sistema.Herramientas.getStackTraceString(e))
        }
    }

    static def lanzarModificarCaja(ID)
    {
        def formCaja = new omoikane.formularios.CajaDetalles()
        formCaja.setVisible(true)
        escritorio.getPanelEscritorio().add(formCaja)
        Herramientas.iconificable(formCaja)
        formCaja.toFront()
        try { formCaja.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles Caja", Herramientas.getStackTraceString(e)) }

        def alm         = Nadesico.conectar().getCaja(ID)

        formCaja.setTxtIDCaja           alm.id_caja    as String
        formCaja.setTxtDescripcion      alm.descripcion
        formCaja.setTxtCreado           alm.creado as String
        formCaja.setTxtUModificacion    alm.uModificacion as String
        formCaja.setModoModificar();
        formCaja
    }
    static def modificar(formCaja)
    {
        Herramientas.verificaCampos {
        Herramientas.verificaCampo(formCaja.getTxtDescripcion(),/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+áéíóúü]+)$/,"Descripcion sólo puede incluír números, letras, espacios, á, é, í, ó, ú, ü, _, -, * y +.")
        def serv = Nadesico.conectar()
            Dialogos.lanzarAlerta(serv.modCaja(formCaja.getTxtIDCaja(),formCaja.getTxtDescripcion()))
        }
    }

    static def eliminarCaja(ID)
    {
        def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        db.execute("DELETE FROM cajas WHERE id_caja = " + ID)
        db.close()
        Dialogos.lanzarAlerta("Caja " + ID + " supuestamente eliminado")
    }

    static def lanzarImprimir()
  {
        def reporte = new Reporte('omoikane/reportes/ReporteCajas.jasper', [QueryTxt:queryCaja]);
        reporte.lanzarPreview()
  }
}
