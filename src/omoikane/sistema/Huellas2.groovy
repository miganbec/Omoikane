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

import javax.swing.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import omoikane.formularios.*;


import com.griaule.grfingerjava.FingerprintImage;
import com.griaule.grfingerjava.GrFingerJava;
import com.griaule.grfingerjava.GrFingerJavaException;
import com.griaule.grfingerjava.IFingerEventListener;
import com.griaule.grfingerjava.IImageEventListener;
import com.griaule.grfingerjava.IStatusEventListener;
import com.griaule.grfingerjava.MatchingContext;
import com.griaule.grfingerjava.Template;

import omoikane.principal.Principal

/**
 *
 * @author Octavio
 */


public class Huellas2 extends MiniLeerHuella
{
    public Template template;
    public MatchingContext matchContext;
    public JInternalDialog2 parent;
    public String IDLector;
    public byte[] byteTemplate = new byte[0];
    public FingerUtil fingerUtil;
    Object focoCerrar = new Object();
    /** Creates a new instance of Huellas */

    
    public Huellas2(JInternalDialog2 parent) {
        this.setVisible(true);
        this.setBounds(0,0,500,500);
        this.parent = parent;
        omoikane.principal.Principal.toFinalizeTracker[this] = true

        
        //getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cerrar");
        //getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "cerrar");
        //getActionMap().put("cerrar" , new adminAcciones(this, adminAcciones.CERRAR));
        //this.btnCancelar.getActionMap().put("cerrar", new adminAcciones(this, adminAcciones.CERRAR));
        //this.btnCancelar.addActionListener(new adminAcciones(this, adminAcciones.CERRAR));
        
        //this.btnCancelar.getActionMap().put("cerrar", new adminAcciones(this, adminAcciones.CERRAR));

        fingerUtil = new FingerUtil();
        fingerUtil.onPlugAction      = { super.setLectorActivo(true);  }
        fingerUtil.onUnplugAction    = { super.setLectorActivo(false); }
        fingerUtil.onFingerInAction  = {  }
        fingerUtil.onFingerOutAction = { synchronized(focoCerrar) { focoCerrar.notifyAll(); } }
        fingerUtil.onFingerCatch     = { template -> byteTemplate = template.getData() }
        fingerUtil.iniciar()

        HiloParaCerrar HPC = new HiloParaCerrar(this);
        HPC.start();
        
    }
    protected void finalize() {
        fingerUtil.destroy();
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
                omoikane.principal.Principal.toFinalizeTracker.remove(this)
                fingerUtil.destroy();

                parent.setActivo(false);

            }
            catch(Exception gr)
            {
                Dialogos.error("Error al finalizar lector de huella.", gr);
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

class HiloParaCerrar extends Thread
{
    Huellas2 hu;
    HiloParaCerrar(Huellas2 hu)
    {
        this.hu = hu;
    }
    public void run()
    {
        hu.cerrar();
    }
}