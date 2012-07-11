package omoikane.sistema.huellas;

import com.digitalpersona.onetouch.DPFPFingerIndex;
import com.digitalpersona.onetouch.DPFPTemplate;
import org.apache.log4j.Logger;

import javax.xml.transform.Templates;
import java.io.*;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: usuario1
 * Date: 8/07/12
 * Time: 08:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class TemplateMap extends EnumMap<DPFPFingerIndex, Template> {
    public static Logger logger        = Logger.getLogger(TemplateMap.class);
    public TemplateMap() {
        super(DPFPFingerIndex.class);
    }

    public byte[] serializar() {
         try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos;
            oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            return bos.toByteArray();
         } catch (IOException e) {
            logger.error("Error al serializar huellas", e);
         }
        return null;
    }

    public static TemplateMap deserializar(byte[] bytes) {
        TemplateMap templateMap = new TemplateMap();

        try {
            ByteArrayInputStream bis   = new ByteArrayInputStream(bytes);
            ObjectInputStream    ois;
            ois = new ObjectInputStream(bis);
            templateMap = (TemplateMap) ois.readObject();
            bis.close();
            ois.close();
        } catch (IOException e) {
            logger.error("Error al deserializar huellas", e);
        } catch (ClassNotFoundException e) {
            logger.error("Error en el código de deserialización de mapa de plantillas de huellas (TemplateMap)", e);
        }


        return templateMap;
    }
}
