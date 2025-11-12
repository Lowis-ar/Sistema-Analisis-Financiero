package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.TipoGarantia;

public class TipoGarantiaDAO {

    private Connection conexion;

    public TipoGarantiaDAO(Connection conexion) {
        this.conexion = conexion;
    }

    // Insertar tipo de garantía
    public boolean insertar(TipoGarantia tipo) {
        String sql = "INSERT INTO tipo_garantias (nombre_tipo) VALUES (?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, tipo.getNombre_tipo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Listar todos los tipos
    public List<TipoGarantia> listar() {
        List<TipoGarantia> lista = new ArrayList<>();
        String sql = "SELECT * FROM tipo_garantias";
        try (Statement st = conexion.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                TipoGarantia t = new TipoGarantia();
                t.setId_tipo_garantia(rs.getInt("id_tipo_garantia"));
                t.setNombre_tipo(rs.getString("nombre_tipo"));
                lista.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Buscar por ID
    public TipoGarantia buscarPorId(int id) {
        String sql = "SELECT * FROM tipo_garantias WHERE id_tipo_garantia = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                TipoGarantia t = new TipoGarantia();
                t.setId_tipo_garantia(rs.getInt("id_tipo_garantia"));
                t.setNombre_tipo(rs.getString("nombre_tipo"));
                return t;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Actualizar tipo
    public boolean actualizar(TipoGarantia tipo) {
        String sql = "UPDATE tipo_garantias SET nombre_tipo=? WHERE id_tipo_garantia=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, tipo.getNombre_tipo());
            ps.setInt(2, tipo.getId_tipo_garantia());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar tipo
    public boolean eliminar(int id) {
        String sql = "DELETE FROM tipo_garantias WHERE id_tipo_garantia=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
