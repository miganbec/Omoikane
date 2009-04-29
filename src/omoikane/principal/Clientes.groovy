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
import static omoikane.sistema.Usuarios.*;
import static omoikane.sistema.Permisos.*;
/**
 *
 * @author Adan
 */
class Clientes {
    static def escritorio = omoikane.principal.Principal.escritorio

    static def lanzarCatalogo()
    {
        if(cerrojo(PMA_ABRIRCLIENTE)){
            def cat = (new omoikane.formularios.CatalogoClientes())
            cat.setVisible(true);
            escritorio.getPanelEscritorio().add(cat)
            Herramientas.objetosAll(cat)
            Herramientas.setColumnsWidth(cat.jTable1, [0.15,0.15,0.31,0.12,0.12,0.12]);
            Herramientas.In2ActionX(cat, KeyEvent.VK_ESCAPE, "cerrar"   ) { cat.btnCerrar.doClick()   }
            cat.txtBusqueda.keyReleased = { if(it.keyCode == it.VK_ESCAPE) cat.btnCerrar.doClick() }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F4    , "detalles" ) { cat.btnDetalles.doClick() }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F5    , "nuevo"    ) { cat.btnNuevo.doClick()    }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F6    , "modificar") { cat.btnModificar.doClick()}
            Herramientas.In2ActionX(cat, KeyEvent.VK_DELETE, "eliminar" ) { cat.btnEliminar.doClick() }
            Herramientas.iconificable(cat)
            cat.toFront()
            try { cat.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario catálogo de clientes", Herramientas.getStackTraceString(e)) }
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
        if(cerrojo(PMA_ELIMINARCLIENTE)){
            def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
            db.execute("DELETE FROM clientes WHERE id_cliente = " + ID)
            db.close()
            Dialogos.lanzarAlerta("Cliente " + ID + " supuestamente eliminado")
         }else{Dialogos.lanzarAlerta("Acceso Denegado")}
        */
    }

    static def lanzarDetallesClientes(ID)
    {
        if(cerrojo(PMA_DETALLESCLIENTE)){
            def formCliente = new omoikane.formularios.Cliente()
            formCliente.setVisible(true)
            Herramientas.funcionesObjetos(formCliente)
            escritorio.getPanelEscritorio().add(formCliente)
            formCliente.toFront()
            Herramientas.funcionesObjetos(formCliente)
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
            return formCliente
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def guardar(formCliente)
    {
        if(cerrojo(PMA_MODIFICARCLIENTE)){
            Herramientas.verificaCampos {
                def RFC             = formCliente.getTxtRFC()
                def direccion       = formCliente.getTxtDireccion()
                def telefono        = formCliente.getTxtTelefono()
                def RazonSocial     = formCliente.getTxtRazonSocial()
                def Saldo           = formCliente.getTxtSaldo()
                def descuento       = formCliente.getTxtDescuento()
                def CP              = formCliente.getTxtCP()
                Herramientas.verificaCampo(RFC,/^([a-zA-Z0-9_\-\s\ñ\Ñ\.\´]+)$/,"RFC sólo puede incluír números, letras, espacios, - , _ y puntos ")
                Herramientas.verificaCampo(direccion,/^([a-zA-Z0-9_\-\s\ñ\Ñ\(\)\,\.\#\\\/]+áéíóú)$$/,"Direccion puede incluir numeros, letras, espacios, parentecis, comas, puntos, #, _, - , acentos y diagonales")
                Herramientas.verificaCampo(telefono,/^([a-zA-Z0-9_\-\s\ñ\Ñ\(\)\,\.\#\\\/]+)$/,"Telefono puede incluir numeros, letras, espacios, parentecis, comas, puntos, #, _, - y diagonales")
                Herramientas.verificaCampo(RazonSocial,/^([a-zA-Z0-9_\-\s\ñ\Ñ\(\)\,\.\#\\\/]+)$/,"Razon Social puede incluir numeros, letras, espacios, parentecis, comas, puntos, #, _, - y diagonales")
                Herramientas.verificaCampo(Saldo,/^([0-9]*[\.]{0,1}[0-9]+)$/,"Saldo sólo puede incluír números reales positivos")
                Herramientas.verificaCampo(CP,/^([a-zA-Z0-9_\-\s\ñ\Ñ\.]+)$/,"CP sólo puede incluír números, letras, espacios , - , _ y .")
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
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def lanzarFormNuevoCliente()
    {
        if(cerrojo(PMA_MODIFICARCLIENTE)){
            def form = new omoikane.formularios.Cliente()
            form.setVisible(true)
            Herramientas.funcionesObjetos(form)
            Herramientas.In2ActionX(form, KeyEvent.VK_F6    , "guardar"  ) { form.btnGuardar.doClick()  }
            escritorio.getPanelEscritorio().add(form)
            form.toFront()
            try { form.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles Cliente", Herramientas.getStackTraceString(e)) }
            form.setEditable(true);
            form.setModoNuevo();
            return form
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def lanzarModificarCliente(ID)
    {
        def formCliente = lanzarDetallesClientes(ID)
        Herramientas.In2ActionX(formCliente, KeyEvent.VK_F6    , "modificar"  ) { formCliente.btnModificar.doClick()  }
        formCliente.setModoModificar();
        formCliente
    }

    static def modificar(formCliente)
    {
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
                Herramientas.verificaCampo(RFC,/^([a-zA-Z0-9_\-\s\ñ\Ñ\.]+)$/,"RFC sólo puede incluír números, letras, espacios, - , _ y puntos ")
                Herramientas.verificaCampo(direccion,/^([a-zA-Z0-9_\-\s\ñ\Ñ\(\)\,\.\#\\\/]+)$/,"Direccion puede incluir numeros, letras, espacios, parentecis, comas, puntos, #, _, - y diagonales")
                Herramientas.verificaCampo(telefono,/^([a-zA-Z0-9_\-\s\ñ\Ñ\(\)\,\.\#\\\/]+)$/,"Telefono puede incluir numeros, letras, espacios, parentecis, comas, puntos, #, _, - y diagonales")
                Herramientas.verificaCampo(RazonSocial,/^([a-zA-Z0-9_\-\s\ñ\Ñ\(\)\,\.\#\\\/]+)$/,"Razon Social puede incluir numeros, letras, espacios, parentecis, comas, puntos, #, _, - y diagonales")
                Herramientas.verificaCampo(Saldo,/^([0-9]*[\.]{0,1}[0-9]+)$/,"Saldo sólo puede incluír números reales positivos")
                Herramientas.verificaCampo(CP,/^([a-zA-Z0-9_\-\s\ñ\Ñ\.]+)$/,"CP sólo puede incluír números, letras, espacios , - , _ y .")
                Herramientas.verificaCampo(descuento,/^([0-9]*[\.]{0,1}[0-9]+)$/,"Descuento sólo puede incluír números reales positivos")
                descuento     = descuento as Double
                Saldo         = Saldo as Double
                def serv = Nadesico.conectar()
                Dialogos.lanzarAlerta(serv.modCliente(RFC,direccion,telefono,RazonSocial,Saldo,CP,descuento,IDCliente))
                return formCliente
             }
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

}

