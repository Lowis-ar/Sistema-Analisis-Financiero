package controlador;

import dao.NivelAccesoDao;
import dao.UsuarioDao;
import modelo.NivelAcceso;
import modelo.Usuario;
import vista.VistaUsuario;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladorUsuario implements ActionListener {

    private VistaUsuario vista;
    private UsuarioDao usuarioDao;
    private NivelAccesoDao nivelDao;
    private int idSeleccionado = -1; // id del usuario seleccionado
    private Map<String, Integer> nivelesMap = new HashMap<>(); // para almacenar id de niveles
    private ButtonGroup grupoEstado;

    public ControladorUsuario(VistaUsuario vista, UsuarioDao usuarioDao, NivelAccesoDao nivelDao) {
        this.vista = vista;
        this.usuarioDao = usuarioDao;
        this.nivelDao = nivelDao;

        // Botones
        this.vista.btnAgregar.addActionListener(this);
        this.vista.btnCancelar.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);

        // Solo se pueda seleccionar uno: Activo/Inactivo
        grupoEstado = new ButtonGroup();
        grupoEstado.add(vista.rbActivo);
        grupoEstado.add(vista.rbInactivo);

        // Llenar combo box de niveles
        llenarComboNiveles();

        // Click en tabla
        this.vista.tbDatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                seleccionarFila();
            }
        });

        mostrarDatos();
    }

    private void llenarComboNiveles() {
        vista.cbNiveles.removeAllItems();
        List<NivelAcceso> lista = nivelDao.listar();
        for (NivelAcceso n : lista) {
            vista.cbNiveles.addItem(n.getNombre_nivel());
            nivelesMap.put(n.getNombre_nivel(), n.getId_nivel());
        }
    }

    private void mostrarDatos() {
        String[] columnas = {"ID", "Nombre", "Nivel", "Estado"};
        javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel(null, columnas);

        for (Usuario u : usuarioDao.listar()) {
            String estadoStr = u.isEstado() ? "Activo" : "Inactivo";
            String nivelNombre = nivelDao.buscarPorId(u.getId_nivel()).getNombre_nivel();
            modelo.addRow(new Object[]{
                u.getId_usuario(),
                u.getNombre_usuario(),
                nivelNombre,
                estadoStr
            });
        }
        vista.tbDatos.setModel(modelo);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnAgregar) {
            guardarOActualizar();
        } else if (e.getSource() == vista.btnCancelar) {
            limpiarCampos();
        } else if (e.getSource() == vista.btnEliminar) {
            eliminar();
        }
    }

    private void guardarOActualizar() {
        String nombre = vista.tfNombre.getText().trim();
        String password = new String(vista.tfPassword.getPassword()).trim();
        String nivelSeleccionado = (String) vista.cbNiveles.getSelectedItem();
        boolean estado = vista.rbActivo.isSelected();

        // Validación
        if (nombre.isEmpty() || password.isEmpty() || nivelSeleccionado == null) {
            JOptionPane.showMessageDialog(null, "Debe llenar todos los campos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario u = new Usuario();
        u.setNombre_usuario(nombre);
        u.setContraseña(password);
        u.setId_nivel(nivelesMap.get(nivelSeleccionado));
        u.setEstado(estado);

        boolean resultado;
        if (idSeleccionado == -1) {
            resultado = usuarioDao.insertar(u);
            if (resultado) {
                JOptionPane.showMessageDialog(null, "Usuario agregado correctamente");
            }
        } else {
            u.setId_usuario(idSeleccionado);
            resultado = usuarioDao.actualizar(u);
            if (resultado) {
                JOptionPane.showMessageDialog(null, "Usuario actualizado correctamente");
            }
        }

        if (resultado) {
            mostrarDatos();
            limpiarCampos();
        }
    }

    private void eliminar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un usuario para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "¿Seguro que desea eliminar este usuario?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean resultado = usuarioDao.eliminar(idSeleccionado);
            if (resultado) {
                JOptionPane.showMessageDialog(null, "Usuario eliminado correctamente");
                mostrarDatos();
                limpiarCampos();
            }
        }
    }

    private void seleccionarFila() {
        int fila = vista.tbDatos.getSelectedRow();
        if (fila >= 0) {
            idSeleccionado = (int) vista.tbDatos.getValueAt(fila, 0);
            vista.tfNombre.setText((String) vista.tbDatos.getValueAt(fila, 1));

            // Obtener usuario completo desde la base para recuperar la contraseña
            Usuario u = usuarioDao.buscarPorId(idSeleccionado);
            if (u != null) {
                vista.tfPassword.setText(u.getContraseña());
            }

            String nivel = (String) vista.tbDatos.getValueAt(fila, 2);
            vista.cbNiveles.setSelectedItem(nivel);

            String estado = (String) vista.tbDatos.getValueAt(fila, 3);
            if ("Activo".equals(estado)) {
                vista.rbActivo.setSelected(true);
            } else {
                vista.rbInactivo.setSelected(true);
            }

            // Cambiar texto del botón
            vista.btnAgregar.setText("Actualizar");
        }
    }

    private void limpiarCampos() {
        idSeleccionado = -1;
        vista.tfNombre.setText("");
        vista.tfPassword.setText("");
        if (vista.cbNiveles.getItemCount() > 0) {
            vista.cbNiveles.setSelectedIndex(0);
        }
        grupoEstado.clearSelection();
        vista.btnAgregar.setText("Agregar");
        vista.tbDatos.clearSelection();
    }
}
