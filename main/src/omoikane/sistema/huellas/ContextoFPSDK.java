package omoikane.sistema.huellas;

import omoikane.principal.Principal;
import omoikane.sistema.JInternalDialog2;

/**
 * Created by IntelliJ IDEA.
 * User: usuario1
 * Date: 26/04/12
 * Time: 03:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContextoFPSDK {
    public static enum SDK {
        GRIAULE,
        ONETOUCH
    }

    public static MiniLeerHuella instanciar(JInternalDialog2 parent) throws Exception {

        SDK sdk = Principal.sdkFingerprint;

        if(sdk == SDK.ONETOUCH) {
            return new HuellasOneTouchSDK(parent);
        } else if (sdk == SDK.GRIAULE) {
            //return new HuellasGriaule(parent);
            throw new Exception("Soporte para griaule descontinuado");
        }
        throw new Exception("Error en la configuración, valor del nodo SDKFingerPrint incorrecto");
    }

    public static SDK sdkValueOf(String sdk) throws Exception {
        try {
            return SDK.valueOf(sdk);
        } catch(IllegalArgumentException exc) {
            throw new Exception("Error en la configuración, valor del nodo SDKFingerPrint incorrecto");
        }
    }
}
