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

class Usuarios {
    static def escritorio = omoikane.principal.Principal.escritorio
    static def Almacen = Principal.IDAlmacen
    
    static def lanzarCatalogo()
    {
        try{
        if(cerrojo(PMA_ABRIRUSUARIO)){
            def cat = (new omoikane.formularios.CatalogoUsuarios())
            cat.setVisible(true);
            escritorio.getPanelEscritorio().add(cat)
            Herramientas.setColumnsWidth(cat.jTable1, [0.4,0.3,0.3]);
            Herramientas.panelCatalogo(cat)
            Herramientas.In2ActionX(cat, KeyEvent.VK_F1    , "nada"     ) { }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F3    , "buscar"   ) { cat.txtBusqueda.requestFocusInWindow() }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F4    , "detalles" ) { cat.btnDetalles.doClick() }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F5    , "nuevo"    ) { cat.btnNuevo.doClick()    }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F6    , "modificar") { cat.btnModificar.doClick()}
            Herramientas.In2ActionX(cat, KeyEvent.VK_F7    , "nada"     ) { }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F8    , "imprimir" ) { cat.btnImprimir.doClick() }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F11   , "nada"     ) { }
            Herramientas.In2ActionX(cat, KeyEvent.VK_F12   , "nada"     ) { }
            Herramientas.In2ActionX(cat, KeyEvent.VK_DELETE, "eliminar" ) { cat.btnEliminar.doClick() }
            Herramientas.iconificable(cat)
            cat.toFront()
            try { cat.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario catálogo de Usuarios", Herramientas.getStackTraceString(e)) }
            cat.txtBusqueda.requestFocus()
            return cat
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
        } catch(e) { Dialogos.error("Error al lanzar catalogo de Usuarios", e) }
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
        cat.btnAceptar.actionPerformed = { def catTab = cat.jTable1; retorno = catTab.getModel().getValueAt(catTab.getSelectedRow(), 0) as String; cat.btnCerrar.doClick(); }
        synchronized(foco){foco.wait()}
        return retorno
        } catch(e) { Dialogos.error("Error al lanzar el modo dialogo del catalogo de Usuarios", e) }
    }

    static def lanzarImprimir(txtQuery)
    {
        try{
        def reporte = new Reporte('omoikane/reportes/ReporteUsuarios.jasper',[txtQuery:txtQuery]);
        reporte.lanzarPreview()
        } catch(e) { Dialogos.error("Error al crear el Reporte de Usuarios", e) }
    }

    static def eliminarUsuario(ID)
    {
        try{
        Dialogos.lanzarAlerta("Función desactivada!")
        /*
        if(cerrojo(PMA_ELIMINARUSUARIO)){
        def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        db.execute("DELETE FROM usuarios WHERE id_usr = " + ID)
        db.close()
        Dialogos.lanzarAlerta("Usuarios " + ID + " supuestamente eliminado")
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
        */
       } catch(e) { Dialogos.error("Error al crear el Reporte de Usuarios", e) }
    }

    static def lanzarDetallesUsuario(ID)
    {
        try{
        if(cerrojo(PMA_DETALLESUSUARIO)){
            def formUsuario = new omoikane.formularios.Usuario()
            formUsuario.setVisible(true)
            escritorio.getPanelEscritorio().add(formUsuario)
            formUsuario.toFront()
            Herramientas.panelFormulario(formUsuario)
            Herramientas.In2ActionX(formUsuario, KeyEvent.VK_F1    , "nada") { }
            Herramientas.In2ActionX(formUsuario, KeyEvent.VK_F3    , "nada") { }
            Herramientas.In2ActionX(formUsuario, KeyEvent.VK_F4    , "nada") { }
            Herramientas.In2ActionX(formUsuario, KeyEvent.VK_F5    , "nada") { }
            Herramientas.In2ActionX(formUsuario, KeyEvent.VK_F6    , "nada") { }
            Herramientas.In2ActionX(formUsuario, KeyEvent.VK_F7    , "nada") { }
            Herramientas.In2ActionX(formUsuario, KeyEvent.VK_F8    , "nada") { }
            Herramientas.In2ActionX(formUsuario, KeyEvent.VK_F11   , "nada") { }
            Herramientas.In2ActionX(formUsuario, KeyEvent.VK_F12   , "nada") { }
            Herramientas.In2ActionX(formUsuario, KeyEvent.VK_DELETE, "nada") { }
            try { formUsuario.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles Usuario", Herramientas.getStackTraceString(e)) }
            def art         = Nadesico.conectar().getUsuario(ID,Almacen)
            formUsuario.setTxtIDUSR          art.id_usuario          as String
            formUsuario.setTxtFecha          art.fecha_hora_alta     as String
            formUsuario.setTxtNombre         art.nombre              as String
            formUsuario.setTxtUModificacion  art.uModificacion       as String
            formUsuario.setTxtNIP            art.nip                 as String
            switch(art.perfil){
                case 0:formUsuario.setTxtPerfil( "Cajero"      ) ; break;
                case 0.5:formUsuario.setTxtPerfil( "Capturista"      ) ; break;
                case 1:formUsuario.setTxtPerfil("Supervisor"   ); break;
                case 2:formUsuario.setTxtPerfil( "Gerente"      ); break;
                case 3:formUsuario.setTxtPerfil( "Administrador"); break;
                case 4:formUsuario.setTxtPerfil( "Propietario")  ; break;
                default: break;}
            formUsuario.ID                   = ID
            formUsuario.setModoDetalles();
            return formUsuario
        } else{ Dialogos.lanzarAlerta("Acceso Denegado") }
        } catch(e) { Dialogos.error("Error al lanzar detalles Usuarios", e) }
    }

    static def guardar(formUsuario)
    {
        try{
        if(cerrojo(PMA_MODIFICARUSUARIO)){
            Herramientas.verificaCampos {
                def Nombre            = formUsuario.getTxtNombre()
                def H1                = formUsuario.getTxtH1()
                def H2                = formUsuario.getTxtH2()
                def H3                = formUsuario.getTxtH3()
                def NIP               = formUsuario.getTxtNIP()
                def Perfil            = formUsuario.getTxtPerfil()
                Herramientas.verificaCampo(Nombre,Herramientas.texto,"Nombre"+Herramientas.error1)
                Herramientas.verificaCampo(NIP,Herramientas.numero,"NIP"+Herramientas.error2)
                switch(Perfil) {
                    case "Cajero"       :Perfil=0; break;
                    case "Capturista"   :Perfil=0.5; break;
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
        } catch(e) { Dialogos.error("Error al lanzar guardar Usuario", e) }
    }

    static def lanzarFormNuevoUsuario()
    {
        try{
        def form = new omoikane.formularios.Usuario()
        form.setVisible(true)
        escritorio.getPanelEscritorio().add(form)
        Herramientas.In2ActionX(form, KeyEvent.VK_F3    , "nada"    ) { }
        Herramientas.In2ActionX(form, KeyEvent.VK_F3    , "nada"    ) { }
        Herramientas.In2ActionX(form, KeyEvent.VK_F4    , "nada"    ) { }
        Herramientas.In2ActionX(form, KeyEvent.VK_F5    , "nada"    ) { }
        Herramientas.In2ActionX(form, KeyEvent.VK_F6    , "guardar" ) { form.btnGuardar.doClick()}
        Herramientas.In2ActionX(form, KeyEvent.VK_F7    , "nada"    ) { }
        Herramientas.In2ActionX(form, KeyEvent.VK_F8    , "nada"    ) { }
        Herramientas.In2ActionX(form, KeyEvent.VK_F11   , "nada"    ) { }
        Herramientas.In2ActionX(form, KeyEvent.VK_F12   , "nada"    ) { }
        Herramientas.In2ActionX(form, KeyEvent.VK_DELETE, "nada"    ) { }
        Herramientas.panelFormulario(form)
        form.toFront()
        try { form.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles Usuarios", Herramientas.getStackTraceString(e)) }
        form.setEditable(true);
        form.setModoNuevo();
        return form
        } catch(e) { Dialogos.error("Error al lanzar formulario de un nuevo Usuario", e) }
    }

    static def lanzarModificarUsuario(ID)
    {
        try{
        def formUsuario = lanzarDetallesUsuario(ID)
        formUsuario.setModoModificar();
        Herramientas.In2ActionX(formUsuario, KeyEvent.VK_F6    , "modificar") { formUsuario.btnModificar.doClick()}
        return formUsuario
        } catch(e) { Dialogos.error("Error al lanzar modificar Usuarios", e) }
    }

    static def modificar(formUsuario)
    {
        try{
        if(cerrojo(PMA_MODIFICARUSUARIO)){
            Herramientas.verificaCampos {
                def USR               = formUsuario.getTxtIDUSR()
                def Nombre            = formUsuario.getTxtNombre()
                def NIP               = formUsuario.getTxtNIP()
                def Perfil            = formUsuario.getTxtPerfil()
                Herramientas.verificaCampo(Nombre,Herramientas.texto,"Nombre"+Herramientas.error1)
                Herramientas.verificaCampo(NIP,Herramientas.numero,"NIP"+Herramientas.error2)
                switch(Perfil) {
                    case "Cajero"       :Perfil=0; break;
                    case "Capturista"   :Perfil=0.5; break;
                    case "Supervisor"   :Perfil=1; break;
                    case "Gerente"      :if(cerrojo(ADMINISTRADOR)){Perfil=2}else{Dialogos.lanzarAlerta("Acceso Denegado solo el administrador puede agregar un nuevo gerente")}; break;
                    case "Administrador":if(cerrojo(PROPIETARIO)){Perfil=3}else{Dialogos.lanzarAlerta("Acceso Denegado solo un propietario puede agregar un nuevo administrador")}; break;
                    case "Propietario"  :if(cerrojo(PROPIETARIO)){Perfil=4}else{Dialogos.lanzarAlerta("Acceso Denegado no es un propietario")}; break;
                default: break;}
                Herramientas.verificaCampo(Perfil,/^([0-9]{1,1})$/,"Solo puede agregar usuario con menor privilegio que el suyo")
                try{
                def serv = Nadesico.conectar()
                Dialogos.lanzarAlerta(serv.modUsuario(Nombre,NIP,Perfil,Almacen,USR))
                serv.desconectar()
                }catch(e) { Dialogos.error("Error al enviar a la base de datos. El Usuario no se modifico", e)}}
        }else{Dialogos.lanzarAlerta("Acceso Denegado")}
    }catch(e) { Dialogos.error("Error al lanzar modificar Usuarios", e) }
    } 
}