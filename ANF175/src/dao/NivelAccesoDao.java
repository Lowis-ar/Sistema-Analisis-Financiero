package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.NivelAcceso;

public class NivelAccesoDao {

    private Connection conexion;

    public NivelAccesoDao(Connection conexion) {
        this.conexion = conexion;
    }

    // Insertar un nuevo nivel
    public boolean insertar(NivelAcceso nivel) {
        String sql = "INSERT INTO niveles_acceso (nombre_nivel, descripcion) VALUES (?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nivel.getNombre_nivel());
            ps.setString(2, nivel.getDescripcion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Listar todos los niveles
    public List<NivelAcceso> listar() {
        List<NivelAcceso> lista = new ArrayList<>();
        String sql = "SELECT * FROM niveles_acceso";
        try (Statement st = conexion.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                NivelAcceso n = new NivelAcceso(
                        rs.getInt("id_nivel"),
                        rs.getString("nombre_nivel"),
                        rs.getString("descripcion")
                );
                lista.add(n);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Buscar por ID
    public NivelAcceso buscarPorId(int id) {
        String sql = "SELECT * FROM niveles_acceso WHERE id_nivel = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new NivelAcceso(
                        rs.getInt("id_nivel"),
                        rs.getString("nombre_nivel"),
                        rs.getString("descripcion")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Actualizar nivel
    public boolean actualizar(NivelAcceso nivel) {
        String sql = "UPDATE niveles_acceso SET nombre_nivel=?, descripcion=? WHERE id_nivel=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nivel.getNombre_nivel());
            ps.setString(2, nivel.getDescripcion());
            ps.setInt(3, nivel.getId_nivel());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar nivel
    public boolean eliminar(int id) {
        String sql = "DELETE FROM niveles_acceso WHERE id_nivel=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
