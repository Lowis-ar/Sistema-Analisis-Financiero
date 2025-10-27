package dao;

public class TestConexion {

    public static void main(String[] args) {
        // Crear instancia de la conexión
        Conexion conexion = new Conexion();

        // Verificar si la conexión no es nula
        if (conexion.getConnection() != null) {
            System.out.println("¡La conexión a la base de datos funciona correctamente!");
        } else {
            System.out.println("La conexión falló.");
        }

        // Cerrar la conexión
        conexion.cerrarConexion();
    }
}
