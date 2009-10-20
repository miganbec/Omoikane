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
import java.text.*;
import java.util.Calendar;

public class CatalogoVentas extends javax.swing.JInternalFrame {

    //TimerBusqueda          timerBusqueda;
    TimerBusqueda          timerBusqueda;
    public boolean modal = false;
    public int IDAlmacen = omoikane.principal.Principal.IDAlmacen;
    BufferedImage          fondo;
    public int             IDSeleccionado;
    public String          codigoSeleccionado;
    public String          txtQuery;
    VentasTableModel modelo;

    class TimerBusqueda extends Thread
    {
        CatalogoVentas ca;
        boolean busquedaActiva = true;

        TimerBusqueda(CatalogoVentas ca) { this.ca = ca; }
        public void run()
        {
            synchronized(this)
            {
                busquedaActiva = true;
                try { this.wait(500); } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error en el timer de búsqueda automática", Herramientas.getStackTraceString(e)); }
                if(busquedaActiva && ca.modelo != null) { ca.buscar(); }
            }
        }
        void cancelar()
        {
            busquedaActiva = false;
            try { this.notify(); } catch(Exception e) {}
        }
    }
    /** Creates new form CatalogoVentas */
    public CatalogoVentas() {
        initComponents();
        Calendar calendario = Calendar.getInstance();
        txtFechaHasta.setDate(calendario.getTime());
        calendario.add(Calendar.DAY_OF_MONTH, -2);
        txtFechaDesde.setDate(calendario.getTime());

        String[]  columnas = { "Fecha","Venta", "Caja", "Almacen", "Cliente","Total"};
        ArrayList cols     = new ArrayList<String>(Arrays.asList(columnas));
        Class[]   clases   = {String.class, Integer.class, Integer.class, String.class, String.class, Double.class};
        ArrayList cls      = new ArrayList<Class>(Arrays.asList(clases));

        VentasTableModel modeloTabla = new VentasTableModel(cols, cls);
        //jTable1.enableInputMethods(false);
        this.modelo = modeloTabla;
        this.jTable1.setModel(modeloTabla);

        String whereFecha = "" ;
        String fechaDesde     = "";
        String fechaHasta     = "";
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
        calendario.setTime(this.txtFechaHasta.getDate());
        calendario.add(Calendar.DAY_OF_MONTH, 1);

        if(this.txtFechaDesde.getDate() != null || this.txtFechaHasta.getDate() != null)
        {
            try {
                fechaDesde = sdf.format(this.txtFechaDesde.getDate());
                fechaHasta = sdf.format(calendario.getTime());
                whereFecha = " AND ventas.fecha_hora >= '"+fechaDesde+"' AND ventas.fecha_hora <= '"+fechaHasta+"'  ";

            } catch(Exception e) { omoikane.sistema.Dialogos.lanzarDialogoError(null, "Error en el registro: Fecha inválida", omoikane.sistema.Herramientas.getStackTraceString(e)); }
        }

        setQueryTable("SELECT ventas.id_venta,ventas.fecha_hora,ventas.id_venta,ventas.id_caja,almacenes.descripcion,clientes.razonSocial,ventas.total FROM ventas,clientes,almacenes WHERE ventas.id_almacen="+IDAlmacen+" AND ventas.id_cliente=clientes.id_cliente"+whereFecha);

        //Instrucciones para el funcionamiento del fondo semistransparente
        this.setOpaque(false);
        ((JPanel)this.getContentPane()).setOpaque(false);
        this.getLayeredPane().setOpaque(false);
        this.getRootPane().setOpaque(false);
        this.generarFondo(this);

        Herramientas.centrarVentana(this);
        this.btnAceptar.setVisible(false);

        //Instrucciones para el funcionamiento de las teclas de navegación
        Set newKeys = new HashSet(getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));
        newKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
        setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, newKeys);

        newKeys = new HashSet(getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        newKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newKeys);

    }

    public void setModoDialogo()
    {
        modal=true;
        this.btnAceptar.setVisible(true);
        Action aceptar = new AbstractAction() { public void actionPerformed(ActionEvent e) {
            ((CatalogoVentas)e.getSource()).btnAceptar.doClick();
        } };
        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "aceptar");
        getActionMap().put("aceptar"                 , aceptar  );
    }

    public void setQueryTable(String query) {
        txtQuery = query;

        modelo.setQuery(query);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        txtBusqueda = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnFiltrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnCerrar = new javax.swing.JButton();
        txtFechaDesde = new org.jdesktop.swingx.JXDatePicker();
        txtFechaHasta = new org.jdesktop.swingx.JXDatePicker();
        chkCajero = new javax.swing.JCheckBox();
        chkCliente = new javax.swing.JCheckBox();
        chkCaja = new javax.swing.JCheckBox();
        btnAceptar = new javax.swing.JButton();
        btnDetalles = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        btnVentasXLinea = new javax.swing.JButton();

        setTitle("Catálogo de Ventas");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Catálogo de Ventas");

        txtBusqueda.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBusquedaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                salir(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBusquedaKeyTyped(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Buscar [F3]:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Desde:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Hasta:");

        btnFiltrar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnFiltrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/16x16/search.png"))); // NOI18N
        btnFiltrar.setText("Filtrar [F1]");
        btnFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrarActionPerformed(evt);
            }
        });
        btnFiltrar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                salir(evt);
            }
        });

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.setFocusable(false);
        jTable1.setRowHeight(20);
        jScrollPane1.setViewportView(jTable1);

        btnCerrar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/32x32/back.png"))); // NOI18N
        btnCerrar.setText("<HTML>Regresar a menú [Esc]</HTML>");
        btnCerrar.setRequestFocusEnabled(false);
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });
        btnCerrar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                salir(evt);
            }
        });

        txtFechaDesde.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtFechaDesde.setFormats(java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM));
        txtFechaDesde.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                salir(evt);
            }
        });

        txtFechaHasta.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtFechaHasta.setFormats(java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM));
        txtFechaHasta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                salir(evt);
            }
        });

        chkCajero.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        chkCajero.setText("Cajero [F5]");
        chkCajero.setFocusable(false);

        chkCliente.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        chkCliente.setSelected(true);
        chkCliente.setText("Id Venta [F6]");
        chkCliente.setFocusable(false);

        chkCaja.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        chkCaja.setText("Caja [F7]");
        chkCaja.setFocusable(false);

        btnAceptar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnAceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/32x32/accept.png"))); // NOI18N
        btnAceptar.setText("Aceptar [Enter]");
        btnAceptar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                salir(evt);
            }
        });

        btnDetalles.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnDetalles.setIcon(new javax.swing.ImageIcon(getClass().getResource("/32x32/page_search.png"))); // NOI18N
        btnDetalles.setText("Detalles [F4]");
        btnDetalles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetallesActionPerformed(evt);
            }
        });
        btnDetalles.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                salir(evt);
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
        btnImprimir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                salir(evt);
            }
        });

        btnVentasXLinea.setIcon(new javax.swing.ImageIcon(getClass().getResource("/32x32/printer.png"))); // NOI18N
        btnVentasXLinea.setText("<html><center>Ventas x Línea</center></html>");
        btnVentasXLinea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentasXLineaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 924, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDetalles, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 282, Short.MAX_VALUE)
                        .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(10, 10, 10)
                                .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(chkCajero)
                                .addGap(34, 34, 34))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtFechaDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
<<<<<<< .mine
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtFechaHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(chkCliente)
                                        .addGap(49, 49, 49)
                                        .addComponent(chkCaja)))))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(chkCajero)
                        .addGap(317, 317, 317))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnAceptar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDetalles)
                        .addGap(220, 220, 220)
                        .addComponent(btnVentasXLinea, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnImprimir, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                        .addContainerGap())))
=======
                                .addComponent(txtFechaHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(chkCliente)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                                .addComponent(chkCaja)
                                .addGap(301, 301, 301)))))
                .addContainerGap())
>>>>>>> .r172
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtFechaDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                        .addComponent(txtBusqueda)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtFechaHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                        .addComponent(btnFiltrar)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
<<<<<<< .mine
                    .addComponent(btnDetalles)
                    .addComponent(btnAceptar)
                    .addComponent(btnImprimir)
                    .addComponent(btnVentasXLinea))
=======
                    .addComponent(chkCajero)
                    .addComponent(chkCaja)
                    .addComponent(chkCliente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnDetalles, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
>>>>>>> .r172
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        // TODO add your handling code here:
        buscar();
        
}//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        // TODO add your handling code here:
        VentasTableModel tabModelo = this.modelo;
        this.modelo = null;
        tabModelo.destroy();
        this.dispose();
        if(!modal){
        ((javax.swing.JInternalFrame)((omoikane.principal.MenuPrincipal)omoikane.principal.Principal.getMenuPrincipal()).getMenuPrincipal()).requestFocusInWindow();
        }
}//GEN-LAST:event_btnCerrarActionPerformed

    private void btnDetallesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetallesActionPerformed
        // TODO add your handling code here:
        int sel = this.jTable1.getSelectedRow();
        int id;

        if(sel == -1)
        {
            Dialogos.lanzarAlerta("Ningúna fila ha sido seleccionada.");
        } else {
            //++++++++++++++++++++++++++++
            id = (Integer)this.jTable1.getValueAt(sel, -1);
            //Lanzar ventana y agregarle un listener
            omoikane.principal.Ventas.lanzarDetalles(id);
        }


}//GEN-LAST:event_btnDetallesActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        // TODO add your handling code here:
        omoikane.principal.Ventas.lanzarImprimir(txtQuery);
       
}//GEN-LAST:event_btnImprimirActionPerformed

    private void txtBusquedaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaKeyTyped
        // TODO add your handling code here:
        preBuscar();
    }//GEN-LAST:event_txtBusquedaKeyTyped

    private void txtBusquedaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == evt.VK_ENTER) {
            this.txtBusqueda.selectAll();
        }
        if(evt.getKeyCode() == evt.VK_DOWN)
        {
            int sigFila = jTable1.getSelectedRow()+1;
            if(sigFila < jTable1.getRowCount())
            {
                this.jTable1.setRowSelectionInterval(sigFila, sigFila);
                this.jTable1.scrollRectToVisible(jTable1.getCellRect(sigFila, 1, true));
            }
        }
        if(evt.getKeyCode() == evt.VK_UP)
        {
            int antFila = jTable1.getSelectedRow()-1;
            if(antFila >= 0) {
                this.jTable1.setRowSelectionInterval(antFila, antFila);
                this.jTable1.scrollRectToVisible(jTable1.getCellRect(antFila, 1, true));
            }
        }
        if(evt.getKeyCode() == evt.VK_PAGE_DOWN)
        {
            int nFilas  = (int) this.jScrollPane1.getViewportBorderBounds().getHeight() / jTable1.getRowHeight();
            int sigFila = jTable1.getSelectedRow()+nFilas;
            if(sigFila > jTable1.getRowCount()) {
                sigFila = jTable1.getRowCount()-1;
            }
            if(sigFila < jTable1.getRowCount()) {
                this.jTable1.setRowSelectionInterval(sigFila, sigFila);
                this.jTable1.scrollRectToVisible(jTable1.getCellRect(sigFila, 1, true));
            }
        }
        if(evt.getKeyCode() == evt.VK_PAGE_UP)
        {
            int nFilas  = (int) this.jScrollPane1.getViewportBorderBounds().getHeight() / jTable1.getRowHeight();
            int antFila = jTable1.getSelectedRow()-nFilas;
            if(antFila < 0) {
                antFila = 0;
            }
            this.jTable1.setRowSelectionInterval(antFila, antFila);
            this.jTable1.scrollRectToVisible(jTable1.getCellRect(antFila, 1, true));
        }
    }//GEN-LAST:event_txtBusquedaKeyPressed

<<<<<<< .mine
    private void btnVentasXLineaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentasXLineaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnVentasXLineaActionPerformed

=======
    private void salir(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_salir
        // TODO add your handling code here:
        if(evt.getKeyCode() == evt.VK_ESCAPE) { this.btnCerrar.doClick(); }
    }//GEN-LAST:event_salir

>>>>>>> .r172
    public boolean getBuscarCliente()
    {
        return this.chkCliente.getModel().isSelected();
    }
    public boolean getBuscarCaja() {
        return this.chkCaja.getModel().isSelected();
    }
    public boolean getBuscarCajero() {
        return this.chkCajero.getModel().isSelected();
    }

    public void activarCliente(boolean a)
    {
        this.chkCliente.setSelected(a);
    }
    public void activarCaja(boolean a)
    {
        this.chkCaja.setSelected(a);
    }
    public void activarCajero(boolean a)
    {
        this.chkCajero.setSelected(a);
    }

    public void preBuscar()
    {
        if(timerBusqueda != null && timerBusqueda.isAlive()) { timerBusqueda.cancelar(); }
        this.timerBusqueda = new TimerBusqueda(this);
        timerBusqueda.start();

    }

    public void buscar()
    {
     
        boolean xCliente = getBuscarCliente();
        boolean xCaja = getBuscarCaja();
        boolean xCajero= getBuscarCajero();
        String whereFecha = "" ;
        String fechaDesde     = "";
        String fechaHasta     = "";
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(this.txtFechaHasta.getDate());
        calendario.add(Calendar.DAY_OF_MONTH, 1);
        if(this.txtFechaDesde.getDate() != null || this.txtFechaHasta.getDate() != null)
        {
            try {
                fechaDesde = sdf.format(this.txtFechaDesde.getDate());
                fechaHasta = sdf.format(calendario.getTime());
                whereFecha = " AND ventas.fecha_hora >= '"+fechaDesde+"' AND ventas.fecha_hora <= '"+fechaHasta+"'  ";

            } catch(Exception e) { omoikane.sistema.Dialogos.lanzarDialogoError(null, "Error en el registro: Fecha inválida", omoikane.sistema.Herramientas.getStackTraceString(e)); }
        }
 
        String busqueda = this.txtBusqueda.getText();
        String query    = "SELECT ventas.id_venta,ventas.fecha_hora,ventas.id_venta,ventas.id_caja,almacenes.descripcion,clientes.razonSocial,ventas.total FROM ventas,clientes,almacenes WHERE ventas.id_almacen="+IDAlmacen+" AND ventas.id_cliente=clientes.id_cliente "+whereFecha;
        if(xCliente || xCaja || xCajero) { query += "AND ("; }
        if(xCliente) {
                //query += "(clientes.razonSocial like '%"+busqueda+"%') ";
                query += "(ventas.id_venta like '%"+busqueda+"%') ";
        }
        if(xCliente && (xCaja || xCajero)) { query += "OR "; }
        if(xCaja) {
                query += "(id_caja like '%"+busqueda+"%') ";
        }
        if((xCaja||xCliente) && xCajero) { query += "OR "; }
        if(xCajero) {
                query += "(id_caja like '%"+busqueda+"%') ";
        }
        if(xCliente || xCaja || xCajero) { query += ")"; }
        setQueryTable(query);

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
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnDetalles;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnVentasXLinea;
    private javax.swing.JCheckBox chkCaja;
    private javax.swing.JCheckBox chkCajero;
    private javax.swing.JCheckBox chkCliente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    public javax.swing.JTextField txtBusqueda;
    private org.jdesktop.swingx.JXDatePicker txtFechaDesde;
    private org.jdesktop.swingx.JXDatePicker txtFechaHasta;
    // End of variables declaration//GEN-END:variables

}

class VentasTableModel extends ScrollableTableModel{
VentasTableModel(java.util.List ColNames,ArrayList ColClasses){super(ColNames,ColClasses);}
public Object getValueAt(int row,int col){    if(col==0)
    {
    SimpleDateFormat sdf  = new SimpleDateFormat("dd-MM-yyyy '@' hh:mm a");
    return sdf.format((java.util.Date) super.getValueAt(row, col));}
    else
    {return super.getValueAt(row,col);}
}}