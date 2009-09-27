/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.sistema;
import java.io.IOException;

/**
 *
 * @author Rubén
 */
public class Apagado {
        public static void apagar() {
            if(isWindows())
                ejecComando("shutdown.exe -s -t 01");
            else if(isUnix()) {
               ejecComando("poweroff");
               ejecComando("shutdown -h now");
            }
        }

        public static void ejecComando( String comando ){
            try {
                    Runtime.getRuntime().exec(comando);
            } catch (IOException ex) {
                    System.out.println(ex.getMessage());
            }
        }

	public static boolean isWindows(){
		String os = System.getProperty("os.name").toLowerCase();
		//Windows
		return (os.indexOf( "win" ) >= 0);
	}

	public static boolean isUnix(){
		String os = System.getProperty("os.name").toLowerCase();
		//Linux or Unix
		return (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0);
	}
}
