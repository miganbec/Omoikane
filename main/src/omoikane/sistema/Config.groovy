
 /* Author Phesus        //////////////////////////////
 *  ORC,ACR             /////////////
 *                     /////////////
 *                    /////////////
 *                   /////////////
 * //////////////////////////////                   */

package omoikane.sistema


import groovy.inspect.swingui.*
 import omoikane.principal.Principal
 import omoikane.sistema.huellas.ContextoFPSDK

 class Config {
    def    prefs
    Config config
    
    Config() {
       cargar()
       if(config == null ) {
           config = this
           defineAtributos()
       }
       config = this
    }

    def cargar () {
        //def xmlTxt = getClass().getResourceAsStream("/omoikane/principal/config.xml")
        def xmlTxt = new File("config.xml")
        def xml    = new groovy.util.XmlParser().parseText(xmlTxt.text)
        prefs = xml
    }

    Object propertyMissing(String name, Object args) {
        if(prefs."$name".size() > 0) {
            return prefs."$name"
        } else {
            throw new Exception("Falta parámetro $name en la configuración!") }
    }

    def defineAtributos() {
            Principal.sysAncho                = Integer.valueOf(config.resolucionPantalla.@ancho[0])
            Principal.sysAlto                 = Integer.valueOf(config.resolucionPantalla.@alto[0])
            Principal.CacheSTableAtras        = Integer.valueOf(config.cacheSTable.@atras[0])
            Principal.CacheSTableAdelante     = Integer.valueOf(config.cacheSTable.@adelante[0])
            Principal.fondoBlur               = Boolean.valueOf(config.fondoBlur[0].text())
            Principal.IDAlmacen               = Integer.valueOf(config.idAlmacen[0].text())
            Principal.IDCaja                  = Integer.valueOf(config.idCaja[0].text())
            Principal.puertoImpresion         = String .valueOf(config.puertoImpresion[0].text())
            Principal.impresoraActiva         = Boolean.valueOf(config.impresoraActiva[0].text())
            Principal.URLMySQL                = String .valueOf(config.URLMySQL[0].text())
            Principal.loginJasper             = String .valueOf(config.loginJasper[0].text())
            Principal.passJasper              = String .valueOf(config.passJasper[0].text())
            Principal.scannerBaudRate         = Integer.valueOf(config.ScannerBaudRate[0].text())
            Principal.scannerPort             = String .valueOf(config.ScannerPort[0].text())
            Principal.scannerActivo           = Boolean.valueOf(config.scannerActivo[0].text())
            Principal.sdkFingerprint          = ContextoFPSDK.sdkValueOf(String.valueOf(config.fingerPrintSDK[0].text()))
            Principal.basculaActiva           = Boolean.valueOf(config.bascula.@activa[0])
            Principal.tipoCorte               = Integer.valueOf(config.tipoCorte[0].text())
            if(Principal.basculaActiva) {
                String cmd = ""
                String.valueOf(config.bascula.@weightCommand[0]).split(",").each { cmd += (it as Integer) as char }
                Principal.driverBascula       = [
                        port: String.valueOf(config.bascula.@port[0]),
                        baud: Integer.valueOf(config.bascula.@baud[0]),
                        bits: String.valueOf(config.bascula.@bits[0]),
                        stopBits: String.valueOf(config.bascula.@stopBits[0]),
                        parity:   String.valueOf(config.bascula.@parity[0]),
                        stopChar: String.valueOf(config.bascula.@stopChar[0]),
                        weightCommand: cmd
                ];
            }
        }
}