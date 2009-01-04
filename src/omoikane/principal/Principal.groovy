/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.principal

import omoikane.sistema.*
/**
 * ////////////////////////////////
 * @author Octavio
 */
public class Principal {
        static def escritorio
        static def menuPrincipal
        static def config
        public static int IDAlmacen = 1

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
            splash.setText("Inicializando escritorio...")
            escritorio = new Escritorio()
            escritorio.iniciar()
            println "iniciando menús"
            splash.setText("Inicializando menú principal...")
            menuPrincipal = new MenuPrincipal()
            menuPrincipal.iniciar()
            splash.detener()
            } catch(e) { Dialogos.lanzarDialogoError(null, "Error al iniciar aplicaciòn", Herramientas.getStackTraceString(e)) }
            ///////////////////////
        }
}
