/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.sistema


import groovy.inspect.swingui.*
/**
 *
 * @author Usuario
 */
class Config {
    def prefs
    
    Config() { cargar() }

    def cargar () {
        def xmlTxt = getClass().getResourceAsStream("/omoikane/principal/config.xml")
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