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
class Usuarios {
    static def escritorio = omoikane.principal.Principal.escritorio
    static def Almacen = 1

    static def lanzarCatalogo()
    {

        def cat = (new omoikane.formularios.CatalogoUsuarios())
        cat.setVisible(true);
        escritorio.getPanelEscritorio().add(cat)
        Herramientas.setColumnsWidth(cat.jTable1, [0.4,0.3,0.3]);
        Herramientas.In2ActionX(cat, KeyEvent.VK_ESCAPE, "cerrar"   ) { cat.btnCerrar.doClick()   }
        cat.txtBusqueda.keyReleased = { if(it.keyCode == it.VK_ESCAPE) cat.btnCerrar.doClick() }
        Herramientas.In2ActionX(cat, KeyEvent.VK_F4    , "detalles" ) { cat.btnDetalles.doClick() }
        Herramientas.In2ActionX(cat, KeyEvent.VK_F5    , "nuevo"    ) { cat.btnNuevo.doClick()    }
        Herramientas.In2ActionX(cat, KeyEvent.VK_F6    , "modificar") { cat.btnModificar.doClick()}
        Herramientas.In2ActionX(cat, KeyEvent.VK_DELETE, "eliminar" ) { cat.btnEliminar.doClick() }
        Herramientas.iconificable(cat)
        cat.toFront()
        try { cat.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario catálogo de Usuarios", Herramientas.getStackTraceString(e)) }
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
        def reporte = new Reporte('omoikane/reportes/ReporteUsuarios.jasper',[txtQuery:txtQuery]);
        reporte.lanzarPreview()
    }

    static def eliminarUsuario(ID)
    {
        Dialogos.lanzarAlerta("Función desactivada!")
        /*
        def db = Sql.newInstance("jdbc:mysql://localhost/omoikane?user=root&password=", "root", "", "com.mysql.jdbc.Driver")
        db.execute("DELETE FROM usuarios WHERE id_usr = " + ID)
        db.close()
        Dialogos.lanzarAlerta("Usuarios " + ID + " supuestamente eliminado")
        */
    }

    static def lanzarDetallesUsuario(ID)
    {
        def formUsuario = new omoikane.formularios.Usuario()
        formUsuario.setVisible(true)
        escritorio.getPanelEscritorio().add(formUsuario)
        formUsuario.toFront()
        try { formUsuario.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles Usuario", Herramientas.getStackTraceString(e)) }

        def art         = Nadesico.conectar().getUsuario(ID,Almacen)

        formUsuario.setTxtIDUSR          art.id_usuario     as String
        formUsuario.setTxtFecha          art.fecha_hora_alta  as String
        formUsuario.setTxtNombre         art.nombre     as String
        formUsuario.setTxtUModificacion  art.uModificacion as String
        formUsuario.setTxtNIP            art.nip        as String
        switch(art.perfil) {
	case 0:formUsuario.setTxtPerfil( "Cajero"      ) ; break;
    case 1:formUsuario.setTxtPerfil("Supervisor"   ); break;
    case 2:formUsuario.setTxtPerfil( "Gerente"      ); break;
    case 3:formUsuario.setTxtPerfil( "Administrador"); break;
    case 4:formUsuario.setTxtPerfil( "Propietario")  ; break;
    default: break;
	}

        formUsuario.ID                   = ID
        formUsuario.setModoDetalles();
        formUsuario


    }

    static def guardar(formUsuario)
    {
        Herramientas.verificaCampos {

        def Nombre            = formUsuario.getTxtNombre()
        def H1                = formUsuario.getTxtH1()
        def H2                = formUsuario.getTxtH2()
        def H3                = formUsuario.getTxtH3()
        def NIP               = formUsuario.getTxtNIP()
        def Perfil            = formUsuario.getTxtPerfil()
     

        Herramientas.verificaCampo(Nombre,/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+]+)$/,"Nombre sólo puede incluír números, letras, espacios, *, -,_ y +.")
        Herramientas.verificaCampo(H1,/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+]+)$/,"Huella 1 sólo puede incluír números, letras, espacios, *, -,_ y +.")
        Herramientas.verificaCampo(H2,/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+]+)$/,"huella 2 sólo puede incluír números, letras, espacios, *, -,_ y +.")
        Herramientas.verificaCampo(H3,/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+]+)$/,"huella 3 sólo puede incluír números, letras, espacios, *, -,_ y +.")
        Herramientas.verificaCampo(NIP,/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+]+)$/,"NIP sólo puede incluír números, letras, espacios, *, -,_ y +.")

    switch(Perfil) {
	case "Cajero"       :Perfil=0; break;
    case "Supervisor"   :Perfil=1; break;
    case "Gerente"      :Perfil=2; break;
    case "Administrador":Perfil=3; break;
    case "Propietario"  :Perfil=4; break;
    default: break;
	}
       
        try {
            def serv = Nadesico.conectar()
            Dialogos.lanzarAlerta(serv.addUsuario(Nombre,H1,H2,H3,NIP,Perfil,Almacen))
            serv.desconectar()
        } catch(e) { Dialogos.error("Error al enviar a la base de datos. El Usuario no se registró", e) }

        formUsuario.dispose()
        }
    }

    static def lanzarFormNuevoUsuario()
    {
        def form = new omoikane.formularios.Usuario()
        form.setVisible(true)
        escritorio.getPanelEscritorio().add(form)
        form.toFront()
         try { form.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario detalles Usuarios", Herramientas.getStackTraceString(e)) }
        form.setEditable(true);
        form.setModoNuevo();
    }
    static def lanzarModificarUsuario(ID)
    {
        def formUsuario = lanzarDetallesUsuario(ID)
        formUsuario.setModoModificar();
    }
    static def modificar(formUsuario)
    {
        Herramientas.verificaCampos {
        def USR               = formUsuario.getTxtIDUSR()
        def Nombre            = formUsuario.getTxtNombre()
        def H1                = formUsuario.getTxtH1()
        def H2                = formUsuario.getTxtH2()
        def H3                = formUsuario.getTxtH3()
        def NIP               = formUsuario.getTxtNIP()
        def Tem               = formUsuario.getTxtPerfil()
        def Perfil

        Herramientas.verificaCampo(Nombre,/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+]+)$/,"Nombre sólo puede incluír números, letras, espacios, *, -,_ y +.")
        Herramientas.verificaCampo(H1,/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+]+)$/,"Huella 1 sólo puede incluír números, letras, espacios, *, -,_ y +.")
        Herramientas.verificaCampo(H2,/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+]+)$/,"huella 2 sólo puede incluír números, letras, espacios, *, -,_ y +.")
        Herramientas.verificaCampo(H3,/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+]+)$/,"huella 3 sólo puede incluír números, letras, espacios, *, -,_ y +.")
        Herramientas.verificaCampo(NIP,/^([a-zA-Z0-9_\-\s\ñ\Ñ\*\+]+)$/,"NIP sólo puede incluír números, letras, espacios, *, -,_ y +.")

        switch(Tem) {
	case "Cajero"       :Perfil=0; break;
    case "Supervisor"   :Perfil=1; break;
    case "Gerente"      :Perfil=2; break;
    case "Administrador":Perfil=3; break;
    case "Propietario"  :Perfil=4; break;
    default: break;
	}

            def serv = Nadesico.conectar()
            Dialogos.lanzarAlerta(serv.modUsuario(Nombre,H1,H2,H3,NIP,Perfil,Almacen,USR))
            }
        }

}

