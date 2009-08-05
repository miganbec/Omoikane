/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.sistema

/**
 *
 * @author SYSTEM
 */
import java.io.*;
import javax.comm.*;
import java.util.*;
import gnu.io.*

/**
 * The implementation of the BarCodeScanner.  It opens the port specified to listen for data through
 * the InputStream.  Events are thrown when data is available in the InputStream.
 * <br>BarCodeScanner wraps these basic SerialPortEvents into a BarCodeScanner listener/event interface,
 * which more closely matches the java.awt.event listener/event model.
 * <p> The settings are currently set for the common barcode scanners found on the market.  To find settings
 * for a specific barcode scanner, see the manufacturer settings of your particular gun and adjust the code
 * accordingly.
 */

public class BarCodeScanner implements SerialPortEventListener {

     // The portname that the barcode scanner is connected to (customize for your machine)
    private final static java.lang.String BARCODE_SCANNER_PORTNAME = "COM2";

    //  The timeout waiting for the serial port to respond
    private final static int TIMEOUT_TIME = 3000;

    //  The baud rate of the bar code scanner
    private final static int BAUD_RATE = 9600;

    // The buffer size to read in data
    private final static int BUFFER_SIZE = 200;

    private int length;
    private SerialPort serialPort;
    private java.io.BufferedInputStream inputStream;
    private java.util.Vector listeners;
    private java.lang.String barCodeString;
    public def barCodeHandler = { throw new Exception("No se registró handler alguno para BarCodeScanner") }
    //private BarCodeThread barcodeThread;

    public def setHandler(claus) {
        barCodeHandler = claus
    }
/**
 * Creates the BarCodeScanner object
 */
public BarCodeScanner(int length) throws Exception
{
    listeners = new java.util.Vector();
    this.length = length;

    try
    {
        // Set up the barcode scanner
        CommPortIdentifier commPort = CommPortIdentifier.getPortIdentifier(BARCODE_SCANNER_PORTNAME);
        serialPort = (SerialPort)commPort.open("Barcode Scanner", TIMEOUT_TIME);
        inputStream = new BufferedInputStream(serialPort.getInputStream());
        serialPort.setInputBufferSize(BUFFER_SIZE);
        serialPort.addEventListener(this);
        serialPort.notifyOnDataAvailable(true);
        // Each barcode scanner will have its own settings - see manufacturer specifics for settings of each gun
        serialPort.setSerialPortParams(BAUD_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        serialPort.setRTS(true);
        println "Puerto configurado!"
    }
    catch (NoSuchPortException e)
    {
        throw e;
    }
    catch (PortInUseException e)
    {
        throw e;
    }
    catch (IOException e)
    {
        throw e;
    }
    catch (java.util.TooManyListenersException e)
    {
        throw e;
    }
    catch (UnsupportedCommOperationException e)
    {
        throw e;
    }
}
/*
public synchronized void addBarCodeListener(BarCodeListener b)
{
    if ( ! listeners.contains(b))
        listeners.add(b);
}
*/
public void close()
{
    try
    {
        inputStream.close();
        serialPort.close();
    }
    catch (IOException e) {}
}
/*
public synchronized void removeBarCodeListener(BarCodeListener b)
{
    listeners.remove(b);
}
*/
/**
 * Captures input from the Serial Port.
 * @param javax.comm.SerialPortEvent the input data
 */
public void serialEvent(SerialPortEvent e)
{
        println "eventillo"
        if (e.getEventType() == SerialPortEvent.DATA_AVAILABLE)
        {
            try
            {
                // Create a buffer to read in the data
                byte[] buffer = new byte[BUFFER_SIZE];

                // If a partial barcode was read, return so the buffer will be filled with the entire barcode information
                // (longer barcode strings will sometimes fire 2 events with partial data in each)
                if (inputStream.available() < length)
                    return;

                // read in the barcode information
                while (inputStream.available() > 0)
                {
                        inputStream.read(buffer);
                        break;
                }

                barCodeString = new String(buffer, 0, length);
                println "devuelto: "+barCodeString
                this.barCodeHandler(barCodeString)

                //BarCodeThread b = new BarCodeThread(listeners, barCodeString);
                //Thread t = new Thread(b);
                //t.start();
            }
            catch (IOException ex)
            {
                System.err.println("Error in barcode");
            }
        }

}
}

