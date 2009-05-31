/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.principal

import omoikane.sistema.*
import omoikane.sistema.Usuarios as SisUsuarios
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
        public static boolean         fondoBlur
        public static String          puertoImpresion
        public static boolean         impresoraActiva
        public static int             cajon
        public static ShutdownHandler shutdownHandler
        public static def             toFinalizeTracker = [:]

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
            sysAncho        = Integer.valueOf(config.resolucionPantalla.@ancho[0])
            sysAlto         = Integer.valueOf(config.resolucionPantalla.@alto[0])
            fondoBlur       = Boolean.valueOf(config.fondoBlur[0].text())
            IDAlmacen       = Integer.valueOf(config.idAlmacen[0].text())
            IDCaja          = Integer.valueOf(config.idCaja[0].text())
            puertoImpresion = String.valueOf(config.puertoImpresion[0].text())
            impresoraActiva = Boolean.valueOf(config.impresoraActiva[0].text())
            cajon           = Integer.valueOf(config.cajon[0].text())
        }

    static def iniciarSesion(){
        while(!SisUsuarios.login().cerrojo(SisUsuarios.CAJERO)) {}  // Aquí se detendrá el programa a esperar login
        escritorio.setNombreUsuario(SisUsuarios.usuarioActivo.nombre)
    }

    static def cerrarSesion(){
                SisUsuarios.logout()
                escritorio.setNombreUsuario("Sin Sesión")
                Principal.menuPrincipal = new MenuPrincipal()
                Thread.start(){
                Principal.iniciarSesion()
                Principal.menuPrincipal.iniciar()
                }
    }
}
