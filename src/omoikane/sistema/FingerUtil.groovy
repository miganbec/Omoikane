
 /* Author Phesus        //////////////////////////////
 *  ORC,ACR             /////////////
 *                     /////////////
 *                    /////////////
 *                   /////////////
 * //////////////////////////////                   */

package omoikane.sistema;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import com.griaule.grfingerjava.FingerprintImage;
import com.griaule.grfingerjava.GrFingerJava;
import com.griaule.grfingerjava.GrFingerJavaException;
import com.griaule.grfingerjava.IFingerEventListener;
import com.griaule.grfingerjava.IImageEventListener;
import com.griaule.grfingerjava.IStatusEventListener;
import com.griaule.grfingerjava.MatchingContext;
import com.griaule.grfingerjava.Template;


/**
* Class responsible for handling Fingerprint SDK.
*
* It handles fingerprint loading/capturing, template extraction,
* fingerprint image saving and storing/retrieving from template base.
*/
public class FingerUtil implements IStatusEventListener, IImageEventListener, IFingerEventListener {

   /** Fingerprint SDK context used for capture / extraction / matching of fingerprints. */
   private MatchingContext fingerprintSDK;
   /** User interface, where logs, images and other things will be sent. */

   /** Sets if template must be automatically extracted after capture. */
   private boolean autoExtract = true;
   /** Sets if template must be automatically identified after capture.
    It's only effective when *autoExtract == true) */
   private boolean autoIdentify = false;

   public def onPlugAction      = { throw new Exception("Sin acción asignada a onPlugAction")      }
   public def onUnplugAction    = { throw new Exception("Sin acción asignada a onUnplugAction")    }
   public def onFingerInAction  = { throw new Exception("Sin acción asignada a onFingerInAction")  }
   public def onFingerOutAction = { throw new Exception("Sin acción asignada a onFingerOutAction") }
   public def onFingerCatch     = { template -> throw new Exception("Sin acción asignada a onFingerCatch") }
   /** The last fingerprint image acquired. */
   private FingerprintImage fingerprint;
   /** The template extracted from the last acquired image. */
   private Template template;


   /**
    * Creates a new Util to be used by the specified Main Form.
    *
    * Initializes fingerprint capture and database connection.
    */
   public FingerUtil() {


   }
   public iniciar() { initFingerprintSDK(); }

   /**
    * Stops fingerprint capture and closes the database connection.
    */
   public void destroy() {
       destroyFingerprintSDK();
   }

   /**
    * Initializes Fingerprint SDK and enables fingerprint capture.
    */
   private void initFingerprintSDK() {
       try {
           fingerprintSDK = new MatchingContext();
           //Starts fingerprint capture.
           GrFingerJava.initializeCapture(this);

           //println ("Soporte de lector de huellas dactilares activo");

       } catch (Exception e) {
           //If any error ocurred while initializing GrFinger,
           //writes the error to log
           println(e.getMessage());
       }
   }

   /**
    * Stops fingerprint capture.
    */
   private void destroyFingerprintSDK() {
       try {
           GrFingerJava.finalizeCapture();
       } catch (GrFingerJavaException e) {
           e.printStackTrace();
       }
   }


   /**
    * This function is called every time a fingerprint reader is plugged.
    *
    * @see griaule.grFinger.StatusCallBack#onPlug(java.lang.String)
    */
   public void onSensorPlug(String idSensor) {
       //Logs the sensor has been pluged.
       this.onPlugAction()
       try {
           //Start capturing from plugged sensor.
           GrFingerJava.startCapture(idSensor, this, this);
       } catch (GrFingerJavaException e) {
           //write error to log
           println(e.getMessage());
       }
   }

   /**
    * This function is called every time a fingerprint reader is unplugged.
    *
    * @see griaule.grFinger.StatusCallBack#onUnplug(java.lang.String)
    */
   public void onSensorUnplug(String idSensor) {
       //Logs the sensor has been unpluged.
       this.onUnplugAction()
       try {
           GrFingerJava.stopCapture(idSensor);
       } catch (GrFingerJavaException e) {
           println(e.getMessage());
       }
   }

   /**
    * This function is called every time a fingerfrint image is captured.
    *
    * @see griaule.grFinger.ImageCallBack#onImage(java.lang.String, griaule.grFinger.FingerprintImage)
    */
   public void onImageAcquired(String idSensor, FingerprintImage fingerprint) {

       this.fingerprint=fingerprint;

       if (autoExtract) {
           extract();
           onFingerCatch(template)
           //If auto-Identify is also enabled, let's identify it.
           if (autoIdentify) {}
               //identify();
       }

   }

   /**
    * This Function is called every time a finger is placed on sensor.
    *
    * @see griaule.grFinger.FingerCallBack#onFingerDown(java.lang.String)
    */
   public void onFingerDown(String idSensor) {
       // Just signals that a finger event ocurred.
       onFingerInAction()

   }

   /**
    * This Function is called every time a finger is removed from sensor.
    *
    * @see griaule.grFinger.FingerCallBack#onFingerUp(java.lang.String)
    */
   public void onFingerUp(String idSensor) {
       // Just signals that a finger event ocurred.
       onFingerOutAction()
   }


   /**
    * Sets the colors used to paint templates.
    */
   public void setBiometricDisplayColors(
           Color minutiaeColor, Color minutiaeMatchColor,
           Color segmentColor, Color segmentMatchColor,
           Color directionColor, Color directionMatchColor)
   {
       try {
           // set new colors for BiometricDisplay
           GrFingerJava.setBiometricImageColors(
                   minutiaeColor,  minutiaeMatchColor,
                   segmentColor,   segmentMatchColor,
                   directionColor, directionMatchColor);

       } catch (GrFingerJavaException e) {
           //write error to log
           println(e.getMessage());
       }
   }

   public String getFingerprintSDKVersion() {
       try {
           return
               "Fingerprint SDK version " + GrFingerJava.getMajorVersion() + "." + GrFingerJava.getMinorVersion() + "\n" +
               "License type is '" + (GrFingerJava.getLicenseType() == GrFingerJava.GRFINGER_JAVA_FULL ? "Identification" : "Verification") + "'.";

       } catch (GrFingerJavaException e) {
           return null;
       }
   }

   public BufferedImage getFingerprint() {
       return this.fingerprint;
   }

   public void saveToFile(File file, ImageWriterSpi spi) {
       try {
           //Creates a image writer.
           ImageWriter writer = spi.createWriterInstance();
           ImageOutputStream output = ImageIO.createImageOutputStream(file);
           writer.setOutput(output);

           //Writes the image.
           writer.write(fingerprint);

           //Closes the stream.
           output.close();
           writer.dispose();
       } catch (IOException e) {
           // write error to log
           println(e.toString());
       }

   }

   /**
    * Loads a fingerprint image from file using an ImageReaderSpi.
    * See ImageIO API.
    */
   public void loadFile(File file, int resolution, ImageReaderSpi spi) {
       try {
           //Creates a image reader.
           ImageReader reader = spi.createReaderInstance();
           ImageInputStream input = ImageIO.createImageInputStream(file);
           reader.setInput(input);
           //Reads the image.
           BufferedImage img = reader.read(0);
           //Close the stream
           reader.dispose();
           input.close();
           // creates and processes the fingerprint image
           onImageAcquired("File", new FingerprintImage(img, resolution));
       } catch (Exception e) {
           // write error to log
           println(e.toString());
       }
   }

   /**
    * Sets the parameters used for identifications / verifications.
    */
   public void setParameters(int identifyThreshold, int identifyRotationTolerance, int verifyThreshold, int verifyRotationTolorance) {
       try {
           fingerprintSDK.setIdentificationThreshold(identifyThreshold);
           fingerprintSDK.setIdentificationRotationTolerance(identifyRotationTolerance);
           fingerprintSDK.setVerificationRotationTolerance(verifyRotationTolorance);
           fingerprintSDK.setVerificationThreshold(verifyThreshold);

       } catch (GrFingerJavaException e) {
           //write error to log
           println(e.getMessage());
       }
   }

   /**
    * Returns the current verification threshold.
    */
   public int getVerifyThreshold() {
       try {
           //Try to get the parameters from Fingerprint SDK.
           return fingerprintSDK.getVerificationThreshold();
       } catch (GrFingerJavaException e) {
           //If fails to load the parameters, writes error to log and returns 0
           println(e.getMessage());
           return 0;
       }
   }

   /**
    * Returns the current rotation tolerance on verifications.
    */
   public int getVerifyRotationTolerance() {
       try {
           //Try to get the parameters from Fingerprint SDK.
           return fingerprintSDK.getVerificationRotationTolerance();
       } catch (GrFingerJavaException e) {
           //If fails to load the parameters, writes error to log and returns 0
           println(e.getMessage());
           return 0;
       }
   }

   /**
    * Returns the current identification threshold.
    */
   public int getIdentifyThreshold() {
       try {
           //Try to get the parameters from Fingerprint SDK.
           return fingerprintSDK.getIdentificationThreshold();
       } catch (GrFingerJavaException e) {
           //If fails to load the parameters, writes error to log and returns 0
           println(e.getMessage());
           return 0;
       }
   }

   /**
    * Returns the current rotation tolerance on identification.
    */
   public int getIdentifyRotationTolerance() {
       try {
           //Try to get the parameters from Fingerprint SDK.
           return fingerprintSDK.getIdentificationRotationTolerance();
       } catch (GrFingerJavaException e) {
           //If fails to load the parameters, writes error to log and returns 0
           println(e.getMessage());
           return 0;
       }
   }

   /**
    * Enables / Disables automatic fingerprint identification after
    * capture.
    *
    * As identification must be done after template extraction, this property
    * will only be effective if autoExtract if set to true.
    */
   public void setAutoIdentify(boolean state) {
       autoIdentify = state;
   }

   /**
    * Enables / Disables automatic fingerprint extraction after
    * capture.
    */
   public void setAutoExtract(boolean state) {
       autoExtract = state;
   }






  
   
   /**
    * Extract a fingerprint template from current image.
    */  
   public void extract() {
       
       try {
           //Extracts a template from the current fingerprint image. 
           template = fingerprintSDK.extract(fingerprint);
           
           //Notifies it has been extracted and the quality of the extraction
           String msg = "Template extracted successfully. ";
           //write template quality to log
           switch (template.getQuality()){
               case Template.HIGH_QUALITY:     msg +="High quality.";   break;
               case Template.MEDIUM_QUALITY:   msg +="Medium quality."; break;
               case Template.LOW_QUALITY:      msg +="Low quality.";    break;
           }
           //println(msg);
           
           //Notifies the UI that template operations can be enabled.
           //ui.enableTemplate();
           //display minutiae/segments/directions into image
           //ui.showImage(GrFingerJava.getBiometricImage(template,fingerprint));
           
       } catch (GrFingerJavaException e) {
           //write error to log
           println(e.getMessage());
       }
       
   }
   
   
   /**
    * Sets the directory where Fingerprint SDK's native libraries / license file are placed.
    */
   public static void setFingerprintSDKNativeDirectory(String nativeDirectory) {
       File directory = new File(nativeDirectory);
       
       try {
           GrFingerJava.setNativeLibrariesDirectory(directory);
           GrFingerJava.setLicenseDirectory(directory);
       } catch (GrFingerJavaException e) {
           e.printStackTrace(); 
       }
   }      
   public static void inicializar() {
            System.out.println(System.getProperty("user.dir"));
            String grFingerNativeDirectory = (new File(".")).getAbsolutePath();

            setFingerprintSDKNativeDirectory(grFingerNativeDirectory)
   }
}
