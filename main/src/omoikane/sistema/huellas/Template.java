package omoikane.sistema.huellas;

import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPTemplate;
import sun.misc.IOUtils;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: usuario1
 * Date: 8/07/12
 * Time: 08:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class Template implements Serializable {
    private DPFPTemplate template;
    public DPFPTemplate getTemplate() {
        return template;
    }

    public void setTemplate(DPFPTemplate template) {
        this.template = template;
    }

    public Template(DPFPTemplate template) {
        this.template = template;
    }

     private void writeObject(java.io.ObjectOutputStream out) throws IOException {
         out.write(template.serialize());
     }
     private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
         //ByteArrayOutputStream baos = new ByteArrayOutputStream();

         //byte[] bytes = new byte[in.available()];
         //DataInputStream dis = new DataInputStream(in);

         byte[] bytes = IOUtils.readFully(in, -1, true);
         //dis.read(bytes);
         //dis.read(baos.toByteArray());
         //dis.close();
         template = DPFPGlobal.getTemplateFactory().createTemplate(bytes);
     }
}
