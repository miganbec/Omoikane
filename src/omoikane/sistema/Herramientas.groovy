
 /* Author Phesus        //////////////////////////////
 *  ORC,ACR             /////////////
 *                     /////////////
 *                    /////////////
 *                   /////////////
 * //////////////////////////////                   */

package omoikane.sistema

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import groovy.inspect.swingui.*
import groovy.swing.SwingBuilder;
import com.griaule.grfingerjava.*;
import javax.swing.JComponent;
import org.jdesktop.swingx.JXDatePicker;


public class Herramientas
{
        static def texto      = /^([a-zA-Z0-9_\-\s\&\ñ\Ñ\+áéíóúüàèìòùÁÉÍÓÚÀÈÌÒÙÜ\\\%\.\/\"\'\,\;\.\:\#\@]+)$/
        static def textoVacio = /^([a-zA-Z0-9_\-\s\&\ñ\Ñ\+áéíóúüàèìòùÁÉÍÓÚÀÈÌÒÙÜ\\\%\.\/\"\'\,\;\.\:\#\@]*)$/
        static def numero     = /^([0-9]+)$/
        static def numeroReal = /^([0-9]*[\.]{0,1}[0-9]+)$/
        static def error1         = " No puede estar vacio y sólo puede incluir numeros, letras, espacios, acentos, diagonales, coma, comillas y los siguientes caracteres . _ - + % ; : # @ & "
        static def error2         = " No puede estar vacio y sólo puede incluír números."
        static def error3         = " No puede estar vacio y sólo puede incluír números reales positivos"
        static def error4         = " Sólo puede incluir numeros, letras, espacios, acentos, diagonales, coma, comillas y los siguientes caracteres . _ - + % ; : # @ & "

    def static void setColumnsWidth(tabla, anchos) {
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        def tabAncho = tabla.getBounds().width
        def column
        def anchoCols= anchos
        def residuo  = 0.0, preAncho, ancho
        for (int i=0; i<anchoCols.size(); i++) {
          column  = tabla.getColumnModel().getColumn(i);
          preAncho= anchoCols[i]*tabAncho
          ancho   = Math.floor(preAncho)
          residuo+= preAncho-ancho
          if(i==anchoCols.size()-1) { ancho += Math.floor(residuo) }
          column.setPreferredWidth(ancho as int)
        }
    }

    def static void verificaCampo(txt,expresion,error)
    {
        if(!(txt==~ expresion))
        {throw new Alerta(error)}
    }

    def static void verificaCampos(c)
    {
        try { c() }
        catch (Alerta e){Dialogos.lanzarAlerta(e.message)}
        catch (e) {Dialogos.lanzarDialogoError(null,e.message,getStackTraceString(e))}
    }

    def static String getStackTraceString(java.lang.Exception exc)
    {
        String salida = exc.toString() + "\n";
        java.lang.StackTraceElement[] elementos = exc.getStackTrace();
        for(int i = 0; i < elementos.length; i++)
        {
            salida += elementos[i].toString() + " at \n" ;
        }
        return salida;
    }

    def static centrarVentana(ventana)
    {
        def compensaY = 0
        def config    = omoikane.principal.Principal.config.resolucionPantalla
        
        if(((config.@ancho[0] as Double)/4)==((config.@alto[0] as Double)/3)) { compensaY = 100 } //Compensaciòn particular para 1024768
        Dimension screenSize = omoikane.principal.Principal.escritorio.escritorioFrame.getSize();
        Dimension ventanSize = ventana.getPreferredSize();

        def posX = screenSize.width/2 - (ventanSize.width/2) as int
        def posY = screenSize.height/2 - (ventanSize.height/2) as int
        ventana.setLocation(posX,posY+compensaY);
        [x:posX,y:posY]
    }

    def static centrarAbsoluto(ventana) {
        def compensaY = 0

        Dimension screenSize = omoikane.principal.Principal.escritorio.escritorioFrame.getSize();
        Dimension ventanSize = ventana.getPreferredSize();

        def posX = screenSize.width/2 - (ventanSize.width/2) as int
        def posY = screenSize.height/2 - (ventanSize.height/2) as int
        ventana.setLocation(posX,posY+compensaY);
        [x:posX,y:posY]
    }

    def static centrarVentanaAPantalla(ventana) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventanSize = ventana.getPreferredSize();

        def posX = screenSize.width/2 - (ventanSize.width/2) as int
        def posY = screenSize.height/2 - (ventanSize.height/2) as int
        ventana.setLocation(posX,posY);
        [x:posX,y:posY]
    }

    def static Input2Action(JComponent componente, int tecla, String nombre, Action claseAccion)
    {
        componente.getInputMap(JInternalFrame.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(tecla, 0), nombre);
        componente.getActionMap().put(nombre, claseAccion);
        componente.getInputMap(JInternalFrame.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(tecla, 0), nombre);
        componente.getActionMap().put(nombre, claseAccion);
    }

    def static In2ActionX(componente, tecla, nombre, Closure cls) {
        def ax = ProxyGenerator.instantiateAggregateFromBaseClass([actionPerformed:{e -> cls()}], AbstractAction.class)
        componente.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(tecla, 0), nombre);
        componente.getActionMap().put(nombre, ax);
    }

    def static iconificable(cat) {
        cat.setIconifiable(true);
        SwingBuilder.build { cat.internalFrameIconified = { e -> omoikane.principal.Principal.escritorio.escritorioFrame.fondoToBack() } }
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

    public static int  FingerMatch(String huellaA, String huellaB)
    {
        System.out.println("Verificando zero...");
        Template  ref1, ref2;
        ref1 = new Template();
        ref2 = new Template();
        boolean autorizado = false;

        ref1.setData(Herramientas.hexString2Bytes(huellaA));
        ref2.setData(Herramientas.hexString2Bytes(huellaB));

        try {
            System.out.println("Verificando...");
            autorizado = (new MatchingContext()).verify(ref2, ref1);
        } catch(GrFingerJavaException grje)
        {
            grje.printStackTrace();
        }
        return (autorizado?1:0);
    }

    public static panelFormulario(component){
        for(Component child: component.getContentPane().getComponents()){

            child.focusGained={evt->
                evt.getComponent().setBackground(new Color(110,110,255));
                evt.getComponent().setForeground(new Color(255,255,255));
                evt.getComponent().setFont(new Font("ARIAL",Font.BOLD,14));//arial 15
            }

            child.focusLost={evt->
                evt.getComponent().setBackground(null);
                evt.getComponent().setForeground(new Color(0,0,0));
                evt.getComponent().setFont(new Font("ARIAL",Font.PLAIN,12));//arial 14
            }

            if(child instanceof JTextField){
                child.keyReleased = {evt ->
                    if(evt.getKeyCode() == evt.VK_ESCAPE){component.dispose();}
                    if(evt.getKeyCode() == evt.VK_ENTER ){evt.getComponent().transferFocus();}
                    if(evt.getKeyCode() == evt.VK_UP    ){evt.getComponent().transferFocusBackward();}
                    if(evt.getKeyCode() == evt.VK_DOWN  ){evt.getComponent().transferFocus();}
                }
            }

            if(child instanceof JButton){
                child.keyReleased = {evt ->
                    if(evt.getKeyCode() == evt.VK_ESCAPE){component.dispose();}
                    if(evt.getKeyCode() == evt.VK_ENTER){evt.getComponent().doClick();}
                    if(evt.getKeyCode() == evt.VK_UP    ){evt.getComponent().transferFocusBackward();}
                    if(evt.getKeyCode() == evt.VK_DOWN  ){evt.getComponent().transferFocus();}
                }
            }

            if(child instanceof JComboBox){
                child.focusGained={evt->
                evt.getComponent().showPopup();
                }
                child.keyReleased = {evt ->
                    if(evt.getKeyCode() == evt.VK_ESCAPE){component.dispose();}
                    if(evt.getKeyCode() == evt.VK_ENTER){evt.getComponent().transferFocus();}
                    if(evt.getKeyCode() == evt.VK_BACK_SPACE){evt.getComponent().transferFocusBackward();}
                }
            }

            if(child instanceof JTabbedPane){
                child.keyReleased = {evt ->
                    if(evt.getKeyCode() == evt.VK_ESCAPE){component.dispose();}
                }
            }
        }
    }

    public static panelCatalogo(component){
            for(Component child: component.getContentPane().getComponents()){
            child.focusGained={evt->
                evt.getComponent().setBackground(new Color(110,110,255));
                evt.getComponent().setForeground(new Color(255,255,255));
            }

            child.focusLost={evt->
                evt.getComponent().setBackground(null);
                evt.getComponent().setForeground(new Color(0,0,0));
            }

            if(child instanceof JButton){
                child.keyReleased = {evt ->
                    if(evt.getKeyCode() == evt.VK_ENTER){evt.getComponent().doClick()}
                }
            }
        }
    }

    public static panelAviso(component){
            for(Component child: component.getContentPane().getComponents()){
            child.focusGained={evt->
                evt.getComponent().setBackground(new Color(110,110,255));
                evt.getComponent().setForeground(new Color(255,255,255));
                evt.getComponent().setFont(new Font("ARIAL",Font.BOLD,14));
            }

            child.focusLost={evt->
                evt.getComponent().setBackground(null);
                evt.getComponent().setForeground(new Color(0,0,0));
                evt.getComponent().setFont(new Font("ARIAL",Font.PLAIN,12));
            }

            if(child instanceof JButton){
                child.keyReleased = {evt ->
                    if(evt.getKeyCode() == evt.VK_ESCAPE){component.dispose();}
                    if(evt.getKeyCode() == evt.VK_ENTER){evt.getComponent().doClick()}
                }
            }

            
        }
    }

}