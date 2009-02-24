/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.uuuuuuuuuuuuuuuuuuuuuuuu
 */

package omoikane.principal

import omoikane.sistema.*
import omoikane.sistema.Usuarios as SisUsuarios
/**
 * ////////////////////////////////////////////////////////////////////////////////////////////
 * @author Octavio
 */
public class Principal {
        static def escritorio
        static def menuPrincipal
        static def config
        public static int     IDAlmacen
        public static int     IDCaja
        public static int     sysAncho 
        public static int     sysAlto  
        public static boolean fondoBlur

	static void main(String[] args)
        {
            iniciar()
        }
        static iniciar()
        {
            try {
            println "iniciando"
            def splash = new Splash()
            splash.iniciar()
            splash.setText("Cargando configuración...")
            config = new omoikane.sistema.Config()
            defineAtributos()
            splash.setText("Inicializando escritorio...")
            escritorio = new Escritorio()
            escritorio.iniciar()
            println "iniciando menús"
            splash.setText("Inicializando menú principal...")
            menuPrincipal = new MenuPrincipal()
            splash.detener()

           while(!SisUsuarios.login().cerrojo(SisUsuarios.CAJERO)) {}  // Aquí se detendrá el programa a esperar login
           escritorio.setNombreUsuario(SisUsuarios.usuarioActivo.nombre)
            menuPrincipal.iniciar()

            } catch(e) { Dialogos.lanzarDialogoError(null, "Error al iniciar aplicaciòn", Herramientas.getStackTraceString(e)) }
            ///////////////////////  
        }
        static def defineAtributos() {
            sysAncho = Integer.valueOf(config.resolucionPantalla.@ancho[0])
            sysAlto  = Integer.valueOf(config.resolucionPantalla.@alto[0])
            fondoBlur= Boolean.valueOf(config.fondoBlur[0].text())
            IDAlmacen= Integer.valueOf(config.idAlmacen[0].text())
            IDCaja   = Integer.valueOf(config.idCaja[0].text())
        }
}
