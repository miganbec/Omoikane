/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.nadesicoiLegacy

import java.util.zip.*;

/**
 *
 * @author Usuario
 */
class Util {
    static def generarUID() {
        return java.util.UUID.randomUUID().toString()
    }
    static def descargar(origen, destino) {
        def bytes = new byte[8]
        def fos=new FileOutputStream(destino)
        def fis=(new URL(origen)).eachByte {
            fos.write it
        }
        fos.close()
    }
    static def descomprimir(origen, destino) {
       try {
         byte[] buf = new byte[4096];
         ZipFile zf = new ZipFile(origen);
         ZipEntry entry;
         int len;
         def fis;

         Enumeration<? extends ZipEntry> contenido = zf.entries();
         while (contenido.hasMoreElements()) {
           entry = contenido.nextElement();

           FileOutputStream out = new FileOutputStream(destino+entry.getName());
           //FileOutputStream out = new FileOutputStream(destino+"auto2c.dat");
           fis = zf.getInputStream(entry);

           while ((len = fis.read(buf)) > 0) {
             out.write(buf, 0, len);
           }
           fis.close();
           out.close();
         }
         zf.close();
       } catch (IOException e) {
         println "Error: "+e.message
       }
    }
    public static String bytes2HexString(byte[] huellaBytes)
    {
        String huellaString = "";
        String hexChar;
        int byteCompleto;
        int byteA;
        int byteB;

        for(int i = 0; i < huellaBytes.length; i++)
        {
            byteCompleto = ((Byte)huellaBytes[i]).intValue() + 128;
            hexChar = Integer.toHexString(byteCompleto);
            hexChar = (hexChar.length() < 2) ? hexChar = "0" + hexChar : hexChar;
            //System.out.println("Int: " + ((Byte)huellaBytes[i]).intValue() +  " Hex: " + hexChar);
            huellaString += hexChar;
        }
        //System.out.println("---Fin byte2hex---------------------------------------");
        return huellaString;
    }
    public static byte[] hexString2Bytes(String hexString)
    {
        byte[] hB = new byte[(int)Math.floor(hexString.length()/2)];
        String hexChar;
        int    intByte;
        int subA, elByte, j;

        for(int i = 0; i < hB.length; i++)
        {
            j = i * 2;
            hexChar = String.valueOf(hexString.charAt(j)) + String.valueOf(hexString.charAt(j+1));
            intByte = Integer.parseInt(hexChar, 16) - 128;
            //System.out.println("int: " + intByte + " hex: " + hexChar);
            hB[i] = Byte.valueOf(String.valueOf(intByte));
        }
        //System.out.println("---Fin hex2byte---------------------------------------");
        return hB;
    }

    public static boolean compararBytes(byte[] a, byte[] b)
    {
        if(a.length == b.length)
        {
            for(int i = 0; i < a.length; i++)
            {
                if(a[i] == b[i]) { continue; }
                return false;
            }
            return true;
        }

        return false;
    }

    public static def redondea(Double cant) {
        Math.round(cant*100.0)/100.0
    }

}

