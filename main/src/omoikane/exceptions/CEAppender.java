/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.exceptions;

import omoikane.principal.Principal;
import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;

import javax.swing.*;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 *
 * @author mora
 */
public class CEAppender extends AppenderSkeleton {

    @Override
    public void close() {
        //Nothing
    }

    @Override
    public boolean requiresLayout() {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void append(LoggingEvent event) {
        if ( event.getLevel().isGreaterOrEqual(Priority.WARN) ) {
            errorWindow(event);
        } else if(event.getLevel().isGreaterOrEqual(Priority.INFO)) {
            JOptionPane.showMessageDialog(null,
                    event.getMessage(),
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        } else if(Principal.DEBUG) {
            JOptionPane.showMessageDialog(null,
                    event.getMessage(),
                    "Información de depuración",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private void errorWindow(LoggingEvent event) {
        ExceptionWindow ew = new ExceptionWindow();
        ew.getLblTituloError().setText((String) event.getMessage());

        if(event.getThrowableInformation() != null) {
            event.getThrowableInformation().getThrowable().printStackTrace();
            ew.getTxtExcepcion().setText(Misc.getStackTraceString(event.getThrowableInformation().getThrowable()));
        }
        ew.setVisible(true);
    }

}
