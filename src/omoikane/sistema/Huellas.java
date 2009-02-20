/*
 * Huellas.java
 *
 * Created on 17 de septiembre de 2007, 10:27 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package omoikane.sistema;

import java.awt.*;
import com.griaule.grfingerjava.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.*;
import omoikane.formularios.*;

/**
 *
 * @author Octavio
 */


public class Huellas extends MiniLeerHuella implements com.griaule.grfingerjava.IFingerEventListener, com.griaule.grfingerjava.IImageEventListener, com.griaule.grfingerjava.IStatusEventListener 
{
    public Template template;
    public MatchingContext matchContext;
    public JInternalDialog parent;
    public String IDLector;
    public byte[] byteTemplate = new byte[0];
    Object focoCerrar = new Object();
    /** Creates a new instance of Huellas */

        
    class HiloParaCerrar extends Thread
    {
        Huellas hu;
        HiloParaCerrar(Huellas hu)
        {
            this.hu = hu;
        }
        public void run()
        {
            hu.cerrar();
        }
    }
    
    public Huellas(JInternalDialog parent) {
        this.setVisible(true);
        this.setBounds(0,0,500,500);
        this.parent = parent;
        
        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cerrar");
        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "cerrar");
        getActionMap().put("cerrar" , new adminAcciones(this, adminAcciones.CERRAR));
        this.btnCancelar.getActionMap().put("cerrar", new adminAcciones(this, adminAcciones.CERRAR));
        this.btnCancelar.addActionListener(new adminAcciones(this, adminAcciones.CERRAR));
        
        //this.btnCancelar.getActionMap().put("cerrar", new adminAcciones(this, adminAcciones.CERRAR));
        
        try {
            System.out.println(System.getProperty("user.dir"));
            //com.griaule.grfingerjava.GrFingerJava.setNativeLibrariesDirectory();
            com.griaule.grfingerjava.GrFingerJava.initializeCapture(this);

        } catch(com.griaule.grfingerjava.GrFingerJavaException GrEx)
        {
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
        Huellas HU;
        JTextField    destino;
        int           accion;
        final static int ACEPTAR  = 1;
        final static int CANCELAR = 2;
        final static int CERRAR   = 3;
        
        adminAcciones(Huellas HU, int accion)
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
}
