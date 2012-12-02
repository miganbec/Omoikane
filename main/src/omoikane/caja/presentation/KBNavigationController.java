package omoikane.caja.presentation;

import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KBNavigationController implements EventHandler<KeyEvent> {

        CajaController cc;

        public KBNavigationController(CajaController controller) {
            cc = controller;
        }

        @Override
        public void handle(KeyEvent event) {
            Object target = event.getTarget();
            if (isControl(target)) {
                Control control = (Control) target;
                enterKeyNavigationRules(event.getCode(), control);
            }
        }

        private boolean isControl(Object o) {
            return Control.class.isAssignableFrom(o.getClass());
        }

        private void enterKeyNavigationRules(KeyCode keyCode, Control control) {
            if (keyCode.equals(KeyCode.ENTER))
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
        }
    }