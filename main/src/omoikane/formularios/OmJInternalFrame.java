package omoikane.formularios;

import com.jhlabs.image.BoxBlurFilter;
import com.jhlabs.image.PointillizeFilter;
import omoikane.principal.Principal;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 2/07/13
 * Time: 04:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class OmJInternalFrame extends javax.swing.JInternalFrame {
    public BufferedImage cacheFondo;
    private BufferedImage fondo;

    public OmJInternalFrame() {
        ((JPanel)this.getContentPane()).setOpaque(false);
        this.getLayeredPane().setOpaque(false);
        this.getRootPane().setOpaque(false);
    }

    public void paintComponent(Graphics g)
    {
        if(!Principal.fondoBlur) { g.drawImage(this.fondo, 0, 0, null); return; }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        BufferedImage fondo = Principal.getEscritorio().getPanelEscritorio().getBufferImage(getHeight(), getWidth());

        if(cacheFondo == null) {
            BoxBlurFilter filter = new BoxBlurFilter();

            cacheFondo = copyImage(fondo);
            System.out.println("Bounds: "+getX()+","+getY()+","+getWidth()+","+getHeight());

            //fondo = new GaussianBlurFilter(4).filter(fondo, null);
            filter.setIterations(2);
            filter.setRadius(5);

            PointillizeFilter pointillizeFilter = new PointillizeFilter();
            pointillizeFilter.setEdgeColor(6);
            pointillizeFilter.setEdgeThickness(1);
            pointillizeFilter.setFadeEdges(false);
            pointillizeFilter.setFuzziness(6);
            //pointillizeFilter.filter(cacheFondo, cacheFondo);

            filter.filter(cacheFondo, cacheFondo);

            Graphics2D graphics2D = (Graphics2D) cacheFondo.getGraphics();
            graphics2D.setColor(new Color(0,0,0,125));
            graphics2D.fillRect(0,0,cacheFondo.getWidth(),cacheFondo.getHeight());

        }

        BufferedImage fondoVentana = cacheFondo.getSubimage(getX(), getY(), getWidth(), getHeight());
        g.drawImage(fondoVentana, 0, 0, null);


    }

    static BufferedImage copyImage(BufferedImage bi) {
        //BufferedImage copyOfImage =
        //        new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_ARGB);
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        BufferedImage copyOfImage = gc.createCompatibleImage(bi.getWidth(), bi.getHeight(),BufferedImage.TRANSLUCENT);
        Graphics g = copyOfImage.getGraphics();
        g.drawImage(bi, 0, 0, null);
        return copyOfImage;
    }

    public void generarFondo()
    {
        Rectangle areaDibujo = this.getBounds();
        BufferedImage tmp;
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

        tmp = gc.createCompatibleImage(areaDibujo.width, areaDibujo.height,BufferedImage.TRANSLUCENT);
        Graphics2D g2d = (Graphics2D) tmp.getGraphics();
        g2d.setColor(new Color(0,0,0,165));
        g2d.fillRect(0,0,areaDibujo.width,areaDibujo.height);
        fondo = tmp;
    }

}
