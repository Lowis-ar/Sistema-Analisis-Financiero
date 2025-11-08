package dao;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import modelo.ClienteNatural;
import modelo.LugarTrabajo;

public class ClienteNaturalDAO {
    private Connection conexion;

    public ClienteNaturalDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public String generarCodigoCliente() throws SQLException {
        String sql = "SELECT COUNT(*) + 1 as siguiente FROM clientes WHERE tipo_cliente = 'Natural'";
        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return "NAT-" + String.format("%06d", rs.getInt("siguiente"));
            }
        }
        return "NAT-000001";
    }

    public boolean insertarClienteNatural(ClienteNatural cliente) throws SQLException {
        conexion.setAutoCommit(false);
        try {
            // Insertar en tabla clientes
            String sqlCliente = "INSERT INTO clientes (codigo_cliente, tipo_cliente, nombre, direccion, telefono, estado) VALUES (?, 'Natural', ?, ?, ?, 'Activo')";
            try (PreparedStatement stmt = conexion.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, cliente.getCodigoCliente());
                stmt.setString(2, cliente.getNombre());
                stmt.setString(3, cliente.getDireccion());
                stmt.setString(4, cliente.getTelefono());
                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        cliente.setIdCliente(generatedKeys.getInt(1));
                    }
                }
            }

            // Insertar en tabla clientes_naturales
            String sqlNatural = "INSERT INTO clientes_naturales (id_cliente, dui, estado_civil, ingresos, egresos) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conexion.prepareStatement(sqlNatural)) {
                stmt.setInt(1, cliente.getIdCliente());
                stmt.setString(2, cliente.getDui());
                stmt.setString(3, cliente.getEstadoCivil());
                stmt.setBigDecimal(4, cliente.getIngresos());
                stmt.setBigDecimal(5, cliente.getEgresos());
                stmt.executeUpdate();
            }

            // Insertar lugares de trabajo
            String sqlTrabajo = "INSERT INTO lugares_trabajo (id_cliente, nombre_empresa, direccion_empresa, telefono_empresa) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conexion.prepareStatement(sqlTrabajo)) {
                for (LugarTrabajo trabajo : cliente.getLugaresTrabajo()) {
                    stmt.setInt(1, cliente.getIdCliente());
                    stmt.setString(2, trabajo.getNombreEmpresa());
                    stmt.setString(3, trabajo.getDireccionEmpresa());
                    stmt.setString(4, trabajo.getTelefonoEmpresa());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            conexion.commit();
            return true;
        } catch (SQLException e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    public boolean existeDUI(String dui) throws SQLException {
        String sql = "SELECT COUNT(*) FROM clientes_naturales WHERE dui = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, dui);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public BigDecimal calcularMaximoPrestamo(BigDecimal ingresos, BigDecimal egresos) {
        // Lógica de cálculo: máximo 12 veces la capacidad de pago mensual
        BigDecimal capacidadPago = ingresos.subtract(egresos);
        if (capacidadPago.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        return capacidadPago.multiply(new BigDecimal("12"));
    }

    public BigDecimal calcularCuota(BigDecimal monto, int plazoMeses, BigDecimal tasaInteres) {
        if (monto.compareTo(BigDecimal.ZERO) <= 0 || plazoMeses <= 0) {
            return BigDecimal.ZERO;
        }
        
        // Fórmula de cuota fija: Cuota = P * (i(1+i)^n) / ((1+i)^n - 1)
        BigDecimal tasaMensual = tasaInteres.divide(new BigDecimal("1200"), 10, BigDecimal.ROUND_HALF_UP);
        BigDecimal factor = BigDecimal.ONE.add(tasaMensual).pow(plazoMeses);
        BigDecimal numerador = monto.multiply(tasaMensual).multiply(factor);
        BigDecimal denominador = factor.subtract(BigDecimal.ONE);
        
        return numerador.divide(denominador, 2, BigDecimal.ROUND_HALF_UP);
    }
}