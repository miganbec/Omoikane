
 /* Author Phesus        //////////////////////////////
 *  ORC,ACR             /////////////
 *                     /////////////
 *                    /////////////
 *                   /////////////
 * //////////////////////////////                   */

package omoikane.sistema

class PuertoNadesico {
	def user
	def pass
	def IDConn
	def conn

	static def uno = 1
	def PuertoNadesico() {
        def nad= new Nadesico()
		conn   = nad
		IDConn = conn.conectar()
	}
	Object methodMissing(String name, Object args) {
		//println "Invocando: ${name}, con argumentos: ${args}"
	}
	def propertyMissing(name) {
		def valProp  = conn.invocaPropiedadOClase(IDConn, "", name)
		def dummy    = new Dummy(conn:conn, IDObj:valProp.val, IDConn:IDConn)
		def resultado= (valProp.raw) ? valProp.val : dummy
		return resultado
	}
        static def workIn(Closure clos) {
                def puerto = new PuertoNadesico()
                clos(puerto)
                puerto.desconectar()
                puerto = null
        }
}
class Dummy {
	def conn
	def IDObj
	def IDConn
	String toString() { "[IDObj:${IDObj}, Conn:${conn}]" }

	Object methodMissing(String name, args) {
                
                if(args.size() > 0) {
                    for(i in 0..args.size()-1) {
                        if(args[i] instanceof Dummy) {
                            args[i] = [puertoNadesicoDummy:true, IDObj:args[i].IDObj]
                        }
                    }
                }

                def IDNewObj = conn.invocaMetodo(IDConn, IDObj, name, args)
		def dummy    = new Dummy(conn:conn, IDObj:IDNewObj.val, IDConn:IDConn)
		def resultado= (IDNewObj.raw)?IDNewObj.val : dummy
		return resultado
	}
	def propertyMissing(name) {
		def valProp  = conn.invocaPropiedadOClase(IDConn, IDObj, name)
		def dummy    = new Dummy(conn:conn, IDObj:valProp.val, IDConn:IDConn)
		def resultado= (valProp.raw) ? valProp.val : dummy
		return resultado
	}

}