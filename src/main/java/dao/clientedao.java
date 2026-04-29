package dao;

import Modelo.cliente;
import util.connectionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author User
 */
public class clientedao  {
 
    private cliente mapear(ResultSet rs) throws SQLException {
        cliente c = new cliente();
        c.setIdCliente(rs.getInt("id_cliente"));
        c.setNombres(rs.getString("nombres"));
        c.setApellidos(rs.getString("apellidos"));
        c.setCedula(rs.getString("cedula"));
        c.setTelefono(rs.getString("telefono"));
        c.setEmail(rs.getString("email"));
        c.setDireccion(rs.getString("direccion"));
        c.setCreatedAt(rs.getString("created_at"));
        return c;
    }
 
    public void insertar(cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (nombres, apellidos, cedula, telefono, email, direccion) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
 
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
 
            ps.setString(1, cliente.getNombres());
            ps.setString(2, cliente.getApellidos());
            ps.setString(3, cliente.getCedula());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getEmail());       
            ps.setString(6, cliente.getDireccion());   
            ps.executeUpdate();
 
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    cliente.setIdCliente(keys.getInt(1));
                }
            }
        }
    }
 
    public Optional<cliente> buscarPorId(int idCliente) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE id_cliente = ?";
 
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
 
            ps.setInt(1, idCliente);
 
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }
 
    public Optional<cliente> buscarPorCedula(String cedula) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE cedula = ?";
 
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
 
            ps.setString(1, cedula);
 
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }
 
    public List<cliente> listarTodos() throws SQLException {
        String sql = "SELECT * FROM cliente ORDER BY apellidos ASC, nombres ASC";
        List<cliente> lista = new ArrayList<>();
 
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
 
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }
 
    public List<cliente> buscar(String termino) throws SQLException {
        String sql = "SELECT * FROM cliente " +
                     "WHERE cedula LIKE ? OR nombres LIKE ? OR apellidos LIKE ? " +
                     "ORDER BY apellidos ASC";
        List<cliente> lista = new ArrayList<>();
        String like = "%" + termino + "%";
 
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
 
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
 
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }
 
    public boolean actualizar(cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET nombres = ?, apellidos = ?, cedula = ?, " +
                     "telefono = ?, email = ?, direccion = ? WHERE id_cliente = ?";
 
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
 
            ps.setString(1, cliente.getNombres());
            ps.setString(2, cliente.getApellidos());
            ps.setString(3, cliente.getCedula());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getEmail());
            ps.setString(6, cliente.getDireccion());
            ps.setInt(7, cliente.getIdCliente());
 
            return ps.executeUpdate() > 0;
        }
    }
 
    public boolean eliminar(int idCliente) throws SQLException {
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";
 
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
 
            ps.setInt(1, idCliente);
            return ps.executeUpdate() > 0;
        }
    }
 
    public boolean existeCedula(String cedula) throws SQLException {
        String sql = "SELECT 1 FROM cliente WHERE cedula = ?";
 
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
 
            ps.setString(1, cedula);
 
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
 
    public boolean tieneVehiculos(int idCliente) throws SQLException {
        String sql = "SELECT 1 FROM vehiculo WHERE id_cliente = ? LIMIT 1";
 
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
 
            ps.setInt(1, idCliente);
 
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
