package dao;

import dto.ClienteDTO;
import dto.PrestamoDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Garantia;
import modelo.TipoGarantia;

public class GarantiaDAO {

    private Connection conexion;

    public GarantiaDAO(Connection conexion) {
        this.conexion = conexion;
    }

    // Insertar garantía
    public boolean insertar(Garantia g) {
        String sql = "INSERT INTO garantias (id_prestamo, id_tipo_garantia, descripcion, valor_estimado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, g.getId_prestamo());
            ps.setInt(2, g.getId_tipo_garantia());
            ps.setString(3, g.getDescripcion());
            ps.setDouble(4, g.getValor_estimado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Listar garantías con el tipo
    public List<Garantia> listar() {
        List<Garantia> lista = new ArrayList<>();
        String sql = """
            SELECT g.*, t.nombre_tipo
            FROM garantias g
            LEFT JOIN tipo_garantias t ON g.id_tipo_garantia = t.id_tipo_garantia
        """;
        try (Statement st = conexion.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Garantia g = new Garantia();
                g.setId_garantia(rs.getInt("id_garantia"));
                g.setId_prestamo(rs.getInt("id_prestamo"));
                g.setId_tipo_garantia(rs.getInt("id_tipo_garantia"));
                g.setDescripcion(rs.getString("descripcion"));
                g.setValor_estimado(rs.getDouble("valor_estimado"));
                lista.add(g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Buscar por ID
    public Garantia buscarPorId(int id) {
        String sql = "SELECT * FROM garantias WHERE id_garantia = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Garantia g = new Garantia();
                g.setId_garantia(rs.getInt("id_garantia"));
                g.setId_prestamo(rs.getInt("id_prestamo"));
                g.setId_tipo_garantia(rs.getInt("id_tipo_garantia"));
                g.setDescripcion(rs.getString("descripcion"));
                g.setValor_estimado(rs.getDouble("valor_estimado"));
                return g;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Actualizar garantía
    public boolean actualizar(Garantia g) {
        String sql = "UPDATE garantias SET id_prestamo=?, id_tipo_garantia=?, descripcion=?, valor_estimado=? WHERE id_garantia=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, g.getId_prestamo());
            ps.setInt(2, g.getId_tipo_garantia());
            ps.setString(3, g.getDescripcion());
            ps.setDouble(4, g.getValor_estimado());
            ps.setInt(5, g.getId_garantia());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar garantía
    public boolean eliminar(int id) {
        String sql = "DELETE FROM garantias WHERE id_garantia=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<ClienteDTO> listaClientes() {
        ArrayList<ClienteDTO> lista = new ArrayList<>();
        String sql = "select c.id_cliente, c.nombre from clientes c ORDER BY c.nombre";
        try (Statement st = conexion.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                ClienteDTO c = new ClienteDTO();
                c.setIdCliente(rs.getInt("id_cliente"));
                c.setNombre(rs.getString("nombre"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public ArrayList<PrestamoDTO> listaPrestamos(int idCliente) {
        ArrayList<PrestamoDTO> lista = new ArrayList<>();

        String sql = """
                 SELECT p.id_prestamo, p.monto
                 FROM prestamos p
                 WHERE p.id_cliente = ?
                 """;
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PrestamoDTO p = new PrestamoDTO();
                    p.setId_prestamo(rs.getInt("id_prestamo"));
                    p.setMonto(rs.getFloat("monto"));
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Listar todos los tipos
    public List<TipoGarantia> listarTipoGarantia() {
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

}
