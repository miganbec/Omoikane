
 /* Author Phesus        //////////////////////////////
 *  ORC,ACR             /////////////
 *                     /////////////
 *                    /////////////
 *                   /////////////
 * //////////////////////////////                   */

 package omoikane.sistema.huellas;

 import com.digitalpersona.onetouch.*;
 import com.digitalpersona.onetouch.capture.DPFPCapture;
 import com.digitalpersona.onetouch.capture.event.*;
 import com.digitalpersona.onetouch.processing.DPFPFeatureExtraction;
 import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
 import omoikane.entities.Usuario;
 import omoikane.sistema.Dialogos;
 import omoikane.sistema.JInternalDialog2;
 import org.apache.log4j.Logger;

 import javax.swing.*;

 /**
 *
 * @author Octavio
 */


public class HuellasOneTouchSDK extends MiniLeerHuella implements DPFPReaderStatusListener
{
    public String IDLector;

    Object focoCerrar = new Object();
    private DPFPCapture capturer = DPFPGlobal.getCaptureFactory().createCapture();
    public static Logger logger = Logger.getLogger(HuellasOneTouchSDK.class);
    private boolean aceptable = false;

    @Override
    public void readerConnected(DPFPReaderStatusEvent dpfpReaderStatusEvent) {
        super.setLectorActivo(true);
    }

    @Override
    public void readerDisconnected(DPFPReaderStatusEvent dpfpReaderStatusEvent) {
        super.setLectorActivo(false);
    }

    /** Creates a new instance of HuellasGriaule */



    public HuellasOneTouchSDK(JInternalDialog2 parent) {
        super(parent);

        try {

            init();
            startCapture();
            logger.info ("Lector Huella DigitalPersona inicializado");

        } catch(Exception GrEx)
        {
            Dialogos.error("Error al inicializar SDK lector", GrEx);
        }
        HiloParaCerrar HPC = new HiloParaCerrar(this);
        HPC.start();

    }

    @Override
    public Usuario identify() throws DPFPImageQualityException {

        IUserIdentifier validador     = new UserIdentifierOneTouch();
        DPFPSampleFactory sampleFactory = DPFPGlobal.getSampleFactory();
        DPFPSample        sample        = sampleFactory.createSample(this.byteSample);

        return validador.identify(sample);
    }

    public void cerrar()
    {
        synchronized(focoCerrar)
        {
            try {
                focoCerrar.wait();
            } catch(InterruptedException ie) {
                Dialogos.error("Error al cerrar capturador de huella", ie);
            }
            stopCapture();
            parent.setActivo(false);
        }
    }

    protected void init()
	{
		capturer.addDataListener(new DPFPDataAdapter() {
			@Override public void dataAcquired(final DPFPDataEvent e) {
				SwingUtilities.invokeLater(new Runnable() {	public void run() {
					process(e.getSample());
				}});
			}
		});
		capturer.addReaderStatusListener(this);

		capturer.addSensorListener(new DPFPSensorAdapter() {
			@Override public void fingerTouched(final DPFPSensorEvent e) {

			}
			@Override public void fingerGone(final DPFPSensorEvent e) {
                synchronized(focoCerrar) {
                    focoCerrar.notifyAll();
                }
			}
		});
		capturer.addImageQualityListener(new DPFPImageQualityAdapter() {
			@Override public void onImageQuality(final DPFPImageQualityEvent e) {
                logger.info("quality listener");
				SwingUtilities.invokeLater(new Runnable() {	public void run() {
					if (e.getFeedback().equals(DPFPCaptureFeedback.CAPTURE_FEEDBACK_GOOD)) {
                        aceptable = true;
                    } else {
                        aceptable = false;
                    }
				}});
                logger.info("¿Calidad aceptable de la huella? "+aceptable);
			}
		});
	}

	protected void process(DPFPSample sample)
	{
        DPFPFeatureSet enrollFeatures = extractFeatures(sample, DPFPDataPurpose.DATA_PURPOSE_ENROLLMENT);
        enrollFeatureSet = enrollFeatures.serialize();

        DPFPFeatureSet verifyFeatures = extractFeatures(sample, DPFPDataPurpose.DATA_PURPOSE_VERIFICATION);
        verifyFeatureSet = verifyFeatures.serialize();

        byteSample = sample.serialize();
    }

    private DPFPFeatureSet extractFeatures(DPFPSample sample, DPFPDataPurpose purpose) {
        DPFPFeatureExtraction extractor = DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
        try {
            return extractor.createFeatureSet(sample, purpose);
        } catch (DPFPImageQualityException e) {
            logger.error("Error de calidad de la huella extraída", e);
            return null;
        }
    }

	protected void startCapture()
	{
		capturer.startCapture();
	}

	protected void stopCapture()
	{
		capturer.stopCapture();
	}

}
