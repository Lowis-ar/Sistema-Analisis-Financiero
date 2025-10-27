package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    // Parámetros de conexión
    String url = "jdbc:mysql://localhost:3306/sistema_financiero";
    String usuario = "root"; 
    String contraseña = "";  
    Connection conexion;

    // Constructor
    public Conexion() {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Crear la conexión
            conexion = DriverManager.getConnection(url, usuario, contraseña);
            System.out.println("Conexión exitosa a la base de datos");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Driver no encontrado");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error: No se pudo conectar a la base de datos");
            e.printStackTrace();
        }
    }

    // Método para obtener la conexión
    public Connection getConnection() {
        return conexion;
    }

    // Método para cerrar la conexión
    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
