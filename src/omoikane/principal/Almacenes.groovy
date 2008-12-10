/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.principal

import omoikane.principal.*
import omoikane.sistema.*
import groovy.sql.*;
import groovy.inspect.swingui.*;
import groovy.xml.MarkupBuilder
import groovy.util.XmlParser;
import java.sql.*;
import java.util.Calendar;
import groovy.swing.*;
import java.text.*;

/**
 *
 * @author Octavio
 */
class Almacenes {
    static def lastMovID  = -1
    static def queryMovs  = ""
    static def queryAlmacen  = ""
    static def escritorio = omoikane.principal.Principal.escritorio
//-------------------Funciones CRUD almacenes
    static def lanzarCatalogo()
    {
        def cat = (new omoikane.formularios.CatalogoAlmacenes())
        cat.setVisible(true);
        escritorio.getPanelEscritorio().add(cat)
        cat.toFront()
        try { cat.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario catÃ¡logo de almacenes", Herramientas.getStackTraceString(e)) }
        cat.txtBusqueda.requestFocus()
        poblarAlmacenes(cat.getTablaAlmacenes(),"")

    }

    static def lanzarCatalogoDialogo()
    {
        def foco=new Object()
        def cat = (new omoikane.formularios.CatalogoAlmacenes())
        cat.setVisible(true);
        escritorio.getPanelEscritorio().add(cat)
        cat.toFront()
        try { cat.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario catÃ¡logo de almacenes", Herramientas.getStackTraceString(e)) }
        cat.txtBusqueda.requestFocus()
        cat.internalFrameClosed = {synchronized(foco){foco.notifyAll()} }
        cat.txtBusqueda.keyPressed = { if(it.keyCode == it.VK_ENTER) cat.btnAceptar.doClick() }
        def retorno
        cat.btnAceptar.actionPerformed = { def catTab = cat.tablaAlmacenes; retorno = catTab.getModel().getValueAt(catTab.getSelectedRow(), 0) as int; cat.btnCerrar.doClick(); }
        poblarAlmacenes(cat.getTablaAlmacenes(),"")
        synchronized(foco){foco.wait()}
        retorno
    }

    static def lanzarFormNuevoAlmacen()
    {
        def formAlmacen = new omoikane.formularios.Almacen()
        formAlmacen.setVisible(true)
        escritorio.getPanelEscritorio().add(formAlmacen)
        formAlmacen.toFront()
        try { formAlmacen.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles almacen", Herramientas.getStackTraceString(e)) }
        formAlmacen.setEditable(true);
        formAlmacen.setModoNuevo();
        formAlmacen
    }
        static def guardar(formAlmacen)
    {
        Herramientas.verificaCampos {
        def descripcion = formAlmacen.getTxtDescripcion()
        Herramientas.verificaCampo(descripcion,/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+áéíóúü]+)$/,"Descripcion sólo puede incluír números, letras, espacios, á, é, í, ó, ú, ü, _, -, * y +.")

        try {
            def serv = Nadesico.conectar()
            Dialogos.lanzarAlerta(serv.addAlmacen(descripcion))
        } catch(e) { Dialogos.error("Error al enviar a la base de datos. El grupo no se registró", e) }

        formAlmacen.dispose()
        }
    }

    static def lanzarDetallesAlmacen(ID)
    {
        def formAlmacen = new omoikane.formularios.Almacen()
        formAlmacen.setVisible(true)
        escritorio.getPanelEscritorio().add(formAlmacen)
        formAlmacen.toFront()
        try { formAlmacen.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles almacen", Herramientas.getStackTraceString(e)) }

        def alm         = Nadesico.conectar().getAlmacen(ID)

        formAlmacen.setTxtIDAlmacen      alm.id_almacen    as String
        formAlmacen.setTxtDescripcion    alm.descripcion
        formAlmacen.setTxtUModificacion  alm.uModificacion as String
        formAlmacen.setModoDetalles();
        formAlmacen
    }

    static def poblarAlmacenes(tablaMovs,txtBusqueda)
    {

        def dataTabMovs = tablaMovs.getModel()
         try {
            def movimientos = Nadesico.conectar().getRows(queryAlmacen =("SELECT * FROM almacenes WHERE (descripcion LIKE '%"+txtBusqueda+"%' OR id_almacen LIKE '%"+txtBusqueda+"%')") )
            def filaNva = []

            movimientos.each {
                filaNva = [it.id_almacen, it.descripcion]
                dataTabMovs.addRow(filaNva.toArray())
            }
        } catch(Exception e) {
            Dialogos.lanzarDialogoError(null, "Error grave. No hay conexion con la base de datos!", omoikane.sistema.Herramientas.getStackTraceString(e))
        }
    }

    static def lanzarModificarAlmacen(ID)
    {
        def formAlmacen = new omoikane.formularios.Almacen()
        formAlmacen.setVisible(true)
        escritorio.getPanelEscritorio().add(formAlmacen)
        formAlmacen.toFront()
        try { formAlmacen.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles Almacen", Herramientas.getStackTraceString(e)) }

        def alm         = Nadesico.conectar().getAlmacen(ID)

        formAlmacen.setTxtIDAlmacen      alm.id_almacen    as String
        formAlmacen.setTxtDescripcion    alm.descripcion
        formAlmacen.setTxtUModificacion  alm.uModificacion as String
        formAlmacen.setModoModificar();
        formAlmacen
    }
    static def modificar(formAlmacen)
    {
        Herramientas.verificaCampos {
        Herramientas.verificaCampo(formAlmacen.getTxtDescripcion(),/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+áéíóúü]+)$/,"Descripcion sólo puede incluír números, letras, espacios, á, é, í, ó, ú, ü, _, -, * y +.")
        def serv = Nadesico.conectar()
            Dialogos.lanzarAlerta(serv.modAlmacen(formAlmacen.getTxtIDAlmacen(),formAlmacen.getTxtDescripcion()))
        }
    }

    static def eliminarAlmacen(ID)
    {
        def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        db.execute("DELETE FROM almacenes WHERE id_almacen = " + ID)
        db.close()
        Dialogos.lanzarAlerta("Almacen " + ID + " supuestamente eliminado")
    }

    static def lanzarImprimir(ID)
  {
        def reporte = new Reporte('omoikane/reportes/ReporteAlmacenes.jasper', [QueryTxt:queryAlmacen]);
        reporte.lanzarPreview()
  }

//-------------------Funciones Movimientos de almacén
    static def lanzarMovimientos()
    {
        def cat = (new omoikane.formularios.MovimientosAlmacen())
        cat.setVisible(true);
        escritorio.getPanelEscritorio().add(cat)
        cat.toFront()
        try { cat.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario movimientos de almacén", Herramientas.getStackTraceString(e)) }
        cat.txtBusqueda.requestFocus()

        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendario = Calendar.getInstance();
        def fechaHasta = sdf.format(calendario.getTime())
        calendario.add(Calendar.DAY_OF_MONTH, -30);
        def fechaDesde = sdf.format(calendario.getTime())

        //Poblar tabla de movimientos
        poblarMovimientos(cat.getTablaMovimientos(), "", fechaDesde, fechaHasta)
        cat
    }
    static def poblarMovimientos(tablaMovs, busqueda, fechaDesde, fechaHasta)
    {
        def whereFecha = ""
        // Determina si se hará una búsqueda por fecha y de ser asi crea la clausua necesaria
        if(fechaHasta != "" || fechaDesde != "")
        {
            if(fechaHasta == "" || fechaDesde == "")
            {
                Dialogos.lanzarAlerta("El intervalo de fechas para filtrar resultados está incompleto, completelo o vacíelo para dejar de leer este mensaje.")
                fechaDesde = ""; fechaHasta = ""
            } else {
                whereFecha = " AND fecha >= '$fechaDesde' AND fecha <= '$fechaHasta'"
            }
        }
        //Comienza la población
        def dataTabMovs = tablaMovs.getModel()
        try {
            
            def movimientos = Nadesico.conectar().getRows(queryMovs = ("SELECT * FROM movimientos_almacen WHERE (descripcion LIKE '%"+busqueda+"%' OR monto LIKE '%"+busqueda+"%' OR folio LIKE '%"+busqueda+"%')" + whereFecha))
            def filaNva = []
            def fecha

            movimientos.each {
                //ObjectBrowser.inspect it.fecha
                fecha = String.format("%02d-%02d-%04d", it.fecha.getDate(), (it.fecha.getMonth()+1), (it.fecha.getYear()+1900))
                filaNva = [fecha, it.id_movimiento, it.folio, it.id_almacen, it.descripcion, it.tipo, it.monto]
                dataTabMovs.addRow(filaNva.toArray())
            }
        } catch(Exception e) {
            Dialogos.lanzarDialogoError(null, "Error grave. No hay conexión con la base de datos!", omoikane.sistema.Herramientas.getStackTraceString(e))
        }
    }
    static def lanzarImprimirMovimientos()
    {
        def reporte = new Reporte('omoikane/reportes/MovimientosAlmacen.jasper', [QueryTxt:queryMovs]);
        reporte.lanzarPreview()
    }

    static def lanzarNuevoMovimiento()
    {
        def nvo = (new omoikane.formularios.MovimientoAlmacen())
        nvo.cellCodigo.component.keyPressed = { def src = it; if(it.getKeyCode()==it.VK_F2) { Thread.start { src.getSource().setText(Articulos.lanzarDialogoCatalogo()); src.getSource().requestFocus() } } }
        nvo.setVisible(true);
        escritorio.getPanelEscritorio().add(nvo)
        nvo.setModoNuevo()
        nvo.toFront()
        SwingBuilder.build {
          //Al presionar F2: (lanzarCatalogoDialogo)

          nvo.getFieldAlmacen().keyPressed = { if(it.keyCode == it.VK_F2) Thread.start { nvo.almacen = Almacenes.lanzarCatalogoDialogo() as String; nvo.getFieldAlmacen().requestFocus() } }
        }
        try { nvo.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario nuevo movimiento de almacén", Herramientas.getStackTraceString(e)) }
        nvo
    }

    static def tabla2xml(def tabla)
    {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)

        tabla.each { row ->
            xml.fila() {
                row.each { col ->
                    columna(col)
                }
            }
        }
        writer.toString()
    }
    static def xml2tabla(def xmlString)
    {
        def row   = []
        def tabla = []
        xmlString = "<document>$xmlString</document>"
        def xml = new groovy.util.XmlParser().parseText(xmlString)
        xml.fila.each { fila ->
            row = []
            fila.each { columna ->
                row << columna.text()
            }
            tabla << row
        }
        tabla
    }

    static def guardarMovimiento(formMovimiento)
    {
        Herramientas.verificaCampos{
            Herramientas.verificaCampo(formMovimiento.getAlmacen(),/^([0-9]+)$/,"Almacen sólo puede incluír números enteros.")
            Herramientas.verificaCampo(formMovimiento.getDescripcion(),/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+áéíóúü]+)$/,"Descripción sólo puede incluír números, letras, espacios, á, é, í, ó, ú, ü, _, -, * y +.")
            Herramientas.verificaCampo(formMovimiento.getFolio(),/^([a-zA-Z0-9_\-\s\ñ\Ñ]*)$/,"Folio sólo puede estar vacío o incluír números, letras, espacios, _ y -.")

        try {
            def tipo              = formMovimiento.getTipoMovimiento()
            def almacen           = formMovimiento.getAlmacen()
            def fecha             = formMovimiento.getFecha()
            def descripcion       = formMovimiento.getDescripcion()
            def folio             = formMovimiento.getFolio()
            def tabPrincipalArray = formMovimiento.getTablaPrincipal()
            def granTotal         = formMovimiento.getGranTotal() as Double

            almacen               = java.lang.Integer.valueOf(almacen)
            //def tablaPrincipal    = tabla2xml(tabPrincipalArray)
                try {
                    def serv = Nadesico.conectar()
                    def msj = serv.addMovimiento([almacen:almacen, fecha:fecha, descripcion:descripcion, tipo:tipo, granTotal:granTotal, folio:folio],tabPrincipalArray)
                    Dialogos.lanzarAlerta(" "+msj)
                    formMovimiento.dispose()
                }catch(Exception e) {
           Dialogos.lanzarDialogoError(null, "Error en la base de datos", omoikane.sistema.Herramientas.getStackTraceString(e))
        }
                }catch(Exception e) {
            Dialogos.lanzarDialogoError(null, "Error en la captura de los datos del formulario NO DEJE LINEAS EN BLANCO SOLO FOLIO PUEDE ESTAR VACIO.", omoikane.sistema.Herramientas.getStackTraceString(e))}
        }
    }

    static def lanzarDetallesMovimiento(ID)
    {
        lastMovID = ID
        def form = (new omoikane.formularios.MovimientoAlmacen())
        form.setVisible(true);
        escritorio.getPanelEscritorio().add(form)
        form.toFront()
        try { form.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario nuevo movimiento de almacén", Herramientas.getStackTraceString(e)) }

        def mov         = Nadesico.conectar().getMovimiento(ID)

        form.setTipoMovimiento(mov.tipo)
        form.setAlmacen(mov.id_almacen as String)
        form.setFecha(mov.fecha as String)
        form.setDescripcion(mov.descripcion)
        form.setFolio(mov.folio as String)
        //def lmov = xml2tabla(mov.detalles)
        form.setTablaPrincipal(mov.tabMatriz as List)
        form.setModoDetalles()
        form
    }
    static def lanzarImprimirMovimiento(form)
    {
        /*def movData = []
        def i       = 0
        form.getTablaPrincipal().each { row ->
            movData[i]                = [codigo:row[0], descripcion:row[1]]
            //movData[i]['codigo']      = row[0]
            //movData[i]['descripcion'] = row[1]
            movData[i]['costo']       = row[2]
            movData[i]['cantidad']    = row[3] as Double
            movData[i]['total']       = row[4]
            i++
        }

        def reporte = new Reporte('omoikane/reportes/MovimientoAlmacen.jasper', movData);
        reporte.lanzarPreview()
    */
        def reporte = new Reporte('omoikane/reportes/MovimientoAlmacenEncabezado.jasper',[SUBREPORT_DIR:"omoikane/reportes/",IDMov:lastMovID as String]);
          reporte.lanzarPreview()
        }
    static def groovyPort(String codigo) { Eval.me(codigo) }
    
    //*****************************************************************************************************
    //*****************************************************************************************************
    //Fuera de servicio por ir contra la integridad del modelo
    // una modificación debería ser hecha con otro movimiento de almacen para que quede registro
    // del acto
    static def lanzarModificarMovimiento(ID)
    {
        def form = lanzarDetallesMovimiento(ID)
        form.setID(ID)
        form.setModoModificaciones()
        form
    }
    static def modificarMovimiento(formMovimiento)
    {
        def ID                = formMovimiento.getID()
        def tipo              = formMovimiento.getTipoMovimiento()
        def almacen           = formMovimiento.getAlmacen() as Integer
        def fecha             = formMovimiento.getFecha()
        def descripcion       = formMovimiento.getDescripcion()
        def folio             = formMovimiento.getFolio()
        def tablaPrincipal    = formMovimiento.getTablaPrincipal()
        def granTotal         = formMovimiento.getGranTotal() as Double
        tablaPrincipal        = tabla2xml(tablaPrincipal)

        def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        def IDMov = db.executeUpdate("UPDATE movimientos_almacen SET id_almacen = ?, descripcion = ?, fecha = ?, tipo = ?, monto = ?, folio = ?, detalles = ? WHERE id_movimiento = ?"
            , [
                almacen,
                descripcion,
                fecha,
                tipo,
                granTotal,
                folio,
                tablaPrincipal,
                ID
            ])
        db.close()
        Dialogos.lanzarAlerta("Movimiento de almacén modificado exitosamente!.")
    }
    //*****************************************************************************************************
    //*****************************************************************************************************
}

