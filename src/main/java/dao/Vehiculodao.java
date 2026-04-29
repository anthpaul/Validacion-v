package dao;
import Modelo.vehiculo;
import util.connectionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
 
/**
 *
 * @author 
 */
public class Vehiculodao {

    private vehiculo mapear(ResultSet rs) throws SQLException {
        vehiculo v = new vehiculo();
        v.setIdVehiculo(rs.getInt("id_vehiculo"));
        v.setIdCliente(rs.getInt("id_cliente"));
        v.setPlaca(rs.getString("placa"));
        v.setMarca(rs.getString("marca"));
        v.setModelo(rs.getString("modelo"));
        v.setAnio(rs.getInt("anio"));
        v.setColor(rs.getString("color"));
        v.setKilometraje(rs.getInt("kilometraje"));
        return v;
    }

    public void insertar(vehiculo vehiculo) throws SQLException {
        String sql = "INSERT INTO vehiculo (id_cliente, placa, marca, modelo, anio, color, kilometraje) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
 
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
 
            ps.setInt(1, vehiculo.getIdCliente());
            ps.setString(2, vehiculo.getPlaca());
            ps.setString(3, vehiculo.getMarca());
            ps.setString(4, vehiculo.getModelo());
            ps.setInt(5, vehiculo.getAnio());
            ps.setString(6, vehiculo.getColor());     
            ps.setInt(7, vehiculo.getKilometraje());
            ps.executeUpdate();
 
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) vehiculo.setIdVehiculo(keys.getInt(1));
            }
        }
    }

    public Optional<vehiculo> buscarPorId(int idVehiculo) throws SQLException {
        String sql = "SELECT * FROM vehiculo WHERE id_vehiculo = ?";
 
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
 
            ps.setInt(1, idVehiculo);
 
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }
 
    public Optional<vehiculo> buscarPorPlaca(String placa) throws SQLException {
        String sql = "SELECT * FROM vehiculo WHERE placa = ?";
 
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
 
            ps.setString(1, placa.toUpperCase().trim());
 
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }
 
    public List<vehiculo> listarPorCliente(int idCliente) throws SQLException {
        String sql = "SELECT * FROM vehiculo WHERE id_cliente = ? ORDER BY marca ASC";
        List<vehiculo> lista = new ArrayList<>();
 
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
 
            ps.setInt(1, idCliente);
 
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }
 
    public List<vehiculo> listarTodos() throws SQLException {
        String sql = "SELECT * FROM vehiculo ORDER BY marca ASC, modelo ASC";
        List<vehiculo> lista = new ArrayList<>();
 
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
 
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }
 
    public boolean actualizar(vehiculo vehiculo) throws SQLException {
        String sql = "UPDATE vehiculo SET id_cliente = ?, placa = ?, marca = ?, modelo = ?, " +
                     "anio = ?, color = ?, kilometraje = ? WHERE id_vehiculo = ?";
 
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
 
            ps.setInt(1, vehiculo.getIdCliente());
            ps.setString(2, vehiculo.getPlaca());
            ps.setString(3, vehiculo.getMarca());
            ps.setString(4, vehiculo.getModelo());
            ps.setInt(5, vehiculo.getAnio());
            ps.setString(6, vehiculo.getColor());
            ps.setInt(7, vehiculo.getKilometraje());
            ps.setInt(8, vehiculo.getIdVehiculo());
 
            return ps.executeUpdate() > 0;
        }
    }
 
    public boolean eliminar(int idVehiculo) throws SQLException {
        String sql = "DELETE FROM vehiculo WHERE id_vehiculo = ?";
 
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
 
            ps.setInt(1, idVehiculo);
            return ps.executeUpdate() > 0;
        }
    }
 
    public boolean existePlaca(String placa) throws SQLException {
        String sql = "SELECT 1 FROM vehiculo WHERE placa = ?";
 
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
 
            ps.setString(1, placa.toUpperCase().trim());
 
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
