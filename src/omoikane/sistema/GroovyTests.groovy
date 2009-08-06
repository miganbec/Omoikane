/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.sistema

/**
 *
 * @author SYSTEM
 */
class GroovyTests {
	public static void comManScannerTest() {
            //static def miniDriver = [port:"COM2", baud:9600, bits: "8", stopBits:"1", parity:"None", stopChar:"3"]
            //def comPort = new ComManScanner(null, miniDriver)
            //Thread.start {
            /*
                for(int i = 0; i < 20; i++) {
                    println "sólo esperando..."+comPort.dump()
                    sleep(2500)
                }
            */
            //}
        def scanMan = new ScanMan()
        scanMan.connect("COM1")
        scanMan.setHandler = { println "Valor recibido: " + it }

        }
}

