package omoikane.caja.handlers;

import javafx.event.Event;
import omoikane.caja.presentation.CajaController;
import omoikane.caja.presentation.CajaModel;
import omoikane.sistema.ComMan;

import java.util.HashMap;

/**
* Created with IntelliJ IDEA.
* User: Octavio
* Date: 17/08/13
* Time: 01:10 PM
* To change this template use File | Settings | File Templates.
*/
public class BasculaHandler extends ICajaEventHandler {

    boolean basculaActiva = omoikane.principal.Principal.basculaActiva;
    HashMap miniDriver    = omoikane.principal.Principal.driverBascula;
    ComMan comMan;
    boolean pesando       = false;


    public BasculaHandler(CajaController cajaController, CajaModel cajaModel) {
        super(cajaController);

        if(basculaActiva) { comMan = new ComMan((String) miniDriver.get("port")); }
    }

    public void pesar() {
        if(basculaActiva && !pesando) {
            try {
                pesando = true;
                String pesoString = (String) comMan.readWeight(miniDriver.get("weightCommand"), miniDriver);
                Double peso = (Double) Double.parseDouble(pesoString);
                cajaController.getModel().getCaptura().set( peso + "*" );
                cajaController.getCapturaTextField().end();
            } catch(ExceptionInInitializerError exPeso) {
                throw exPeso;
            } finally {
                pesando = false;
            }
        }
    }

    public void close() {
        if(comMan != null) comMan.close();
    }

    @Override
    public void handle(Event event) {
        pesar();
    }
}
