package omoikane.sistema.huellas;

import com.digitalpersona.onetouch.*;
import com.digitalpersona.onetouch.processing.DPFPFeatureExtraction;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;
import omoikane.entities.Usuario;
import omoikane.principal.Principal;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: usuario1
 * Date: 7/07/12
 * Time: 02:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserIdentifierOneTouch implements IUserIdentifier {

    public static Logger logger = Logger.getLogger(UserIdentifierOneTouch.class);

    @Override
    public Usuario identify(DPFPSample muestra) throws DPFPImageQualityException {
        DPFPFeatureExtraction featureExtractor = DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
        DPFPFeatureSet featureSet = featureExtractor.createFeatureSet(muestra, DPFPDataPurpose.DATA_PURPOSE_VERIFICATION);

        DPFPVerification matcher = DPFPGlobal.getVerificationFactory().createVerification();
        matcher.setFARRequested(DPFPVerification.MEDIUM_SECURITY_FAR);

        HuellasCache huellasData = Principal.applicationContext.getBean(HuellasCache.class);

        for (Usuario usuario : huellasData.getHuellasBD()) {
            TemplateMap templateMap = TemplateMap.deserializar(usuario.getHuella1());

            if (templateMap != null) {
                for(Template template : templateMap.values()) {
                    DPFPVerificationResult result = matcher.verify(featureSet, template.getTemplate());
                    if (result.isVerified()) {
                        logger.debug("FingerPrint FAR achieved: %g."+
                                (double) result.getFalseAcceptRate() / DPFPVerification.PROBABILITY_ONE);
                        return usuario;
                    }
                }
            }
        }
        return null;
    }
}
