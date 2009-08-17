 /* Author Phesus        //////////////////////////////
 *  ORC,ACR             /////////////
 *                     /////////////
 *                    /////////////
 *                   /////////////
 * //////////////////////////////                   */

package omoikane.principal

import omoikane.sistema.*
import java.text.*;
import groovy.sql.*;
import omoikane.sistema.*;
import javax.swing.event.*;
import java.awt.event.*;
import javax.swing.*;
import static omoikane.sistema.Usuarios.*;
import static omoikane.sistema.Permisos.*;

class Clientes {
    static def escritorio = omoikane.principal.Principal.escritorio

    static def lanzarCatalogo()
    {
        
        try{
        if(cerrojo(PMA_ABRIRCLIENTE)){
            def cat = (new omoikane.formularios.CatalogoClientes())
            cat.setVisible(true);
            escritorio.getPanelEscritorio().add(cat)
            Herramientas.panelCatalogo(cat)
            Herramientas.setColumnsWidth(cat.jTable1, [0.15,0.15,0.34,0.12,0.12,0.12]);
            Herramientas.In2ActionX(cat, KeyEvent.VK_F3    , "buscar"   ) { cat.txtBusqueda.requestFocusInWindow() }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F4    , "detalles" ) { cat.btnDetalles.doClick() }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F5    , "nuevo"    ) { cat.btnNuevo.doClick()    }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F6    , "modificar") { cat.btnModificar.doClick()}
            Herramientas.In2ActionX(cat, KeyEvent.VK_F8    , "imprimir" ) { cat.btnImprimir.doClick()}
            Herramientas.In2ActionX(cat, KeyEvent.VK_DELETE, "eliminar" ) { cat.btnEliminar.doClick() }
            Herramientas.iconificable(cat)
            cat.toFront()
            try { cat.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario catálogo de clientes", Herramientas.getStackTraceString(e)) }
            cat.txtBusqueda.requestFocus()
            return cat
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
        } catch(e) { Dialogos.error("Error al lanzar catalogo de clientes", e) }
    }

    public static String lanzarDialogoCatalogo()
    {
        try{
        def foco=new Object()
        def cat = lanzarCatalogo()
        cat.setModoDialogo()
        cat.internalFrameClosed = {synchronized(foco){foco.notifyAll()} }
        cat.txtBusqueda.keyReleased = { if(it.keyCode == it.VK_ENTER) cat.btnAceptar.doClick() }
        def retorno
        cat.btnAceptar.actionPerformed = { def catTab = cat.jTable1; retorno = catTab.getModel().getValueAt(catTab.getSelectedRow(), -1 ) as String; cat.btnCerrar.doClick(); }
        synchronized(foco){foco.wait()}
        return retorno
        } catch(e) { Dialogos.error("Error al lanzar el modo dialogo del catalogo de clientes", e) }
    }

    static def lanzarImprimir(txtQuery)
    {
        try{
        def reporte = new Reporte('omoikane/reportes/ReporteClientes.jasper',[txtQuery:txtQuery]);
        reporte.lanzarPreview()
        } catch(e) { Dialogos.error("Error al crear el Reporte de clientes", e) }
    }

    static def eliminarCliente(ID)
    {
        try{
        Dialogos.lanzarAlerta("Función desactivada!")
        /*
        if(cerrojo(PMA_ELIMINARCLIENTE)){
            def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
            db.execute("DELETE FROM clientes WHERE id_cliente = " + ID)
            db.close()
            Dialogos.lanzarAlerta("Cliente " + ID + " supuestamente eliminado")
         }else{Dialogos.lanzarAlerta("Acceso Denegado")}
        */
       } catch(e) { Dialogos.error("Error al eliminar Cliente", e) }
    }

    static def lanzarDetallesClientes(ID)
    {
        try{
        if(cerrojo(PMA_DETALLESCLIENTE)){
            def formCliente = new omoikane.formularios.Cliente()
            formCliente.setVisible(true)
            Herramientas.panelFormulario(formCliente)
            escritorio.getPanelEscritorio().add(formCliente)
            formCliente.toFront()
            Herramientas.panelFormulario(formCliente)
            Herramientas.In2ActionX(formCliente, KeyEvent.VK_F3    , "nada") { }
            Herramientas.In2ActionX(formCliente, KeyEvent.VK_F4    , "nada") { }
            Herramientas.In2ActionX(formCliente, KeyEvent.VK_F5    , "nada") { }
            Herramientas.In2ActionX(formCliente, KeyEvent.VK_F6    , "nada") { }
            Herramientas.In2ActionX(formCliente, KeyEvent.VK_F8    , "nada") { }
            Herramientas.In2ActionX(formCliente, KeyEvent.VK_DELETE, "nada") { }
            try { formCliente.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles clientes", Herramientas.getStackTraceString(e)) }
            def art = Nadesico.conectar().getCliente(ID)
            formCliente.setTxtIDCliente     art.id_cliente    as String
            formCliente.setTxtRFC           art.RFC           as String
            formCliente.setTxtDireccion     art.direccion     as String
            formCliente.setTxtTelefono      art.telefono      as String
            formCliente.setTxtRazonSocial   art.razonSocial   as String
            formCliente.setTxtCP            art.cp            as String
            formCliente.setTxtUModificacion art.uModificacion as String
            formCliente.setTxtDescuento     art.descuento     as String
            formCliente.setTxtSaldo         art.saldo         as String
            formCliente.ID                   = ID
            formCliente.setModoDetalles();
            return formCliente
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
        } catch(e) { Dialogos.error("Error al lanzar los detalles Cliente", e) }
    }

    static def guardar(formCliente)
    {
        try{
        if(cerrojo(PMA_MODIFICARCLIENTE)){
            Herramientas.verificaCampos {
                def RFC             = formCliente.getTxtRFC()
                def direccion       = formCliente.getTxtDireccion()
                def telefono        = formCliente.getTxtTelefono()
                def RazonSocial     = formCliente.getTxtRazonSocial()
                def Saldo           = formCliente.getTxtSaldo()
                def descuento       = formCliente.getTxtDescuento()
                def CP              = formCliente.getTxtCP()
                Herramientas.verificaCampo(RFC,Herramientas.texto,"RFC"+Herramientas.error1)
                Herramientas.verificaCampo(direccion,Herramientas.texto,"Dirreccion"+Herramientas.error1)
                Herramientas.verificaCampo(telefono,Herramientas.textoVacio,"Telefono"+Herramientas.error4)
                Herramientas.verificaCampo(RazonSocial,Herramientas.texto,"Razon Social"+Herramientas.error1)
                Herramientas.verificaCampo(Saldo,Herramientas.numeroReal,"Saldo"+Herramientas.error3)
                Herramientas.verificaCampo(CP,Herramientas.textoVacio,"CP"+Herramientas.error4)
                Herramientas.verificaCampo(descuento,Herramientas.numeroReal,"Descuento"+Herramientas.error3)
                descuento     = descuento as Double
                Saldo         = Saldo as Double
                try {
                    def serv = Nadesico.conectar()
                    Dialogos.lanzarAlerta(serv.addCliente(RFC,direccion,telefono,RazonSocial,Saldo,CP,descuento))
                    serv.desconectar()
                } catch(e) { Dialogos.error("Error al enviar a la base de datos. El Cliente no se registró", e) }
                formCliente.dispose()
            }
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
        } catch(e) { Dialogos.error("Error al guardar nuevo cliente", e) }
    }

    static def lanzarFormNuevoCliente()
    {
        try{
        if(cerrojo(PMA_MODIFICARCLIENTE)){
            def form = new omoikane.formularios.Cliente()
            form.setVisible(true)
            Herramientas.panelFormulario(form)
            Herramientas.In2ActionX(form, KeyEvent.VK_F3    , "nada") { }
            Herramientas.In2ActionX(form, KeyEvent.VK_F4    , "nada") { }
            Herramientas.In2ActionX(form, KeyEvent.VK_F5    , "nada") { }
            Herramientas.In2ActionX(form, KeyEvent.VK_F8    , "nada") { }
            Herramientas.In2ActionX(form, KeyEvent.VK_DELETE, "nada") { }
            Herramientas.In2ActionX(form, KeyEvent.VK_F6    , "guardar"  ) { form.btnGuardar.doClick()  }
            escritorio.getPanelEscritorio().add(form)
            form.toFront()
            try { form.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles Cliente", Herramientas.getStackTraceString(e)) }
            form.setEditable(true);
            form.setModoNuevo();
            return form
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
        } catch(e) { Dialogos.error("Error al lanzar formulario de un nuevo Cliente", e) }
    }

    static def lanzarModificarCliente(ID)
    {
        try{
        def formCliente = lanzarDetallesClientes(ID)
        Herramientas.In2ActionX(formCliente, KeyEvent.VK_F6    , "modificar"  ) { formCliente.btnModificar.doClick()  }
        formCliente.setModoModificar();
        return formCliente
        } catch(e) { Dialogos.error("Error al lanzar modificar Cliente", e) }
    }

    static def modificar(formCliente)
    {
        try{
        if(cerrojo(PMA_MODIFICARCLIENTE)){
            Herramientas.verificaCampos {
                def RFC             = formCliente.getTxtRFC()
                def IDCliente       = formCliente.getTxtIDCliente()
                def direccion       = formCliente.getTxtDireccion()
                def telefono        = formCliente.getTxtTelefono()
                def RazonSocial     = formCliente.getTxtRazonSocial()
                def Saldo           = formCliente.getTxtSaldo()
                def descuento       = formCliente.getTxtDescuento()
                def CP              = formCliente.getTxtCP()
                Herramientas.verificaCampo(RFC,Herramientas.texto,"RFC"+Herramientas.error1)
                Herramientas.verificaCampo(direccion,Herramientas.texto,"Dirreccion"+Herramientas.error1)
                Herramientas.verificaCampo(telefono,Herramientas.textoVacio,"Telefono"+Herramientas.error4)
                Herramientas.verificaCampo(RazonSocial,Herramientas.texto,"Razon Social"+Herramientas.error1)
                Herramientas.verificaCampo(Saldo,Herramientas.numeroReal,"Saldo"+Herramientas.error3)
                Herramientas.verificaCampo(CP,Herramientas.textoVacio,"CP"+Herramientas.error4)
                Herramientas.verificaCampo(descuento,Herramientas.numeroReal,"Descuento"+Herramientas.error3)
                descuento     = descuento as Double
                Saldo         = Saldo as Double
                try{
                def serv = Nadesico.conectar()
                Dialogos.lanzarAlerta(serv.modCliente(RFC,direccion,telefono,RazonSocial,Saldo,CP,descuento,IDCliente))
                serv.desconectar()
                } catch(e) { Dialogos.error("Error al enviar a la base de datos. El Cliente no se modifico", e) }
                return formCliente
             }
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
        } catch(e) { Dialogos.error("Error al modificar Cliente", e) }
    }
}