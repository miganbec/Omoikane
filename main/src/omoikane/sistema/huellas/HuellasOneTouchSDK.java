
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
 import com.digitalpersona.onetouch.processing.DPFPEnrollment;
 import com.digitalpersona.onetouch.processing.DPFPFeatureExtraction;
 import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
 import com.griaule.grfingerjava.*;
 import omoikane.sistema.Dialogos;
 import omoikane.sistema.JInternalDialog2;
 import org.apache.log4j.Logger;

 import javax.swing.*;
 import java.awt.*;
 import java.awt.image.BufferedImage;
 import java.io.File;

 /**
 *
 * @author Octavio
 */


public class HuellasOneTouchSDK extends MiniLeerHuella implements DPFPReaderStatusListener
{
    public Template template;
    public MatchingContext matchContext;
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
            System.out.println(System.getProperty("user.dir"));
            String grFingerNativeDirectory = (new File(".")).getAbsolutePath();

            File directory = new File(grFingerNativeDirectory);

            GrFingerJava.setNativeLibrariesDirectory(directory);
            GrFingerJava.setLicenseDirectory(directory);
            //com.griaule.grfingerjava.GrFingerJava.setNativeLibrariesDirectory();
            System.out.println("Carpeta dependencias griaule: "+grFingerNativeDirectory);

            matchContext = new MatchingContext();
            init();
            startCapture();
            System.out.println ("Lector Huella DigitalPersona inicializado");

        } catch(Exception GrEx)
        {
            System.out.println ("Error griaule: "+GrEx.getMessage());
            Dialogos.error("Error al inicializar SDK lector", GrEx);
        }
        HiloParaCerrar HPC = new HiloParaCerrar(this);
        HPC.start();

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
            try
            {
                stopCapture();

                if(matchContext != null)
                {
                    matchContext.destroy();
                }

                parent.setActivo(false);
            }
            catch(GrFingerJavaException gr)
            {
                Dialogos.error("Error al finalizar lector de huella.", gr);
            }
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
			}
		});
	}

	protected void process(DPFPSample sample)
	{
        try {
            Image            image          = convertSampleToBitmap(sample);
            BufferedImage    bufferedImage  = toBufferedImage(image);

            FingerprintImage fi             = new FingerprintImage(bufferedImage, 512);

            Template griauleTemplate = matchContext.extract(fi);
            BufferedImage buf2 = com.griaule.grfingerjava.GrFingerJava.getBiometricImage(griauleTemplate, fi);

            this.byteTemplate = griauleTemplate.getData();
        } catch (GrFingerJavaException e) {
            logger.error("Error procesando huella durante la traducción onetouch -> griaule", e);
        }
    }

    private DPFPFeatureSet extractFeatures(DPFPSample sample, DPFPDataPurpose purpose) {
        DPFPFeatureExtraction extractor = DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
        try {
            return extractor.createFeatureSet(sample, purpose);
        } catch (DPFPImageQualityException e) {
            return null;
        }
    }

    protected Image convertSampleToBitmap(DPFPSample sample) {
        Image huella = DPFPGlobal.getSampleConversionFactory().createImage(sample);

        return huella;
	}

	protected void startCapture()
	{
		capturer.startCapture();
	}

	protected void stopCapture()
	{
		capturer.stopCapture();
	}

    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see Determining If an Image Has Transparent Pixels
        boolean hasAlpha = false;

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_BYTE_GRAY;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }
}
