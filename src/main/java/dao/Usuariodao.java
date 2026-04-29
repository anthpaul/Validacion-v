package dao;

import enums.Rol;
import Modelo.usuario;
import util.connectionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Anthony Delgado
 */
public class Usuariodao {
    private static final String SELECT_BASE =
        "SELECT u.*, r.nombre AS rol " +
        "FROM usuario u " +
        "JOIN rol r ON u.id_rol = r.id_rol ";

    private usuario mapear(ResultSet rs) throws SQLException {
        usuario u = new usuario();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setNombre(rs.getString("nombre"));
        u.setEmail(rs.getString("email"));
        u.actualizarPasswordHash(rs.getString("password_hash"));
        u.setRol(Rol.fromValor(rs.getString("rol"))); 
        u.setActivo(rs.getBoolean("activo"));
        return u;
    }

    public void insertar(usuario usuario) throws SQLException {
        // Primero obtener el id_rol desde la tabla rol
        String sqlRol = "SELECT id_rol FROM rol WHERE nombre = ?";
        int idRol = 0;

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sqlRol)) {
            ps.setString(1, usuario.getRol().getValor());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) idRol = rs.getInt("id_rol");
                else throw new SQLException("Rol no encontrado: " + usuario.getRol().getValor());
            }
        }

        String sql = "INSERT INTO usuario (id_rol, nombre, email, password_hash, activo) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, idRol);
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getPasswordHash());
            ps.setBoolean(5, usuario.isActivo());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) usuario.setIdUsuario(keys.getInt(1));
            }
        }
    }

    public Optional<usuario> buscarPorId(int idUsuario) throws SQLException {
        String sql = SELECT_BASE + "WHERE u.id_usuario = ?";

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    public Optional<usuario> buscarPorEmail(String email) throws SQLException {
        String sql = SELECT_BASE + "WHERE u.email = ? AND u.activo = 1";

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, email.toLowerCase().trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    public List<usuario> listarTodos() throws SQLException {
        String sql = SELECT_BASE + "ORDER BY u.nombre ASC";
        List<usuario> lista = new ArrayList<>();

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<usuario> listarPorRol(Rol rol) throws SQLException {
        String sql = SELECT_BASE + "WHERE r.nombre = ? AND u.activo = 1 ORDER BY u.nombre ASC";
        List<usuario> lista = new ArrayList<>();

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, rol.getValor());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public boolean actualizar(usuario usuario) throws SQLException {
        // Obtener id_rol
        String sqlRol = "SELECT id_rol FROM rol WHERE nombre = ?";
        int idRol = 0;

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sqlRol)) {
            ps.setString(1, usuario.getRol().getValor());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) idRol = rs.getInt("id_rol");
            }
        }

        String sql = "UPDATE usuario SET id_rol = ?, nombre = ?, email = ?, activo = ? " +
                     "WHERE id_usuario = ?";

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idRol);
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getEmail());
            ps.setBoolean(4, usuario.isActivo());
            ps.setInt(5, usuario.getIdUsuario());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizarPassword(int idUsuario, String nuevoHash) throws SQLException {
        String sql = "UPDATE usuario SET password_hash = ? WHERE id_usuario = ?";

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nuevoHash);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean desactivar(int idUsuario) throws SQLException {
        String sql = "UPDATE usuario SET activo = 0 WHERE id_usuario = ?";

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean existeEmail(String email) throws SQLException {
        String sql = "SELECT 1 FROM usuario WHERE email = ?";

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, email.toLowerCase().trim());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}