/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.nadesicoiLegacy

/**
 *
 * @author SYSTEM
 */
class PuertoNadesico {

    static def sesiones = [:]
    static def nodos    = [:]

    static def conectar = { user = "public", pass = "" ->
        try {
            def UID = nadesicoi.Util.generarUID()
            sesiones[UID] = [user:user, pass:pass]
            nodos[UID]    = [:]
            return UID
        } catch(e) {
            Consola.error("Error al iniciar sesión en portal nadesico", e)
            throw e
        }
    }
    static def desconectar = { IDConn, user = "public", pass = "" ->
        try {
            nodos.remove(IDConn)
        } catch(e) {
            Consola.error("Error al cerrar sesión en portal nadesico", e)
            throw e
        }
    }
    static synchronized def addNodo(IDSesion, objeto) {
        def UID  = Util.generarUID()
        def nodo = new Nodo(IDSesion:IDSesion, IDObjeto:UID, objeto:objeto)
        nodos[IDSesion][UID] = nodo
        //println "Nodo:"+nodo
        return UID
    }
    static synchronized def compatibleRPC(obj) {
        if(obj instanceof String) {
            return true
        } else if(obj instanceof Double) {
            return true
        } else if(obj instanceof Integer) {
            return true
        } else if(obj instanceof Map) {
            return true
        } else if(obj instanceof List) {
            return true
        } else {
            return false
        }
    }
    static def persistentSet2List(elSet) {
        def lista = [], fila

        elSet.eachWithIndex() { obj, i ->
            fila = [:]
            fila = obj.asMap()
            lista << fila
        }
        lista
    }
    static def invocaPropiedadOClase = { IDSesion, IDObjeto, name ->
        if(!sesiones.containsKey(IDSesion)) { throw new Exception("Permiso denegado para invocar propiedad o clase en puerto nadesico") }
        try {
            //if(Class.forName("nadesicoi."+name) instanceof Class) {  }
            if(IDObjeto == "" || IDObjeto == null) {
                IDObjeto = "nadesicoi"
            } else {
                IDObjeto = "nadesicoi.PuertoNadesico.nodos['${IDSesion}']['${IDObjeto}'].objeto"
            }
            //println "IDObjeto: "+IDObjeto
            def generatedCode = "class X { def getIt = { "+IDObjeto+"."+name+"} }"
            InputStream inp   = new ByteArrayInputStream(generatedCode.getBytes());
            GroovyObject obj  = (GroovyObject)new GroovyClassLoader().parseClass(inp, "lala").newInstance();
            def resultado     = obj.getIt()

            if(resultado.class.toString() == "class org.hibernate.collection.PersistentSet") {
                resultado = PuertoNadesico.persistentSet2List(resultado);
            }
            if(compatibleRPC(resultado)) {
                return [raw:true, val:resultado]
            } else {
                return [raw:false, val:addNodo(IDSesion, resultado)]
            }

        } catch(e) {    
            Consola.error("Error al invocar función 'invocaPropiedadOClase'", e)
            throw e
        }
    }
    static def procesarArgs = { IDSesion, args ->
            args = (args=="")?null:args.toArray()
            if(args!=null) {
                for(i in 0..args.size()-1) {
                    if(args[i] instanceof Map) {
                        if(args[i]['puertoNadesicoDummy']) {
                            args[i] = nodos[IDSesion][args[i]['IDObj']].objeto
                        }
                    }
                }
            }
            args
    }
    static def invocaMetodo = { IDSesion, IDObj, methodName, args ->
        if(!sesiones.containsKey(IDSesion)) { throw new Exception("Permiso denegado para invocar métodos en puerto nadesico") }
        try {
            def obj
            args = PuertoNadesico.procesarArgs(IDSesion, args)
            obj  = nodos[IDSesion][IDObj].objeto.invokeMethod(methodName, args)
            if(compatibleRPC(obj)) {
                return [raw:true, val:obj]
            } else {
                return [raw:false, val:addNodo(IDSesion, obj)]
            }

        } catch(e) {
            Consola.error("Error al invocar función 'invocaMetodo'", e)
            throw e
        }
    }
    static def asignarA(serv) {
        serv.conectar              = conectar
        serv.invocaPropiedadOClase = invocaPropiedadOClase
        serv.invocaMetodo          = invocaMetodo
    }
}

class Nodo {
    def objeto
    def IDSesion
    def IDObjeto
    String toString() {
        "[ID Conexión: $IDSesion, IDObjeto: $IDObjeto, Objeto: ${objeto}]"
    }
}

public class ObjPrueba {
    def prop1 = "lala"
    def prop2 = 44
    def metodo1() {
        println "aquí método 1"
    }
    def metodo2(msj) {
        println "mensaje: "+msj
    }
    def metodo3() {
        return "se retorna string"
    }
    def static staticMetodo1() {
        println "aquí método estático 1"
    }
}