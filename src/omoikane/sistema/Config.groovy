
 /* Author Phesus        //////////////////////////////
 *  ORC,ACR             /////////////
 *                     /////////////
 *                    /////////////
 *                   /////////////
 * //////////////////////////////                   */

package omoikane.sistema


import groovy.inspect.swingui.*

class Config {
    def prefs
    
    Config() { cargar() }

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
}