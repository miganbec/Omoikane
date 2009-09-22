/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.principal

import omoikane.sistema.*
import omoikane.sistema.Usuarios as SisUsuarios
import java.awt.event.*;
import omoikane.sistema.cortes.ContextoCorte;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////
 * 
 *  * @author Octavio
 */
public class Principal {
        static def escritorio
        static def menuPrincipal
        static def config
        public static int             IDAlmacen
        public static int             IDCaja
        public static int             sysAncho
        public static int             sysAlto
        public static int             CacheSTableAtras
        public static int             CacheSTableAdelante
        public static boolean         fondoBlur
        public static String          puertoImpresion
        public static boolean         impresoraActiva
        public static boolean         scannerActivo
        public static boolean         basculaActiva
        public static String          puertoBascula
        public static String          URLMySQL
        public static int             scannerBaudRate
        public static String          scannerPort
        public static ShutdownHandler shutdownHandler
        public static def             toFinalizeTracker = [:]
        public static def             scanMan
        public static def             tipoCorte         = ContextoCorte.TIPO_DUAL

	public static void main(args)
        {
            iniciar()
        }
        static iniciar()
        {
            try {
            println "iniciando"
            def splash = new Splash()
            splash.iniciar()

            shutdownHandler = new ShutdownHandler()
            Runtime.getRuntime().addShutdownHook(shutdownHandler);
            FingerUtil.inicializar()

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
            iniciarSesion()
            menuPrincipal.iniciar()

            if(scannerActivo){
                scanMan = new ScanMan()
                try {
                    println "comienza intento de conexión"
                    scanMan.connect(Principal.scannerPort, Principal.scannerBaudRate)
                    println "fin intento de conexión"
                } catch(Exception ex2) { Dialogos.error(ex2.getMessage(), ex2) }


                def robot = new java.awt.Robot()
                scanMan.setHandler {
                    println "iniciando handler antes de each"
                    it.each {
                        if((it as int)==13) { return null }
                        println "caracter:"+it
                        robot.keyPress(it as int);
                    }
                    println "después de each"

                    try {
                        println "supuesto enter"
                        robot.keyPress(KeyEvent.VK_ENTER)
                        robot.keyRelease(KeyEvent.VK_ENTER)
                        println "fin supuesto enter"
                    } catch(Exception exc) { Dialogos.error("Error capturar desde escáner de códigos de barras", exc) }

                }
                toFinalizeTracker.put("scanMan", "")

            }
           
            //
            //new SimpleForm() {
            //        it.form.visible = true
            //}
            /*
            def puerto = new PuertoNadesico()
            println puerto.dump()
            
            def objPrueba = puerto.ObjPrueba.newInstance()
            objPrueba.metodo1()
            objPrueba.metodo2("hola desde invocación rara")
            println "resultado metodo 3: "+objPrueba.metodo3()
            println "resultado de la propiedad1 : " +objPrueba.prop1
            println "resultado de la propiedad2 : " +objPrueba.prop2
            println puerto.Articulos.get(145594).descripcion
            //puerto.(new Articulos(descripcion:"último objeto8")).addToCodigos(new CodigosArticulo(codigo:"lalalauiiuiui")).save()
            def art = (puerto.Articulos.newInstance(descripcion:"otro más")).addToCodigos(puerto.CodigosArticulo.newInstance(codigo:"él código")).save()
            //art.save()
            */
            } catch(e) { Dialogos.lanzarDialogoError(null, "Error al iniciar aplicaciòn", Herramientas.getStackTraceString(e)) }
            ///////////////////////

        }
        static def defineAtributos() {
            sysAncho            = Integer.valueOf(config.resolucionPantalla.@ancho[0])
            sysAlto             = Integer.valueOf(config.resolucionPantalla.@alto[0])
            CacheSTableAtras    = Integer.valueOf(config.cacheSTable.@atras[0])
            CacheSTableAdelante = Integer.valueOf(config.cacheSTable.@adelante[0])
            fondoBlur           = Boolean.valueOf(config.fondoBlur[0].text())
            IDAlmacen           = Integer.valueOf(config.idAlmacen[0].text())
            IDCaja              = Integer.valueOf(config.idCaja[0].text())
            puertoImpresion     = String.valueOf(config.puertoImpresion[0].text())
            impresoraActiva     = Boolean.valueOf(config.impresoraActiva[0].text())
            puertoBascula       = String.valueOf(config.puertoBascula[0].text())
            URLMySQL            = String.valueOf(config.URLMySQL[0].text())
            scannerBaudRate     = Integer.valueOf(config.ScannerBaudRate[0].text())
            scannerPort         = String.valueOf(config.ScannerPort[0].text())
            scannerActivo       = Boolean.valueOf(config.scannerActivo[0].text())
            basculaActiva       = Boolean.valueOf(config.basculaActiva[0].text())
        }

    static def iniciarSesion(){
        try{
        while(!SisUsuarios.login().cerrojo(SisUsuarios.CAJERO)) {}  // Aquí se detendrá el programa a esperar login
        escritorio.setNombreUsuario(SisUsuarios.usuarioActivo.nombre)
        } catch(e) { Dialogos.lanzarDialogoError(null, "Error al iniciar secion ciclo de huella", Herramientas.getStackTraceString(e)) }
    }

    static def cerrarSesion(){
        try{
                SisUsuarios.logout()
                escritorio.setNombreUsuario("Sin Sesión")
                Principal.menuPrincipal = new MenuPrincipal()
                Thread.start(){
                Principal.iniciarSesion()
                Principal.menuPrincipal.iniciar()
                }
        } catch(e) { Dialogos.lanzarDialogoError(null, "Error al cerrar secion ciclo de huella", Herramientas.getStackTraceString(e)) }
    }
}
