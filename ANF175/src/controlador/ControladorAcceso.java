package controlador;

import dao.Conexion;
import dao.NivelAccesoDao;
import modelo.NivelAcceso;

import java.awt.event.*;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import vista.VistaAcceso;

public class ControladorAcceso implements ActionListener {

    private VistaAcceso vista;
    private NivelAccesoDao dao;
    private DefaultTableModel modeloTabla;
    private int idSeleccionado = -1; // para saber cuál registro está seleccionado
    // Crear conexión y pasarla al DAO
    Conexion conexion = new Conexion();

    public ControladorAcceso(VistaAcceso vista) {

        this.vista = vista;
        dao = new NivelAccesoDao(conexion.getConnection());

        // Escuchar eventos
        this.vista.btnAgregar.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);
        this.vista.btnCancelar.addActionListener(this);

        // Evento al hacer click en la tabla
        this.vista.tbDatos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarFila();
            }
        });

        mostrarDatos();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnAgregar) {
            guardar();
        } else if (e.getSource() == vista.btnEliminar) {
            eliminar();
        } else if (e.getSource() == vista.btnCancelar) {
            limpiarCampos();
        }
    }

    // Mostrar registros en la tabla
    private void mostrarDatos() {
        String[] columnas = {"ID", "Nombre", "Descripción"};
        modeloTabla = new DefaultTableModel(null, columnas);
        List<NivelAcceso> lista = dao.listar();
        for (NivelAcceso n : lista) {
            modeloTabla.addRow(new Object[]{
                n.getId_nivel(),
                n.getNombre_nivel(),
                n.getDescripcion()

            }
            );
        }

        vista.tbDatos.setModel(modeloTabla);
    }

// Guardar o actualizar registro
    private void guardar() {
        String nombre = vista.tfNombre.getText().trim();
        String descripcion = vista.tfCodigo.getText().trim();

        // Validación
        if (nombre.isEmpty() || descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe llenar todos los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        NivelAcceso nivel = new NivelAcceso();
        nivel.setNombre_nivel(nombre);
        nivel.setDescripcion(descripcion);

        boolean resultado;
        if (idSeleccionado == -1) { // nuevo registro
            resultado = dao.insertar(nivel);
            if (resultado) {
                JOptionPane.showMessageDialog(null, "Nivel agregado correctamente.");
            }
        } else { // actualización
            nivel.setId_nivel(idSeleccionado);
            resultado = dao.actualizar(nivel);
            if (resultado) {
                JOptionPane.showMessageDialog(null, "Nivel actualizado correctamente.");
            }
        }

        if (resultado) {
            mostrarDatos();
            limpiarCampos();
        }
    }

    // Eliminar registro
    private void eliminar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "¿Seguro que desea eliminar este registro?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean resultado = dao.eliminar(idSeleccionado);
            if (resultado) {
                JOptionPane.showMessageDialog(null, "Registro eliminado correctamente.");
                mostrarDatos();
                limpiarCampos();
            }
        }
    }

    // Seleccionar fila en la tabla
    private void seleccionarFila() {
        int fila = vista.tbDatos.getSelectedRow();
        if (fila >= 0) {
            idSeleccionado = Integer.parseInt(vista.tbDatos.getValueAt(fila, 0).toString());
            String nombre = vista.tbDatos.getValueAt(fila, 1).toString();
            String descripcion = vista.tbDatos.getValueAt(fila, 2).toString();

            vista.tfNombre.setText(nombre);
            vista.tfCodigo.setText(descripcion);

            // Cambiar texto del botón a "Actualizar"
            vista.btnAgregar.setText("Actualizar");
        }
    }

    // Limpiar campos
    private void limpiarCampos() {
        vista.tfNombre.setText("");
        vista.tfCodigo.setText("");
        idSeleccionado = -1;
        vista.tbDatos.clearSelection();

        // Cambiar texto del botón de nuevo a "Agregar"
        vista.btnAgregar.setText("Agregar");
    }
}
