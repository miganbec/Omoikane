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
        def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        def tablaAlmacenes = db.dataSet('almacenes')
        tablaAlmacenes.add(descripcion:descripcion)
        db.close()
        Dialogos.lanzarAlerta("Almacen $descripcion agregado.")
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

        def db   = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        def almacen = db.rows("SELECT * FROM almacenes WHERE id_almacen = $ID")

        formAlmacen.setTxtIDAlmacen    ((String)almacen[0].id_almacen)
        formAlmacen.setTxtDescripcion   (almacen[0].descripcion)
        formAlmacen.setTxtUModificacion (almacen[0].uModificacion as String)
        formAlmacen.setModoDetalles();

        db.close()
        formAlmacen
    }

    static def poblarAlmacenes(tablaMovs,txtBusqueda)
    {

        def dataTabMovs = tablaMovs.getModel()
         try {
            def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
            def movimientos = db.rows(queryAlmacen =("SELECT * FROM almacenes WHERE (descripcion LIKE '%"+txtBusqueda+"%' OR id_almacen LIKE '%"+txtBusqueda+"%')") )
            db.close()
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

        def db   = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        def almacen = db.rows("SELECT * FROM Almacenes WHERE id_almacen = $ID")
        db.close()

        formAlmacen.setTxtIDAlmacen    ((String)almacen[0].id_almacen)
        formAlmacen.setTxtDescripcion   (almacen[0].descripcion)
        formAlmacen.setTxtUModificacion (almacen[0].uModificacion as String)
        formAlmacen.setModoModificar();
        formAlmacen
    }
    static def modificar(formAlmacen)
    {
        Herramientas.verificaCampos {
        Herramientas.verificaCampo(formAlmacen.getTxtDescripcion(),/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+áéíóúü]+)$/,"Descripcion sólo puede incluír números, letras, espacios, á, é, í, ó, ú, ü, _, -, * y +.")
        def db   = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        db.executeUpdate("UPDATE almacenes SET descripcion = ? WHERE id_almacen = ?"
            , [
                formAlmacen.getTxtDescripcion(),
                formAlmacen.getTxtIDAlmacen()
            ])
        Dialogos.lanzarAlerta("Almacen modificado con Exito!")
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
            def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
            def movimientos = db.rows(queryMovs = ("SELECT * FROM movimientos_almacen WHERE (descripcion LIKE '%"+busqueda+"%' OR monto LIKE '%"+busqueda+"%' OR folio LIKE '%"+busqueda+"%')" + whereFecha))

            db.close()
            def filaNva = []
            def fecha

            movimientos.each {
                fecha = it.fecha.toString()
                fecha = fecha.split("-")[2]+"-"+fecha.split("-")[1]+"-"+fecha.split("-")[0]
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
            def db                = null
            def IDMov             = -1

            try {

                db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
                db.connection.autoCommit = false

                IDMov   = db.executeInsert("INSERT INTO movimientos_almacen SET id_almacen = ?, fecha = ?, descripcion = ?, tipo = ?, monto = ?, folio = ?", [almacen, fecha, descripcion, tipo, granTotal, folio])

                IDMov = IDMov[0][0]
                def movData = []


                tabPrincipalArray.each { row ->
                    Herramientas.verificaCampo(row[2],/^([0-9]*[\.]{0,1}[0-9]+)$/,"Costo sólo puede incluír números reales positivos")
                    Herramientas.verificaCampo(row[3],/^([0-9]*[\.]{0,1}[0-9]+)$/,"Cantidad sólo puede incluír números reales positivos")
                    movData << [codigo:row[0], descripcion:row[1], costo:row[2], cantidad:row[3], total:row[4]]
                    //aquí guardar
                    db.executeUpdate("INSERT INTO movimientos_almacen_detalles SET id_movimiento = ?, id_articulo = ?, cantidad = ?, costo = ?, id_almacen = ?",
                    [IDMov, Articulos.getArticulo("codigo = '${row[0]}'").id_articulo, row[3], row[2], almacen])
                }


                def id_art, cant, cost;
                tabPrincipalArray.each { row ->
                    id_art = db.rows("SELECT id_articulo FROM articulos WHERE codigo = '" + row[0]+"'")[0].id_articulo
                    cost   = row[2]
                    cant   = row[3]
                    switch(tipo) {
                        case "Entrada al almacén":
                            cant = "+$cant"
                        break
                        case "Salida del almacén":
                            cant = "-$cant"
                        break
                        case "Ajuste": break
                        default:
                            throw new Exception("Tipo de movimiento erróneo (pj. entrada, salida, ajuste)")
                        break
                    }
                    Existencias.cambiar(db, almacen, id_art, cant)
                    Precios.modificar(db, almacen, id_art, costo:cost)
                }

                db.commit()
                Dialogos.lanzarAlerta("Movimiento \"$descripcion\" hecho al almacén.")
                formMovimiento.dispose()
            } catch(Exception e) {
                db.rollback()
                Dialogos.lanzarDialogoError(null, "Error al enviar a la base de datos. El movimiento no se Guardo verifique almacen ,cantidad ,costo .", omoikane.sistema.Herramientas.getStackTraceString(e))
            } finally {
                db.connection.autoCommit = true
            }
        } catch(Exception e) {
            Dialogos.lanzarDialogoError(null, "Error en la captura de los datos del formulario NO DEJE LINEAS EN BLANCO SOLO FOLIO PUEDE ESTAR VACIO.", omoikane.sistema.Herramientas.getStackTraceString(e))
        }
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

        def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        def mov = db.rows("SELECT * FROM movimientos_almacen WHERE id_movimiento = $ID")

        mov = mov[0]
        form.setTipoMovimiento(mov.tipo)
        form.setAlmacen(mov.id_almacen as String)
        form.setFecha(mov.fecha as String)
        form.setDescripcion(mov.descripcion)
        form.setFolio(mov.folio as String)

        def tabMatriz = []
        def articulo

        db.eachRow("SELECT * FROM movimientos_almacen_detalles WHERE id_movimiento = $ID")
        {
            articulo = Articulos.getArticulo("id_articulo = ${it.id_articulo}")
            tabMatriz << [articulo.codigo, articulo.descripcion, it.costo, it.cantidad, it.cantidad*it.costo]
            
        }
        db.close()
        //def lmov = xml2tabla(mov.detalles)
        form.setTablaPrincipal(tabMatriz as List)
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

