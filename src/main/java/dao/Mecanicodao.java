package dao;

import Modelo.Mecanico;
import util.connectionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Hamilton
 */
public class Mecanicodao {

    private static final String SELECT_BASE =
        "SELECT m.id_mecanico, m.cedula, m.especialidad, m.telefono, " +
        "       u.id_usuario, u.nombre, u.email, u.activo " +
        "FROM mecanico m " +
        "JOIN usuario u ON m.id_usuario = u.id_usuario ";

    private Mecanico mapear(ResultSet rs) throws SQLException {
        Mecanico m = new Mecanico();
        m.setId(rs.getInt("id_mecanico"));
        m.setIdUsuario(rs.getInt("id_usuario"));
        // nombre completo viene de usuario — separamos por el primer espacio
        String nombreCompleto = rs.getString("nombre");
        if (nombreCompleto != null && nombreCompleto.contains(" ")) {
            int idx = nombreCompleto.indexOf(" ");
            m.setNombre(nombreCompleto.substring(0, idx));
            m.setApellido(nombreCompleto.substring(idx + 1));
        } else {
            m.setNombre(nombreCompleto != null ? nombreCompleto : "");
            m.setApellido("");
        }
        m.setEmail(rs.getString("email"));
        m.setCedula(rs.getString("cedula"));
        m.setEspecialidad(rs.getString("especialidad"));
        m.setTelefono(rs.getString("telefono"));
        m.setActivo(rs.getBoolean("activo"));
        return m;
    }

    public void insertar(Mecanico mecanico) throws SQLException {
        int idRol = obtenerIdRol("mecanico");
        Connection cn = null;
        try {
            cn = connectionBD.getConnection();
            cn.setAutoCommit(false);

            // Guardamos nombre + apellido concatenados en usuario.nombre
            String nombreCompleto = mecanico.getNombre() + " " + mecanico.getApellido();

            String sqlUsuario = "INSERT INTO usuario (id_rol, nombre, email, password_hash, activo) " +
                                "VALUES (?, ?, ?, ?, 1)";
            int idUsuario;
            try (PreparedStatement ps = cn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, idRol);
                ps.setString(2, nombreCompleto.trim());
                ps.setString(3, mecanico.getEmail());
                ps.setString(4, mecanico.getPasswordHash());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) idUsuario = keys.getInt(1);
                    else throw new SQLException("No se generó id de usuario");
                }
            }

            String sqlMecanico = "INSERT INTO mecanico (id_usuario, cedula, especialidad, telefono) " +
                                 "VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = cn.prepareStatement(sqlMecanico, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, idUsuario);
                ps.setString(2, mecanico.getCedula());
                ps.setString(3, mecanico.getEspecialidad());
                ps.setString(4, mecanico.getTelefono());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) mecanico.setId(keys.getInt(1));
                }
            }

            mecanico.setIdUsuario(idUsuario);
            cn.commit();

        } catch (SQLException e) {
            if (cn != null) cn.rollback();
            throw e;
        } finally {
            if (cn != null) { cn.setAutoCommit(true); cn.close(); }
        }
    }

    public Optional<Mecanico> buscarPorId(int idMecanico) throws SQLException {
        String sql = SELECT_BASE + "WHERE m.id_mecanico = ? AND u.activo = 1";
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idMecanico);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    public List<Mecanico> listarTodos() throws SQLException {
        String sql = SELECT_BASE + "WHERE u.activo = 1 ORDER BY u.nombre ASC";
        List<Mecanico> lista = new ArrayList<>();
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Mecanico> buscar(String termino) throws SQLException {
        String sql = SELECT_BASE +
                     "WHERE u.activo = 1 AND (m.cedula LIKE ? OR u.nombre LIKE ? OR m.especialidad LIKE ?) " +
                     "ORDER BY u.nombre ASC";
        List<Mecanico> lista = new ArrayList<>();
        String like = "%" + termino + "%";
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, like); ps.setString(2, like); ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public boolean actualizar(Mecanico mecanico) throws SQLException {
        Connection cn = null;
        try {
            cn = connectionBD.getConnection();
            cn.setAutoCommit(false);

            String nombreCompleto = mecanico.getNombre() + " " + mecanico.getApellido();
            String sqlUsuario = "UPDATE usuario SET nombre = ?, email = ? WHERE id_usuario = ?";
            try (PreparedStatement ps = cn.prepareStatement(sqlUsuario)) {
                ps.setString(1, nombreCompleto.trim());
                ps.setString(2, mecanico.getEmail());
                ps.setInt(3, mecanico.getIdUsuario());
                ps.executeUpdate();
            }

            String sqlMecanico = "UPDATE mecanico SET cedula = ?, especialidad = ?, telefono = ? WHERE id_mecanico = ?";
            try (PreparedStatement ps = cn.prepareStatement(sqlMecanico)) {
                ps.setString(1, mecanico.getCedula());
                ps.setString(2, mecanico.getEspecialidad());
                ps.setString(3, mecanico.getTelefono());
                ps.setInt(4, mecanico.getId());
                ps.executeUpdate();
            }

            cn.commit();
            return true;
        } catch (SQLException e) {
            if (cn != null) cn.rollback();
            throw e;
        } finally {
            if (cn != null) { cn.setAutoCommit(true); cn.close(); }
        }
    }

    public boolean eliminar(int idMecanico) throws SQLException {
        String sql = "UPDATE usuario u JOIN mecanico m ON u.id_usuario = m.id_usuario " +
                     "SET u.activo = 0 WHERE m.id_mecanico = ?";
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idMecanico);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean existeCedula(String cedula) throws SQLException {
        String sql = "SELECT 1 FROM mecanico WHERE cedula = ?";
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, cedula);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    private int obtenerIdRol(String nombreRol) throws SQLException {
        String sql = "SELECT id_rol FROM rol WHERE nombre = ?";
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nombreRol);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id_rol");
                throw new SQLException("Rol no encontrado: " + nombreRol);
            }
        }
    }
}