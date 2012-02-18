 /* Author Phesus        //////////////////////////////
 *  ORC,ACR             /////////////
 *                     /////////////
 *                    /////////////
 *                   /////////////
 * //////////////////////////////                   */
 //iiiiiiiiiiiiiiiiiiiiiiiiiiiiii
 //iiiiiiiiiiiiiiiiiiiiiiiiiiiiii
 //iiiiiiiiiiiiiiiiiiiiiiiiiiiiii
 //iiiiiiiiiiiiiiiiiiiiiiiiiiiiii

package omoikane.sistema;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.regex.*;
import java.util.Enumeration;
import com.griaule.grfingerjava.*;
import groovy.sql.*;
import omoikane.principal.*
 import java.util.logging.Logger
 import java.util.logging.Level
 import org.springframework.beans.factory.annotation.Autowired
 import omoikane.repository.UsuarioRepo;

public class Usuarios {
    //public Usuario usrActivo = new Usuario();
    private static boolean autorizado = false;
    public static def usuarioActivo = null
    public static def CAJERO        = 0
    public static def CAPTURISTA    = 0.5
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
            def respuesta
            def fingerPrint

            if(Principal.ASEGURADO) {
              fingerPrint  = new omoikane.formularios.WndLeerHuella(escritorio).getHuella()
            }

            def userSystem   = new Usuarios()

            if(Principal.ASEGURADO) {
                respuesta    = userSystem.checkFingerPrint(fingerPrint)
            } else {
                respuesta = [ID:20,huella:"",nombre:"Pruebas",sucursales:["1":4]]
            }
            if(respuesta != 0) {
                respuesta.cerrojo= { llave ->
                  return llave<=respuesta.sucursales[Principal.IDAlmacen as String]
                }
            } else {
                respuesta = [:]
                respuesta.cerrojo= { return false; }
            }

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
        return fingerPrint
    }

    @Autowired
    UsuarioRepo usuarioRepo;
    def checkFingerPrint(fingerP ) {
        try {
            Template        ref1, ref2;
            ref1            = new Template();
            ref2            = new Template();
            MatchingContext checador = null;
            def autorizado  = false, respuesta = null

            try {
                checador = new MatchingContext();
                ref1.setData(fingerP);
            } catch(grje) {
                Logger.getLogger(Usuarios.class.getName()).log(Level.SEVERE, "Error al convertir hex a bytes", grje);
                throw grje
            }

            def data = [:], usr_suc, mC = null

            try {
                    def usuarios = usuarioRepo.findAll();

                    try {
                        usuarios.each {
                            data['ID']       = it.id;
                            data['nombre']   = it.nombre;

                            mC = new MatchingContext()
                            ref2.setData(it.huella1);
                            if(ref2!=null)  {autorizado = (mC.verify(ref1, ref2));
                            if(!autorizado) {ref2.setData(it.huella2);  autorizado = (mC.verify(ref1, ref2)); }
                            if(!autorizado) {ref2.setData(it.huella3);  autorizado = (mC.verify(ref1, ref2)); }
                            }
                            if(mC != null)
                            {
                                mC.destroy();
                            }
                            if(autorizado) {
                                autorizado = data;
                                throw new Exception("BREAK")
                            }
                        }
                    } catch(ex)  { if(ex.message != "BREAK") { throw ex } }

                    respuesta = (autorizado)?data:null
                    return respuesta

            } catch(exc) {
                Logger.getLogger(Usuarios.getName()).log(Level.SEVERE, "Error al autenticar: ${exc.message}", exc);
                throw exc
            }
        } catch(e) {
          Logger.getLogger(Usuarios.getName()).log(Level.SEVERE, "Error al identificar usuario", e);
          throw e
        }
    }
}