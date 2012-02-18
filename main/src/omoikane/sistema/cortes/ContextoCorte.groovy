/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.sistema.cortes
/**
 *
 * @author SYSTEM
 */
class ContextoCorte {
        static def TIPO_ESTANDAR = 1
        static def TIPO_DUAL     = 2

        public static def instanciar() {
            // Aquí debe determinar que estrategia se está utilizando
            def tipo = omoikane.principal.Principal.tipoCorte

            if(tipo==TIPO_ESTANDAR) {
                return (new EstrategiaEstandar())
            } else {
              throw new UnsupportedOperationException("No soportado desde SmartPos 2");
                //return (new EstrategiaDual())
            }

        }
}

