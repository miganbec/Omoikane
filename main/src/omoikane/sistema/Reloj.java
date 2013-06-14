
 /* Author Phesus        //////////////////////////////
 *  ORC,ACR             /////////////
 *                     /////////////
 *                    /////////////
 *                   /////////////
 * //////////////////////////////                   */

package omoikane.sistema;

import javax.swing.JLabel;
import java.util.Calendar;

  class Reloj extends Thread
  {
        JLabel objetivo;

        Reloj(JLabel obj)
        {
            objetivo = obj;
        }
        public void run()
        {
            Calendar hora = Calendar.getInstance();
            String h,m,s,am;
            while(true)
            {
                hora = Calendar.getInstance();
                h = String.format("%d"  , (int) hora.get(Calendar.HOUR));
                m = String.format("%02d", (int) hora.get(Calendar.MINUTE));
                s = String.format("%02d", (int) hora.get(Calendar.SECOND));
                am= (hora.get(Calendar.AM_PM)==Calendar.AM) ? "AM" : "PM";

                if(Integer.parseInt(h) == 0) { h = "12"; }
                if(objetivo != null)
                {
                    objetivo.setText("<html><head><style type='text/css'>body { font-family: 'Roboto Thin'; font-size: 28px; } .ampm { font-family: 'Roboto Medium'; font-size: 14px; }</style></head>" +
                            "<body>" + h + ":" + m + "<span class='ampm'>" + am + "</span></body></html>");
                }

                try {
                  Thread.sleep(5000);
                } catch (InterruptedException ignored) {
                }
            }
        }

  }