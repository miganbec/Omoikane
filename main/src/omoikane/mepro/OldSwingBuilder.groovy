/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.mepro

import groovy.swing.SwingBuilder
import java.awt.BorderLayout
import javax.swing.*
import groovy.inspect.swingui.*;

/**
 *
 * @author Octavio
 */
public class OldSwingBuilder {

    /**
     * @param args the command line arguments
     */
    OldSwingBuilder() {
        // TODO code application logic here
        def swing = new SwingBuilder()

        def wnd2 = new FramePrincipal()
        wnd2.setVisible(true)
        wnd2.add(
            swing.panel(preferredSize:[300,300]) {
                gridLayout()
                label("eti1")
                button("btn1")
                label("eti2")
                button("btn2")
            }
        )
        wnd2.pack()

        def pnl
        def frame =
          swing.frame(title:'Phesus MePro', preferredSize:[300,300], defaultCloseOperation:JFrame.EXIT_ON_CLOSE, pack:true, show:true) {
            tableLayout {
                tr {
                    td(colspan:2) { label(preferredSize:[292,100], text:"Phesus MePro", font:["Tahoma", 1, 36]) }
                }
                tr {
                  td { label(text:"Scripts")  }
                  td { label(text:"Acciones") }
                }
                tr {
                  td(colspan:2) {
                      pnl = panel() {
                      }
                  }
                }
                tr {
                  td { button(text:"boton2", actionPerformed: {println "click"}) }
                  td {
                      panel(id:"nPnl") {
                          gridLayout()
                          button(text:"boton")
                          button(text:"boton")
                      }
                  }
                }
                tr {
                  td() { panel() {
                          gridLayout()
                          button(text:"+")
                         }
                  }
                  td() {}
                }
            }
          }
          //swing.nPnl.add(swing.button(text:"btn2"))
          pnl.add(swing.button(text:"btn3"))
          //frame.pack()

          //def btnAqui
          //wnd2.add(btnAqui = swing.button(text:"aquí", actionPerformed: { println "reclick" }))
          //btnAqui.text = "lalalala"
          //btnAqui.actionPerformed = { println "jijijiji" }


    }

}


