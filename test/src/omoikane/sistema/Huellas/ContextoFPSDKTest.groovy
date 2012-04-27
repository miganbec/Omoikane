package omoikane.sistema.Huellas

import org.junit.Before
import org.junit.Test
import omoikane.principal.Principal
import omoikane.sistema.huellas.ContextoFPSDK.SDK
import omoikane.sistema.huellas.ContextoFPSDK
import omoikane.sistema.huellas.HuellasGriaule
import omoikane.sistema.huellas.HuellasOneTouchSDK

/**
 * Created by IntelliJ IDEA.
 * User: usuario1
 * Date: 26/04/12
 * Time: 05:56 PM
 * To change this template use File | Settings | File Templates.
 */
class ContextoFPSDKTest extends GroovyTestCase  {

    @Before
    public void setUp() {

    }

    @Test
    public void testInstanciar() {
        Principal.sdkFingerprint = SDK.GRIAULE;
        assert ContextoFPSDK.instanciar(null).class == HuellasGriaule.class;
        Principal.sdkFingerprint = SDK.ONETOUCH;
        assert ContextoFPSDK.instanciar(null).class == HuellasOneTouchSDK.class;
    }

}
