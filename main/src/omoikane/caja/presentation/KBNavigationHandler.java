package omoikane.caja.presentation;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import omoikane.caja.handlers.AbrirCajon;
import omoikane.caja.handlers.CancelarProducto;
import omoikane.caja.handlers.CancelarVenta;
import omoikane.caja.handlers.MovimientosDeCaja;

public class KBNavigationHandler implements EventHandler<KeyEvent> {

        CajaController cc;

        public KBNavigationHandler(CajaController controller) {
            cc = controller;
        }

        @Override
        public void handle(KeyEvent event) {
            Object target = event.getTarget();
            if (isControl(target)) {
                Control control = (Control) target;
                enterKeyNavigationRules(event.getCode(), control, event);
            }
        }

        private boolean isControl(Object o) {
            return Control.class.isAssignableFrom(o.getClass());
        }

        private void enterKeyNavigationRules(KeyCode keyCode, Control control, Event event) {
            if (keyCode.equals(KeyCode.ENTER) && control.getId() != null)
                switch (control.getId()) {
                    case "capturaTextField":
                        String text = cc.getCapturaTextField().getText();
                        if (text == null || text.equals("")) {
                            cc.getBtnEfectivo().requestFocus();
                        }
                        break;
                    case "btnEfectivo":
                    case "btnCheque":
                    case "btnTarjeta":
                    case "btnVale":
                        cc.getEfectivoTextField().setFocusTraversable(false);
                        cc.getEfectivoTextField().requestFocus();

                        break;
                    case "efectivoTextField":
                        cc.getCambioTextField().requestFocus();
                        break;
                    case "cambioTextField":
                        cc.getBtnCobrar().requestFocus();
                        break;
                    case "btnCobrar":
                        cc.getBtnCobrar().fire();
                        break;
                }
            if (keyCode.equals(KeyCode.F3))
                cc.getCapturaTextField().requestFocus();
            if (keyCode.equals(KeyCode.F4))
                cc.ventaEspecialHandler.handle(event);
            if (keyCode.equals(KeyCode.F5))
                new MovimientosDeCaja(cc).handle(event);
            if (keyCode.equals(KeyCode.F6))
                new AbrirCajon(cc).handle(event);
            if (keyCode.equals(KeyCode.F7))
                new CancelarProducto(cc).handle(event);
            if (keyCode.equals(KeyCode.F12))
                new CancelarVenta(cc).handle(event);
        }
    }