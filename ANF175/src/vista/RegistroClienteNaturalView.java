package vista;

import controlador.ClienteNaturalController;
import modelo.ClienteNatural;
import dao.Conexion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Connection;

public class RegistroClienteNaturalView extends JFrame {
    // Componentes de la vista (usando los nombres que te sugerí)
    private JTextField txtCodigo, txtNombre, txtTelefono, txtDUI, txtIngresos, txtEgresos;
    private JTextField txtEmpresa, txtTelefonoEmpresa;
    private JTextArea txtDireccion, txtDireccionEmpresa;
    private JComboBox<String> cmbEstadoCivil;
    private JLabel lblMaximoPrestar, lblCuotaCalculada;
    private JLabel lblValidacionNombre, lblValidacionDUI, lblValidacionTelefono, lblValidacionIngresos, lblValidacionEgresos;
    private JTable tblLugaresTrabajo;
    private DefaultTableModel modeloTabla;
    private JButton btnAgregarTrabajo, btnEliminarTrabajo, btnGuardar, btnLimpiar, btnCancelar;
    
    private ClienteNaturalController controlador;
    private Connection conexion;

    public RegistroClienteNaturalView() {
        try {
            this.controlador = new ClienteNaturalController();
            inicializarComponentes();
            configurarEventos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inicializarComponentes() {
        setTitle("Registro de Cliente Natural");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);

        // Panel principal con pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Pestaña 1: Datos Personales
        tabbedPane.addTab("Datos Personales", crearPanelDatosPersonales());
        
        // Pestaña 2: Información Financiera
        tabbedPane.addTab("Información Financiera", crearPanelInformacionFinanciera());
        
        // Pestaña 3: Lugares de Trabajo
        tabbedPane.addTab("Lugares de Trabajo", crearPanelLugaresTrabajo());

        add(tabbedPane, BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelDatosPersonales() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Código (automático)
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Código Cliente:"), gbc);
        gbc.gridx = 1;
        txtCodigo = new JTextField(20);
        txtCodigo.setEditable(false);
        txtCodigo.setBackground(Color.LIGHT_GRAY);
        panel.add(txtCodigo, gbc);

        // Nombre
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Nombre Completo:*"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        panel.add(txtNombre, gbc);
        gbc.gridx = 2;
        lblValidacionNombre = new JLabel("");
        lblValidacionNombre.setForeground(Color.RED);
        panel.add(lblValidacionNombre, gbc);

        // DUI
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("DUI:*"), gbc);
        gbc.gridx = 1;
        txtDUI = new JTextField(20);
        panel.add(txtDUI, gbc);
        gbc.gridx = 2;
        lblValidacionDUI = new JLabel("");
        lblValidacionDUI.setForeground(Color.RED);
        panel.add(lblValidacionDUI, gbc);

        // Teléfono
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Teléfono:*"), gbc);
        gbc.gridx = 1;
        txtTelefono = new JTextField(20);
        panel.add(txtTelefono, gbc);
        gbc.gridx = 2;
        lblValidacionTelefono = new JLabel("");
        lblValidacionTelefono.setForeground(Color.RED);
        panel.add(lblValidacionTelefono, gbc);

        // Estado Civil
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Estado Civil:"), gbc);
        gbc.gridx = 1;
        cmbEstadoCivil = new JComboBox<>(new String[]{"Soltero/a", "Casado/a", "Divorciado/a", "Viudo/a", "Unión Libre"});
        panel.add(cmbEstadoCivil, gbc);

        // Dirección
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtDireccion = new JTextArea(3, 20);
        txtDireccion.setLineWrap(true);
        JScrollPane scrollDireccion = new JScrollPane(txtDireccion);
        panel.add(scrollDireccion, gbc);

        return panel;
    }

    private JPanel crearPanelInformacionFinanciera() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Ingresos
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Ingresos Mensuales ($):*"), gbc);
        gbc.gridx = 1;
        txtIngresos = new JTextField(15);
        panel.add(txtIngresos, gbc);
        gbc.gridx = 2;
        lblValidacionIngresos = new JLabel("");
        lblValidacionIngresos.setForeground(Color.RED);
        panel.add(lblValidacionIngresos, gbc);

        // Egresos
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Egresos Mensuales ($):*"), gbc);
        gbc.gridx = 1;
        txtEgresos = new JTextField(15);
        panel.add(txtEgresos, gbc);
        gbc.gridx = 2;
        lblValidacionEgresos = new JLabel("");
        lblValidacionEgresos.setForeground(Color.RED);
        panel.add(lblValidacionEgresos, gbc);

        // Resultados calculados
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Máximo a Prestar:"), gbc);
        gbc.gridx = 1;
        lblMaximoPrestar = new JLabel("$0.00");
        lblMaximoPrestar.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblMaximoPrestar, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Cuota Estimada (36 meses):"), gbc);
        gbc.gridx = 1;
        lblCuotaCalculada = new JLabel("$0.00");
        lblCuotaCalculada.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblCuotaCalculada, gbc);

        return panel;
    }

    private JPanel crearPanelLugaresTrabajo() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel superior para ingresar datos
        JPanel panelSuperior = new JPanel(new GridLayout(3, 2, 5, 5));
        
        panelSuperior.add(new JLabel("Empresa:"));
        txtEmpresa = new JTextField();
        panelSuperior.add(txtEmpresa);
        
        panelSuperior.add(new JLabel("Teléfono Empresa:"));
        txtTelefonoEmpresa = new JTextField();
        panelSuperior.add(txtTelefonoEmpresa);
        
        panelSuperior.add(new JLabel("Dirección Empresa:"));
        txtDireccionEmpresa = new JTextArea(2, 20);
        JScrollPane scrollEmpresa = new JScrollPane(txtDireccionEmpresa);
        panelSuperior.add(scrollEmpresa);

        // Panel de botones para lugares de trabajo
        JPanel panelBotonesTrabajo = new JPanel();
        btnAgregarTrabajo = new JButton("Agregar Lugar de Trabajo");
        btnEliminarTrabajo = new JButton("Eliminar Seleccionado");
        panelBotonesTrabajo.add(btnAgregarTrabajo);
        panelBotonesTrabajo.add(btnEliminarTrabajo);

        // Tabla de lugares de trabajo
        modeloTabla = new DefaultTableModel(new String[]{"Empresa", "Teléfono", "Dirección"}, 0);
        tblLugaresTrabajo = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tblLugaresTrabajo);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(panelBotonesTrabajo, BorderLayout.CENTER);
        panel.add(scrollTabla, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel();
        btnGuardar = new JButton("Guardar Cliente");
        btnLimpiar = new JButton("Limpiar Formulario");
        btnCancelar = new JButton("Cancelar");

        panel.add(btnGuardar);
        panel.add(btnLimpiar);
        panel.add(btnCancelar);

        return panel;
    }

    private void configurarEventos() {
        // Validaciones en tiempo real
        txtNombre.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                ClienteNaturalController.ValidacionResultado resultado = controlador.validarNombre(txtNombre.getText());
                lblValidacionNombre.setText(resultado.getMensaje());
                lblValidacionNombre.setForeground(resultado.isValido() ? Color.GREEN : Color.RED);
            }
        });

        txtDUI.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                ClienteNaturalController.ValidacionResultado resultado = controlador.validarDUI(txtDUI.getText());
                lblValidacionDUI.setText(resultado.getMensaje());
                lblValidacionDUI.setForeground(resultado.isValido() ? Color.GREEN : Color.RED);
            }
        });

        txtTelefono.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                ClienteNaturalController.ValidacionResultado resultado = controlador.validarTelefono(txtTelefono.getText());
                lblValidacionTelefono.setText(resultado.getMensaje());
                lblValidacionTelefono.setForeground(resultado.isValido() ? Color.GREEN : Color.RED);
            }
        });

        txtIngresos.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                ClienteNaturalController.ValidacionResultado resultado = controlador.validarIngresos(txtIngresos.getText());
                lblValidacionIngresos.setText(resultado.getMensaje());
                lblValidacionIngresos.setForeground(resultado.isValido() ? Color.GREEN : Color.RED);
                
                // Recalcular máximo a prestar y cuota
                recalcularFinanzas();
            }
        });

        txtEgresos.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                BigDecimal ingresos = BigDecimal.ZERO;
                try {
                    ingresos = new BigDecimal(txtIngresos.getText());
                } catch (NumberFormatException ex) {
                    // Si ingresos no es válido, no validar egresos
                }
                
                ClienteNaturalController.ValidacionResultado resultado = controlador.validarEgresos(txtEgresos.getText(), ingresos);
                lblValidacionEgresos.setText(resultado.getMensaje());
                lblValidacionEgresos.setForeground(resultado.isValido() ? Color.GREEN : Color.RED);
                
                // Recalcular máximo a prestar y cuota
                recalcularFinanzas();
            }
        });

        // Botón agregar lugar de trabajo
        btnAgregarTrabajo.addActionListener(e -> agregarLugarTrabajo());

        // Botón eliminar lugar de trabajo
        btnEliminarTrabajo.addActionListener(e -> eliminarLugarTrabajo());

        // Botón guardar
        btnGuardar.addActionListener(e -> guardarCliente());

        // Botón limpiar
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        // Botón cancelar
        btnCancelar.addActionListener(e -> dispose());
    }

    private void recalcularFinanzas() {
        try {
            BigDecimal ingresos = new BigDecimal(txtIngresos.getText());
            BigDecimal egresos = new BigDecimal(txtEgresos.getText());
            
            controlador.setDatosFinancieros(ingresos, egresos);
            
            ClienteNatural cliente = controlador.getClienteActual();
            lblMaximoPrestar.setText("$" + cliente.getMaximoPrestar());
            lblCuotaCalculada.setText("$" + cliente.getCuotaCalculada());
            
        } catch (NumberFormatException e) {
            lblMaximoPrestar.setText("$0.00");
            lblCuotaCalculada.setText("$0.00");
        }
    }

    private void agregarLugarTrabajo() {
        String empresa = txtEmpresa.getText().trim();
        String telefono = txtTelefonoEmpresa.getText().trim();
        String direccion = txtDireccionEmpresa.getText().trim();
        
        if (empresa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre de la empresa es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        controlador.agregarLugarTrabajo(empresa, direccion, telefono);
        modeloTabla.addRow(new Object[]{empresa, telefono, direccion});
        
        // Limpiar campos
        txtEmpresa.setText("");
        txtTelefonoEmpresa.setText("");
        txtDireccionEmpresa.setText("");
    }

    private void eliminarLugarTrabajo() {
        int filaSeleccionada = tblLugaresTrabajo.getSelectedRow();
        if (filaSeleccionada >= 0) {
            controlador.removerLugarTrabajo(filaSeleccionada);
            modeloTabla.removeRow(filaSeleccionada);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un lugar de trabajo para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void guardarCliente() {
        try {
            // Validar campos obligatorios
            if (!validarCamposObligatorios()) {
                return;
            }
            
            // Establecer datos en el controlador
            controlador.setDatosBasicos(
                txtNombre.getText(),
                txtDireccion.getText(),
                txtTelefono.getText(),
                txtDUI.getText(),
                cmbEstadoCivil.getSelectedItem().toString()
            );
            
            BigDecimal ingresos = new BigDecimal(txtIngresos.getText());
            BigDecimal egresos = new BigDecimal(txtEgresos.getText());
            controlador.setDatosFinancieros(ingresos, egresos);
            
            // Guardar en la base de datos
            boolean exito = controlador.guardarCliente();
            
            if (exito) {
                JOptionPane.showMessageDialog(this, 
                    "Cliente registrado exitosamente!\nCódigo: " + controlador.getClienteActual().getCodigoCliente(),
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al guardar el cliente", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCamposObligatorios() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtDUI.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El DUI es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtTelefono.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El teléfono es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void limpiarFormulario() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtDUI.setText("");
        txtIngresos.setText("");
        txtEgresos.setText("");
        txtEmpresa.setText("");
        txtTelefonoEmpresa.setText("");
        txtDireccionEmpresa.setText("");
        cmbEstadoCivil.setSelectedIndex(0);
        lblMaximoPrestar.setText("$0.00");
        lblCuotaCalculada.setText("$0.00");
        modeloTabla.setRowCount(0);
        
        // Limpiar mensajes de validación
        lblValidacionNombre.setText("");
        lblValidacionDUI.setText("");
        lblValidacionTelefono.setText("");
        lblValidacionIngresos.setText("");
        lblValidacionEgresos.setText("");
        
        // Crear nuevo controlador para limpiar datos
        this.controlador = new ClienteNaturalController();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RegistroClienteNaturalView().setVisible(true);
        });
    }
}