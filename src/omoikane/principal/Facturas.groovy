package omoikane.principal

import javax.swing.table.DefaultTableModel;
import omoikane.sistema.Nadesico;
import static omoikane.sistema.Permisos.*;
import omoikane.principal.*;
import omoikane.sistema.*;
import groovy.sql.*;
import groovy.swing.*;
import javax.swing.*;
import java.awt.*;
import omoikane.sistema.*;
import javax.swing.event.*;
import java.awt.event.*;
import static omoikane.sistema.Usuarios.*;
import static omoikane.sistema.Permisos.*;

/**
 *
 * @author Phesus - RNB
 */
class Facturas {

    static DefaultTableModel modeloFacturas;
    static DefaultTableModel modeloDetallesFactura;
    static def escritorio = omoikane.principal.Principal.escritorio;
    static def IDAlmacen = Principal.IDAlmacen;
    static def cat;
    static def factura;
    static boolean facturaGuardada;
    static def ventasEnLista = [];

    /**
     * Carga el formulario ListadoFacturas que muestra las últimas facturas
     */
    static def lanzarListadoFacturas() {
        if(cerrojo(Permisos.PMA_ABRIRFACTURAS) && cat == null ){
            cat = (new omoikane.formularios.ListadoFacturas());
            cat.tblFacturas.setModel(generarListadoFacturas());
            cat.setVisible(true);
            escritorio.getPanelEscritorio().add(cat);
            Herramientas.In2ActionX(cat, java.awt.event.KeyEvent.VK_ESCAPE, "cerrar"   ) { cat.btnCerrar.doClick()   }
            Herramientas.iconificable(cat);
            cat.internalFrameClosed = { cat = null; }
            cat.toFront();
            try {
                cat.setSelected(true);
                cat.requestFocus();
                cat.txtBusqueda.requestFocus();
            } catch(Exception e) {
                Dialogos.lanzarDialogoError(null, "Error al iniciar formulario listado de facturas", Herramientas.getStackTraceString(e));
            }
            return cat;
        }
        else if( !cerrojo(Permisos.PMA_ABRIRFACTURAS))
            Dialogos.lanzarAlerta("Acceso Denegado");
        else if(cat != null) {
            try {
                cat.setSelected(true);
                cat.requestFocus();
                cat.txtBusqueda.requestFocus();
            } catch(Exception e) {
                Dialogos.lanzarDialogoError(null, "Error al iniciar formulario listado de facturas", Herramientas.getStackTraceString(e));
            }
        }
    }

    /**
     * Crea el DefaultTableModel que listará las facturas
     *
     * @return DefaultTableModel
     */
    static def generarListadoFacturas() {
        def columnas = [ "Fecha", "Factura", "Cancelada", "Ventas", "Total" ];
        modeloFacturas = new DefaultTableModel((Object[])columnas, 0);
        poblarListadoFacturas();
        return modeloFacturas;
    }

    /**
     * Llena el DefaultTableModel que lista las facturas con las últimas 30 facturas
     */
    static def poblarListadoFacturas() {
        def serv = Nadesico.conectar();
        def filasUltimasFacturas = serv.getRows( "SELECT fecha, id_factura, cancelada, total FROM facturas ORDER BY fecha DESC LIMIT 30" );
        // println "filasUltimasFacturas: " + filasUltimasFacturas.toString();
        filasUltimasFacturas.each { fila ->
            def filasVentas = serv.getRows( "SELECT id_venta FROM ventas_facturadas WHERE id_factura = ${fila.id_factura} " );
            // println "filasVentas: " + filasVentas.toString();
            String strVentas = "";
            filasVentas.eachWithIndex { venta, i ->
                if( i > 0 )
                    strVentas += ", ";
                strVentas += venta.id_venta;
            }
            // println "strVentas: " + strVentas;
            def filaInsertable = [ 
                String.format( '%tY-%<tm-%<td %<tH:%<tM:%<tS', fila.fecha ),
                fila.id_factura, (fila.cancelada == true)? "sí":"",
                strVentas, fila.total
            ];
            modeloFacturas.addRow((Object[])filaInsertable);
        }
    }

    /**
     * Actualiza el listado de facturas (notable al cambiar una de las facturas en lista)
     */
    static def actualizarListado() {
        if(cat != null) {
            for( int i = modeloFacturas.getRowCount() - 1; i >= 0; i--)
                modeloFacturas.removeRow(i);
            poblarListadoFacturas();
        }
    }

    /**
     * Carga el formulario FacturaDetalles a través de lanzarFactura() y lo modifica
     * dependiendo de si se verán los detalles de una factura existente o se creará una
     * factura nueva
     *
     * @param nueva - boolean, si la factura es nueva
     */
    static def abrirFactura( boolean nueva ) {
        if ( cerrojo(Permisos.PMA_CREARFACTURAS) && nueva ) {
            facturaGuardada = false; // Factura nueva
            lanzarFactura();
            cargarDatosAlmacenUsuario();
            try {
                factura.setSelected(true);
                factura.txtTicket.requestFocus();
            } catch(Exception e) {
                Dialogos.lanzarDialogoError(null, "Error al iniciar formulario formato de factura", Herramientas.getStackTraceString(e));
            }
        }
        else {
            facturaGuardada = true; // Factura guardada ( incluyendo canceladas )
            int seleccion = cat.tblFacturas.getSelectedRow();
            if(seleccion == -1)
                Dialogos.lanzarAlerta("Ninguna fila ha sido seleccionada.");
            else {
                int id_factura = cat.tblFacturas.getValueAt(seleccion,1);
                lanzarFactura();
                factura.lblIdFactura.setText(id_factura.toString());
                cargarDatosAlmacenUsuario();
                try {
                   factura.setSelected(true);
                   factura.txtTicket.requestFocus();
                } catch(Exception e) {
                    Dialogos.lanzarDialogoError(null, "Error al iniciar formulario formato de factura", Herramientas.getStackTraceString(e));
                }
            }
        }
    }

   /**
    * Carga el formulario FacturaDetalles que muestra los detalles de una factura
    * o los campos a llenar para una factura nueva
    */
    static def lanzarFactura() {
        // Creación del formulario
        factura = (new omoikane.formularios.FacturaDetalles());
        // Creación y asignación del DefaultTableModel
        def columnas = [ "Venta", "Articulo", "Descripción", "Costo", "Cantidad", "Total" ];
        modeloDetallesFactura = new DefaultTableModel((Object[])columnas, 0);
        factura.tblDetalles.setModel(modeloDetallesFactura);
        if (facturaGuardada)
            factura.txtTicket.setEditable(false);
        // Mostrando el formulario y asignación de la tecla de cierre
        factura.setVisible(true);
        escritorio.getPanelEscritorio().add(factura);
        Herramientas.In2ActionX(factura, java.awt.event.KeyEvent.VK_ESCAPE, "cerrar"   ) { factura.btnCerrar.doClick()   }
        factura.txtTicket.keyReleased = {
            if(it.keyCode == it.VK_F1 && !facturaGuardada )
                Thread.start {
                    factura.txtCliente.setText(Clientes.lanzarDialogoCatalogo());
                }
        }
        factura.internalFrameClosed = {
            actualizarListado();
            cat.txtBusqueda.requestFocus();
        }

        Herramientas.iconificable(factura);
        factura.toFront();
        return factura;
    }

    /**
     * Carga los datos referentes al usuario que emitirá o cancelará la factura,
     * el almacén de donde se realiza/realizó la emisión y el folio de la factura
     */
    static def cargarDatosAlmacenUsuario() {
        def serv = Nadesico.conectar();
        def datosFactura;
        if(facturaGuardada) {   // Si la factura ya existe
            // Carga toda la factura desde el servidor
            datosFactura = serv.getFactura( factura.lblIdFactura.getText() );
            factura.jxdFecha.setDate(datosFactura.factura.fecha);
            factura.txtExpidio.setText(datosFactura.expidio.nombre);
            factura.txtCancelo.setText( datosFactura.factura.cancelada? datosFactura.cancelo.nombre: "" );
            factura.txtAlmacen.setText(datosFactura.almacen.descripcion);
            factura.txtCliente.setText(datosFactura.factura.id_cliente.toString());
            factura.txtSubtotal.setText( String.format("%.2f", datosFactura.factura.subtotal) );
            factura.txtImpuestos.setText( String.format("%.2f", datosFactura.factura.impuestos) );
            factura.txtTotal.setText( String.format("%.2f", datosFactura.factura.total) );
            // Recorrido de las listas de ventas ( Factura -> ventas[] -> articulos[] )
            datosFactura.articulos.each { venta ->
                venta.each { articulo ->
                    def listaInsertable = [ articulo.id_venta, articulo.id_articulo,
                        articulo.descripcion, articulo.precio, articulo.cantidad,
                        articulo.total ];
                    modeloDetallesFactura.addRow( (Object[])listaInsertable );
                }
            } 
        }
        else {  // Si la factura es nueva, se usan los nombres actuales de usuario y almacén
            // Obtención del folio de la factura nueva
            def folioFactura = serv.getLastIDFactura();
            if(folioFactura != "0")
                folioFactura++;
            factura.lblIdFactura.setText(folioFactura.toString());
            // El usuario actual expide la factura
            factura.txtExpidio.setText(omoikane.sistema.Usuarios.usuarioActivo.nombre);
            // La factura se expide en el almacen actual del usuario
            def resultadoAlmacen = serv.getRows("SELECT descripcion " +
                "FROM almacenes WHERE id_almacen = ${IDAlmacen} ;");
            if(resultadoAlmacen.size > 0)
                factura.txtAlmacen.setText(resultadoAlmacen.descripcion);
            else // WTF? Almacen inexistente
                Dialogos.lanzarAlerta("Error: No se encontró el almacén.");
        }
        serv.desconectar();
    }

    /**
     * Toma la clave de la venta dada en FacturaDetalles.txtTicket y busca en la BD
     * si existe esa venta. En caso de que exista y no esté facturada, se agrega
     * a la factura mostrando sus detalles (artículos) en la tabla
     */
    static def agregarVenta() {
        if( facturaGuardada )   // Factura guardada
            Dialogos.lanzarAlerta("La factura ya fué guardada, no se puede realizar esta operación");
        else if(factura.txtTicket.getText().length() == 0)  // No hay venta a buscar
            Dialogos.lanzarAlerta("No se ha indicado el ticket de venta a agregar");
        else {
            def serv = Nadesico.conectar();
            def resultadoVentas = serv.getDetallesVenta( factura.txtTicket.getText() );
            if( ArrayList.class.isInstance(resultadoVentas) && !isVentaEnLista( factura.txtTicket.getText() ) ) {  // Venta existente y sin facturar
                // Agregado a lista para evitar repeticiones de ventas
                agregarVentaALista(factura.txtTicket.getText());
                resultadoVentas.each { articulo ->
                    modeloDetallesFactura.addRow( (Object[])[ articulo.id_venta,
                            articulo.id_articulo, articulo.descripcion,
                            articulo.precio, articulo.cantidad, articulo.total ]
                    );
                }
                // Suma de totales
                def resultadoTotales = serv.getTotalesVenta( factura.txtTicket.getText() );
                sumaTotalesVenta(resultadoTotales, true);
                
            }
            else    // Venta existente pero incluída en otra factura
                Dialogos.lanzarAlerta("No se encontró la venta indicada o ya fué facturada");
            serv.desconectar();
            factura.txtTicket.setText("");
            factura.requestFocusInWindow();
            //factura.txtTicket.requestFocus();
        }
    }

    /**
     * Revisa si existen tickets en la lista ventasEnLista que registra las ventas
     * que ya se agregaron a la tabla para facturar pero aún no están en la BD
     * @param ticket - Cadena que representa el id de venta o ticket
     * @return true - Si el id de venta está en la lista
     */
    static boolean isVentaEnLista(String ticket) {
        boolean existe = false;
        for ( int i = 0; i < ventasEnLista.size; i++ ) {
            if (ventasEnLista[i] == ticket)
                existe = true;
        }
        return existe;
    }

    /**
     * Agrega el ticket a la lista de ventas por facturar para evitar repeticiones
     * en la tabla del formulario DetallesFactura
     * @param ticket - Cadena que representa el id de la venta
     */
    static def agregarVentaALista(String ticket) {
        ventasEnLista << ticket;
    }

    /**
     * Quita el ticket de la lista de ventas por facturar tras haberlo quitado
     * de la tabla en el formulario DetallesFactura
     * @param ticket - Cadena que representa el id de la venta
     */
    static def quitarVentaDeLista(String ticket) {
        def listaNueva = [];
        for ( int i = 0; i < ventasEnLista.size; i++ ) {
            if (ventasEnLista[i] != ticket)
                listaNueva << ventasEnLista[i];
        }
        ventasEnLista = listaNueva;
    }

    /**
     * Borra los detalles o artículos de la venta dada por la selección en la tabla
     * FacturaDetalles.tblDetalles
     */
    static def borrarVenta() {
        int seleccion = factura.tblDetalles.getSelectedRow();
        if( seleccion == -1 )
            Dialogos.lanzarAlerta("No se ha indicado la venta a eliminar (seleccione uno de sus artículos)");
        else if ( facturaGuardada )
            Dialogos.lanzarAlerta("La factura ha sido guardada, no se puede realizar esta operación");
        else {
            def ticket = modeloDetallesFactura.getValueAt(seleccion, 0);
            // Eliminación en lista
            quitarVentaDeLista(Integer.toString(ticket));
            // Eliminación en tabla
            int i = 0;
            while(i < modeloDetallesFactura.getRowCount()) {
                if( modeloDetallesFactura.getValueAt(i,0) == ticket )
                    modeloDetallesFactura.removeRow(i);
                else
                    i++;
            }
             // Resta de totales
            def serv = Nadesico.conectar();
            def resultadoTotales = serv.getTotalesVenta(ticket);
            sumaTotalesVenta(resultadoTotales, false);
            serv.desconectar();
        }
    }

    /**
     * Método usado para sumar o restar totales de cada venta a los totales de la factura
     *
     * @param venta - ResultSet (groovy?) con los campos 'subtotal', 'impuestos', y 'total' de una fila en 'ventas'
     * @param sumaresta - true si se suman los totales del ResultSet, false si se restan
     */
    static def sumaTotalesVenta( venta, boolean sumaresta ) {
        def mult = sumaresta ? 1 : -1;
        def sumaSubtotal = Double.parseDouble(factura.txtSubtotal.getText()) + mult * venta.subtotal;
        def sumaImpuestos = Double.parseDouble(factura.txtImpuestos.getText()) + mult * venta.impuestos;
        def sumaTotal = Double.parseDouble(factura.txtTotal.getText()) + mult * venta.total;
        factura.txtSubtotal.setText(String.format('%.2f',sumaSubtotal));
        factura.txtImpuestos.setText(String.format('%.2f',sumaImpuestos));
        factura.txtTotal.setText(String.format('%.2f',sumaTotal));
    }

    /**
     * Realiza el guardado y la impresión de una factura completa
     */
    static def imprimirFactura() {
        if( validaFactura() ) {
            if(!facturaGuardada)
                guardarFactura();
            def n = new omoikane.sistema.n2t()
            def letra = n.aCifra(Double.parseDouble(factura.txtTotal.getText()))
            def reporte = new Reporte('omoikane/reportes/FacturaEncabezado.jasper',[SUBREPORT_DIR:"omoikane/reportes/",NumLetra:letra,IDFactura:factura.lblIdFactura.getText()]);
            reporte.lanzarPreview()
        }
        else
            Dialogos.lanzarAlerta("No hay ventas en la factura. No se guardará la factura.");
        factura.txtTicket.requestFocus();
    }

    /**
     * Verifica que el formulario de la factura esté completa para poder guardar e imprimir
     *
     * @return true - Si el formulario está completo
     */
    static boolean validaFactura() {
        if(factura.txtCliente.getText().length() > 0 && modeloDetallesFactura.getRowCount() > 0)
            return true;
        else
            return false;
    }

    /**
     * Realiza el guardado de la factura considerándola como nueva
     * Esta función es llamada por imprimirFactura() verificando que la factura no se
     * guardó anteriormente
     */
    static def guardarFactura() {
        def serv = Nadesico.conectar();
        serv.addFactura( getDatosFactura(), getArregloVentas() );
        serv.desconectar();
        facturaGuardada = true;
    }

    /**
     * Obtiene un arreglo con los datos de la factura necesarios para guardar la factura
     * desde la tabla en el formulario FacturaDetalles
     *
     * @return datFactura - Arreglo de mapa con datos de la factura como se manejan en la BD
     */
    static def getDatosFactura() {
        def datFactura = [
            id_factura:factura.lblIdFactura.getText(),
            fecha:String.format( '%tY-%<tm-%<td %<tH:%<tM:%<tS', factura.jxdFecha.getDate() ),
            cliente:factura.txtCliente.getText(),
            expidio:factura.txtExpidio.getText(),
            cancelo:factura.txtCancelo.getText(),
            subtotal:factura.txtSubtotal.getText(),
            impuestos:factura.txtImpuestos.getText(),
            total:factura.txtTotal.getText()
        ];
        return datFactura;
    }

    /**
     * Obtiene un arreglo con los id de las ventas facturadas o por facturar
     * desde la tabla en el formulario FacturaDetalles
     *
     * @return ventas - Arreglo con ids de venta
     */
    static def getArregloVentas() {
        def ventas = [];
        int j = 0;
        def id_muestra = modeloDetallesFactura.getValueAt(0,0);
        for( int i = 0; i < modeloDetallesFactura.getRowCount() ; i++ ) {
            if (i == 0 || id_muestra != modeloDetallesFactura.getValueAt(i,0) ) {
                ventas[j] = modeloDetallesFactura.getValueAt(i,0);
                id_muestra = ventas[j];
                j++;
            }
        }
        return ventas;
    }

    /**
     * Cancela una factura seleccionada en el catálogo de facturas
     */
    static def cancelarFacturaDesdeLista() {
        int seleccion = cat.tblFacturas.getSelectedRow();
        if(seleccion == -1)
            Dialogos.lanzarAlerta("Ninguna fila ha sido seleccionada.");
        else if( cerrojo(Permisos.PMA_CANCELARFACTURAS) ) {
            if( modeloFacturas.getValueAt(seleccion,2) == "" ) { // Factura sin cancelar
                def serv = Nadesico.conectar();
                int id_factura = cat.tblFacturas.getValueAt(seleccion,1);
                def datosServFactura = serv.getFactura( id_factura );
                def datFactura = getDatosFacturaFromServ(datosServFactura); // Datos de factura sin cancelar
                // println datFactura;
                datFactura.cancelo = omoikane.sistema.Usuarios.usuarioActivo.nombre; // Cancelación
                def arrVentas = getArregloVentasFromServ(datosServFactura);
                // println datFactura;
                // println arrVentas;
                cancelarFactura(datFactura, arrVentas);
                actualizarListado();
                serv.desconectar();
            }
            else
                Dialogos.lanzarAlerta("La factura ya fué cancelada.");
            
        }
        else
            Dialogos.lanzarAlerta("No cuenta con el permiso necesario para esta operación.");
    }

    /**
     * Cancela una factura abierta en FacturaDetalles
     */
    static def cancelarFacturaDesdeDetalles() {
        if( facturaGuardada && cerrojo(Permisos.PMA_CANCELARFACTURAS) && factura.txtCancelo.getText().length() == 0 ) {
            factura.txtCancelo.setText(omoikane.sistema.Usuarios.usuarioActivo.nombre);
            cancelarFactura( getDatosFactura(), getArregloVentas() );
        }
        else {
            Dialogos.lanzarAlerta("No se puede cancelar la factura, es nueva o no cuenta con el permiso.");
        }
        factura.txtTicket.requestFocus();
    }

    /**
     * Realiza la cancelación de una factura. Esta función es llamada por
     * cancelaFacturaDesdeLista() y por cancelaFacturaDesdeDetalles()
     *
     * @param datFactura - Mapa de datos de la factura obtenido por getDatosFactura()
     *                      o getDatosFacturaFromServ()
     * @param arrVentas - Arreglo de las ventas facturadas, obtenido por get ArregloVentas()
     *                      o getArregloVentasFromServ()
     */
    static def cancelarFactura(datFactura, arrVentas) {
        def serv = Nadesico.conectar();
        serv.cancelFactura( datFactura, arrVentas );
        serv.desconectar();
    }

    /**
     * Obtiene un arreglo con los datos de la factura necesarios para guardar la factura
     * desde datos obtenidos del servidor
     *
     * @param datosServFactura - Arreglo con datos obtenidos del servidor ( Nadesico.getFactura() )
     * @return datFactura - Arreglo de mapa con datos de la factura como se manejan en la BD
     */
    static def getDatosFacturaFromServ(datosServFactura) {
        def datFactura = [ id_factura:datosServFactura.factura.id_factura,
            fecha:String.format( '%tY-%<tm-%<td %<tH:%<tM:%<tS', datosServFactura.factura.fecha ),
            cliente:datosServFactura.factura.id_cliente,
            expidio:datosServFactura.expidio,
            cancelo:datosServFactura.cancelo,
            subtotal:datosServFactura.factura.subtotal,
            impuestos:datosServFactura.factura.impuestos,
            total:datosServFactura.factura.total
            ];
        return datFactura;
    }

    /**
     * Obtiene un arreglo con los id de las ventas facturadas o por facturar
     * desde datos obtenidos del servidor
     *
     * @param datosServFactura - Arreglo con datos obtenidos del servidor ( Nadesico.getFactura() )
     * @return ventas - Arreglo con ids de venta
     */
    static def getArregloVentasFromServ(datosServFactura) {
        def ventas = [];
        int j = 0;
        def id_muestra = datosServFactura.articulos.id_venta[0][0];
        // println datosServFactura.articulos.id_venta[0][0];
        // println datosServFactura;
        for( int i = 0; i < datosServFactura.articulos.id_venta.size ; i++ ) {
            if (i == 0 || id_muestra != datosServFactura.articulos.id_venta[i][0] ) {
                ventas[j] = datosServFactura.articulos.id_venta[i][0];
                id_muestra = ventas[j];
                j++;
            }
        }
        return ventas;
    }
    
}

