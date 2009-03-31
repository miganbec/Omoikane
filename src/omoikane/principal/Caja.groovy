/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.principal

import omoikane.sistema.*

import javax.swing.table.*
import groovy.inspect.swingui.*
import java.text.*;
import groovy.sql.*;
import omoikane.sistema.*;
import javax.swing.event.*;
import java.awt.event.*;
import groovy.swing.*
import java.util.Calendar;
import static omoikane.sistema.Usuarios.*;
import static omoikane.sistema.Permisos.*;

/** 
 * @author Usuario
 */

class Caja {
    static def IDCaja    = Principal.IDCaja
    static def IDAlmacen = Principal.IDAlmacen
    static def IDCliente = 1
    static def IDUsuario = 0
    static def queryCaja  = ""
    static def escritorio = omoikane.principal.Principal.escritorio

    static def abrirCaja(ID = -1)
    {
        if(cerrojo(PMA_ABRIRCAJAS)){
            if(ID == -1){
                def retorna = false
                def foco = new Object()
                def form = new omoikane.formularios.AbrirCaja()
                form.visible = true
                escritorio.getPanelEscritorio().add(form)
                Herramientas.centrarAbsoluto(form);
                Herramientas.iconificable(form)
                Herramientas.In2ActionX(form          , KeyEvent.VK_ESCAPE, "cerrar"   ) { form.btnCerrar.doClick()        }
                Herramientas.In2ActionX(form.txtIDCaja, KeyEvent.VK_ESCAPE, "cerrar"   ) { form.btnCerrar.doClick()        }
                Herramientas.In2ActionX(form          , KeyEvent.VK_F2    , "buscar"   ) { form.btnBuscar.doClick()        }
                Herramientas.In2ActionX(form          , KeyEvent.VK_ENTER , "aceptar"  ) { form.btnAceptar.doClick()       }
                form.txtIDCaja.keyPressed = { e ->
                    if(e.keyCode==e.VK_ENTER) { form.btnAceptar.doClick() }
                }
                def catArticulos = { def retorno = Caja.lanzarCatalogoDialogo() as String; return retorno==null?"":retorno }
                form.btnBuscar.actionPerformed = { e -> Thread.start { form.txtIDCaja.text = form.txtIDCaja.text + catArticulos(); form.txtIDCaja.requestFocus() } }
                form.btnAceptar.actionPerformed= { e ->
                    def serv = Nadesico.conectar();
                    def txtID= form.txtIDCaja.text
                    def res  = serv.getCaja(txtID);
                    if(res == 0) {
                        Dialogos.lanzarAlerta("No exíste esa caja")
                    }else{
                        if(serv.cajaAbierta(txtID) != false) { Dialogos.lanzarAlerta("La caja ya estaba abierta!!!")  }else{
                            def caja = serv.getCaja(txtID)
                            SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            def hAbierta = sdf.format(caja.horaAbierta)
                            def hCerrada = sdf.format(caja.horaCerrada)
                            if(serv.getCorteWhere("id_caja = $txtID AND desde = '$hAbierta' AND hasta = '$hCerrada'") == 0 && hAbierta != "1970-01-01 00:00:00") {
                                Dialogos.lanzarAlerta("Se debe realizar el corte de caja para ésta caja antes de volver a abrirla")
                            }else{
                                serv.abrirCaja(txtID)
                                retorna = true
                                Dialogos.lanzarAlerta("Se inició la sesión de la caja correctamente")
                                form.dispose()
                            }
                        }
                    }
                    serv.desconectar() }
                form.toFront()
                try { form.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario abrir caja", Herramientas.getStackTraceString(e)) }
                form.internalFrameClosed = {synchronized(foco){foco.notifyAll()} }
                form.txtIDCaja.requestFocus()
                synchronized(foco){foco.wait()}
                return retorna
            }
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def lanzar() 
    {
        def abierta = Sucursales.abierta(Principal.IDAlmacen)
        switch(abierta) {
            
            case -1: Dialogos.lanzarAlerta("Configuración de sucursal-almacen errónea."); break;
            case  0: abierta = Sucursales.abrirSucursal(Principal.IDAlmacen);  //Sin break para continuar
            case  1:
                    if(abierta!=1) { break; }
                    def serv = Nadesico.conectar()
                    def cajaAbierta = serv.cajaAbierta(IDCaja)
                    serv.desconectar()
                    Thread.start {
                    cajaAbierta = cajaAbierta?true:abrirCaja()
                    if(cajaAbierta) { lanzarCaja() }
                }
            break;
        }
    }

    static def lanzarCaja() {
        if(cerrojo(PMA_LANZARCAJA)){
            def form = new omoikane.formularios.Caja()
            def modelo = new CajaTableModel()
            form.tablaVenta.setModel(modelo)
            form.modelo = modelo
            escritorio.getPanelEscritorio().add(form)
            Herramientas.centrarVentana(form);
            form.setVisible(true);
            Herramientas.iconificable(form)
            Herramientas.setColumnsWidth(form.tablaVenta, [0.48,0.12,0.12,0.12,0.13]);
            Herramientas.In2ActionX(form, KeyEvent.VK_ESCAPE, "cerrar"   ) { form.btnCerrar.doClick()        }
            Herramientas.In2ActionX(form.txtCaptura, KeyEvent.VK_ESCAPE, "cerrar"   ) { form.btnCerrar.doClick()        }
            Herramientas.In2ActionX(form, KeyEvent.VK_F8    , "imprimir" ) { form.btnImprimir.doClick()      }
            Herramientas.In2ActionX(form, KeyEvent.VK_F12   , "cancelar" ) { form.btnCancelacion.doClick()   }
            Herramientas.In2ActionX(form.btnCerrar, KeyEvent.VK_ESCAPE, "cerrar2") { form.btnCerrar.doClick()        }
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
                def dat   = form.modelo.getDataMap()
                def sumas = [0.0,0.0,0.0,0.0]
                dat.each { linea ->
                sumas[0] += linea['Precio'] as Double; sumas[1] += linea['Descuento'] as Double; sumas[2] += aDoble(linea['Total']); sumas[3] += linea['Impuestos'] as Double
                }
                form.txtNArticulos.text = dat.size()
                form.txtSubtotal.text   = cifra (sumas[2])
                form.txtTotal.text      = cifra (sumas[2])
                form.txtDescuento.text  = cifra (sumas[1])
                form.impuestos          = sumas[3]
            }
            def addArtic = { codigo ->
                try {
                    def captura = form.txtCaptura.text.split("\\*")
                    def cantidad= captura.size()==1?1:captura[0..captura.size()-2].inject(1) { acum, i -> acum*(i as Double) }
                    def art     = serv.codigo2Articulo(IDAlmacen, captura[captura.size()-1])
                    if(art == null || art == 0) { Dialogos.lanzarAlerta("Articulo no encontrado!!"); } else {
                        def precio  = serv.getPrecio(art.id_articulo, IDAlmacen, IDCliente)
                        def total   = cifra(cantidad * precio.total)
                        form.txtCaptura.text = ""
                        //form.modelo.addRow([art.id_articulo,art.descripcion,cantidad,precio.total,precio['descuento$'],total].toArray())
                        form.modelo.addRowMap(["ID Artículo":art.id_articulo, "Concepto" :     art.descripcion, "Cantidad" :            cantidad,
                                               "Precio"     :   precio.total, "Descuento":precio['descuento$'], "Impuestos": precio['impuestos'],
                                               "Total"      :          total])
                        sumarTodo()
                        form.repaint()
                    }
                } catch(e) { Dialogos.error("Error al obtener información de nadesico!", e) }
            }
            form.txtCaptura.keyPressed = {   e ->
                if(e.keyCode==e.VK_ENTER) if(form.txtCaptura.text != "") { addArtic(form.txtCaptura.text) } else { form.btnTerminar.doClick() }
                //Al presionar   F2: (lanzarCatalogoDialogo)
                if(e.keyCode == e.VK_F2) { form.btnCatalogo.doClick() }
                if(e.getKeyCode() == e.VK_DOWN)
                {
                    int sigFila = form.tablaVenta.getSelectedRow()+1;
                    if(sigFila < form.tablaVenta.getRowCount())
                    {
                        form.tablaVenta.setRowSelectionInterval(sigFila, sigFila);
                        form.tablaVenta.scrollRectToVisible(form.tablaVenta.getCellRect(sigFila, 1, true));
                    }
                }
                if(e.getKeyCode() == e.VK_UP)
                {
                    int antFila = form.tablaVenta.getSelectedRow()-1;
                    if(antFila >= 0) {
                        form.tablaVenta.setRowSelectionInterval(antFila, antFila);
                        form.tablaVenta.scrollRectToVisible(form.tablaVenta.getCellRect(antFila, 1, true));
                    }
                }

            }
            def catArticulos = { def retorno = Articulos.lanzarDialogoCatalogo() as String; return retorno==null?"":retorno }
            form.btnCatalogo.actionPerformed = { e -> Thread.start { form.txtCaptura.text = form.txtCaptura.text + catArticulos(); form.txtCaptura.requestFocus() } }
            form.btnTerminar.actionPerformed = { e ->
                try {
                if(form.modelo.getDataMap().size() == 0) { throw new Exception("Venta vacía") }
                    def detalles = []
                    form.modelo.getDataMap().each {
                        detalles << [IDArticulo:it['ID Artículo'], cantidad:it['Cantidad'], precio:it['Precio'], descuento:it['Descuento'], total:aDoble(it['Total'])]
                    }
                    def salida = serv.conectar().aplicarVenta(IDCaja, IDAlmacen, IDCliente, IDUsuario, aDoble(form.txtSubtotal.text), aDoble(form.txtDescuento.text), form.impuestos, aDoble(form.txtTotal.text), detalles)
                    serv.desconectar()
                    def comprobante = new Comprobantes()
                    comprobante.ticket(IDAlmacen, salida.ID)//imprimir ticket
                    comprobante.probar()//imprimir ticket
                    Dialogos.lanzarAlerta(salida.mensaje)
                    form.dispose()
                    lanzar()
                } catch(err) { Dialogos.error("Error: La venta no se pudo registrar", err) }
            }
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def lanzarCatalogo()
    {
        if(cerrojo(PMA_ABRIRCAJA)){
            def cat = (new omoikane.formularios.CatalogoCajas())
            cat.setVisible(true);
            escritorio.getPanelEscritorio().add(cat)
            Herramientas.In2ActionX(cat, KeyEvent.VK_ESCAPE, "cerrar"   ) { cat.btnCerrar.doClick()   }
            cat.txtBusqueda.keyReleased = { if(it.keyCode == it.VK_ESCAPE) cat.btnCerrar.doClick() }
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
            return cat
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def lanzarCatalogoDialogo()
    {
            def foco=new Object()
            def cat = lanzarCatalogo()
            cat.setModoDialogo()
            cat.internalFrameClosed = {synchronized(foco){foco.notifyAll()} }
            cat.txtBusqueda.keyPressed = { if(it.keyCode == it.VK_ENTER) cat.btnAceptar.doClick() }
            def retorno
            cat.btnAceptar.actionPerformed = { def catTab = cat.tablaCajas; retorno = catTab.getModel().getValueAt(catTab.getSelectedRow(), 0) as String; cat.btnCerrar.doClick(); }
            synchronized(foco){foco.wait()}
            retorno
    }
    
    static def lanzarTotalVentasDia(IDCaja, cortar=false) 
    {
        if(cerrojo(PMA_TOTALVENTA)){
            def serv = Nadesico.conectar()
            def res  = serv.getCaja(IDCaja);
            def newCorte
            if(res == 0) {Dialogos.lanzarAlerta("No exíste esa caja")} else {
                if(!serv.cajaAbierta(IDCaja)) {Dialogos.lanzarAlerta("La caja ya estaba cerrada")}
                def horas      = serv.getCaja(IDCaja)
                SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy @ hh:mm:ss a ");
                Calendar         fecha= Calendar.getInstance()
                if(serv.getCorteWhere("id_caja = $IDCaja AND desde = '${sdf.format(horas.horaAbierta)}' AND hasta = '${sdf.format(horas.horaCerrada)}'")!=0) {
                    Dialogos.lanzarAlerta("Ya se realizó corte de Caja y no se han hecho ventas desde el último corte de caja")
                } else {
                    if(cortar) { serv.cerrarCaja(IDCaja) }
                    horas = serv.getCaja(IDCaja)
                    def ventas= serv.sumaVentas(IDCaja, sdf.format(horas.horaAbierta), sdf.format(horas.horaCerrada))
                    def form  = Cortes.lanzarVentanaDetalles()
                    def caja  = serv.getCaja(IDCaja)
                    def desde = horas.horaAbierta
                    def hasta = horas.horaCerrada
                    form.setTxtDescuento     (ventas.descuento as String)
                    form.setTxtDesde         (sdf2.format(desde) as String)
                    form.setTxtFecha         (sdf2.format(fecha.getTime()) as String)
                    form.setTxtHasta         (sdf2.format(hasta) as String)
                    form.setTxtIDAlmacen     (caja.id_almacen as String)
                    form.setTxtIDCaja        (IDCaja as String)
                    //form.setTxtIDCorte       (ventas.id_corte as String)
                    form.setTxtImpuesto      (ventas.impuestos as String)
                    form.setTxtNumeroVenta   (ventas.nVentas as String)
                    form.setTxtSubtotal      (ventas.subtotal as String)
                    form.setTxtTotal         (ventas.total as String)
                    if(cortar) { 
                        newCorte=serv.addCorte(IDCaja, caja.id_almacen, ventas.subtotal, ventas.descuento, ventas.impuestos, ventas.total, ventas.nVentas, desde, hasta)
                        Dialogos.lanzarAlerta(newCorte.mensaje)
                        def comprobante = new Comprobantes()
                        comprobante.Corte(newCorte.IDCorte)//imprimir ticket
                        comprobante.probar()//imprimir ticket
                    }
                }
            }
            serv.desconectar()
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def lanzarFormNuevoCaja()
    {
        if(cerrojo(PMA_MODIFICARCAJA)){
            def formCaja = new omoikane.formularios.CajaDetalles()
            formCaja.setVisible(true)
            escritorio.getPanelEscritorio().add(formCaja)
            Herramientas.iconificable(formCaja)
            formCaja.toFront()
            try { formCaja.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles Caja", Herramientas.getStackTraceString(e)) }
            formCaja.setEditable(true);
            formCaja.setModoNuevo();
            return formCaja
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }
    static def guardar(formCaja)
    {
        if(cerrojo(PMA_MODIFICARCAJA)){
            Herramientas.verificaCampos {
                def descripcion = formCaja.getTxtDescripcion()
                Herramientas.verificaCampo(descripcion,/^([a-zA-Z0-9\_\-\s\ñ\Ñ\*\+áéíóú]+)$/,"Descripcion sólo puede incluír nímeros, letras, espacios, _, -, * y +.")
                try {
                    def serv = Nadesico.conectar()
                    Dialogos.lanzarAlerta(serv.addCaja(descripcion))
                } catch(e) { Dialogos.error("Error al enviar a la base de datos. El grupo no se registró", e) }
                formCaja.dispose()
            }
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def lanzarDetallesCaja(ID)
    {
        if(cerrojo(PMA_DETALLESCAJA)){
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
            return formCaja
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
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
        def formCaja = lanzarDetallesCaja(ID)
        formCaja.setModoModificar();
        formCaja
    }

    static def modificar(formCaja)
    {
        if(cerrojo(PMA_MODIFICARCAJA)){
            Herramientas.verificaCampos {
                Herramientas.verificaCampo(formCaja.getTxtDescripcion(),/^([a-zA-Z0-9_\-\s\�\�\*\+áéíóúñÑ]+)$/,"Descripcion sólo puede incluír números, letras, espacios, _, -, * y +.")
                def serv = Nadesico.conectar()
                Dialogos.lanzarAlerta(serv.modCaja(formCaja.getTxtIDCaja(),formCaja.getTxtDescripcion()))
            }
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def eliminarCaja(ID)
    {
        if(cerrojo(PMA_ELIMINARCAJA)){
            def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
            db.execute("DELETE FROM cajas WHERE id_caja = " + ID)
            db.close()
            Dialogos.lanzarAlerta("Caja " + ID + " supuestamente eliminada")
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def lanzarImprimir()
  {
        def reporte = new Reporte('omoikane/reportes/ReporteCajas.jasper', [QueryTxt:queryCaja]);
        reporte.lanzarPreview()
  }
}

class CajaTableModel extends DefaultTableModel {

    def data = []
    CajaTableModel() { 
        super(new Vector(), new Vector(["Concepto", "Cantidad", "Precio", "Descuento", "Total"]))  }

    public void addRowMap(rowData) { data << rowData; addRow(rowData.values()) }
    public Object getValueAt(int row, int col) { return data[row][getColumnName(col)] /*super.getValueAt(row,col)*/ }
    public def getDataMap() { return data }
}
