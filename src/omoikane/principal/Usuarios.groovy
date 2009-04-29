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

class Usuarios {
    static def escritorio = omoikane.principal.Principal.escritorio
    static def Almacen = Principal.IDAlmacen

    static def lanzarCatalogo()
    {
        if(cerrojo(PMA_ABRIRUSUARIO)){
            def cat = (new omoikane.formularios.CatalogoUsuarios())
            cat.setVisible(true);
            escritorio.getPanelEscritorio().add(cat)
            Herramientas.setColumnsWidth(cat.jTable1, [0.4,0.3,0.3]);
            Herramientas.objetosAll(cat)
            Herramientas.In2ActionX(cat, KeyEvent.VK_ESCAPE, "cerrar"   ) { cat.btnCerrar.doClick()   }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F4    , "detalles" ) { cat.btnDetalles.doClick() }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F5    , "nuevo"    ) { cat.btnNuevo.doClick()    }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F6    , "modificar") { cat.btnModificar.doClick()}
            Herramientas.In2ActionX(cat, KeyEvent.VK_DELETE, "eliminar" ) { cat.btnEliminar.doClick() }
            Herramientas.iconificable(cat)
            cat.txtBusqueda.keyReleased = { if(it.keyCode == it.VK_ESCAPE) cat.btnCerrar.doClick() }
            cat.toFront()
            try { cat.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario catálogo de Usuarios", Herramientas.getStackTraceString(e)) }
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
        def reporte = new Reporte('omoikane/reportes/ReporteUsuarios.jasper',[txtQuery:txtQuery]);
        reporte.lanzarPreview()
    }

    static def eliminarUsuario(ID)
    {
        Dialogos.lanzarAlerta("Función desactivada!")
        /*
        if(cerrojo(PMA_ELIMINARUSUARIO)){
        def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        db.execute("DELETE FROM usuarios WHERE id_usr = " + ID)
        db.close()
        Dialogos.lanzarAlerta("Usuarios " + ID + " supuestamente eliminado")
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
        */
    }

    static def lanzarDetallesUsuario(ID)
    {
        if(cerrojo(PMA_DETALLESUSUARIO)){
            def formUsuario = new omoikane.formularios.Usuario()
            formUsuario.setVisible(true)
            escritorio.getPanelEscritorio().add(formUsuario)
            formUsuario.toFront()
            Herramientas.funcionesObjetos(formUsuario)
            try { formUsuario.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles Usuario", Herramientas.getStackTraceString(e)) }
            def art         = Nadesico.conectar().getUsuario(ID,Almacen)
            formUsuario.setTxtIDUSR          art.id_usuario          as String
            formUsuario.setTxtFecha          art.fecha_hora_alta     as String
            formUsuario.setTxtNombre         art.nombre              as String
            formUsuario.setTxtUModificacion  art.uModificacion       as String
            formUsuario.setTxtNIP            art.nip                 as String
            switch(art.perfil){
                case 0:formUsuario.setTxtPerfil( "Cajero"      ) ; break;
                case 1:formUsuario.setTxtPerfil("Supervisor"   ); break;
                case 2:formUsuario.setTxtPerfil( "Gerente"      ); break;
                case 3:formUsuario.setTxtPerfil( "Administrador"); break;
                case 4:formUsuario.setTxtPerfil( "Propietario")  ; break;
                default: break;}
            formUsuario.ID                   = ID
            formUsuario.setModoDetalles();
            return formUsuario
        } else{ Dialogos.lanzarAlerta("Acceso Denegado") }
    }

    static def guardar(formUsuario)
    {
        if(cerrojo(PMA_MODIFICARUSUARIO)){
            Herramientas.verificaCampos {
                def Nombre            = formUsuario.getTxtNombre()
                def H1                = formUsuario.getTxtH1()
                def H2                = formUsuario.getTxtH2()
                def H3                = formUsuario.getTxtH3()
                def NIP               = formUsuario.getTxtNIP()
                def Perfil            = formUsuario.getTxtPerfil()
                Herramientas.verificaCampo(Nombre,/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+]+)$/,"Nombre sólo puede incluír números, letras, espacios, *, -,_ y +.")
                Herramientas.verificaCampo(NIP,/^([0-9]+)$/,"NIP sólo puede incluír números.")
                switch(Perfil) {
                    case "Cajero"       :Perfil=0; break;
                    case "Supervisor"   :Perfil=1; break;
                    case "Gerente"      :if(cerrojo(ADMINISTRADOR)){Perfil=2}else{Dialogos.lanzarAlerta("Acceso Denegado solo el administrador puede agregar un nuevo gerente")}; break;
                    case "Administrador":if(cerrojo(PROPIETARIO)){Perfil=3}else{Dialogos.lanzarAlerta("Acceso Denegado solo un propietario puede agregar un nuevo administrador")}; break;
                    case "Propietario"  :if(cerrojo(PROPIETARIO))  {Perfil=4}else{Dialogos.lanzarAlerta("Acceso Denegado no es un propietario")}; break;
                    default: break;}
                if (Perfil instanceof String ){
                    Perfil = -1
                }
                try {
                    if(Perfil>=0)
                    {
                        def serv = Nadesico.conectar()
                        Dialogos.lanzarAlerta(serv.addUsuario(Nombre,H1,H2,H3,NIP,Perfil,Almacen))
                        serv.desconectar()
                        formUsuario.dispose()
                        return formUsuario
                    }
                }catch(e) { Dialogos.error("Error al enviar a la base de datos. El Usuario no se registró", e) }
                }
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }

    static def lanzarFormNuevoUsuario()
    {
        def form = new omoikane.formularios.Usuario()
        form.setVisible(true)
        escritorio.getPanelEscritorio().add(form)
        Herramientas.In2ActionX(form, KeyEvent.VK_F6    , "modificar") { form.btnGuardar.doClick()}
        Herramientas.funcionesObjetos(form)
        form.toFront()
        try { form.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles Usuarios", Herramientas.getStackTraceString(e)) }
        form.setEditable(true);
        form.setModoNuevo();
        return form
    }

    static def lanzarModificarUsuario(ID)
    {
        def formUsuario = lanzarDetallesUsuario(ID)
        formUsuario.setModoModificar();
        Herramientas.In2ActionX(formUsuario, KeyEvent.VK_F6    , "modificar") { formUsuario.btnModificar.doClick()}
        return formUsuario
    }

    static def modificar(formUsuario)
    {
        if(cerrojo(PMA_MODIFICARUSUARIO)){
            Herramientas.verificaCampos {
                def USR               = formUsuario.getTxtIDUSR()
                def Nombre            = formUsuario.getTxtNombre()
                def NIP               = formUsuario.getTxtNIP()
                def Perfil            = formUsuario.getTxtPerfil()
                Herramientas.verificaCampo(Nombre,/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+]+)$/,"Nombre sólo puede incluír números, letras, espacios, *, -,_ y +.")
                Herramientas.verificaCampo(NIP,/^([0-9]+)$/,"NIP sólo puede incluír números.")
                switch(Perfil) {
                    case "Cajero"       :Perfil=0; break;
                    case "Supervisor"   :Perfil=1; break;
                    case "Gerente"      :if(cerrojo(ADMINISTRADOR)){Perfil=2}else{Dialogos.lanzarAlerta("Acceso Denegado solo el administrador puede agregar un nuevo gerente")}; break;
                    case "Administrador":if(cerrojo(PROPIETARIO)){Perfil=3}else{Dialogos.lanzarAlerta("Acceso Denegado solo un propietario puede agregar un nuevo administrador")}; break;
                    case "Propietario"  :if(cerrojo(PROPIETARIO)){Perfil=4}else{Dialogos.lanzarAlerta("Acceso Denegado no es un propietario")}; break;
                default: break;}
                Herramientas.verificaCampo(Perfil,/^([0-9]{1,1})$/,"Solo puede agregar usuario con menor privilegio que el suyo")
                def serv = Nadesico.conectar()
                Dialogos.lanzarAlerta(serv.modUsuario(Nombre,NIP,Perfil,Almacen,USR))}
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }
}

