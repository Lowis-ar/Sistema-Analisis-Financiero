package vista;

import controlador.ControladorTipoGarantia;
import dao.Conexion;
import dao.TipoGarantiaDAO;

public class test {

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Crear vista
                VistaTipoGarantia vista = new VistaTipoGarantia();

                // Crear conexión
                Conexion conexion = new Conexion();

                // Crear DAO
                TipoGarantiaDAO tipoGarantiaDao = new TipoGarantiaDAO(conexion.getConnection());

                // Crear controlador
                new ControladorTipoGarantia(vista);

                // Mostrar ventana
                vista.setVisible(true);
            }
        });
    }
}
