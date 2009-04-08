
package omoikane.sistema;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.regex.*;
import java.util.Enumeration;
import com.griaule.grfingerjava.*;
import groovy.sql.*;
import omoikane.principal.*;


public class Usuarios {
    //public Usuario usrActivo = new Usuario();
    private static boolean autorizado = false;
    public static def usuarioActivo = null
    public static def CAJERO        = 0
    public static def SUPERVISOR    = 1
    public static def GERENTE       = 2
    public static def ADMINISTRADOR = 3
    public static def PROPIETARIO   = 4

    public static def login() {
        usuarioActivo = identificaPersona()
        usuarioActivo
    }
    public static def logout() {
        usuarioActivo = null
    }
    public static def identificaPersona() {
            def escritorio   = omoikane.principal.Principal.escritorio.getFrameEscritorio()
            def fingerPrint  = new omoikane.formularios.WndLeerHuella(escritorio).getHuella()
            //fingerPrint      = Herramientas.bytes2HexString(fingerPrint)
            def serv         = Nadesico.conectar()
//            def respuesta    = serv.checkFingerPrint(fingerPrint)
            Dialogos.lanzarAlerta("Sistema de usuarios ináctivo")
            def respuesta = [ID:15, huella:"", nombre:"Octavio Ruiz", sucursales:["1":3]]
            //def respuesta = [ID:15,huella:"",nombre:"lo que quieras",sucursales:["1":4]]
            if(respuesta != 0) {
                respuesta.cerrojo= { llave -> return llave<=respuesta.sucursales[Principal.IDAlmacen as String] }
            } else {
                respuesta = [:]
                respuesta.cerrojo= { return false; }
            }
            serv.desconectar()
            respuesta
    }
    //Esta función sirve para dar un acceso especial a un usuario, por ejemplo para cancelaciones
    public static def autentifica(llave) {
        return identificaPersona().cerrojo(llave)
    }
    public static boolean cerrojo(llave) {
        return usuarioActivo.cerrojo(llave) as boolean
    }

    public static def leerHuella(){
        def escritorio   = omoikane.principal.Principal.escritorio.getFrameEscritorio()
        def fingerPrint  = new omoikane.formularios.WndLeerHuella(escritorio).getHuella()
        //fingerPrint      = Herramientas.bytes2HexString(fingerPrint)
        return fingerPrint
    }
        
}
