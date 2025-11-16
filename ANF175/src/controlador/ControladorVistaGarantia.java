package controlador;

import dao.GarantiaDAO;
import dto.ClienteDTO;
import dto.PrestamoDTO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Garantia;
import modelo.TipoGarantia;
import vista.VistaGarantia;

public class ControladorVistaGarantia {

    private VistaGarantia vista;
    private GarantiaDAO dao;
    private Connection conexion;

    // Para saber si estamos EDITANDO
    private Integer idSeleccionado = null;

    public ControladorVistaGarantia(VistaGarantia vista, Connection conexion) {
        this.vista = vista;
        this.conexion = conexion;
        this.dao = new GarantiaDAO(conexion);

        cargarClientes();
        cargarTipoGarantia();
        configurarEventos();
        listarGarantias();
    }

    private void configurarEventos() {
        vista.cbClientes.addActionListener(e -> cargarPrestamos());

        vista.tbDatos.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });

        vista.btnAgregar.addActionListener(e -> guardarGarantia());
        vista.btnEliminar.addActionListener(e -> eliminarGarantia());
        vista.btnCancelar.addActionListener(e -> limpiarCampos());
    }

    // ========================== CARGAS INICIALES ==========================
    private void cargarClientes() {
        vista.cbClientes.removeAllItems();
        ArrayList<ClienteDTO> clientes = dao.listaClientes();

        for (ClienteDTO c : clientes) {
            vista.cbClientes.addItem(c.getIdCliente() + " - " + c.getNombre());
        }
    }

    private void cargarPrestamos() {
        vista.cbPrestamos.removeAllItems();

        if (vista.cbClientes.getSelectedItem() == null) {
            return;
        }

        String texto = vista.cbClientes.getSelectedItem().toString();
        int idCliente = Integer.parseInt(texto.split(" - ")[0]);

        ArrayList<PrestamoDTO> prestamos = dao.listaPrestamos(idCliente);

        for (PrestamoDTO p : prestamos) {
            vista.cbPrestamos.addItem("PRESTAMO-" + p.getId_prestamo() + " - $" + p.getMonto());
        }
    }

    private void cargarTipoGarantia() {
        vista.cbTipoGarantia.removeAllItems();
        List<TipoGarantia> tipos = dao.listarTipoGarantia();

        for (TipoGarantia t : tipos) {
            vista.cbTipoGarantia.addItem(t.getId_tipo_garantia() + " - " + t.getNombre_tipo());
        }
    }

    // ========================== CRUD ==========================
    private void guardarGarantia() {
        try {
            Garantia g = new Garantia();

            // Prestamo
            String p = vista.cbPrestamos.getSelectedItem().toString();
            g.setId_prestamo(Integer.parseInt(p.split("-")[1].split(" ")[0]));

            // Tipo garantía
            String tg = vista.cbTipoGarantia.getSelectedItem().toString();
            g.setId_tipo_garantia(Integer.parseInt(tg.split(" - ")[0]));

            g.setDescripcion(vista.txDescripcion.getText());
            g.setValor_estimado(Double.parseDouble(vista.tfValorEstimado.getText()));

            boolean ok;

            // ---------- SI ES NUEVO ----------
            if (idSeleccionado == null) {
                ok = dao.insertar(g);
                if (ok) {
                    JOptionPane.showMessageDialog(null, "Garantía registrada.");
                }
            } // ---------- SI ES EDICIÓN ----------
            else {
                g.setId_garantia(idSeleccionado);
                ok = dao.actualizar(g);
                if (ok) {
                    JOptionPane.showMessageDialog(null, "Garantía actualizada.");
                }
            }

            limpiarCampos();
            listarGarantias();
            idSeleccionado = null;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private void eliminarGarantia() {
        int fila = vista.tbDatos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(null, "Seleccione una garantía para eliminar.");
            return;
        }

        int id = Integer.parseInt(vista.tbDatos.getValueAt(fila, 0).toString());

        if (dao.eliminar(id)) {
            JOptionPane.showMessageDialog(null, "Garantía eliminada.");
            listarGarantias();
        }
    }

    private void cargarSeleccion() {
        int fila = vista.tbDatos.getSelectedRow();
        if (fila < 0) {
            return;
        }

        // Guardamos ID para actualizar
        idSeleccionado = Integer.parseInt(vista.tbDatos.getValueAt(fila, 0).toString());

        int idPrestamo = Integer.parseInt(vista.tbDatos.getValueAt(fila, 1).toString());
        int idTipo = Integer.parseInt(vista.tbDatos.getValueAt(fila, 2).toString());

        seleccionarItemCombo(vista.cbPrestamos, "PRESTAMO-" + idPrestamo);
        seleccionarItemCombo(vista.cbTipoGarantia, idTipo + " -");

        vista.txDescripcion.setText(vista.tbDatos.getValueAt(fila, 3).toString());
        vista.tfValorEstimado.setText(vista.tbDatos.getValueAt(fila, 4).toString());
    }

    private void seleccionarItemCombo(javax.swing.JComboBox combo, String inicioTexto) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).toString().startsWith(inicioTexto)) {
                combo.setSelectedIndex(i);
                break;
            }
        }
    }

    // ========================== TABLA ==========================
    private void listarGarantias() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"ID", "Prestamo", "Tipo", "Descripción", "Valor"}, 0
        );

        for (Garantia g : dao.listar()) {
            modelo.addRow(new Object[]{
                g.getId_garantia(),
                g.getId_prestamo(),
                g.getId_tipo_garantia(),
                g.getDescripcion(),
                g.getValor_estimado()
            });
        }

        vista.tbDatos.setModel(modelo);
    }

    // ========================== UTILIDADES ==========================
    private void limpiarCampos() {
        // Limpiar campos de texto
        vista.txDescripcion.setText("");
        vista.tfValorEstimado.setText("");
        idSeleccionado = null;

        // Reiniciar ComboBox
        vista.cbClientes.removeAllItems();
        vista.cbPrestamos.removeAllItems();
        vista.cbTipoGarantia.removeAllItems();

        // Volver a cargar combos iniciales
        cargarClientes();        // esto rellena cbClientes
        cargarTipoGarantia();   // esto rellena cbTipoGarantia

       

        
    }

}
