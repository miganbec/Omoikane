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
import javax.swing.table.*;

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
        txtCliente1 = new javax.swing.JTextField();

        setFocusable(false);

        lblTitulo.setFont(new java.awt.Font("Arial", 1, 36));
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setText("Factura Detalles");

        btnCerrar.setFont(new java.awt.Font("Arial", 0, 14));
        btnCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/32x32/back.png"))); // NOI18N
        btnCerrar.setText("<HTML>Regresar a menú [Esc]</HTML>");
        btnCerrar.setNextFocusableComponent(txtCliente);
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

        txtAlmacen.setBackground(new java.awt.Color(55, 55, 255));
        txtAlmacen.setEditable(false);
        txtAlmacen.setFont(new java.awt.Font("Arial", 0, 12));
        txtAlmacen.setForeground(new java.awt.Color(255, 255, 255));
        txtAlmacen.setBorder(null);
        txtAlmacen.setFocusable(false);

        txtCliente.setEditable(false);
        txtCliente.setFont(new java.awt.Font("Arial", 0, 12));
        txtCliente.setNextFocusableComponent(txtTicket);

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
                "Venta", "Articulo", "Descripcion", "Precio", "Cantidad", "Total"
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

        txtSubtotal.setBackground(new java.awt.Color(55, 55, 255));
        txtSubtotal.setEditable(false);
        txtSubtotal.setFont(new java.awt.Font("Arial", 0, 12));
        txtSubtotal.setForeground(new java.awt.Color(255, 255, 255));
        txtSubtotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSubtotal.setText("0");
        txtSubtotal.setBorder(null);
        txtSubtotal.setFocusable(false);

        txtImpuestos.setBackground(new java.awt.Color(55, 55, 255));
        txtImpuestos.setEditable(false);
        txtImpuestos.setFont(new java.awt.Font("Arial", 0, 12));
        txtImpuestos.setForeground(new java.awt.Color(255, 255, 255));
        txtImpuestos.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtImpuestos.setText("0");
        txtImpuestos.setBorder(null);
        txtImpuestos.setFocusable(false);

        txtTotal.setBackground(new java.awt.Color(55, 55, 255));
        txtTotal.setEditable(false);
        txtTotal.setFont(new java.awt.Font("Arial", 0, 12));
        txtTotal.setForeground(new java.awt.Color(255, 255, 255));
        txtTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotal.setText("0");
        txtTotal.setBorder(null);
        txtTotal.setFocusable(false);

        btnAgregar.setFont(new java.awt.Font("Arial", 0, 14));
        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/32x32/page_add.png"))); // NOI18N
        btnAgregar.setText("Agregar [F5]");
        btnAgregar.setEnabled(false);
        btnAgregar.setFocusable(false);
        btnAgregar.setNextFocusableComponent(btnBorrar);

        btnBorrar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/32x32/page_remove.png"))); // NOI18N
        btnBorrar.setText("Borrar Venta");
        btnBorrar.setEnabled(false);
        btnBorrar.setNextFocusableComponent(btnCancelar);

        btnCancelar.setFont(new java.awt.Font("Arial", 0, 14));
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/32x32/remove.png"))); // NOI18N
        btnCancelar.setText("Cancelar factura [F4]");
        btnCancelar.setNextFocusableComponent(btnImprimir);
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

        txtExpidio.setBackground(new java.awt.Color(55, 55, 255));
        txtExpidio.setEditable(false);
        txtExpidio.setFont(new java.awt.Font("Arial", 0, 12));
        txtExpidio.setForeground(new java.awt.Color(255, 255, 255));
        txtExpidio.setBorder(null);
        txtExpidio.setFocusable(false);

        txtCancelo.setBackground(new java.awt.Color(55, 55, 255));
        txtCancelo.setEditable(false);
        txtCancelo.setFont(new java.awt.Font("Arial", 0, 12));
        txtCancelo.setForeground(new java.awt.Color(255, 255, 255));
        txtCancelo.setBorder(null);
        txtCancelo.setFocusable(false);

        btnImprimir.setFont(new java.awt.Font("Arial", 0, 14));
        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/32x32/printer.png"))); // NOI18N
        btnImprimir.setText("Guardar e Imprimir [F8]");
        btnImprimir.setNextFocusableComponent(btnCerrar);
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });

        txtTicket.setEditable(false);
        txtTicket.setFont(new java.awt.Font("Arial", 0, 12));
        txtTicket.setNextFocusableComponent(btnAgregar);

        lblTotal1.setFont(new java.awt.Font("Arial", 1, 14));
        lblTotal1.setForeground(new java.awt.Color(255, 255, 255));
        lblTotal1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotal1.setText("ID Venta:");
        lblTotal1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        txtCliente1.setBackground(new java.awt.Color(55, 55, 255));
        txtCliente1.setEditable(false);
        txtCliente1.setFont(new java.awt.Font("Arial", 0, 12));
        txtCliente1.setForeground(new java.awt.Color(255, 255, 255));
        txtCliente1.setBorder(null);
        txtCliente1.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblSubtotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                                            .addComponent(lblImpuestos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                                            .addComponent(lblTotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                                            .addComponent(lblCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblAlmacen, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)))
                                    .addComponent(lblTotal1, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(txtTicket, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txtTotal, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txtImpuestos, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(txtAlmacen, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblExpidio, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(14, 14, 14)
                                                .addComponent(txtCliente1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblCancelo, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtCancelo, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                                            .addComponent(txtExpidio, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jxdFecha, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(57, 57, 57)
                                                .addComponent(lblFecha))))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblIdFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnCerrar, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(btnBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnImprimir, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblAlmacen)
                            .addComponent(txtAlmacen, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblExpidio)
                            .addComponent(txtExpidio, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lblFecha))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCliente1, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                        .addComponent(txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtCancelo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblCliente)
                        .addComponent(jxdFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblCancelo)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSubtotal)
                            .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblImpuestos)
                            .addComponent(txtImpuestos, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTotal)
                            .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTotal1))
                        .addGap(44, 44, 44)
                        .addComponent(btnAgregar))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        this.dispose();
}//GEN-LAST:event_btnCerrarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        if(JOptionPane.showConfirmDialog(null, "¿Realmente desea cancelar esta factura : ?", "seguro...", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
        omoikane.principal.Facturas.cancelarFacturaDesdeDetalles(this);}
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnImprimirActionPerformed

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
    private javax.swing.JTextField txtCliente1;
    private javax.swing.JTextField txtExpidio;
    private javax.swing.JTextField txtImpuestos;
    private javax.swing.JTextField txtSubtotal;
    private javax.swing.JTextField txtTicket;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables

    public void setEditable(boolean editable)
    {
        this.txtCliente.setEditable(editable);
        this.txtTicket.setEditable(editable);
        this.btnAgregar.setEnabled(editable);
        this.btnBorrar.setEnabled(editable);
        this.btnCancelar.setEnabled(!editable);

    }

    public JTextField   getCampoID()                {return txtCliente;}
    public void setTxtClienteDes(String ID){txtCliente1.setText(ID);}
    public void setCliente(String ID){txtCliente.setText(ID);}
}
