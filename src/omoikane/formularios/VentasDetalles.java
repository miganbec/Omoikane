/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CatalogoVentas.java
 *
 * Created on 10/12/2008, 11:56:06 PM
 */

package omoikane.formularios;

import java.sql.*;
import java.util.*;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.*;
import java.awt.event.*;
import omoikane.sistema.*;
import javax.swing.event.*;
import java.text.*;
import omoikane.principal.*;




/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class VentasDetalles extends javax.swing.JInternalFrame {

    //TimerBusqueda          timerBusqueda;
    BufferedImage          fondo;
    public int             IDSeleccionado;
    public String          codigoSeleccionado;
    public String          txtQuery;
    public String          letra;
    public Boolean         hacer = true;
    omoikane.sistema.NadesicoTableModel modelo;

    


    /** Creates new form CatalogoVentas */
    public VentasDetalles() {
        initComponents();
               
        //Instrucciones para el funcionamiento del fondo semistransparente
        this.setOpaque(false);
        ((JPanel)this.getContentPane()).setOpaque(false);
        this.getLayeredPane().setOpaque(false);
        this.getRootPane().setOpaque(false);
        this.generarFondo(this);
        this.txtFecha.setDate(new java.util.Date());
        Herramientas.centrarVentana(this);
    }

    public void setQueryTable(String query) {
        txtQuery = query;
        modelo.setQuery(query);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        btnCerrar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtCliente = new javax.swing.JTextField();
        txtSubtotal = new javax.swing.JTextField();
        txtAlmacen = new javax.swing.JTextField();
        txtDescuento = new javax.swing.JTextField();
        txtImpuesto = new javax.swing.JTextField();
        txtTotal = new javax.swing.JTextField();
        txtFecha = new org.jdesktop.swingx.JXDatePicker();
        chkFacturado = new javax.swing.JCheckBox();
        btnFacturado = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        aFacturar = new javax.swing.JTextField();
        txtIDVenta = new javax.swing.JLabel();

        jLabel2.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Ventas Detalles");

        btnCerrar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/32x32/back.png"))); // NOI18N
        btnCerrar.setText("<HTML>Regresar a menú [Esc]</HTML>");
        btnCerrar.setRequestFocusEnabled(false);
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        btnImprimir.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/32x32/printer.png"))); // NOI18N
        btnImprimir.setText("<html><center>Imprimir [F8]</center></html>");
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });

        jTable1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código artículo", "Descripción", "Costo", "Cantidad", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setFocusable(false);
        jTable1.setRowHeight(20);
        jTable1.setShowHorizontalLines(false);
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("<html>Cliente a <br>facturar [F1]:<html>");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Almacen :");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Total :");

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Subtotal :");

        jLabel7.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Impuesto :");

        jLabel8.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Descuento :");

        jLabel9.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Fecha :");

        txtCliente.setEditable(false);
        txtCliente.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtCliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtSubtotal.setEditable(false);
        txtSubtotal.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtSubtotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtAlmacen.setEditable(false);
        txtAlmacen.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtAlmacen.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtDescuento.setEditable(false);
        txtDescuento.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDescuento.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtImpuesto.setEditable(false);
        txtImpuesto.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtImpuesto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtImpuesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtImpuestoActionPerformed(evt);
            }
        });

        txtTotal.setEditable(false);
        txtTotal.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        txtTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtFecha.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtFecha.setFormats(java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM));
        txtFecha.setEditable(false);
        txtFecha.setFocusable(false);

        chkFacturado.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        chkFacturado.setForeground(new java.awt.Color(255, 255, 255));
        chkFacturado.setText("Facturado");
        chkFacturado.setEnabled(false);
        chkFacturado.setFocusable(false);

        btnFacturado.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnFacturado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/32x32/notes_edit.png"))); // NOI18N
        btnFacturado.setText("Facturar [F6]");
        btnFacturado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFacturadoActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Cliente :");

        aFacturar.setEditable(false);
        aFacturar.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        aFacturar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        aFacturar.setFocusable(true);

        txtIDVenta.setBackground(new java.awt.Color(153, 51, 0));
        txtIDVenta.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        txtIDVenta.setForeground(new java.awt.Color(255, 255, 255));
        txtIDVenta.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txtIDVenta.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        txtIDVenta.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setVerticalGroup(layout.createSequentialGroup()
        	.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
        	    .addComponent(jLabel1, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
        	    .addComponent(aFacturar, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE))
        	.addGap(35)
        	.addGroup(layout.createParallelGroup()
        	    .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
        	        .addComponent(btnCerrar, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
        	        .addGap(12))
        	    .addComponent(txtIDVenta, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
        	    .addComponent(jLabel2, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
        	.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
        	.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
        	    .addComponent(txtFecha, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
        	    .addComponent(jLabel3, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
        	    .addComponent(txtAlmacen, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
        	    .addComponent(jLabel9, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
        	    .addComponent(jLabel10, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
        	    .addComponent(txtCliente, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
        	.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
        	.addGroup(layout.createParallelGroup()
        	    .addGroup(layout.createSequentialGroup()
        	        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
        	            .addComponent(btnFacturado, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
        	            .addComponent(chkFacturado, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
        	        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
        	        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
        	            .addComponent(txtSubtotal, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
        	            .addComponent(jLabel6, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
        	        .addGap(18)
        	        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
        	            .addComponent(txtDescuento, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
        	            .addComponent(jLabel8, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
        	        .addGap(18)
        	        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
        	            .addComponent(txtImpuesto, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
        	            .addComponent(jLabel7, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
        	        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
        	        .addGroup(layout.createParallelGroup()
        	            .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
        	                .addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
        	                .addGap(30))
        	            .addComponent(txtTotal, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE))
        	        .addGap(85))
        	    .addComponent(jScrollPane1, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 295, GroupLayout.PREFERRED_SIZE))
        	.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
        	.addComponent(btnImprimir, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
        	.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setHorizontalGroup(layout.createSequentialGroup()
        	.addGroup(layout.createParallelGroup()
        	    .addComponent(jLabel1, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
        	    .addComponent(aFacturar, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE))
        	.addGap(13)
        	.addGroup(layout.createParallelGroup()
        	    .addGroup(layout.createSequentialGroup()
        	        .addGroup(layout.createParallelGroup()
        	            .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
        	                .addComponent(jLabel10, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
        	                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
        	                .addComponent(txtCliente, GroupLayout.PREFERRED_SIZE, 230, GroupLayout.PREFERRED_SIZE))
        	            .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
        	                .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 271, GroupLayout.PREFERRED_SIZE)
        	                .addGap(29)))
        	        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
        	        .addGroup(layout.createParallelGroup()
        	            .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
        	                .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
        	                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
        	                .addComponent(txtAlmacen, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE))
        	            .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
        	                .addComponent(txtIDVenta, GroupLayout.PREFERRED_SIZE, 202, GroupLayout.PREFERRED_SIZE)
        	                .addGap(83)))
        	        .addGroup(layout.createParallelGroup()
        	            .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
        	                .addComponent(jLabel9, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
        	                .addGap(0, 256, Short.MAX_VALUE))
        	            .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
        	                .addPreferredGap(jLabel9, btnCerrar, LayoutStyle.ComponentPlacement.INDENT)
        	                .addComponent(btnCerrar, GroupLayout.PREFERRED_SIZE, 295, GroupLayout.PREFERRED_SIZE)
        	                .addGap(0, 0, Short.MAX_VALUE)))
        	        .addGap(28))
        	    .addGroup(layout.createSequentialGroup()
        	        .addGroup(layout.createParallelGroup()
        	            .addGroup(layout.createSequentialGroup()
        	                .addPreferredGap(jLabel8, jLabel4, LayoutStyle.ComponentPlacement.INDENT)
        	                .addGroup(layout.createParallelGroup()
        	                    .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
        	                        .addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
        	                        .addGap(6))
        	                    .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
        	                        .addComponent(jLabel7, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
        	                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))
        	                    .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
        	                        .addPreferredGap(jLabel4, jLabel6, LayoutStyle.ComponentPlacement.INDENT)
        	                        .addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))))
        	            .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
        	                .addComponent(jLabel8, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
        	                .addGap(6))
        	            .addComponent(chkFacturado, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
        	        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
        	        .addGroup(layout.createParallelGroup()
        	            .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
        	                .addGroup(layout.createParallelGroup()
        	                    .addComponent(txtDescuento, GroupLayout.Alignment.LEADING, 0, 239, Short.MAX_VALUE)
        	                    .addGroup(layout.createSequentialGroup()
        	                        .addComponent(txtTotal, GroupLayout.PREFERRED_SIZE, 236, GroupLayout.PREFERRED_SIZE)
        	                        .addGap(0, 0, Short.MAX_VALUE))
        	                    .addGroup(layout.createSequentialGroup()
        	                        .addComponent(txtImpuesto, GroupLayout.PREFERRED_SIZE, 236, GroupLayout.PREFERRED_SIZE)
        	                        .addGap(0, 0, Short.MAX_VALUE))
        	                    .addComponent(btnFacturado, GroupLayout.Alignment.LEADING, 0, 239, Short.MAX_VALUE))
        	                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
        	                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 579, GroupLayout.PREFERRED_SIZE))
        	            .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
        	                .addPreferredGap(txtTotal, txtSubtotal, LayoutStyle.ComponentPlacement.INDENT)
        	                .addGroup(layout.createParallelGroup()
        	                    .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
        	                        .addComponent(txtSubtotal, GroupLayout.PREFERRED_SIZE, 227, GroupLayout.PREFERRED_SIZE)
        	                        .addGap(315))
        	                    .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
        	                        .addGap(109)
        	                        .addComponent(btnImprimir, GroupLayout.PREFERRED_SIZE, 433, GroupLayout.PREFERRED_SIZE)))
        	                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
        	                .addComponent(txtFecha, GroupLayout.PREFERRED_SIZE, 238, GroupLayout.PREFERRED_SIZE)
        	                .addGap(0, 28, Short.MAX_VALUE)))))
        	.addContainerGap());

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        // TODO add your handling code here:
        omoikane.principal.Ventas.reimprimirTicket(IDSeleccionado);
}//GEN-LAST:event_btnImprimirActionPerformed

    private void btnFacturadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFacturadoActionPerformed
        // TODO add your handling code here:
        omoikane.principal.Ventas.lanzarImprimirFactura(this,letra);
        if(hacer){
        this.chkFacturado.setSelected(true);
        omoikane.principal.Ventas.actualizar(this.txtIDVenta.getText(),this.aFacturar.getText());}

}//GEN-LAST:event_btnFacturadoActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        // TODO add your handling code here:
        this.dispose();
}//GEN-LAST:event_btnCerrarActionPerformed

    private void txtImpuestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtImpuestoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtImpuestoActionPerformed

   public javax.swing.JTextField getFactura() { return this.aFacturar;    }
   public SimpleDateFormat sdf          = new SimpleDateFormat("yyyy-MM-dd");
   public void setFacturar    (String  val) { aFacturar.setText(val);this.chkFacturado.setSelected(true);this.aFacturar.setVisible(false);this.jLabel1.setVisible(false);this.hacer=false;}
   public void setIDVenta     (String  val) { txtIDVenta.setText(val);  }
   public void setCliente     (String  val) { txtCliente.setText(val);  }
   public void setDescuento   (String  val) { txtDescuento.setText(val);}
   public void setImpuesto    (String  val) { txtImpuesto.setText(val); }
   public void setSubtotal    (String  val) { txtSubtotal.setText(val); }
   public void setTotal       (String  val) { txtTotal.setText(val);    }
   public void setAlmacen     (String  val) { txtAlmacen.setText(val);  }
   public void setFecha       (String  val) { try {txtFecha.setDate(sdf.parse(val)); } catch(Exception e) { omoikane.sistema.Dialogos.lanzarDialogoError(null, "Error en el registro: Fecha inválida", omoikane.sistema.Herramientas.getStackTraceString(e)); } }
   public String getCliente() { return aFacturar.getText();}

   
    public void setTablaPrincipal(java.util.List val) {
        DefaultTableModel modelo = ((DefaultTableModel)this.jTable1.getModel());
        for(int i = 0; i < val.size(); i++)
        {
            modelo.addRow(((java.util.ArrayList) val.get(i)).toArray());
        }
     }

    public void paintComponent(Graphics g)
    {
      Graphics2D g2d = (Graphics2D) g;
      g2d.drawImage(fondo, 0, 0, null);

    }

    public void generarFondo(Component componente)
    {
      Rectangle areaDibujo = this.getBounds();
      BufferedImage tmp;
      GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

      tmp = gc.createCompatibleImage(areaDibujo.width, areaDibujo.height,BufferedImage.TRANSLUCENT);
      Graphics2D g2d = (Graphics2D) tmp.getGraphics();
      g2d.setColor(new Color(55,55,255,165));
      g2d.fillRect(0,0,areaDibujo.width,areaDibujo.height);
      fondo = tmp;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField aFacturar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnFacturado;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JCheckBox chkFacturado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtAlmacen;
    private javax.swing.JTextField txtCliente;
    private javax.swing.JTextField txtDescuento;
    private org.jdesktop.swingx.JXDatePicker txtFecha;
    public javax.swing.JLabel txtIDVenta;
    private javax.swing.JTextField txtImpuesto;
    private javax.swing.JTextField txtSubtotal;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables

}
