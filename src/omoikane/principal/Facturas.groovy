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

    static def escritorio = omoikane.principal.Principal.escritorio;
    static def IDAlmacen = Principal.IDAlmacen;
    

    /**
     * Carga el formulario ListadoFacturas que muestra las últimas facturas
     */
    
    static def lanzarListadoFacturas() {
        if(cerrojo(Permisos.PMA_ABRIRFACTURAS)){
            def cat = (new omoikane.formularios.ListadoFacturas());

            cat.jProgressBar1.setIndeterminate(true)
            Thread.start() {
                    poblarListadoFacturas(cat);
                    cat.jProgressBar1.setIndeterminate(false)
            }
            
            cat.setVisible(true);
            escritorio.getPanelEscritorio().add(cat);
            Herramientas.In2ActionX(cat, java.awt.event.KeyEvent.VK_ESCAPE, "cerrar"   ) { cat.btnCerrar.doClick()   }
            Herramientas.iconificable(cat);
            cat.toFront();
            try {
                cat.setSelected(true);
                cat.requestFocus();
                cat.txtBusqueda.requestFocus();
            } catch(Exception e) {
                Dialogos.lanzarDialogoError(null, "Error al iniciar formulario listado de facturas", Herramientas.getStackTraceString(e));
            }
            return cat;
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def poblarListadoFacturas(form) {
        def modeloFacturas = form.tblFacturas.getModel()
        def serv = Nadesico.conectar();
        def filaNva= []
        def a = "no"
        def filasUltimasFacturas = serv.getRows( "SELECT fecha, id_factura, cancelada, total FROM facturas ORDER BY fecha DESC " );
        // println "filasUltimasFacturas: " + filasUltimasFacturas.toString();
        filasUltimasFacturas.each {
            def filasVentas = serv.getRows( "SELECT id_venta FROM ventas_facturadas WHERE id_factura = ${it.id_factura} " );
            def strVentas = "";
            filasVentas.eachWithIndex { venta, i ->
                if( i > 0 )
                    strVentas += ", ";
                strVentas += venta.id_venta;
            }
            if(it.cancelada == true) {a="sí"}else{a="no"}
            filaNva = [it.fecha, it.id_factura,a,strVentas,it.total]
            modeloFacturas.addRow(filaNva.toArray())
        }
    }
       
    static def lanzarFacturaNueva()
    {
        if(cerrojo(Permisos.PMA_CREARFACTURAS)){
            // Creación del formulario
        def factura = (new omoikane.formularios.FacturaDetalles());
        factura.setEditable(true)
        factura.setVisible(true);
        def serv        = Nadesico.conectar()
        def resultadoAlmacen = serv.getRows("SELECT descripcion FROM almacenes WHERE id_almacen = ${IDAlmacen}");
            if(resultadoAlmacen.size > 0)
               {factura.txtAlmacen.setText(resultadoAlmacen.descripcion);}
            else // WTF? Almacen inexistente
                {Dialogos.lanzarAlerta("Error: No se encontró el almacén.");}
        factura.txtExpidio.setText(omoikane.sistema.Usuarios.usuarioActivo.nombre);
        factura.txtCancelo.setText("");
        def folioFactura = serv.getLastIDFactura();
        if(folioFactura != 0){folioFactura=folioFactura+1;}
        factura.lblIdFactura.setText(folioFactura.toString());
        factura.btnAgregar.actionPerformed= { e -> agregarVenta(factura);}
        factura.btnBorrar.actionPerformed= { e -> borrarVenta(factura);}
        factura.btnImprimir.actionPerformed= { e -> guardarFactura(factura);imprimirFactura(factura);}
        escritorio.getPanelEscritorio().add(factura);
        Herramientas.In2ActionX(factura, java.awt.event.KeyEvent.VK_ESCAPE, "cerrar"   ) { factura.btnCerrar.doClick()}
        Herramientas.In2ActionX(factura, java.awt.event.KeyEvent.VK_F5, "Agregar"   ) {  factura.btnAgregar.doClick()}
        Herramientas.In2ActionX(factura, java.awt.event.KeyEvent.VK_F8, "Guardar"   ) {  factura.btnImprimir.doClick()}
            SwingBuilder.build {
            //Al presionar F1: (lanzarCatalogoDialogo)
            factura.getCampoID().keyReleased = { if(it.keyCode == it.VK_F1) Thread.start {factura.setCliente(Clientes.lanzarDialogoCatalogo() as String); factura.getCampoID().requestFocus();factura.setTxtClienteDes((serv.getCliente(factura.getCampoID().text)).razonSocial)}  }
        }
        Herramientas.iconificable(factura);
        factura.toFront();
        factura.txtTicket.requestFocus()
        serv.desconectar()
        return factura;
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def lanzarFacturaDetalles(cat)
    {
        int seleccion = cat.tblFacturas.getSelectedRow();
        if(seleccion == -1)
        {Dialogos.lanzarAlerta("Ninguna fila ha sido seleccionada.");             }
        else {
        int id_factura = cat.tblFacturas.getValueAt(seleccion,1);
        def factura = (new omoikane.formularios.FacturaDetalles());
        factura.btnImprimir.actionPerformed= { e -> imprimirFactura(factura);}
        Herramientas.In2ActionX(factura, java.awt.event.KeyEvent.VK_ESCAPE, "cerrar"   ) { factura.btnCerrar.doClick()}
        Herramientas.In2ActionX(factura, java.awt.event.KeyEvent.VK_F4, "Cancelar"   ) {  factura.btnCancelar.doClick()}
        Herramientas.In2ActionX(factura, java.awt.event.KeyEvent.VK_F8, "Guardar"   ) {  factura.btnImprimir.doClick()}
        factura.setVisible(true);
        factura.lblIdFactura.setText(id_factura.toString());
        escritorio.getPanelEscritorio().add(factura);
        cargarDatosAlmacenUsuario(factura)
        Herramientas.iconificable(factura);
        factura.toFront();
        return factura;}
    }

static def agregarVenta(factura) {
            def serv = Nadesico.conectar();
            def modeloFactura = factura.tblDetalles.getModel()
            def filaNva= []
            def resultadoVentas = serv.getDetallesVenta( factura.txtTicket.getText() );
            if( ArrayList.class.isInstance(resultadoVentas) && !isVentaEnLista(factura.txtTicket.getText(),modeloFactura)) {  // Venta existente y sin facturar
                // Agregado a lista para evitar repeticiones de ventas
                resultadoVentas.each { articulo ->
                    filaNva= [ articulo.id_venta,articulo.id_articulo,articulo.descripcion,
                            articulo.precio, articulo.cantidad, articulo.total ]
                    modeloFactura.addRow(filaNva.toArray())
                   
                }
                
                // Suma de totales
                def resultadoTotales = serv.getTotalesVenta( factura.txtTicket.getText() );
                sumaTotalesVenta(factura,resultadoTotales, true);

            }
            else
            {Dialogos.lanzarAlerta("No se encontró la venta indicada o ya fué facturada");}
            serv.desconectar();
            factura.txtTicket.setText("");
            factura.requestFocusInWindow();

        }

        static boolean isVentaEnLista(ticket,modeloDetallesFactura) {
        boolean existe = false;
        def lista = modeloDetallesFactura.getDataVector();
        for ( int i = 0; i < lista.size(); i++ ) {           
            if (lista.get(i).get(0) as String == ticket as String){existe = true;}
        }
        return existe;
    }

        static def sumaTotalesVenta( factura,venta, boolean sumaresta ) {
        
        def mult = sumaresta ? 1 : -1;
        def sumaSubtotal = Double.parseDouble(factura.txtSubtotal.getText()) + mult * venta.subtotal;
        def sumaImpuestos = Double.parseDouble(factura.txtImpuestos.getText()) + mult * venta.impuestos;
        def sumaTotal = Double.parseDouble(factura.txtTotal.getText()) + mult * venta.total;
        factura.txtSubtotal.setText(String.format('%.2f',sumaSubtotal));
        factura.txtImpuestos.setText(String.format('%.2f',sumaImpuestos));
        factura.txtTotal.setText(String.format('%.2f',sumaTotal));
    }
          
    static def borrarVenta(factura) {
        int seleccion = factura.tblDetalles.getSelectedRow();
        def modeloDetallesFactura = factura.tblDetalles.getModel()
        if( seleccion == -1 )
            Dialogos.lanzarAlerta("No se ha indicado la venta a eliminar (seleccione uno de sus artículos)");
        else {
            def ticket = modeloDetallesFactura.getValueAt(seleccion, 0);
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
            sumaTotalesVenta(factura,resultadoTotales, false);
            serv.desconectar();
        }
    }


static def imprimirFactura(factura) {
            def serv = Nadesico.conectar();
            def consulta
            consulta = serv.getArregloVentas(factura.lblIdFactura.getText() );
            def res =""
            for(int i = 0; i < consulta.size(); i++) {
            if(i>0) { res += "," }
            res += consulta[i].id_venta
            }
            serv.desconectar();
            def n = new omoikane.sistema.n2t()
            def letra = n.aCifra(Double.parseDouble(factura.txtTotal.getText()))
            def reporte = new Reporte('omoikane/reportes/FacturaEncabezado.jasper',[SUBREPORT_DIR:"omoikane/reportes/",NumLetra:letra,arreglo:res,IDFactura:factura.lblIdFactura.getText()]);
            reporte.lanzarPreview()
    }

        
    static def guardarFactura(factura) {
        if( validaFactura(factura) ) {
        def serv = Nadesico.conectar();
        serv.addFactura( getDatosFactura(factura), getArregloVentas(factura) );
        serv.desconectar();
        }
        else
            Dialogos.lanzarAlerta("No hay ventas en la factura. No se guardará la factura.");
        factura.txtTicket.requestFocus();
    }


    static boolean validaFactura(factura) {
        def modeloFactura = factura.tblDetalles.getModel()
        if(factura.txtCliente.getText().length() > 0 && modeloFactura.getRowCount() > 0)
            return true;
        else
            return false;
    }

   
    static def cargarDatosAlmacenUsuario(factura) {
        def serv = Nadesico.conectar();
        def datosFactura;
        def modeloFactura = factura.tblDetalles.getModel()
            datosFactura = serv.getFactura( factura.lblIdFactura.getText() );
            factura.jxdFecha.setDate(datosFactura.factura.fecha);
            factura.txtExpidio.setText(datosFactura.expidio.nombre);
            factura.txtCancelo.setText(datosFactura.factura.cancelada? datosFactura.cancelo.nombre:"");
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
                    modeloFactura.addRow( (Object[])listaInsertable );
                }
            } 
            serv.desconectar();
    }

    static def getDatosFactura(factura) {
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

    
    
    static def getArregloVentas(factura) {
        def ventas = [];
        def modeloFactura = factura.tblDetalles.getModel()
        int j = 0;
        def id_muestra = modeloFactura.getValueAt(0,0);
        for( int i = 0; i < modeloFactura.getRowCount() ; i++ ) {
            if (i == 0 || id_muestra != modeloFactura.getValueAt(i,0) ) {
                ventas[j] = modeloFactura.getValueAt(i,0);
                id_muestra = ventas[j];
                j++;
            }
        }
        return ventas;
    }

   
    static def cancelarFacturaDesdeLista(cat) {
        int seleccion = cat.tblFacturas.getSelectedRow();
        def modeloFacturas = cat.tblFacturas.getModel()
        if(seleccion == -1)
            Dialogos.lanzarAlerta("Ninguna fila ha sido seleccionada.");
        else if( cerrojo(Permisos.PMA_CANCELARFACTURAS) ) {
            if( modeloFacturas.getValueAt(seleccion,2) == "no" ) { // Factura sin cancelar
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
                serv.desconectar();
            }
            else
                Dialogos.lanzarAlerta("La factura ya fué cancelada.");
            
        }
        else
            Dialogos.lanzarAlerta("No cuenta con el permiso necesario para esta operación.");
    }

    static def cancelarFacturaDesdeDetalles(factura) {
        if( cerrojo(Permisos.PMA_CANCELARFACTURAS) && factura.txtCancelo.getText().length() == 0 ) {
            factura.txtCancelo.setText(omoikane.sistema.Usuarios.usuarioActivo.nombre);
            cancelarFactura( getDatosFactura(factura), getArregloVentas(factura) );
        }
        else {
            Dialogos.lanzarAlerta("No se puede cancelar la factura, es nueva o no cuenta con el permiso.");
        }
        factura.txtTicket.requestFocus();
    }

    
    static def cancelarFactura(datFactura, arrVentas) {
        def serv = Nadesico.conectar();
        serv.cancelFactura( datFactura, arrVentas );
        serv.desconectar();
    }

   
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

