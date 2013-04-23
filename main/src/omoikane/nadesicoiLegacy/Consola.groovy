/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.nadesicoiLegacy

/**
 *
 * @author Usuario
 */
class Consola {

    static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Consola.class);

    static def suceso(msg, detalles) { logger.error("Consola.suceso(...): "+msg, detalles); }

    static def aviso(msg, detalles="") {
        if(detalles.equals("")) {
            logger.info("Consola.aviso(...): "+msg)
        } else {
            error("Consola.aviso(...): "+msg, detalles)
        }
    }

    static def error(msg, detalles) {
        logger.error("(Error)"+msg, detalles)
    }

}

