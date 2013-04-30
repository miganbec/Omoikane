
package omoikane.nadesicoiLegacy;

/**
 * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 *
 * @author Usuario
 */
public class NadesicoLegacy {

    def storage = [:]
    def propertyMissing(String name, value) { storage[name] = value }
    def propertyMissing(String name) { storage[name] }

    Object methodMissing(String name, Object args) {
            return this."${name}".call( * args)
    }
    /**
     * @param args the command line arguments
     */
    public NadesicoLegacy() {
        try {
              def driver   = "com.mysql.jdbc.Driver";
              Class.forName(driver).newInstance();

              ArticulosFunciones.asignarA       this
              LineasFunciones.asignarA          this
              LineasDualesFunciones.asignarA    this
              Grupos.asignarA                   this
              Db.asignarA                       this
              Caja.asignarA                     this
              Almacenes.asignarA                this
              Ventas.asignarA                   this
              Facturas.asignarA                 this
              Usuarios.asignarA                 this
              ClientesFunciones.asignarA        this
              Cortes.asignarA                   this
              CorteDual.asignarA                this
              Sucursales.asignarA               this
              PuertoNadesico.asignarA           this

        } catch(e) { Consola.error("Error al iniciar servicio", e) }
    }

}
