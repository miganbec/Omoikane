package omoikane.sistema.huellas;

import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
import omoikane.entities.Usuario;

/**
 * Created by IntelliJ IDEA.
 * User: usuario1
 * Date: 7/07/12
 * Time: 01:47 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IUserIdentifier {
    public Usuario identify(DPFPSample muestra) throws DPFPImageQualityException;
}
