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
import javax.swing.*;
/**
 *
 * @author Adan
 */
class Clientes {
    static def escritorio = omoikane.principal.Principal.escritorio

    static def lanzarCatalogo()
    {
        def cat = (new omoikane.formularios.CatalogoClientes())
        cat.setVisible(true);
        escritorio.getPanelEscritorio().add(cat)
        Herramientas.setColumnsWidth(cat.jTable1, [0.15,0.15,0.31,0.13,0.13,0.13]);
        Herramientas.In2ActionX(cat, KeyEvent.VK_ESCAPE, "cerrar"   ) { cat.btnCerrar.doClick()   }
        Herramientas.In2ActionX(cat, KeyEvent.VK_F4    , "detalles" ) { cat.btnDetalles.doClick() }
        Herramientas.In2ActionX(cat, KeyEvent.VK_F5    , "nuevo"    ) { cat.btnNuevo.doClick()    }
        Herramientas.In2ActionX(cat, KeyEvent.VK_F6    , "modificar") { cat.btnModificar.doClick()}
        Herramientas.In2ActionX(cat, KeyEvent.VK_DELETE, "eliminar" ) { cat.btnEliminar.doClick() }
        Herramientas.iconificable(cat)
        cat.toFront()
        try { cat.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario catálogo de clientes", Herramientas.getStackTraceString(e)) }
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

    static def lanzarImprimir(txtQuery)
    {
        def reporte = new Reporte('omoikane/reportes/ReporteClientes.jasper',[txtQuery:txtQuery]);
        reporte.lanzarPreview()
    }

    static def eliminarCliente(ID)
    {
        Dialogos.lanzarAlerta("Función desactivada!")
        /*
        def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        db.execute("DELETE FROM clientes WHERE id_cliente = " + ID)
        db.close()
        Dialogos.lanzarAlerta("Cliente " + ID + " supuestamente eliminado")
        */
    }

    static def lanzarDetallesClientes(ID)
    {
        def formCliente = new omoikane.formularios.Cliente()
        formCliente.setVisible(true)
        escritorio.getPanelEscritorio().add(formCliente)
        formCliente.toFront()
        try { formCliente.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles clientes", Herramientas.getStackTraceString(e)) }

        def art         = Nadesico.conectar().getCliente(ID)

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
        formCliente


    }

    static def guardar(formCliente)
    {
        Herramientas.verificaCampos {
        def RFC             = formCliente.getTxtRFC()
        def direccion       = formCliente.getTxtDireccion()
        def telefono        = formCliente.getTxtTelefono()
        def RazonSocial     = formCliente.getTxtRazonSocial()
        def Saldo           = formCliente.getTxtSaldo()
        def descuento       = formCliente.getTxtDescuento()
        def CP              = formCliente.getTxtCP()

        Herramientas.verificaCampo(RFC,/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+]+)$/,"RFC sólo puede incluír números, letras, espacios, *, -,_ y +.")
        Herramientas.verificaCampo(direccion,/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+]+)$/,"Direccion sólo puede incluír números, letras, espacios, *, -,_ y +.")
        Herramientas.verificaCampo(telefono,/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+]+)$/,"Telefono sólo puede incluír números, letras, espacios, *, -,_ y +.")
        Herramientas.verificaCampo(RazonSocial,/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+]+)$/,"RazonSocial sólo puede incluír números, letras, espacios, *, -,_ y +.")
        Herramientas.verificaCampo(Saldo,/^([0-9]*[\.]{0,1}[0-9]+)$/,"Saldo sólo puede incluír números reales positivos")
        Herramientas.verificaCampo(CP,/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+]+)$/,"CP sólo puede incluír números, letras, espacios, *, -,_ y +.")
        Herramientas.verificaCampo(descuento,/^([0-9]*[\.]{0,1}[0-9]+)$/,"Descuento sólo puede incluír números reales positivos")

        descuento     = descuento as Double
        Saldo         = Saldo as Double

        try {
            def serv = Nadesico.conectar()
            Dialogos.lanzarAlerta(serv.addCliente(RFC,direccion,telefono,RazonSocial,Saldo,CP,descuento))
            serv.desconectar()
        } catch(e) { Dialogos.error("Error al enviar a la base de datos. El Cliente no se registró", e) }

        formCliente.dispose()
        }
    }

    static def lanzarFormNuevoCliente()
    {
        def form = new omoikane.formularios.Cliente()
        form.setVisible(true)
        escritorio.getPanelEscritorio().add(form)
        form.toFront()
         try { form.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles Cliente", Herramientas.getStackTraceString(e)) }
        form.setEditable(true);
        form.setModoNuevo();
    }
    static def lanzarModificarCliente(ID)
    {
        def formCliente = lanzarDetallesClientes(ID)
        formCliente.setModoModificar();
    }
    static def modificar(formCliente)
    {
        Herramientas.verificaCampos {
        def RFC             = formCliente.getTxtRFC()
        def IDCliente       = formCliente.getTxtIDCliente()
        def direccion       = formCliente.getTxtDireccion()
        def telefono        = formCliente.getTxtTelefono()
        def RazonSocial     = formCliente.getTxtRazonSocial()
        def Saldo           = formCliente.getTxtSaldo()
        def descuento       = formCliente.getTxtDescuento()
        def CP              = formCliente.getTxtCP()

        Herramientas.verificaCampo(RFC,/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+]+)$/,"RFC sólo puede incluír números, letras, espacios, *, -,_ y +.")
        Herramientas.verificaCampo(direccion,/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+]+)$/,"Direccion sólo puede incluír números, letras, espacios, *, -,_ y +.")
        Herramientas.verificaCampo(telefono,/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+]+)$/,"Telefono sólo puede incluír números, letras, espacios, *, -,_ y +.")
        Herramientas.verificaCampo(RazonSocial,/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+]+)$/,"RazonSocial sólo puede incluír números, letras, espacios, *, -,_ y +.")
        Herramientas.verificaCampo(Saldo,/^([0-9]*[\.]{0,1}[0-9]+)$/,"Saldo sólo puede incluír números reales positivos")
        Herramientas.verificaCampo(CP,/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+]+)$/,"CP sólo puede incluír números, letras, espacios, *, -,_ y +.")
        Herramientas.verificaCampo(descuento,/^([0-9]*[\.]{0,1}[0-9]+)$/,"Descuento sólo puede incluír números reales positivos")

        descuento     = descuento as Double
        Saldo         = Saldo as Double

            def serv = Nadesico.conectar()
            Dialogos.lanzarAlerta(serv.modCliente(RFC,direccion,telefono,RazonSocial,Saldo,CP,descuento,IDCliente))
            }
        }

}

