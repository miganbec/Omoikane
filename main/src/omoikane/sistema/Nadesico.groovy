
 /* Author Phesus        //////////////////////////////
 *  ORC,ACR             /////////////
 *                     /////////////
 *                    /////////////
 *                   /////////////
 * //////////////////////////////                   */

package omoikane.sistema

 import omoikane.nadesicoiLegacy.NadesicoLegacy

 class Nadesico {
    static def nadesicoLegacy = null;

    static def conectar() {
        if(nadesicoLegacy == null) { nadesicoLegacy = new NadesicoLegacy(); }
        return new Nadesico();
    }

    Object methodMissing(String name, Object args) {
            return nadesicoLegacy.invokeMethod("$name", args)
    }
    def desconectar() {

    }
    protected void finalize() throws Throwable {
        try {
            //desconectar()
        } finally {
            super.finalize()
        }
    }

}

