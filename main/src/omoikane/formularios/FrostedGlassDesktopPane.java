package omoikane.formularios;

import omoikane.principal.Principal;
import org.jdesktop.swingx.graphics.GraphicsUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Hashtable;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 12/06/13
 * Time: 12:18
 * To change this template use File | Settings | File Templates.
 */
public class FrostedGlassDesktopPane extends JDesktopPane {

    public boolean isOptimizedDrawingEnabled() {
        return false;
    }

    public void paint(Graphics g) {
        if(!Principal.fondoBlur) { super.paint(g); return; }

        Rectangle clipBounds = g.getClipBounds();
        BufferedImage bufferImage = GraphicsUtilities.createCompatibleImage(clipBounds.width, clipBounds.height);

        Graphics bufferGraphics = bufferImage.createGraphics();
        bufferGraphics.translate(-clipBounds.x, -clipBounds.y);
        bufferGraphics.setClip(clipBounds);

        try {
            // let children paint
            super.paint(bufferGraphics);
            // blit offscreen buffer to given graphics g
            g.drawImage(bufferImage, clipBounds.x, clipBounds.y,
                    null);
            getBufferImage(getWidth(), getHeight()).getGraphics().drawImage(bufferImage, clipBounds.x, clipBounds.y, null);
        } catch (RuntimeException ex) {
            throw ex;
        } finally {
            bufferGraphics.dispose();
        }
    }
    private BufferedImage bufferImage;

    public BufferedImage getBufferImage(int w, int h) {
        if(bufferImage == null) {
            bufferImage = GraphicsUtilities.createCompatibleImage(w, h);
        }
        return bufferImage;
    }

    private Hashtable<String, BufferedImage> bufferedImagesTable;

    public BufferedImage getBufferedImage(Integer width, Integer height) {
        if(bufferedImagesTable == null) { bufferedImagesTable = new Hashtable<>(); }
        String imageSize = width+"x"+height;
        if(bufferedImagesTable.contains(imageSize)) {
            return bufferedImagesTable.get(imageSize);
        } else {
            System.out.println("Nuevo caché: "+width+", "+height);
            return bufferedImagesTable.put(imageSize, GraphicsUtilities.createCompatibleImage(width, height));
        }
    }
}
