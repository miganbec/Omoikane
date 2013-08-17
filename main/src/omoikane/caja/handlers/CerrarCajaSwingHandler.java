package omoikane.caja.handlers;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import omoikane.caja.presentation.CajaController;
import omoikane.principal.Principal;
import omoikane.sistema.Usuarios;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 3/07/13
 * Time: 03:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class CerrarCajaSwingHandler extends ICajaEventHandler implements EventHandler<Event> {
    JInternalFrame frame;
    public CerrarCajaSwingHandler(CajaController cajaController, JInternalFrame frame) {
        super(cajaController);
        this.frame = frame;
    }

    @Override
    public void handle(Event event) {
        frame.setVisible(false);
        frame.dispose();
        getController().shutdownBasculaHandler();
        //Si es cajero, se cierra la sesión
        if( !Usuarios.cerrojo(Usuarios.CAPTURISTA) ) Principal.cerrarSesion();
    }
}
