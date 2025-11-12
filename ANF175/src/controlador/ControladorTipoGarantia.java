package controlador;

import dao.Conexion;
import dao.TipoGarantiaDAO;
import modelo.TipoGarantia;

import java.awt.event.*;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import vista.VistaTipoGarantia;

public class ControladorTipoGarantia implements ActionListener {

    private VistaTipoGarantia vista;
    private TipoGarantiaDAO dao;
    private DefaultTableModel modeloTabla;
    private int idSeleccionado = -1;
    Conexion conexion = new Conexion();

    public ControladorTipoGarantia(VistaTipoGarantia vista) {
        this.vista = vista;
        dao = new TipoGarantiaDAO(conexion.getConnection());

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
        String[] columnas = {"ID", "Nombre"};
        modeloTabla = new DefaultTableModel(null, columnas);
        List<TipoGarantia> lista = dao.listar();
        for (TipoGarantia t : lista) {
            modeloTabla.addRow(new Object[]{
                t.getId_tipo_garantia(),
                t.getNombre_tipo()
            });
        }

        vista.tbDatos.setModel(modeloTabla);
    }

    // Guardar o actualizar registro
    private void guardar() {
        String nombre = vista.tfNombre.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el nombre del tipo de garantía.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        TipoGarantia tipo = new TipoGarantia();
        tipo.setNombre_tipo(nombre);

        boolean resultado;
        if (idSeleccionado == -1) { // nuevo registro
            resultado = dao.insertar(tipo);
            if (resultado) {
                JOptionPane.showMessageDialog(null, "Tipo de garantía agregado correctamente.");
            }
        } else { // actualización
            tipo.setId_tipo_garantia(idSeleccionado);
            resultado = dao.actualizar(tipo);
            if (resultado) {
                JOptionPane.showMessageDialog(null, "Tipo de garantía actualizado correctamente.");
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

        int confirm = JOptionPane.showConfirmDialog(null, "¿Seguro que desea eliminar este tipo de garantía?", "Confirmar", JOptionPane.YES_NO_OPTION);
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

            vista.tfNombre.setText(nombre);
            vista.btnAgregar.setText("Actualizar");
        }
    }

    // Limpiar campos
    private void limpiarCampos() {
        vista.tfNombre.setText("");
        idSeleccionado = -1;
        vista.tbDatos.clearSelection();
        vista.btnAgregar.setText("Agregar");
    }
}
