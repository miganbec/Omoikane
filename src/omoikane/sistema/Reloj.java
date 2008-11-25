/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.sistema;

import javax.swing.JLabel;
import java.util.Calendar;

/**
 *
 * @author Octavio
 */
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
                am= (hora.get(Calendar.AM_PM)==Calendar.AM) ? "am" : "pm";

                if(Integer.parseInt(h) == 0) { h = "12"; }
                if(objetivo != null)
                {
                    objetivo.setText("<html><font size=5 face=verdana color=white><b>" + h + ":" + m + " " + am + "</b></font></html>");
                }

                try {
                  Thread.sleep(5000);
                } catch (InterruptedException ignored) {
                }
            }
        }

  }