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

import javax.swing.*;
import java.awt.image.*;
import java.awt.*;
import omoikane.sistema.*;

public class FacturaDetalles extends javax.swing.JInternalFrame {

    //TimerBusqueda          timerBusqueda;
    BufferedImage          fondo;
    // omoikane.sistema.NadesicoTableModel modelo;
    
    /** Creates new form CatalogoVentas */
    public FacturaDetalles() {
        initComponents();
               
        //Instrucciones para el funcionamiento del fondo semistransparente
        this.setOpaque(false);
        ((JPanel)this.getContentPane()).setOpaque(false);
        this.getLayeredPane().setOpaque(false);
        this.getRootPane().setOpaque(false);
        this.generarFondo(this);
        this.jxdFecha.setDate(new java.util.Date());
        Herramientas.centrarVentana(this);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        btnCerrar = new javax.swing.JButton();
        lblIdFactura = new javax.swing.JLabel();
        lblAlmacen = new javax.swing.JLabel();
        lblCliente = new javax.swing.JLabel();
        txtAlmacen = new javax.swing.JTextField();
        txtCliente = new javax.swing.JTextField();
        jxdFecha = new org.jdesktop.swingx.JXDatePicker();
        lblFecha = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDetalles = new javax.swing.JTable();
        lblSubtotal = new javax.swing.JLabel();
        lblImpuestos = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        txtSubtotal = new javax.swing.JTextField();
        txtImpuestos = new javax.swing.JTextField();
        txtTotal = new javax.swing.JTextField();
        btnAgregar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        lblExpidio = new javax.swing.JLabel();
        lblCancelo = new javax.swing.JLabel();
        txtExpidio = new javax.swing.JTextField();
        txtCancelo = new javax.swing.JTextField();
        btnImprimir = new javax.swing.JButton();
        txtTicket = new javax.swing.JTextField();
        lblTotal1 = new javax.swing.JLabel();

        lblTitulo.setFont(new java.awt.Font("Arial", 1, 36));
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setText("Factura Detalles");

        btnCerrar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/32x32/back.png"))); // NOI18N
        btnCerrar.setText("<HTML>Regresar a menú [Esc]</HTML>");
        btnCerrar.setFocusable(false);
        btnCerrar.setRequestFocusEnabled(false);
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        lblIdFactura.setBackground(new java.awt.Color(153, 51, 0));
        lblIdFactura.setFont(new java.awt.Font("Arial", 1, 24));
        lblIdFactura.setForeground(new java.awt.Color(255, 255, 255));
        lblIdFactura.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblIdFactura.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        lblIdFactura.setFocusable(false);

        lblAlmacen.setFont(new java.awt.Font("Arial", 1, 14));
        lblAlmacen.setForeground(new java.awt.Color(255, 255, 255));
        lblAlmacen.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAlmacen.setText("Almacen:");
        lblAlmacen.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        lblCliente.setFont(new java.awt.Font("Arial", 1, 14));
        lblCliente.setForeground(new java.awt.Color(255, 255, 255));
        lblCliente.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCliente.setText("Cliente[F1]:");
        lblCliente.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        txtAlmacen.setEditable(false);
        txtAlmacen.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtAlmacen.setFocusable(false);

        txtCliente.setEditable(false);
        txtCliente.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtCliente.setFocusable(false);

        jxdFecha.setEditable(false);
        jxdFecha.setFocusable(false);

        lblFecha.setFont(new java.awt.Font("Arial", 1, 14));
        lblFecha.setForeground(new java.awt.Color(255, 255, 255));
        lblFecha.setText("Fecha:");
        lblFecha.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        tblDetalles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblDetalles.setFocusable(false);
        tblDetalles.setRequestFocusEnabled(false);
        tblDetalles.setVerifyInputWhenFocusTarget(false);
        jScrollPane1.setViewportView(tblDetalles);

        lblSubtotal.setFont(new java.awt.Font("Arial", 1, 14));
        lblSubtotal.setForeground(new java.awt.Color(255, 255, 255));
        lblSubtotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSubtotal.setText("Subtotal:");
        lblSubtotal.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        lblImpuestos.setFont(new java.awt.Font("Arial", 1, 14));
        lblImpuestos.setForeground(new java.awt.Color(255, 255, 255));
        lblImpuestos.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblImpuestos.setText("Impuestos:");
        lblImpuestos.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        lblTotal.setFont(new java.awt.Font("Arial", 1, 14));
        lblTotal.setForeground(new java.awt.Color(255, 255, 255));
        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotal.setText("Total:");
        lblTotal.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        txtSubtotal.setEditable(false);
        txtSubtotal.setFont(new java.awt.Font("Arial", 0, 12));
        txtSubtotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSubtotal.setText("0");
        txtSubtotal.setEnabled(false);
        txtSubtotal.setFocusable(false);

        txtImpuestos.setEditable(false);
        txtImpuestos.setFont(new java.awt.Font("Arial", 0, 12));
        txtImpuestos.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtImpuestos.setText("0");
        txtImpuestos.setEnabled(false);
        txtImpuestos.setFocusable(false);

        txtTotal.setEditable(false);
        txtTotal.setFont(new java.awt.Font("Arial", 0, 12));
        txtTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotal.setText("0");
        txtTotal.setEnabled(false);
        txtTotal.setFocusable(false);

        btnAgregar.setFont(new java.awt.Font("Arial", 0, 14));
        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/32x32/page_add.png"))); // NOI18N
        btnAgregar.setText("Agregar [F5]");
        btnAgregar.setFocusable(false);
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnBorrar.setFont(new java.awt.Font("Arial", 0, 14));
        btnBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/32x32/page_remove.png"))); // NOI18N
        btnBorrar.setText("Borrar ticket [Supr]");
        btnBorrar.setFocusable(false);
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });

        btnCancelar.setFont(new java.awt.Font("Arial", 0, 14));
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/32x32/remove.png"))); // NOI18N
        btnCancelar.setText("Cancelar factura [F9]");
        btnCancelar.setFocusable(false);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        lblExpidio.setFont(new java.awt.Font("Arial", 1, 14));
        lblExpidio.setForeground(new java.awt.Color(255, 255, 255));
        lblExpidio.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblExpidio.setText("U. Expidió:");
        lblExpidio.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        lblCancelo.setFont(new java.awt.Font("Arial", 1, 14));
        lblCancelo.setForeground(new java.awt.Color(255, 255, 255));
        lblCancelo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCancelo.setText("U. Canceló:");
        lblCancelo.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        txtExpidio.setEditable(false);
        txtExpidio.setFont(new java.awt.Font("Arial", 0, 12));
        txtExpidio.setFocusable(false);

        txtCancelo.setEditable(false);
        txtCancelo.setFont(new java.awt.Font("Arial", 0, 12));
        txtCancelo.setFocusable(false);

        btnImprimir.setFont(new java.awt.Font("Arial", 0, 14));
        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/32x32/printer.png"))); // NOI18N
        btnImprimir.setText("Guardar e Imprimir [F8]");
        btnImprimir.setFocusable(false);
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });

        txtTicket.setFont(new java.awt.Font("Arial", 0, 12));
        txtTicket.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTicketKeyReleased(evt);
            }
        });

        lblTotal1.setFont(new java.awt.Font("Arial", 1, 14));
        lblTotal1.setForeground(new java.awt.Color(255, 255, 255));
        lblTotal1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotal1.setText("Ticket:");
        lblTotal1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(127, 127, 127)
                        .addComponent(btnBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnImprimir, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblSubtotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                                            .addComponent(lblImpuestos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                                            .addComponent(lblTotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                                            .addComponent(lblCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                                            .addComponent(lblAlmacen, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)))
                                    .addComponent(lblTotal1, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtAlmacen, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                                        .addComponent(txtCliente, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                                        .addComponent(txtImpuestos, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                                        .addComponent(txtSubtotal, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                                        .addComponent(txtTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE))
                                    .addComponent(txtTicket, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(lblExpidio, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtExpidio, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(lblCancelo, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtCancelo, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                                                .addComponent(lblFecha)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jxdFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblIdFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnCerrar, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)))))
                .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lblTitulo)
                        .addComponent(lblIdFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAlmacen)
                    .addComponent(txtAlmacen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblExpidio)
                    .addComponent(txtExpidio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCliente)
                    .addComponent(txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCancelo)
                    .addComponent(jxdFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFecha)
                    .addComponent(txtCancelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSubtotal)
                            .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblImpuestos)
                            .addComponent(txtImpuestos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTotal)
                            .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTicket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTotal1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAgregar)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        this.dispose();
}//GEN-LAST:event_btnCerrarActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        omoikane.principal.Facturas.agregarVenta();
}//GEN-LAST:event_btnAgregarActionPerformed

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        omoikane.principal.Facturas.borrarVenta();
}//GEN-LAST:event_btnBorrarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        omoikane.principal.Facturas.cancelarFacturaDesdeDetalles();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        omoikane.principal.Facturas.imprimirFactura();
}//GEN-LAST:event_btnImprimirActionPerformed

    private void txtTicketKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTicketKeyReleased
        if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE)
            btnCerrar.doClick();
        else if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_F5)
            btnAgregar.doClick();
        else if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_F8)
            btnImprimir.doClick();
        else if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_F9)
            btnCancelar.doClick();
        else if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE)
            btnBorrar.doClick();
        else if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN) {
            int sigFila = tblDetalles.getSelectedRow()+1;
            if(sigFila < tblDetalles.getRowCount()) {
                this.tblDetalles.setRowSelectionInterval(sigFila, sigFila);
                this.tblDetalles.scrollRectToVisible(tblDetalles.getCellRect(sigFila, 1, true));
            }
        }
        else if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_UP) {
            int antFila = tblDetalles.getSelectedRow()-1;
            if(antFila >= 0) {
                this.tblDetalles.setRowSelectionInterval(antFila, antFila);
                this.tblDetalles.scrollRectToVisible(tblDetalles.getCellRect(antFila, 1, true));
            }
        }
    }//GEN-LAST:event_txtTicketKeyReleased

    @Override
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
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JScrollPane jScrollPane1;
    private org.jdesktop.swingx.JXDatePicker jxdFecha;
    private javax.swing.JLabel lblAlmacen;
    private javax.swing.JLabel lblCancelo;
    private javax.swing.JLabel lblCliente;
    private javax.swing.JLabel lblExpidio;
    private javax.swing.JLabel lblFecha;
    public javax.swing.JLabel lblIdFactura;
    private javax.swing.JLabel lblImpuestos;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotal1;
    private javax.swing.JTable tblDetalles;
    private javax.swing.JTextField txtAlmacen;
    private javax.swing.JTextField txtCancelo;
    private javax.swing.JTextField txtCliente;
    private javax.swing.JTextField txtExpidio;
    private javax.swing.JTextField txtImpuestos;
    private javax.swing.JTextField txtSubtotal;
    private javax.swing.JTextField txtTicket;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables

}
