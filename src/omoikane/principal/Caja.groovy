 /* Author Phesus        //////////////////////////////
 *  ORC,ACR             /////////////
 *                     /////////////
 *                    /////////////
 *                   /////////////
 * //////////////////////////////                   */

package omoikane.principal

import omoikane.sistema.*
import javax.swing.table.*
import groovy.inspect.swingui.*
import java.text.*
import groovy.sql.*
import omoikane.sistema.*
import javax.swing.event.*
import java.awt.*
import java.awt.event.*
import groovy.swing.*
import java.util.Calendar
import static omoikane.sistema.Usuarios.*
import static omoikane.sistema.Permisos.*
import omoikane.sistema.cortes.*

class Caja {

    //variables del sistema
    static def IDCaja    = Principal.IDCaja
    static def IDAlmacen = Principal.IDAlmacen
    static def IDCliente = 1 //cambiar cuando se mejore el modulo cliente
    static def queryCaja  = ""
    static def escritorio = omoikane.principal.Principal.escritorio
    static def comMan     = new ComMan()
    static def basculaActiva = omoikane.principal.Principal.basculaActiva
    static def miniDriver = [port:omoikane.principal.Principal.puertoBascula, baud:9600, bits: "8", stopBits:"1", parity:"None", stopChar:"3"]

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
                Herramientas.In2ActionX(form          , KeyEvent.VK_ESCAPE, "cerrar"      ) { form.btnCerrar.doClick()        }
                Herramientas.In2ActionX(form.txtIDCaja, KeyEvent.VK_ESCAPE, "cerrar"      ) { form.btnCerrar.doClick()        }
                Herramientas.In2ActionX(form          , KeyEvent.VK_F1    , "buscar"      ) { form.btnBuscar.doClick()        }
                Herramientas.In2ActionX(form          , KeyEvent.VK_ENTER , "aceptar"     ) { form.btnAceptar.doClick()       }

                form.txtIDCaja.keyReleased = { e ->
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
                            if(serv.getCorteWhere("id_caja = $txtID AND desde = '$hAbierta' AND hasta = '$hCerrada'") == 0 && hAbierta != "2000-01-01 00:00:00") {
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
        }else{Dialogos.lanzarAlerta("Caja Cerrada..... Habilitar para continuar ¨: )");omoikane.principal.Principal.cerrarSesion();}
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

    static def filtroEAN13(codigo) {
        if(codigo[0..1] == "26") {
            //Código de barras de báscula electrónica 26[xxxxx=codigo][yyyyy=cantidad][codigo seguridad] (13 dígitos)
            def cant = (codigo[7..11] as Double)/1000
            codigo   = cant+"*"+codigo[2..6]
        }
        codigo
    }
    static def cancelarArt(form) {

        def tabla     = form.tablaVenta
        def modelo    = tabla.getModel()
        def seleccion = tabla.getSelectedRows()
        for(int i=0; i <= seleccion.size()-1; i++) {
            modelo.eliminar(seleccion[i])
        }
    }
    static def round = { cant -> return (Math.round(cant*100)/100) }
    static def round10 = { cant -> return (Math.round(cant*10)/10) }
    static def cifra = { cant -> return String.format("\$%,.2f", cant) }
    static def aDoble= { cant -> return cant.replace("\$", '').replace(",", '') as Double }

    static def sumarTodo(form) {
        def dat   = form.modelo.getDataMap()
        def sumas = [0.0,0.0,0.0,0.0,0.0]
        dat.each { linea ->
            sumas[0] += ((linea['Precio']as Double)*(linea['Cantidad'] as Double)) as Double; sumas[1] += ((linea['Descuento']as Double)*(linea['Cantidad'] as Double)); sumas[2] += Caja.aDoble(linea['Total']); sumas[3] += linea['Impuestos'] as Double ; sumas[4] += linea['Cantidad'] as Double

        }
        form.txtNArticulos.text = Caja.aDoble(Caja.cifra(sumas[4])) //dat.size()
        form.txtSubtotal.text   = Caja.cifra(sumas[0]-sumas[3])//Caja.cifra (sumas[2] + sumas[1] - sumas[3])
        form.totalOriginal      =(Caja.round10 (sumas[2]))-sumas[2]
        //form.txtTotal.text      = Caja.cifra (Caja.round10 (sumas[2])) redondeo a cifras que si existen monetariamente
        form.txtTotal.text      = Caja.cifra (Caja.round (sumas[2]))
        form.txtDescuento.text  = Caja.cifra (sumas[1])
        form.impuestos          = sumas[3]
        form.txtImpuesto.text    = Caja.cifra (sumas[3])
    }

    static def doMovimientoCaja(panel, tipo,id) {
        def importeCorrecto = false
        Herramientas.verificaCampos {
            Herramientas.verificaCampo(panel.txtImporte.text, /^([0-9]*[\.]{0,1}[0-9]+)$/,"Valor inválido para importe")
            importeCorrecto = true
            def mov2Serv = Nadesico.conectar()
            def salida
            if(tipo=="Retiro") {
               salida = mov2Serv.doRetiro(IDAlmacen, IDCaja, omoikane.sistema.Usuarios.usuarioActivo.ID, id, panel.txtImporte.text)
            } else if(tipo == "Deposito") {
               salida = mov2Serv.doDeposito(IDAlmacen, IDCaja, omoikane.sistema.Usuarios.usuarioActivo.ID, id, panel.txtImporte.text)
            } else { throw new Exception("Estado inválido, tipo de movimiento de caja incorrecto") }
            mov2Serv.desconectar()
            Dialogos.lanzarAlerta(tipo+" confirmado")
            def comprobante = new Comprobantes()
            comprobante.movimiento(salida,tipo)//imprimir ticket
            comprobante.probar()//imprimir ticket
            panel.btnCerrar.doClick()
        }
        if(!importeCorrecto) { panel.txtImporte.requestFocusInWindow() }
    }
    static def lanzarCaja() {
        if(cerrojo(PMA_LANZARCAJA)){
            def form                     = new omoikane.formularios.Caja()
            def modelo                   = new CajaTableModel(form)
            def autorizadorVentaEspecial = null
            Herramientas.panelCatalogo(form)
            form.tablaVenta.setModel(modelo)
            form.modelo = modelo
            escritorio.getPanelEscritorio().add(form)
            Herramientas.centrarVentana(form);
            Herramientas.setColumnsWidth(form.tablaVenta, [0.48,0.12,0.12,0.12,0.13]);
            form.setVisible(true);
            Herramientas.iconificable(form)
            Herramientas.In2ActionX(form           , KeyEvent.VK_F3    , "buscar"    ) { form.txtCaptura.requestFocusInWindow()  }
            Herramientas.In2ActionX(form           , KeyEvent.VK_ESCAPE, "cerrar"    ) { form.btnCerrar.doClick()                }
            Herramientas.In2ActionX(form.txtCaptura, KeyEvent.VK_ESCAPE, "cerrar"    ) { form.btnCerrar.doClick()                }
            Herramientas.In2ActionX(form           , KeyEvent.VK_F4    , "ventaEsp"  ) { form.btnVentaEspecial.doClick()         }
            Herramientas.In2ActionX(form           , KeyEvent.VK_PAUSE , "pausar"    ) { form.btnPausar.doClick()                }
            Herramientas.In2ActionX(form           , KeyEvent.VK_F12   , "cancelar"  ) { form.btnCancelacion.doClick()           }
            Herramientas.In2ActionX(form.btnCerrar , KeyEvent.VK_ESCAPE, "cerrar2"   ) { form.btnCerrar.doClick()                }
            Herramientas.In2ActionX(form           , KeyEvent.VK_F7    , "cancelaArt") { form.btnCancelaArt.doClick()            }
            Herramientas.In2ActionX(form           , KeyEvent.VK_F11   , "movs"      ) { form.btnMovimientos.doClick()           }


            form.toFront()
            try { form.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario caja", Herramientas.getStackTraceString(e)) }
            form.txtCaptura.requestFocus()
            def date = Calendar.getInstance()
            form.txtFecha.text = date.get(date.DAY_OF_MONTH) + "-" + (date.get(date.MONTH)+1) + "-" + date.get(date.YEAR)
            form.txtCaja.text= "Caja "+IDCaja
            def serv = Nadesico.conectar()

            /*def scanMan = new ScanMan()
            try {
                scanMan.connect(Principal.scannerPort, Principal.scannerBaudRate)
            } catch(Exception ex2) { Dialogos.error(ex2.getMessage(), ex2) }
            Principal.scanMan.setHandler { /*form.txtCaptura.text += it;
                    def robot = new java.awt.Robot()
                    it.each { 
                        
                        if((it as int)==13) { return null }
                        robot.keyPress(it as int);
                    }
                    //robot.keyPress(10)
                    robot.keyPress(KeyEvent.VK_ENTER)
                    
                }*/
                    

            def addArtic = { codigo ->
                try {
                    def captura = form.txtCaptura.text

                    if(captura.size()==13) { captura = Caja.filtroEAN13(captura) }
                    captura     = captura.split("\\*")
                    def cantidad= captura.size()==1?1:captura[0..captura.size()-2].inject(1) { acum, i -> acum*(i as Double) }
                    def art     = serv.codigo2Articulo(IDAlmacen, captura[captura.size()-1])
                    if(art == null || art == 0) { Dialogos.lanzarAlerta("Articulo no encontrado!!"); } else {
                        def precio    = serv.getPrecio(art.id_articulo, IDAlmacen, IDCliente)
                        def factImp   = (precio['impuestos%'])/100
                        def total     = Caja.cifra(cantidad * precio.total)
                        def descuento = cantidad * precio['descuento$']
                        def impuesto  = cantidad * (precio.total/(1+factImp)) *factImp
                        form.txtCaptura.text = ""
                        //form.modelo.addRow([art.id_articulo,art.descripcion,cantidad,precio.total,precio['descuento$'],total].toArray())
                        
                        form.modelo.addRowMap(["ID Artículo": art.id_articulo      , "Concepto"           : art.descripcion             ,
                                               "Cantidad"   : cantidad             , "Precio"             : precio['PrecioConImpuestos'],
                                               "Descuento"  : precio['descuento$'] , "Impuestos"          : impuesto                    ,
                                               "Total"      : total                , "Impuestos%"         : precio['impuestos%']        ,
                                               "Descuento%" : precio['descuento%'] , "DescuentoReferencia": descuento])
                        //println "impuestos->"+art.impuestos
                        Caja.sumarTodo(form)
                        form.repaint()
                        form.tablaVenta.scrollRectToVisible(form.tablaVenta.getCellRect(form.tablaVenta.getRowCount()-1, 1, true));
                    }
                } catch(e) { Dialogos.error("Error al obtener información de nadesico!", e) }
            }
            if(basculaActiva){
            form.txtCaptura.keyTyped = { e ->
                if(e.keyChar == '+') {
                    def peso = Caja.comMan.readWeight("K", miniDriver)
                    form.txtCaptura.text = peso + "*"
                    e.consume()
                    //Dialogos.lanzarAlerta("Báscula: "+peso)
                    //println "--"+peso
                }
                }}
            form.txtCaptura.keyReleased = {   e ->
                if(e.keyCode==e.VK_ENTER) if(form.txtCaptura.text != "") { addArtic(form.txtCaptura.text) } else { form.txtEfectivo.requestFocusInWindow() }
                //Al presionar   F1: (lanzarCatalogoDialogo)
                else if(e.keyCode == e.VK_F1) { if(form.btnCatalogo.isEnabled()) { form.btnCatalogo.doClick(); } }
                else if(e.getKeyCode() == e.VK_DOWN)
                {
                    int sigFila = form.tablaVenta.getSelectedRow()+1;
                    if(sigFila < form.tablaVenta.getRowCount())
                    {
                        form.tablaVenta.setRowSelectionInterval(sigFila, sigFila);
                        form.tablaVenta.scrollRectToVisible(form.tablaVenta.getCellRect(sigFila, 1, true));
                    }
                }
                else if(e.getKeyCode() == e.VK_UP)
                {
                    int antFila = form.tablaVenta.getSelectedRow()-1;
                    if(antFila >= 0) {
                        form.tablaVenta.setRowSelectionInterval(antFila, antFila);
                        form.tablaVenta.scrollRectToVisible(form.tablaVenta.getCellRect(antFila, 1, true));
                    }
                }

            }
            form.txtCambio.keyReleased = { e->
                if(e.getKeyCode() == e.VK_ENTER) {
                    form.btnTerminar.doClick()
                }
            }
            /*form.txtEfectivo.keyReleased = { e ->
                if(e.getKeyCode() == e.VK_ENTER) {
                    def sefe  = form.txtEfectivo.text.replace('$', '').replace(',', '')
                    if(sefe == "") { sefe = "0.0" }
                    sefe      = sefe as double
                    def stot  = form.txtTotal.text.replace('$', '').replace(',', '')
                    if(stot == "") { stot = "0.0" }
                    stot      = stot as double
                    form.txtCambio.text = Caja.cifra(Caja.round(sefe - (stot as double)))
                    form.btnTerminar.doClick()
                }
            }*/
            form.txtEfectivo.keyReleased = { e ->
                if(e.getKeyCode() == e.VK_ENTER) {
                    def sefe  = form.txtEfectivo.text.replace('$', '').replace(',', '')
                    if(sefe == "") { sefe = "0.0" }
                    sefe      = sefe as double
                    def stot  = form.txtTotal.text.replace('$', '').replace(',', '')
                    if(stot == "") { stot = "0.0" }
                    stot      = stot as double
                    form.txtCambio.text = Caja.cifra(Caja.round(sefe - (stot as double)))
                    form.txtCambio.requestFocusInWindow()
                }
            }
            form.btnVentaEspecial.actionPerformed = {
                def sisUsers = omoikane.sistema.Usuarios
                def usuario = sisUsers.identificaPersona()
                if(usuario.cerrojo(omoikane.sistema.Permisos.PMA_VENTAESPECIAL)) {
                    autorizadorVentaEspecial = usuario
                    form.tablaVenta.getModel().ventaAbierta = true
                }
                
            }
            form.btnMovimientos.actionPerformed = {
                Thread.start {
                    
                    try {
                        def panel  = new omoikane.formularios.PanelMovimientosCaja()
                        
                        panel.setVisible(true)
                        Herramientas.In2ActionX(panel, KeyEvent.VK_F6, "retiro"   ) { panel.btnRetiro.doClick()     }
                        Herramientas.In2ActionX(panel, KeyEvent.VK_F5, "deposito"   ) { panel.btnDeposito.doClick()     }
                        Herramientas.In2ActionX(panel, KeyEvent.VK_ENTER, "cerrar"   ) { panel.btnCerrar.doClick()     }
                        Herramientas.In2ActionX(panel, KeyEvent.VK_ESCAPE, "cerrar"   ) { panel.btnCerrar.doClick()     }
                        def movServ = Nadesico.conectar()
                        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        def horas = serv.getCaja(IDCaja)

                        def ventas= movServ.sumaVentas(IDCaja, sdf.format(horas.horaAbierta), 'CURRENT_TIMESTAMP')
                        movServ.desconectar()

                        panel.txtNVentas.text   = ventas.nVentas
                        panel.txtVentas.text    = ventas.total
                        panel.txtRetiros.text   = ventas.retiros
                        panel.txtDepositos.text = ventas.depositos

                        panel.btnRetiro.actionPerformed = {
                            Thread.start {
                                def sisUsers = omoikane.sistema.Usuarios
                                def usuario = sisUsers.identificaPersona()
                                if(usuario.cerrojo(omoikane.sistema.Usuarios.SUPERVISOR)){
                                Caja.doMovimientoCaja(panel, "Retiro",usuario.ID)
                                }
                                else{Dialogos.lanzarAlerta("Sin Permiso");panel.txtImporte.requestFocusInWindow()}
                            }
                        }

                        panel.btnDeposito.actionPerformed = {
                            Thread.start {
                                def sisUsers = omoikane.sistema.Usuarios
                                def usuario = sisUsers.identificaPersona()
                                if(usuario.cerrojo(omoikane.sistema.Usuarios.SUPERVISOR)){
                                Caja.doMovimientoCaja(panel, "Deposito",usuario.ID)
                                }
                                else{Dialogos.lanzarAlerta("Sin Permiso");panel.txtImporte.requestFocusInWindow()}
                            }
                        }

                        def dialog = new JInternalDialog2(((omoikane.principal.Escritorio)omoikane.principal.Principal.getEscritorio()).getFrameEscritorio(), "Movimientos Caja", panel)
                        panel.btnCerrar.actionPerformed = { Thread.start { dialog.setActivo(false); form.txtCaptura.requestFocusInWindow() } }
                        panel.txtImporte.requestFocusInWindow()
                        dialog.setActivo(true)
                        panel.txtImporte.requestFocusInWindow()
                    } catch(exce) {
                        Dialogos.error("Error en movimientos caja: ${exce.getMessage()}", exce)
                    }
                    
                }
            }
            form.btnCancelaArt.actionPerformed = {
                Thread.start {
                    def sisUsers = omoikane.sistema.Usuarios

                    if(sisUsers.autentifica(sisUsers.SUPERVISOR)) {
                        Caja.cancelarArt(form);
                        sumarTodo(form)
                    }
                    form.txtCaptura.requestFocusInWindow();
                }
            }
            form.btnCerrar.actionPerformed = {
                Thread.start {
                    def sisUsers = omoikane.sistema.Usuarios

                    if(omoikane.sistema.Usuarios.cerrojo(omoikane.sistema.Usuarios.SUPERVISOR) || sisUsers.autentifica(sisUsers.SUPERVISOR)) {
                        if(!omoikane.sistema.Usuarios.cerrojo(omoikane.sistema.Usuarios.SUPERVISOR)){
                            form.dispose();
                            
                            omoikane.principal.Principal.cerrarSesion();
                        }else{
                            form.dispose();
                            ((javax.swing.JInternalFrame)((omoikane.principal.MenuPrincipal)omoikane.principal.Principal.getMenuPrincipal()).getMenuPrincipal()).requestFocusInWindow();
                        }
                        //scanMan.disconnect()
                    } else {
                        form.txtCaptura.requestFocusInWindow()
                    }
                }
            }

            form.btnPausar.actionPerformed = {
                Thread.start {
                    def sisUsers = omoikane.sistema.Usuarios
                    def idUsuario= omoikane.sistema.Usuarios.usuarioActivo.ID
                    while(true) {
                        def usuario = sisUsers.identificaPersona()
                        if(idUsuario==usuario.ID||usuario.cerrojo(omoikane.sistema.Usuarios.SUPERVISOR))
                        {break}
  
                    }
                    form.txtCaptura.requestFocusInWindow()

                }
            }

            def catArticulos = {
                    def retorno = Articulos.lanzarDialogoCatalogo() as String
                    //return retorno==null?"":retorno
                    retorno = (retorno==null)?"":retorno
                    form.txtCaptura.text = form.txtCaptura.text + retorno
                    form.txtCaptura.requestFocus()
                    form.btnCatalogo.setEnabled(true)
            }

            form.btnCatalogo.actionPerformed = { e ->
                synchronized(this) {
                    form.btnCatalogo.setEnabled(false); Thread.start { catArticulos() }
                }
            }
            form.btnTerminar.actionPerformed = { e ->
                try {
                    if(form.modelo.getDataMap().size() == 0) { throw new Exception("Venta vacía") }
                        //def foco = new Object()
                        //Thread.start {
//                            def aaa= new SimpleForm("omoikane.formularios.DialogoCambio") {
//                                def sform = it.form
//                                Herramientas.panelFormulario(sform)
//                                sform.total.text = form.txtTotal.text
//                                sform.visible = true
//                                sform.txtEfectivo.focusLost = {
//                                    def sefe  = sform.txtEfectivo.text.replace('$', '').replace(',', '')
//                                    if(sefe == "") { sefe = "0.0" }
//                                    sefe      = sefe as double
//                                    def stot  = sform.total.text.replace('$', '').replace(',', '')
//                                    if(stot == "") { stot = "0.0" }
//                                    stot      = stot as double
//                                    sform.cambio.text = Caja.cifra(Caja.round(sefe - (stot as double)))
//                                }
//                                sform.btnContinuar.actionPerformed = { sform.dispose(); synchronized(foco) { foco.notifyAll() } }
//                            }
//                            aaa.sform.setBounds(100,100,100,100)
//                            synchronized(foco) { foco.wait() }
                        def detalles = [], cantTemp
                        form.modelo.getDataMap().each {
                            cantTemp = it['Cantidad']
                            if(cantTemp instanceof String) { cantTemp = Caja.aDoble(cantTemp) }
                            detalles << [IDArticulo:it['ID Artículo'], cantidad:cantTemp, precio:it['Precio'], descuento:it['Descuento'], total:Caja.aDoble(it['Total'])]
                        }
                        def dinero
                        def cambio
                        if(form.txtEfectivo.text == "") { dinero =  Caja.aDoble(form.txtTotal.text) ;cambio = "0.0"}
                        else{dinero = Caja.aDoble(form.txtEfectivo.text);cambio = Caja.aDoble(form.txtCambio.text)}
                        def salida = serv.conectar().aplicarVenta(IDCaja, IDAlmacen, IDCliente, omoikane.sistema.Usuarios.usuarioActivo.ID, Caja.aDoble(form.txtSubtotal.text), Caja.aDoble(form.txtDescuento.text), form.impuestos, Caja.aDoble(form.txtTotal.text), detalles,dinero,cambio,form.totalOriginal)

                            if(autorizadorVentaEspecial != null) {
                                serv.addVentaEspecial(salida.ID, autorizadorVentaEspecial.ID)
                            }

                        serv.desconectar()
                        def comprobante = new Comprobantes()
                        comprobante.ticket(IDAlmacen, salida.ID)//imprimir ticket
                        comprobante.probar()//imprimir ticket
                        Dialogos.lanzarAlerta(salida.mensaje)
                        form.dispose()
                        lanzar()
                    
                } catch(err) { if(err.getMessage()!="Venta vacía"){Dialogos.error("Error: La venta no se pudo registrar", err)} }
            }
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }
//reaparando catalogo cajas

    static def lanzarCatalogo()
    {
        if(cerrojo(PMA_ABRIRCAJA)){
            def cat = (new omoikane.formularios.CatalogoCajas())
            cat.setVisible(true);
            escritorio.getPanelEscritorio().add(cat)
            Herramientas.panelCatalogo(cat)
            Herramientas.iconificable(cat)
            Herramientas.In2ActionX(cat, KeyEvent.VK_ESCAPE, "cerrar"   ) { cat.btnCerrar.doClick()   }
            cat.txtBusqueda.keyReleased = { if(it.keyCode == it.VK_ESCAPE) cat.btnCerrar.doClick()    }
            Herramientas.In2ActionX(cat, KeyEvent.VK_DELETE, "eliminar" ) { cat.btnEliminas.doClick() }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F12   , "corte"    ) { cat.btnCorte.doClick()    }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F4    , "detalles" ) { cat.btnDetalles.doClick() }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F5    , "nuevo"    ) { cat.btnNuevo.doClick() }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F6    , "modificar") { cat.btnModificar.doClick() }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F7    , "imprimir") { cat.btnImprimir.doClick() }
            cat.toFront()
            try { cat.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario catalogo de cajas", Herramientas.getStackTraceString(e)) }
            cat.txtBusqueda.requestFocus()
            poblarCajas(cat.getTablaCajas(),"")
            return cat
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    public static String lanzarCatalogoDialogo()
    {
            def foco=new Object()
            def cat = lanzarCatalogo()
            cat.setModoDialogo()
            cat.internalFrameClosed = {synchronized(foco){foco.notifyAll()} }
            cat.txtBusqueda.keyReleased = { if(it.keyCode == it.VK_ENTER) cat.btnAceptar.doClick() }
            def retorno
            cat.btnAceptar.actionPerformed = {
                def catTab = cat.tablaCajas; retorno = catTab.getModel().getValueAt(catTab.getSelectedRow(), 0) as String; cat.btnCerrar.doClick(); }
            synchronized(foco){foco.wait()}
            retorno
    }

    static def lanzarCorteCaja(IDCaja, cortar=false) {
            omoikane.principal.Caja.lanzarTotalVentasDia(IDCaja, cortar)
    }
    static def lanzarTotalVentasDia(IDCaja, cortar=false) 
    {
        if(cerrojo(PMA_TOTALVENTA)){
            def serv = Nadesico.conectar()
            def res  = serv.getCaja(IDCaja);
            def newCorte
            if(res == 0) {Dialogos.lanzarAlerta("No exíste esa caja")} else {
                if(!serv.cajaAbierta(IDCaja)) {Dialogos.lanzarAlerta("La caja ya estaba cerrada")}
                else{
                def horas      = serv.getCaja(IDCaja)
                SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy @ hh:mm:ss a ");
                Calendar         fecha= Calendar.getInstance()
                if(serv.getCorteWhere("id_caja = $IDCaja AND desde = '${sdf.format(horas.horaAbierta)}' AND hasta = '${sdf.format(horas.horaCerrada)}'")!=0) {
                    Dialogos.lanzarAlerta("Ya se realizó corte de Caja y no se han hecho ventas desde el último corte de caja")
                } else {
                    if(cortar) { serv.cerrarCaja(IDCaja) }
                    horas = serv.getCaja(IDCaja)
                    //def ventas= serv.sumaVentas(IDCaja, sdf.format(horas.horaAbierta), sdf.format(horas.horaCerrada))
                    def instanciaCortes = ContextoCorte.instanciar()
                    def ventas= instanciaCortes.obtenerSumaCaja(IDCaja, sdf.format(horas.horaAbierta), sdf.format(horas.horaCerrada))
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
                    form.setTxtDeposito      (ventas.depositos as String)
                    form.setTxtRetiro        (ventas.retiros as String)
                    form.setTxtTotal         (ventas.total as String)
                    def dinero = ((ventas.total)-(ventas.retiros)+(ventas.depositos))
                    form.setTxtEfectivo      (dinero as String)

                    if(cortar) {
 
                        newCorte     = instanciaCortes.hacerCorteCaja(IDCaja, caja.id_almacen, ventas.subtotal, ventas.impuestos, ventas.descuento, ventas.total, ventas.nVentas, desde, hasta,ventas.depositos,ventas.retiros)

                        form.ID=newCorte.IDCorte
                    }
                }
            }}
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
            Herramientas.In2ActionX(formCaja, KeyEvent.VK_F6    , "guardar"  ) { formCaja.btnGuardar.doClick()  }
            Herramientas.panelFormulario(formCaja)
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
                def serv
                Herramientas.verificaCampo(descripcion,Herramientas.texto,"Descripcion"+Herramientas.error1)
                try {
                    serv = Nadesico.conectar()
                    Dialogos.lanzarAlerta(serv.addCaja(IDAlmacen,descripcion))
                } catch(e) { Dialogos.error("Error al enviar a la base de datos. El grupo no se registró", e) }
                serv.desconectar()
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
            Herramientas.panelFormulario(formCaja)
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
        Herramientas.In2ActionX(formCaja, KeyEvent.VK_F6    , "modificar"  ) { formCaja.btnModificar.doClick()  }
        formCaja
    }

    static def modificar(formCaja)
    {
        if(cerrojo(PMA_MODIFICARCAJA)){
            Herramientas.verificaCampos {
                Herramientas.verificaCampo(formCaja.getTxtDescripcion(),Herramientas.texto,"Descripcion"+Herramientas.error1)
                def serv = Nadesico.conectar()
                Dialogos.lanzarAlerta(serv.modCaja(formCaja.getTxtIDCaja(),IDAlmacen,formCaja.getTxtDescripcion()))
                serv.desconectar()
            }
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def eliminarCaja(ID)
    {
        if(cerrojo(PMA_ELIMINARCAJA)){
            Dialogos.lanzarAlerta("Función desactivada!")
            //def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
            //db.execute("DELETE FROM cajas WHERE id_caja = " + ID)
            //db.close()
            //Dialogos.lanzarAlerta("Caja " + ID + " supuestamente eliminada")
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
    def parent
    def ventaAbierta = false

    CajaTableModel(parent) {
        super(new Vector(), new Vector(["Concepto", "Cantidad", "Precio", "Descuento", "Total"]))  
        this.parent = parent
    }

    public void addRowMap(rowData) {

        data << rowData;
        addRow(rowData.values())
        actualizarSumas()
    }
    public Object getValueAt(int row, int col) { return data[row][getColumnName(col)] /*super.getValueAt(row,col)*/ }
    public def getDataMap() { return data }
    public def eliminar(i) { data.remove(i); super.removeRow(i); }
    public void setValueAt(Object val, int row, int col) {
        try {
        data[row][getColumnName(col)] = val
        def totalTemp,totalTemp1
        def descTemp, factImp,factDes, descTemp1
        
        if(col == 1 || col == 2) {

            factDes    = data[row]['Descuento%']/100
            data[row].DescuentoReferencia = factDes * Caja.aDoble("0"+data[row].Precio.toString())
            descTemp   =  Caja.aDoble("0"+data[row].DescuentoReferencia.toString())
            descTemp1  = Caja.aDoble(data[row].Cantidad.toString()) * Caja.aDoble("0"+data[row].DescuentoReferencia.toString())
            totalTemp  = (Caja.aDoble(data[row].Cantidad.toString()) * Caja.aDoble("0"+data[row].Precio.toString()))- descTemp1
            setValueAt( Caja.aDoble(Caja.cifra(descTemp)) , row, 3)
            factImp = data[row]['Impuestos%']/100
            data[row].Impuestos = (totalTemp/(1 + factImp)) * factImp
            totalTemp = Caja.cifra(totalTemp)
            setValueAt( totalTemp, row, 4)
            
            super.fireTableCellUpdated(row, 3)
            super.fireTableCellUpdated(row, 4)
            
            Caja.sumarTodo(parent)
        }
       // super.setValueAt(val, row, col)
        } catch (Exception e) {
          omoikane.sistema.Dialogos.error("Error al aplicar cambio", e)
        }
    }

    public boolean isCellEditable(int row, int col) {
        if((col==1||col==2)&&ventaAbierta) { return true }
        return false
    }

    public def actualizarSumas() {
        /* Disponible para actualizaciones
        data.each { rowData ->
           
        }
        */
    }
}
