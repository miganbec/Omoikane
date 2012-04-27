
 /* Author Phesus        //////////////////////////////
 *  ORC,ACR             /////////////
 *                     /////////////
 *                    /////////////
 *                   /////////////
 * //////////////////////////////                   */

 package omoikane.sistema.huellas;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;

import com.griaule.grfingerjava.GrFingerJava;
import com.griaule.grfingerjava.GrFingerJavaException;
import com.griaule.grfingerjava.IFingerEventListener;
import com.griaule.grfingerjava.IImageEventListener;
import com.griaule.grfingerjava.IStatusEventListener;
import com.griaule.grfingerjava.MatchingContext;
import com.griaule.grfingerjava.Template;
import omoikane.sistema.Dialogos;
import omoikane.sistema.JInternalDialog2;

 /**
 *
 * @author Octavio
 */


public class HuellasGriaule extends MiniLeerHuella implements IFingerEventListener, IImageEventListener, IStatusEventListener
{
    public Template template;
    public MatchingContext matchContext;
    public String IDLector;

    Object focoCerrar = new Object();
    /** Creates a new instance of HuellasGriaule */

    public HuellasGriaule(JInternalDialog2 parent) {
        super(parent);
        
        //getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cerrar");
        //getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "cerrar");
        //getActionMap().put("cerrar" , new adminAcciones(this, adminAcciones.CERRAR));
        //this.btnCancelar.getActionMap().put("cerrar", new adminAcciones(this, adminAcciones.CERRAR));
        //this.btnCancelar.addActionListener(new adminAcciones(this, adminAcciones.CERRAR));
        
        //this.btnCancelar.getActionMap().put("cerrar", new adminAcciones(this, adminAcciones.CERRAR));
        
        try {
            System.out.println(System.getProperty("user.dir"));
            String grFingerNativeDirectory = (new File(".")).getAbsolutePath();

            File directory = new File(grFingerNativeDirectory);
       
            GrFingerJava.setNativeLibrariesDirectory(directory);
            GrFingerJava.setLicenseDirectory(directory);
            //com.griaule.grfingerjava.GrFingerJava.setNativeLibrariesDirectory();
            System.out.println("Carpeta dependencias griaule: "+grFingerNativeDirectory);

            matchContext = new MatchingContext();
            GrFingerJava.initializeCapture(this);
            System.out.println ("Lector Huella inicializada");

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
                com.griaule.grfingerjava.GrFingerJava.finalizeCapture();            
                //com.griaule.grfingerjava.GrFingerJava.stopCapture(IDLector);

                if(matchContext != null)
                {
                    matchContext.destroy();                
                }

                //Main.entornoBase.pantalla.getLayeredPane().grabFocus();

                parent.setActivo(false);        
            }
            catch(GrFingerJavaException gr)
            {
                Dialogos.error("Error al finalizar lector de huella.", gr);
            }
        }
    }
    
    class adminAcciones extends javax.swing.AbstractAction
    {
        HuellasGriaule HU;
        JTextField    destino;
        int           accion;
        final static int ACEPTAR  = 1;
        final static int CANCELAR = 2;
        final static int CERRAR   = 3;
        
        adminAcciones(HuellasGriaule HU, int accion)
        {
            this.HU     = HU;
            this.accion = accion;
        }
        adminAcciones(JTextField campoDestino, int accion)
        {
            destino = campoDestino;
            this.accion = accion;            
        }
        public void actionPerformed(ActionEvent ae)
        {
            switch(accion)
            {
                case CERRAR:  synchronized(HU.focoCerrar) { HU.focoCerrar.notifyAll(); }  break;
            }
        }
    }

    public byte[] getResultado()
    {
        return this.byteTemplate;
    }
    
    public void onSensorUnplug(String IDLector)
    {
        //consola.out.echo("Lector desconectado");
        super.setLectorActivo(false);
    }
    
    public void onSensorPlug(String IDLector)
    {
        //consola.out.echo("Lector conectado");
        try {
                // Start capturing from plugged sensor.
                com.griaule.grfingerjava.GrFingerJava.startCapture(IDLector, this, this);
        } catch (com.griaule.grfingerjava.GrFingerJavaException e) {
                // write error to log
                Dialogos.error("Error al iniciar captura de huella dactilar", e);
        }
        this.IDLector = IDLector;
        super.setLectorActivo(true);
    }
    
    public void onImageAcquired(String IDLector, com.griaule.grfingerjava.FingerprintImage imagen)
    {

        try
        {
            //this.lblHuella.setIcon(new ImageIcon(imagen));
            
            this.matchContext = new MatchingContext();
            this.template = this.matchContext.extract(imagen);
            this.byteTemplate = this.template.getData();
            
            //consola.out.echo("Lector huella: imagen adquirida");
        }
        catch(GrFingerJavaException gr)
        {
            Dialogos.error("Error al procesar huella dactilar.", gr);
        }

    }
    
    public void onFingerUp(String IDLector)
    {
        //consola.out.echo("Se retir� la huella del lector");
        synchronized(focoCerrar) { focoCerrar.notifyAll(); }
    }
    
    public void onFingerDown(String IDLector)
    {
        //consola.out.echo("Se puso una huella en el lector");
    }
    public void setParameters(int identifyThreshold, int identifyRotationTolerance, int verifyThreshold, int verifyRotationTolorance) {
       try {
           matchContext.setIdentificationThreshold(identifyThreshold);
           matchContext.setIdentificationRotationTolerance(identifyRotationTolerance);
           matchContext.setVerificationRotationTolerance(verifyRotationTolorance);
           matchContext.setVerificationThreshold(verifyThreshold);

       } catch (Exception e) {
           //write error to log
           Dialogos.error("Error al establecer parámetros del SDK de huellas", e);
       }
    }
}
