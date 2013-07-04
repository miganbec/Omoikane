/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.exceptions;

import org.apache.log4j.Logger;

/**
 *
 * @author mora
 */
public class UEHandler implements Thread.UncaughtExceptionHandler {
    public Logger logger                  = Logger.getLogger(UEHandler.class);
    @Override
    public void uncaughtException(Thread th, Throwable ex) {
        logger.error(ex.getMessage(), ex);
    }
}
