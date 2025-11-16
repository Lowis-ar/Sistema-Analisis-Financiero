package dao;

/**
 *
 * @author Kevin
 */
import modelo.Fiador;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FiadorDao {

    private Connection conexion;

    public FiadorDao(Connection conexion) {
        this.conexion = conexion;
    }

    // Insertar fiador
    public boolean insertar(Fiador fiador) {
        String sql = "INSERT INTO fiadores (id_prestamo, nombre, direccion, telefono, dui, ingresos, egresos) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, fiador.getId_prestamo());
            ps.setString(2, fiador.getNombre());
            ps.setString(3, fiador.getDireccion());
            ps.setString(4, fiador.getTelefono());
            ps.setString(5, fiador.getDui());
            ps.setDouble(6, fiador.getIngresos());
            ps.setDouble(7, fiador.getEgresos());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Listar todos los fiadores
    public List<Fiador> listar() {
        List<Fiador> lista = new ArrayList<>();
        String sql = "SELECT * FROM fiadores ORDER BY id_fiador ASC";
        try (Statement st = conexion.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Fiador fiador = mapearFiador(rs);
                lista.add(fiador);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Buscar fiador por ID
    public Fiador buscarPorId(int id) {
        String sql = "SELECT * FROM fiadores WHERE id_fiador = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapearFiador(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Actualizar fiador
    public boolean actualizar(Fiador fiador) {
        String sql = "UPDATE fiadores SET id_prestamo=?, nombre=?, direccion=?, telefono=?, dui=?, ingresos=?, egresos=? WHERE id_fiador=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, fiador.getId_prestamo());
            ps.setString(2, fiador.getNombre());
            ps.setString(3, fiador.getDireccion());
            ps.setString(4, fiador.getTelefono());
            ps.setString(5, fiador.getDui());
            ps.setDouble(6, fiador.getIngresos());
            ps.setDouble(7, fiador.getEgresos());
            ps.setInt(8, fiador.getId_fiador());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar fiador
    public boolean eliminar(int id) {
        String sql = "DELETE FROM fiadores WHERE id_fiador = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Buscar fiador por DUI
    public Fiador buscarPorDui(String dui) {
        String sql = "SELECT * FROM fiadores WHERE dui = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, dui);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapearFiador(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Listar fiadores por préstamo
    public List<Fiador> listarPorPrestamo(int idPrestamo) {
        List<Fiador> lista = new ArrayList<>();
        String sql = "SELECT * FROM fiadores WHERE id_prestamo = ? ORDER BY id_fiador DESC";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idPrestamo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Fiador fiador = mapearFiador(rs);
                lista.add(fiador);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Método auxiliar para mapear ResultSet a objeto Fiador
    private Fiador mapearFiador(ResultSet rs) throws SQLException {
        Fiador fiador = new Fiador();
        fiador.setId_fiador(rs.getInt("id_fiador"));
        fiador.setId_prestamo(rs.getInt("id_prestamo"));
        fiador.setNombre(rs.getString("nombre"));
        fiador.setDireccion(rs.getString("direccion"));
        fiador.setTelefono(rs.getString("telefono"));
        fiador.setDui(rs.getString("dui"));
        fiador.setIngresos(rs.getDouble("ingresos"));
        fiador.setEgresos(rs.getDouble("egresos"));
        return fiador;
    }

    // Verificar si existe un fiador con el mismo DUI (para evitar duplicados)
    public boolean existeDui(String dui, int idFiadorExcluir) {
        String sql = "SELECT COUNT(*) FROM fiadores WHERE dui = ? AND id_fiador != ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, dui);
            ps.setInt(2, idFiadorExcluir);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Obtener el último ID insertado (para mostrar en el formulario)
    public int obtenerUltimoId() {
        String sql = "SELECT MAX(id_fiador) FROM fiadores";
        try (Statement st = conexion.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

   public List<String[]> listarPrestamosParaCombo() {
    List<String[]> lista = new ArrayList<>();
    try {
        System.out.println("DEBUG: Ejecutando query para préstamos...");
        String sql = "SELECT id_prestamo, monto FROM prestamos WHERE estado = 'Normal' ORDER BY id_prestamo";
        
        try (Statement st = conexion.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                String id = String.valueOf(rs.getInt("id_prestamo"));
                String monto = String.valueOf(rs.getDouble("monto"));
                lista.add(new String[]{id, "Préstamo #" + id + " - $" + monto});
            }
        }
        
    } catch (SQLException e) {
        System.out.println("DEBUG: Error en listarPrestamosParaCombo: " + e.getMessage());
        e.printStackTrace();
    }
    return lista;
}

    public String obtenerNombrePrestamo(int idPrestamo) {
        String sql = "SELECT monto FROM prestamos WHERE id_prestamo = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idPrestamo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return "Préstamo #" + idPrestamo + " - $" + rs.getDouble("monto");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Préstamo #" + idPrestamo;
    }
}
