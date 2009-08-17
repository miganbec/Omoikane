/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.sistema

/**
 * @author SYSTEM
 */

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This version of the TwoWaySerialComm example makes use of the
 * SerialPortEventListener to avoid polling.
 *
 */
public class ScanMan implements SerialPortEventListener
{
    public InputStream in7;
    public OutputStream out;
    public CommPort commPort
    def handlerScan = { throw new Exception("No se definió handler alguno para ScanMan (Manejador de escáner serial)") }

    public ScanMan()
    {
        super();
    }

    public setHandler(claus) {
        this.handlerScan = claus
    }
    public void finalize() { disconnect() }
    public void disconnect() {
       
        commPort.close()
    }

    void connect ( String portName, baudRate ) throws Exception
    {
        CommPortIdentifier portIdentifier
        try {
            portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

        } catch(Exception ex) {
            throw new Exception("Error al conectar con el escáner de códigos: ${ex.getClass()}", ex)
        }
        if ( portIdentifier.isCurrentlyOwned() )
        {
            throw new Exception("Error: Puerto de escáner de códigos de barra ocupado");
        }
        else
        {
            commPort = portIdentifier.open(this.getClass().getName(),2000);

            if ( commPort instanceof SerialPort )
            {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(baudRate,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                //serialPort.setInputBufferSize(500);

                serialPort.setRTS(true);
                in7 = serialPort.getInputStream();
                out = serialPort.getOutputStream();

                //(new Thread(new SerialWriter(out))).start();


                serialPort.addEventListener(this);
                serialPort.notifyOnDataAvailable(true);

            }
            else
            {
                throw new Exception("Error: Sólo se aceptan puertos seriales para el escáner de códigos");
            }
        }
    }

    /**
     * Handles the input coming from the serial port. A new line character
     * is treated as the end of a block in this example.
     */

        private byte[] buffer = new byte[1024];

        public void serialEvent(SerialPortEvent arg0) {
            println "en scanman evento"
            int data;

            try
            {
                println "comienza try de recopilar letras de escáner"
                int len = 0;
                while ( ( data = in7.read()) > -1 )
                {
                    println "comienza while de recopilar letras de escáner"
                    //println "a "+data
                    if ( data == (13 as char) ) {
                        println "salto de líneo, termina la recolección de letras del escáner"
                        break;
                    }
                    println "se va a agregar una letra a la recolección"
                    buffer[len++] = (byte) data;
                    println "se agregó una letra a la recolección"
                }
                println "se llamará al handler de la cadena completa del escáner"
                handlerScan(new String(buffer,0,len))
                println "terminó la llamada al handler"
            }
            catch ( Exception e )
            {
                println "se econtró una excepción"
                e.printStackTrace();
                Dialogos.error("Error al leer desde el escáner de códigos de barras", e)
                //System.exit(-1);
            }
        }
}