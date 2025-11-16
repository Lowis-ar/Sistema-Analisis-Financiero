package vista;

public class VistaFiador extends javax.swing.JFrame {

    public VistaFiador() {
        initComponents();

        setLocationRelativeTo(null); // 🔹 Centra la ventana
        setResizable(false);         // 🔹 Evita que cambie de tamaño
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelFormulario = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbDatos = new rojerusan.RSTableMetro();
        btnEliminar = new javax.swing.JButton();
        lbModuloFiador = new javax.swing.JLabel();
        lbIDFiador = new javax.swing.JLabel();
        lbPrestamo = new javax.swing.JLabel();
        lbNombre = new javax.swing.JLabel();
        lbDireccion = new javax.swing.JLabel();
        tfDUI = new javax.swing.JTextField();
        tfIDFiador = new javax.swing.JTextField();
        cbPrestamo = new javax.swing.JComboBox<>();
        tfNombre = new javax.swing.JTextField();
        lbTelefono = new javax.swing.JLabel();
        tfTelefono = new javax.swing.JTextField();
        lbDUI = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDireccion = new javax.swing.JTextArea();
        lbEgresos = new javax.swing.JLabel();
        tfEgresos = new javax.swing.JTextField();
        lbIngresos = new javax.swing.JLabel();
        tfIngresos = new javax.swing.JTextField();
        btnLimpiar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelFormulario.setBackground(new java.awt.Color(255, 255, 255));
        PanelFormulario.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnGuardar.setBackground(new java.awt.Color(0, 102, 51));
        btnGuardar.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("Guardar");
        btnGuardar.setBorder(null);
        btnGuardar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        PanelFormulario.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 40, 90, 30));

        btnActualizar.setBackground(new java.awt.Color(255, 102, 51));
        btnActualizar.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnActualizar.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizar.setText("Actualizar");
        btnActualizar.setBorder(null);
        btnActualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        PanelFormulario.add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 90, 90, 30));

        tbDatos.setModel(new javax.swing.table.DefaultTableModel(
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
        tbDatos.setColorBackgoundHead(new java.awt.Color(153, 0, 0));
        tbDatos.setRowHeight(25);
        jScrollPane2.setViewportView(tbDatos);

        PanelFormulario.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, 840, 230));

        btnEliminar.setBackground(new java.awt.Color(204, 0, 0));
        btnEliminar.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setText("Eliminar");
        btnEliminar.setBorder(null);
        btnEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        PanelFormulario.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 140, 90, 30));

        lbModuloFiador.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbModuloFiador.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbModuloFiador.setText("MÓDULO FIADOR");
        PanelFormulario.add(lbModuloFiador, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 910, 30));

        lbIDFiador.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lbIDFiador.setText("ID Fiador: ");
        PanelFormulario.add(lbIDFiador, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, 30));

        lbPrestamo.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lbPrestamo.setText("Préstamo:");
        PanelFormulario.add(lbPrestamo, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 40, -1, 30));

        lbNombre.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lbNombre.setText("Nombre:");
        PanelFormulario.add(lbNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, 30));

        lbDireccion.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lbDireccion.setText("Dirección:");
        PanelFormulario.add(lbDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, -1, 30));
        PanelFormulario.add(tfDUI, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 140, 200, 30));

        tfIDFiador.setEditable(false);
        tfIDFiador.setBackground(new java.awt.Color(255, 255, 255));
        PanelFormulario.add(tfIDFiador, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, 110, 30));

        cbPrestamo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        PanelFormulario.add(cbPrestamo, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 40, 170, 30));
        PanelFormulario.add(tfNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 90, 270, 30));

        lbTelefono.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lbTelefono.setText("Teléfono:");
        PanelFormulario.add(lbTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 90, -1, 30));
        PanelFormulario.add(tfTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 90, 170, 30));

        lbDUI.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lbDUI.setText("DUI:");
        PanelFormulario.add(lbDUI, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 140, -1, 30));

        txtDireccion.setColumns(20);
        txtDireccion.setRows(5);
        jScrollPane1.setViewportView(txtDireccion);

        PanelFormulario.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 140, 270, -1));

        lbEgresos.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lbEgresos.setText("Egresos:");
        PanelFormulario.add(lbEgresos, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 190, -1, 30));
        PanelFormulario.add(tfEgresos, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 190, 100, 30));

        lbIngresos.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lbIngresos.setText("Ingresos:");
        PanelFormulario.add(lbIngresos, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 190, -1, 30));
        PanelFormulario.add(tfIngresos, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 190, 100, 30));

        btnLimpiar.setBackground(new java.awt.Color(102, 102, 102));
        btnLimpiar.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnLimpiar.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiar.setText("Limpiar");
        btnLimpiar.setBorder(null);
        btnLimpiar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });
        PanelFormulario.add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 190, 90, 30));

        getContentPane().add(PanelFormulario, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 910, 500));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimpiarActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VistaFiador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelFormulario;
    public javax.swing.JButton btnActualizar;
    public javax.swing.JButton btnEliminar;
    public javax.swing.JButton btnGuardar;
    public javax.swing.JButton btnLimpiar;
    public javax.swing.JComboBox<String> cbPrestamo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbDUI;
    private javax.swing.JLabel lbDireccion;
    private javax.swing.JLabel lbEgresos;
    private javax.swing.JLabel lbIDFiador;
    private javax.swing.JLabel lbIngresos;
    private javax.swing.JLabel lbModuloFiador;
    private javax.swing.JLabel lbNombre;
    private javax.swing.JLabel lbPrestamo;
    private javax.swing.JLabel lbTelefono;
    public rojerusan.RSTableMetro tbDatos;
    public javax.swing.JTextField tfDUI;
    public javax.swing.JTextField tfEgresos;
    public javax.swing.JTextField tfIDFiador;
    public javax.swing.JTextField tfIngresos;
    public javax.swing.JTextField tfNombre;
    public javax.swing.JTextField tfTelefono;
    public javax.swing.JTextArea txtDireccion;
    // End of variables declaration//GEN-END:variables
}
