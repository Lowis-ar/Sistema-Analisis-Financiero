package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Usuario;

public class UsuarioDao {

    private Connection conexion;

    public UsuarioDao(Connection conexion) {
        this.conexion = conexion;
    }

    // Insertar usuario
    public boolean insertar(Usuario u) {
        String sql = "INSERT INTO usuarios (nombre_usuario, contraseña, id_nivel, estado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, u.getNombre_usuario());
            ps.setString(2, u.getContraseña());
            ps.setInt(3, u.getId_nivel());
            ps.setBoolean(4, u.isEstado()); // Cambiado a boolean
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Listar usuarios con su nivel
    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = """
            SELECT u.*, n.nombre_nivel 
            FROM usuarios u
            LEFT JOIN niveles_acceso n ON u.id_nivel = n.id_nivel
        """;
        try (Statement st = conexion.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Usuario u = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre_usuario"),
                        rs.getString("contraseña"),
                        rs.getInt("id_nivel"),
                        rs.getBoolean("estado") // Cambiado a boolean
                );
                lista.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Buscar usuario por ID
    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre_usuario"),
                        rs.getString("contraseña"),
                        rs.getInt("id_nivel"),
                        rs.getBoolean("estado") // Cambiado a boolean
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Actualizar usuario
    public boolean actualizar(Usuario u) {
        String sql = "UPDATE usuarios SET nombre_usuario=?, contraseña=?, id_nivel=?, estado=? WHERE id_usuario=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, u.getNombre_usuario());
            ps.setString(2, u.getContraseña());
            ps.setInt(3, u.getId_nivel());
            ps.setBoolean(4, u.isEstado()); // Cambiado a boolean
            ps.setInt(5, u.getId_usuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar usuario
    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id_usuario=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
