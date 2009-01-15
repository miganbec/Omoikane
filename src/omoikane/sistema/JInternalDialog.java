/*
 * JInternalDialog.java
 *
 * Created on 15 de abril de 2007, 02:43 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package omoikane.sistema;

import javax.swing.*;
import java.awt.image.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import javax.swing.event.MouseInputAdapter;
import omoikane.principal.*;
import org.jdesktop.swingx.image.*;
import org.jdesktop.swingx.graphics.*;
import org.jdesktop.animation.timing.*;
import org.jdesktop.animation.timing.interpolation.*;
/**
 * JInternalDialog permite crear un InternalFrame modal y con un efecto de fondo. 
 * Al crearse es invisible, se puede mostrar con setActivo para mostrarlo modalmente
 * o con setVisible para mostrarlo pero no de manera modal (algunos m�todos aplican su propio
 * algoritmo para volverlo modal)
 * @author Octavio
 */
public class JInternalDialog extends javax.swing.JInternalFrame {
    
    Object syncMonitor = new Object();
    BufferedImage fondo;
    public JPanel glass;
    Object JParent;
    JLayeredPane layer = null;
    private BufferedImage blurBuffer;
    private BufferedImage backBuffer;
    private float alpha = 0.0f;

    /** Creates a new instance of JInternalDialog */
    public JInternalDialog(Object parent) {
          //setVisible(true);
          init(parent);
    }    
    public JInternalDialog(Object parent, String titulo)
    {
          setTitle(titulo);
          init(parent);
    }
    public JInternalDialog(Object parent, String titulo, Container contenido)
    {
          //setTitle(titulo);
          init(parent);
          setContentPane((JPanel)contenido, titulo);
    }
    public void init(Object parent)
    {          
        
          glass = new BlurGlass();
          glass.setLayout(null);
         
          this.setLayout(null);
          
          setOpaque(false);
          //getContentPane().setOpaque(false);
          this.getLayeredPane().setOpaque(false);
          this.getRootPane().setOpaque(false);
          //putClientProperty("Synthetica.opaque", Boolean.FALSE);
          
          JParent = parent;
          glass.setOpaque(false);
          
          MouseInputAdapter adapter = new MouseInputAdapter(){};
          glass.addMouseListener(adapter);
          glass.addMouseMotionListener(adapter);
          
          try {
              if(parent instanceof JFrame)
              {
                layer = ((JFrame)parent).getLayeredPane();
              } else if (parent instanceof JInternalFrame)
              {
                layer = ((JInternalFrame)parent).getLayeredPane();  
              } 
              glass.setBounds(layer.getBounds());
              
          } catch(NullPointerException err) { 
              Dialogos.error("Error interno en Dialogo interno modal", err);
          }

          try {
            glass.add(this);
            layer.add(glass, JLayeredPane.DEFAULT_LAYER); 
          } catch(NullPointerException err) { Dialogos.error("Error interno en dialogo modal interno", err); }
          
    }
    public void setActivo(boolean val)

    {                         
        glass.setVisible(val);
        setVisible(val);        
        JLayeredPane.getLayeredPaneAbove(glass).moveToFront(glass);
        
        if(val) {
          synchronized(syncMonitor)
          {
              
                try {
                  if (SwingUtilities.isEventDispatchThread()) {
                    EventQueue theQueue = 
                      getToolkit().getSystemEventQueue();
                    while (isVisible()) {
                      AWTEvent event = theQueue.getNextEvent();
                      Object source = event.getSource();

                      if (event instanceof ActiveEvent) {
                        ((ActiveEvent)event).dispatch();
                      } else if (source instanceof Component) {
                        ((Component)source).dispatchEvent(
                          event);
                      } else if (source instanceof MenuComponent) {
                        ((MenuComponent)source).dispatchEvent(
                          event);
                      } else {
                        System.out.println(
                          "No se puede despachar: " + event);
                      }

                    }
                  } else {
                    while (isVisible()) {
                      syncMonitor.wait();
                    }
                  }
                } catch (InterruptedException ignored) {
                    System.out.println("Excepción de interrupción: "+ignored.getMessage());
                }
               
    
          }                          
        } else 
        {
          synchronized(syncMonitor)
          {
              setVisible(false);
              glass.setVisible(false);
              syncMonitor.notifyAll(); 
              
              eliminarDelContenedor();
          }                                      
        }
     
    }
    void eliminarDelContenedor()
    {
              for(int r = 0; r < layer.getComponentCount(); r++)
              {
                  if(((Component)layer.getComponent(r)).hashCode() == this.glass.hashCode())
                  {
                      layer.remove(r);
                  }
              }          
    }
    
    public void setContentPane(JPanel pane)
    {   
        _setContentPane(pane, null);
    } 
    public void setContentPane(JPanel pane, String titulo)
    {
        _setContentPane(pane, titulo);
    }
      void _setContentPane(JPanel pane, String titulo)
      {
          boolean visibilidad = this.isVisible();
          this.setVisible(false);          
          super.setContentPane(pane);
          ajustarYCentrar(this, pane);
          //try { Thread.sleep(1); } catch (InterruptedException ignored) { }

          if(titulo != null) { setTitle(titulo); }
          generarFondo(this);  
          this.setVisible(visibilidad);
          this.repaint();
      }
      public void paintComponent(Graphics g)
      {

          if(Principal.fondoBlur) {
            if (isVisible() && blurBuffer != null) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(backBuffer, 0, 0, null);
                g2.setComposite(AlphaComposite.SrcOver.derive(alpha));
                g2.drawImage(blurBuffer, 0, 0, getWidth(), getHeight(), null);
                g2.setColor(new Color(55,55,255,150));
                g2.fillRect(0,0, getBounds().width, getBounds().height);
                g2.dispose();
            }
          } else {
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(fondo, 0, 0, null);
          }
          
      }
      public void generarFondo(Component componente)
      {
          boolean               dibujarFondo = false;
          Rectangle             med          = this.getBounds();
          Rectangle             areaDibujo   = this.getBounds();
          BufferedImage         tmp;
          GraphicsConfiguration gc           = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
          
          if(Principal.fondoBlur) { dibujarFondo = true; }
          if(dibujarFondo)
          {
              JRootPane root = SwingUtilities.getRootPane(this);
              blurBuffer = GraphicsUtilities.createCompatibleImage(Principal.sysAncho, Principal.sysAlto);
              Graphics2D g2 = blurBuffer.createGraphics();
              g2.setClip(med);
              blurBuffer = blurBuffer.getSubimage(med.x, med.y, med.width, med.height);
              ((Escritorio)Principal.getEscritorio()).getFrameEscritorio().paint(g2);
              g2.dispose();
              backBuffer = blurBuffer;
              blurBuffer = GraphicsUtilities.createThumbnailFast(blurBuffer, getWidth() / 2);
              blurBuffer = new GaussianBlurFilter(5).filter(blurBuffer, null);
          } else {
              tmp = gc.createCompatibleImage(areaDibujo.width, areaDibujo.height,BufferedImage.TRANSLUCENT);
              Graphics2D g2d = (Graphics2D) tmp.getGraphics();
              g2d.setColor(new Color(55,55,255,155));
              g2d.fillRect(0,0,areaDibujo.width,areaDibujo.height);
              fondo = tmp;
          }
      }
      void ajustarYCentrar(JComponent porAjustar, JComponent ajustador)
      {
          //Se agregan 30 de alto y 6 de ancho por adornos de la ventana
          porAjustar.setSize(ajustador.getMinimumSize().getSize().width + 16, ajustador.getMinimumSize().getSize().height + 44);
          int x = Math.round(Principal.sysAncho / 2) - Math.round(porAjustar.getBounds().width  / 2);
          int y = Math.round(Principal.sysAlto  / 2) - Math.round(porAjustar.getBounds().height / 2);
          porAjustar.setLocation(x,y);
      }          

}

class BlurGlass extends JPanel {

      private BufferedImage blurBuffer;
      private BufferedImage backBuffer;
      private float alpha  = 0.0f;
      private boolean listo= false;

      public void setVisible(boolean val) { if(val) { fadeIn(); } else { super.setVisible(val); } }
      public void generarFondo(Component componente)
      {
          boolean               dibujarFondo = false;
          Rectangle             med          = this.getBounds();
          Rectangle             areaDibujo   = this.getBounds();
          BufferedImage         tmp;
          GraphicsConfiguration gc           = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
          
          if(!Principal.fondoBlur) { dibujarFondo = true; }
          if(dibujarFondo)
          {
              JRootPane root = SwingUtilities.getRootPane(this);
              blurBuffer = GraphicsUtilities.createCompatibleImage(Principal.sysAncho, Principal.sysAlto);
              Graphics2D g2 = blurBuffer.createGraphics();
              g2.setClip(med);
              blurBuffer = blurBuffer.getSubimage(med.x, med.y, med.width, med.height);
              ((Escritorio)Principal.getEscritorio()).getFrameEscritorio().paint(g2);
              g2.dispose();
              backBuffer = blurBuffer;
              blurBuffer = toGrayScale(blurBuffer);
              blurBuffer = GraphicsUtilities.createThumbnailFast(blurBuffer, getWidth() / 2);
              blurBuffer = new GaussianBlurFilter(5).filter(blurBuffer, null);
              listo = true;
          } 
      }
      public void paintComponent(Graphics g)
      {

          if(!Principal.fondoBlur) {
            if (isVisible() && blurBuffer != null && listo) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(backBuffer, 0, 0, null);
                g2.setComposite(AlphaComposite.SrcOver.derive(alpha));
                g2.drawImage(blurBuffer, 0, 0, getWidth(), getHeight(), null);
                g2.dispose();
            }
          }
      }
      public float getAlpha() {
          return alpha;
      }
      public void setAlpha(float alpha) {
          this.alpha = alpha;
          repaint();
      }
      public void fadeIn() {
          generarFondo(this);
          super.setVisible(true);
          SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                  Animator animator = PropertySetter.createAnimator(400, BlurGlass.this, "alpha", 1.0f);
                  animator.setAcceleration(0.2f);
                  animator.setDeceleration(0.3f);
                  //animator.addTarget(new PropertySetter(DetailsView.this, "alpha", 1.0f));
                  animator.start();
              }
          });
      }
    public static BufferedImage toGrayScale(BufferedImage image)
    {
        BufferedImage result = new BufferedImage(
                image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Raster raster = image.getData();
        WritableRaster grayRaster = result.getRaster();
        int height = raster.getHeight();
        int sample = 0;

        for( int x=0; x<raster.getWidth(); ++x)
        {
            for( int y=0; y<height; ++y)
            {

                sample = raster.getSample(x, y, 0) +
                		 raster.getSample(x, y, 1) +
                		 raster.getSample(x, y, 2);

                grayRaster.setSample(x, y, 0, (int)((float)sample / 3.0));
            }
        }
        return result;
    }
}