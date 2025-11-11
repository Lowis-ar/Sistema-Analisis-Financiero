package controlador;

import modelo.ClienteNatural;
import modelo.LugarTrabajo;
import dao.ClienteNaturalDAO;
import vista.ClienteNaturalVista;
import dao.Conexion;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ClienteNaturalController {
    private ClienteNaturalDAO clienteDAO;
    private ClienteNatural clienteActual;
    private ClienteNaturalVista vista;
    private Conexion conexion;

    public ClienteNaturalController() {
        try {
            this.conexion = new Conexion();
            this.clienteDAO = new ClienteNaturalDAO(conexion.getConnection());
            this.clienteActual = new ClienteNatural();
            this.vista = new ClienteNaturalVista();
            configurarVista();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al inicializar: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configurarVista() {
        // Configurar ComboBox
        vista.getCmbEstadoCivil().setModel(new javax.swing.DefaultComboBoxModel<>(
            new String[] { "Soltero/a", "Casado/a", "Divorciado/a", "Viudo/a", "Unión Libre" }
        ));

        // Configurar tabla de lugares de trabajo
        DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[][]{},
            new String[]{"Empresa", "Teléfono", "Dirección"}
        );
        vista.getTblLugaresTrabajo().setModel(modeloTabla);

        // Inicializar etiquetas
        vista.getLblMaximoPrestar().setText("$0.00");
        vista.getLblCuotaCalculada().setText("$0.00");

        // Configurar eventos
        configurarEventos();
        
        // Hacer visible la vista
        vista.setVisible(true);
    }

    private void configurarEventos() {
        // Validaciones en tiempo real
        vista.getTxtNombre().addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                validarNombre();
            }
        });

        vista.getTxtDUI().addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                validarDUI();
            }
        });

        vista.getTxtTelefono().addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                validarTelefono();
            }
        });

        vista.getTxtIngresos().addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                validarIngresos();
                recalcularFinanzas();
            }
        });

        vista.getTxtEgresos().addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                validarEgresos();
                recalcularFinanzas();
            }
        });

        // Botones de acción
        vista.getBtnAgregarTrabajo().addActionListener(e -> agregarLugarTrabajo());
        vista.getBtnEliminarTrabajo().addActionListener(e -> eliminarLugarTrabajo());
        vista.getBtnGuardar().addActionListener(e -> guardarClienteEnBD()); // CAMBIADO EL NOMBRE
        vista.getBtnLimpiar().addActionListener(e -> limpiarFormulario());
        vista.getBtnCancelar().addActionListener(e -> cerrarVista());
    }

    // MÉTODOS DE VALIDACIÓN
    private void validarNombre() {
        String nombre = vista.getTxtNombre().getText();
        ValidacionResultado resultado = validarNombre(nombre);
        vista.getLblValidacionNombre().setText(resultado.getMensaje());
        vista.getLblValidacionNombre().setForeground(resultado.isValido() ? java.awt.Color.GREEN : java.awt.Color.RED);
    }

    private void validarDUI() {
        String dui = vista.getTxtDUI().getText();
        ValidacionResultado resultado = validarDUI(dui);
        vista.getLblValidacionDUI().setText(resultado.getMensaje());
        vista.getLblValidacionDUI().setForeground(resultado.isValido() ? java.awt.Color.GREEN : java.awt.Color.RED);
    }

    private void validarTelefono() {
        String telefono = vista.getTxtTelefono().getText();
        ValidacionResultado resultado = validarTelefono(telefono);
        vista.getLblValidacionTelefono().setText(resultado.getMensaje());
        vista.getLblValidacionTelefono().setForeground(resultado.isValido() ? java.awt.Color.GREEN : java.awt.Color.RED);
    }

    private void validarIngresos() {
        String ingresosStr = vista.getTxtIngresos().getText();
        ValidacionResultado resultado = validarIngresos(ingresosStr);
        vista.getLblValidacionIngresos().setText(resultado.getMensaje());
        vista.getLblValidacionIngresos().setForeground(resultado.isValido() ? java.awt.Color.GREEN : java.awt.Color.RED);
    }

    private void validarEgresos() {
        String egresosStr = vista.getTxtEgresos().getText();
        BigDecimal ingresos = BigDecimal.ZERO;
        try {
            ingresos = new BigDecimal(vista.getTxtIngresos().getText());
        } catch (NumberFormatException e) {
            // Si ingresos no es válido, no validar egresos
        }
        
        ValidacionResultado resultado = validarEgresos(egresosStr, ingresos);
        vista.getLblValidacionEgresos().setText(resultado.getMensaje());
        vista.getLblValidacionEgresos().setForeground(resultado.isValido() ? java.awt.Color.GREEN : java.awt.Color.RED);
    }

    // VALIDACIONES DEL CONTROLADOR (métodos públicos para reutilizar)
    public ValidacionResultado validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return new ValidacionResultado(false, "El nombre es obligatorio");
        }
        if (nombre.length() < 3) {
            return new ValidacionResultado(false, "El nombre debe tener al menos 3 caracteres");
        }
        if (!Pattern.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{3,100}$", nombre)) {
            return new ValidacionResultado(false, "El nombre solo puede contener letras y espacios");
        }
        return new ValidacionResultado(true, "Nombre válido");
    }

    public ValidacionResultado validarDUI(String dui) {
        if (dui == null || dui.trim().isEmpty()) {
            return new ValidacionResultado(false, "El DUI es obligatorio");
        }
        // Formato: 00000000-0
        if (!Pattern.matches("^\\d{8}-\\d{1}$", dui)) {
            return new ValidacionResultado(false, "Formato de DUI inválido. Use: 00000000-0");
        }
        
        try {
            if (clienteDAO.existeDUI(dui)) {
                return new ValidacionResultado(false, "El DUI ya está registrado en el sistema");
            }
        } catch (SQLException e) {
            return new ValidacionResultado(false, "Error al verificar DUI en la base de datos");
        }
        
        return new ValidacionResultado(true, "DUI válido");
    }

    public ValidacionResultado validarTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            return new ValidacionResultado(false, "El teléfono es obligatorio");
        }
        if (!Pattern.matches("^[267]\\d{3}-?\\d{4}$", telefono.replace(" ", ""))) {
            return new ValidacionResultado(false, "Formato de teléfono inválido. Use: 2XXX-XXXX, 6XXX-XXXX o 7XXX-XXXX");
        }
        return new ValidacionResultado(true, "Teléfono válido");
    }

    public ValidacionResultado validarIngresos(String ingresosStr) {
        try {
            BigDecimal ingresos = new BigDecimal(ingresosStr);
            if (ingresos.compareTo(BigDecimal.ZERO) < 0) {
                return new ValidacionResultado(false, "Los ingresos no pueden ser negativos");
            }
            return new ValidacionResultado(true, "Ingresos válidos");
        } catch (NumberFormatException e) {
            return new ValidacionResultado(false, "Formato de ingresos inválido");
        }
    }

    public ValidacionResultado validarEgresos(String egresosStr, BigDecimal ingresos) {
        try {
            BigDecimal egresos = new BigDecimal(egresosStr);
            if (egresos.compareTo(BigDecimal.ZERO) < 0) {
                return new ValidacionResultado(false, "Los egresos no pueden ser negativos");
            }
            if (egresos.compareTo(ingresos) > 0) {
                return new ValidacionResultado(false, "Los egresos no pueden ser mayores a los ingresos");
            }
            return new ValidacionResultado(true, "Egresos válidos");
        } catch (NumberFormatException e) {
            return new ValidacionResultado(false, "Formato de egresos inválido");
        }
    }

    // MÉTODOS DE NEGOCIO
    private void recalcularFinanzas() {
        try {
            BigDecimal ingresos = new BigDecimal(vista.getTxtIngresos().getText());
            BigDecimal egresos = new BigDecimal(vista.getTxtEgresos().getText());
            
            setDatosFinancieros(ingresos, egresos);
            
            vista.getLblMaximoPrestar().setText("$" + clienteActual.getMaximoPrestar());
            vista.getLblCuotaCalculada().setText("$" + clienteActual.getCuotaCalculada());
            
        } catch (NumberFormatException e) {
            vista.getLblMaximoPrestar().setText("$0.00");
            vista.getLblCuotaCalculada().setText("$0.00");
        }
    }

    private void agregarLugarTrabajo() {
        String empresa = vista.getTxtEmpresa().getText().trim();
        String telefono = vista.getTxtTelefonoEmpresa().getText().trim();
        String direccion = vista.getTxtDireccionEmpresa().getText().trim();
        
        if (empresa.isEmpty()) {
            JOptionPane.showMessageDialog(vista, 
                "El nombre de la empresa es obligatorio", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        agregarLugarTrabajo(empresa, direccion, telefono);
        
        DefaultTableModel model = (DefaultTableModel) vista.getTblLugaresTrabajo().getModel();
        model.addRow(new Object[]{empresa, telefono, direccion});
        
        // Limpiar campos
        vista.getTxtEmpresa().setText("");
        vista.getTxtTelefonoEmpresa().setText("");
        vista.getTxtDireccionEmpresa().setText("");
    }

    private void eliminarLugarTrabajo() {
        int filaSeleccionada = vista.getTblLugaresTrabajo().getSelectedRow();
        if (filaSeleccionada >= 0) {
            removerLugarTrabajo(filaSeleccionada);
            DefaultTableModel model = (DefaultTableModel) vista.getTblLugaresTrabajo().getModel();
            model.removeRow(filaSeleccionada);
        } else {
            JOptionPane.showMessageDialog(vista, 
                "Seleccione un lugar de trabajo para eliminar", 
                "Advertencia", 
                JOptionPane.WARNING_MESSAGE);
        }
    }

    // MÉTODO CORREGIDO - nombre diferente para evitar conflicto
    private void guardarClienteEnBD() {
        try {
            // Validar campos obligatorios
            if (!validarCamposObligatorios()) {
                return;
            }
            
            // Establecer datos en el controlador
            setDatosBasicos(
                vista.getTxtNombre().getText(),
                vista.getTxtDireccion().getText(),
                vista.getTxtTelefono().getText(),
                vista.getTxtDUI().getText(),
                vista.getCmbEstadoCivil().getSelectedItem().toString()
            );
            
            BigDecimal ingresos = new BigDecimal(vista.getTxtIngresos().getText());
            BigDecimal egresos = new BigDecimal(vista.getTxtEgresos().getText());
            setDatosFinancieros(ingresos, egresos);
            
            // Guardar en la base de datos
            boolean exito = guardarCliente();
            
            if (exito) {
                JOptionPane.showMessageDialog(vista, 
                    "Cliente registrado exitosamente!\nCódigo: " + clienteActual.getCodigoCliente(),
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(vista, 
                    "Error al guardar el cliente", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, 
                "Error: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private boolean validarCamposObligatorios() {
        if (vista.getTxtNombre().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El nombre es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (vista.getTxtDUI().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El DUI es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (vista.getTxtTelefono().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El teléfono es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (vista.getTxtIngresos().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Los ingresos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (vista.getTxtEgresos().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Los egresos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void limpiarFormulario() {
        vista.getTxtCodigo().setText("");
        vista.getTxtNombre().setText("");
        vista.getTxtDireccion().setText("");
        vista.getTxtTelefono().setText("");
        vista.getTxtDUI().setText("");
        vista.getTxtIngresos().setText("");
        vista.getTxtEgresos().setText("");
        vista.getTxtEmpresa().setText("");
        vista.getTxtTelefonoEmpresa().setText("");
        vista.getTxtDireccionEmpresa().setText("");
        vista.getCmbEstadoCivil().setSelectedIndex(0);
        vista.getLblMaximoPrestar().setText("$0.00");
        vista.getLblCuotaCalculada().setText("$0.00");
        
        DefaultTableModel model = (DefaultTableModel) vista.getTblLugaresTrabajo().getModel();
        model.setRowCount(0);
        
        // Limpiar mensajes de validación
        vista.getLblValidacionNombre().setText("");
        vista.getLblValidacionDUI().setText("");
        vista.getLblValidacionTelefono().setText("");
        vista.getLblValidacionIngresos().setText("");
        vista.getLblValidacionEgresos().setText("");
        
        // Reiniciar el modelo
        this.clienteActual = new ClienteNatural();
    }

    private void cerrarVista() {
        if (conexion != null) {
            conexion.cerrarConexion();
        }
        vista.dispose();
    }

    // MÉTODOS PÚBLICOS PARA GESTIONAR DATOS
    public void setDatosBasicos(String nombre, String direccion, String telefono, String dui, String estadoCivil) {
        clienteActual.setNombre(nombre);
        clienteActual.setDireccion(direccion);
        clienteActual.setTelefono(telefono);
        clienteActual.setDui(dui);
        clienteActual.setEstadoCivil(estadoCivil);
    }

    public void setDatosFinancieros(BigDecimal ingresos, BigDecimal egresos) {
        clienteActual.setIngresos(ingresos);
        clienteActual.setEgresos(egresos);
        
        // Calcular máximo a prestar automáticamente
        BigDecimal maximoPrestar = clienteDAO.calcularMaximoPrestamo(ingresos, egresos);
        clienteActual.setMaximoPrestar(maximoPrestar);
        
        // Calcular cuota estimada (ejemplo: 80% del máximo a 36 meses con 12% de interés)
        BigDecimal montoEjemplo = maximoPrestar.multiply(new BigDecimal("0.8"));
        BigDecimal cuotaEstimada = clienteDAO.calcularCuota(montoEjemplo, 36, new BigDecimal("12.0"));
        clienteActual.setCuotaCalculada(cuotaEstimada);
    }

    public void agregarLugarTrabajo(String empresa, String direccion, String telefono) {
        LugarTrabajo lugar = new LugarTrabajo(empresa, direccion, telefono);
        clienteActual.agregarLugarTrabajo(lugar);
    }

    public void removerLugarTrabajo(int index) {
        clienteActual.removerLugarTrabajo(index);
    }

    // MÉTODO ORIGINAL PARA GUARDAR EN BD (sin cambios)
    public boolean guardarCliente() {
        try {
            // Generar código automático
            String codigo = clienteDAO.generarCodigoCliente();
            clienteActual.setCodigoCliente(codigo);
            
            boolean exito = clienteDAO.insertarClienteNatural(clienteActual);
            
            if (exito) {
                vista.getTxtCodigo().setText(codigo);
            }
            
            return exito;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ClienteNatural getClienteActual() {
        return clienteActual;
    }

    // Clase interna para resultados de validación
    public static class ValidacionResultado {
        private boolean valido;
        private String mensaje;

        public ValidacionResultado(boolean valido, String mensaje) {
            this.valido = valido;
            this.mensaje = mensaje;
        }

        public boolean isValido() { return valido; }
        public String getMensaje() { return mensaje; }
    }
}