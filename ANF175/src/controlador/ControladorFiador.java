package controlador;

import dao.Conexion;
import dao.FiadorDao;
import modelo.Fiador;

import java.awt.event.*;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import vista.VistaFiador;

public class ControladorFiador implements ActionListener {

    private VistaFiador vista;
    private FiadorDao dao;
    private DefaultTableModel modeloTabla;
    private int idSeleccionado = -1;
    Conexion conexion = new Conexion();

    public ControladorFiador(VistaFiador vista) {
        this.vista = vista;
        dao = new FiadorDao(conexion.getConnection());

        // Escuchar eventos de botones
        this.vista.btnGuardar.addActionListener(this);
        this.vista.btnActualizar.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);
        this.vista.btnLimpiar.addActionListener(this);

        // Evento al hacer click en la tabla
        this.vista.tbDatos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarFila();
            }
        });

        // CARGAR PRÉSTAMOS Y DATOS
        cargarPrestamosEnCombo();
        mostrarDatos();
        configurarEstadoInicial();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnGuardar) {
            guardar();
        } else if (e.getSource() == vista.btnActualizar) {
            actualizar();
        } else if (e.getSource() == vista.btnEliminar) {
            eliminar();
        } else if (e.getSource() == vista.btnLimpiar) {
            limpiarCampos();
        }
    }

    // Configurar estado inicial de los botones
    private void configurarEstadoInicial() {
        vista.btnActualizar.setEnabled(false);
        vista.btnEliminar.setEnabled(false);
        // Generar próximo ID
        generarProximoId();
    }

    // Generar próximo ID automáticamente
    private void generarProximoId() {
        int ultimoId = dao.obtenerUltimoId();
        vista.tfIDFiador.setText(String.valueOf(ultimoId + 1));
    }

    // Cargar préstamos en el ComboBox
    private void cargarPrestamosEnCombo() {
        try {
            System.out.println("DEBUG: Intentando cargar préstamos en combo...");

            List<String[]> prestamos = dao.listarPrestamosParaCombo();
            System.out.println("DEBUG: Número de préstamos encontrados: " + prestamos.size());

            vista.cbPrestamo.removeAllItems();
            vista.cbPrestamo.addItem("Seleccione un préstamo");

            for (String[] prestamo : prestamos) {
                System.out.println("DEBUG: Agregando préstamo: " + prestamo[1]);
                vista.cbPrestamo.addItem(prestamo[1]);
            }

            System.out.println("DEBUG: Total items en combo: " + vista.cbPrestamo.getItemCount());

        } catch (Exception e) {
            System.out.println("DEBUG: Error en cargarPrestamosEnCombo: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Error al cargar préstamos");
        }
    }

    // Obtener ID del préstamo seleccionado
    private int obtenerIdPrestamoSeleccionado() {
        int selectedIndex = vista.cbPrestamo.getSelectedIndex();
        if (selectedIndex <= 0) {
            return 0; // Si seleccionó el primero (vacío) o nada
        }
        try {
            List<String[]> prestamos = dao.listarPrestamosParaCombo();
            if (selectedIndex - 1 < prestamos.size()) {
                String[] prestamo = prestamos.get(selectedIndex - 1);
                return Integer.parseInt(prestamo[0]); // Retorna el ID
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Cargar préstamo específico en el ComboBox
    private void cargarPrestamoEnCombo(int idPrestamo) {
        if (idPrestamo == 0) {
            vista.cbPrestamo.setSelectedIndex(0);
            return;
        }

        try {
            String textoPrestamo = dao.obtenerNombrePrestamo(idPrestamo);
            for (int i = 0; i < vista.cbPrestamo.getItemCount(); i++) {
                if (vista.cbPrestamo.getItemAt(i).equals(textoPrestamo)) {
                    vista.cbPrestamo.setSelectedIndex(i);
                    return;
                }
            }
            // Si no lo encuentra, lo agregamos
            vista.cbPrestamo.addItem(textoPrestamo);
            vista.cbPrestamo.setSelectedItem(textoPrestamo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Mostrar registros en la tabla
    private void mostrarDatos() {
        String[] columnas = {"ID", "Préstamo", "Nombre", "Dirección", "Teléfono", "DUI", "Ingresos", "Egresos"};
        modeloTabla = new DefaultTableModel(null, columnas);
        List<Fiador> lista = dao.listar();

        for (Fiador f : lista) {
            modeloTabla.addRow(new Object[]{
                f.getId_fiador(),
                f.getId_prestamo(),
                f.getNombre(),
                f.getDireccion(),
                f.getTelefono(),
                f.getDui(),
                f.getIngresos(),
                f.getEgresos()
            });
        }

        vista.tbDatos.setModel(modeloTabla);
    }

    // Guardar nuevo registro
    private void guardar() {
        if (!validarCampos()) {
            return;
        }

        Fiador fiador = crearFiadorDesdeFormulario();

        // Verificar si ya existe el DUI
        if (dao.existeDui(fiador.getDui(), 0)) {
            JOptionPane.showMessageDialog(null, "Ya existe un fiador con este DUI.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean resultado = dao.insertar(fiador);
        if (resultado) {
            JOptionPane.showMessageDialog(null, "Fiador guardado correctamente.");
            mostrarDatos();
            limpiarCampos();
            generarProximoId();
        } else {
            JOptionPane.showMessageDialog(null, "Error al guardar el fiador.");
        }
    }

    // Actualizar registro existente
    private void actualizar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un fiador para actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validarCampos()) {
            return;
        }

        Fiador fiador = crearFiadorDesdeFormulario();
        fiador.setId_fiador(idSeleccionado);

        // Verificar si ya existe el DUI (excluyendo el actual)
        if (dao.existeDui(fiador.getDui(), idSeleccionado)) {
            JOptionPane.showMessageDialog(null, "Ya existe otro fiador con este DUI.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean resultado = dao.actualizar(fiador);
        if (resultado) {
            JOptionPane.showMessageDialog(null, "Fiador actualizado correctamente.");
            mostrarDatos();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(null, "Error al actualizar el fiador.");
        }
    }

    // Eliminar registro
    private void eliminar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un fiador para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null,
                "¿Seguro que desea eliminar este fiador?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean resultado = dao.eliminar(idSeleccionado);
            if (resultado) {
                JOptionPane.showMessageDialog(null, "Fiador eliminado correctamente.");
                mostrarDatos();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar el fiador.");
            }
        }
    }

    // Seleccionar fila en la tabla
    private void seleccionarFila() {
        int fila = vista.tbDatos.getSelectedRow();
        if (fila >= 0) {
            idSeleccionado = Integer.parseInt(vista.tbDatos.getValueAt(fila, 0).toString());
            int idPrestamo = Integer.parseInt(vista.tbDatos.getValueAt(fila, 1).toString());

            vista.tfIDFiador.setText(vista.tbDatos.getValueAt(fila, 0).toString());

            // Cargar el préstamo en el ComboBox
            cargarPrestamoEnCombo(idPrestamo);

            vista.tfNombre.setText(vista.tbDatos.getValueAt(fila, 2).toString());
            vista.txtDireccion.setText(vista.tbDatos.getValueAt(fila, 3).toString());
            vista.tfTelefono.setText(vista.tbDatos.getValueAt(fila, 4).toString());
            vista.tfDUI.setText(vista.tbDatos.getValueAt(fila, 5).toString());
            vista.tfIngresos.setText(vista.tbDatos.getValueAt(fila, 6).toString());
            vista.tfEgresos.setText(vista.tbDatos.getValueAt(fila, 7).toString());

            // Habilitar botones de actualizar y eliminar
            vista.btnActualizar.setEnabled(true);
            vista.btnEliminar.setEnabled(true);
            vista.btnGuardar.setEnabled(false);
        }
    }

    // Limpiar campos
    private void limpiarCampos() {
        vista.tfNombre.setText("");
        vista.txtDireccion.setText("");
        vista.tfTelefono.setText("");
        vista.tfDUI.setText("");
        vista.tfIngresos.setText("");
        vista.tfEgresos.setText("");
        vista.cbPrestamo.setSelectedIndex(0);

        idSeleccionado = -1;
        vista.tbDatos.clearSelection();

        // Restaurar estado de botones
        vista.btnGuardar.setEnabled(true);
        vista.btnActualizar.setEnabled(false);
        vista.btnEliminar.setEnabled(false);

        generarProximoId();
    }

    // Validar campos del formulario
    private boolean validarCampos() {
        if (vista.tfNombre.getText().trim().isEmpty()
                || vista.txtDireccion.getText().trim().isEmpty()
                || vista.tfTelefono.getText().trim().isEmpty()
                || vista.tfDUI.getText().trim().isEmpty()
                || vista.tfIngresos.getText().trim().isEmpty()
                || vista.tfEgresos.getText().trim().isEmpty()
                || vista.cbPrestamo.getSelectedIndex() <= 0) {

            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validar formato numérico
        try {
            Double.parseDouble(vista.tfIngresos.getText());
            Double.parseDouble(vista.tfEgresos.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ingresos y Egresos deben ser valores numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar DUI (9 dígitos)
        String dui = vista.tfDUI.getText().replaceAll("[^0-9]", "");
        if (dui.length() != 9) {
            JOptionPane.showMessageDialog(null, "El DUI debe tener 9 dígitos.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // Crear objeto Fiador desde el formulario
    private Fiador crearFiadorDesdeFormulario() {
        Fiador fiador = new Fiador();

        // Obtener ID del préstamo seleccionado
        fiador.setId_prestamo(obtenerIdPrestamoSeleccionado());
        fiador.setNombre(vista.tfNombre.getText().trim());
        fiador.setDireccion(vista.txtDireccion.getText().trim());
        fiador.setTelefono(vista.tfTelefono.getText().trim());
        fiador.setDui(vista.tfDUI.getText().trim());
        fiador.setIngresos(Double.parseDouble(vista.tfIngresos.getText()));
        fiador.setEgresos(Double.parseDouble(vista.tfEgresos.getText()));

        return fiador;
    }
}
