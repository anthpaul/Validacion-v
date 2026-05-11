package dao;

import Modelo.OrdenTrabajo;
import util.connectionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Omar Mite
 */
public class OrdenTrabajodao {

    // JOIN para traer placa, cliente y nombre del mecánico
    private static final String SELECT_BASE =
        "SELECT ot.*, " +
        "v.placa, " +
        "CONCAT(c.nombres, ' ', c.apellidos) AS nombre_cliente, " +
        "u.nombre AS nombre_mecanico " +
        "FROM orden_trabajo ot " +
        "JOIN recepcion r   ON ot.id_recepcion = r.id_recepcion " +
        "JOIN vehiculo v    ON r.id_vehiculo   = v.id_vehiculo " +
        "JOIN cliente c     ON v.id_cliente    = c.id_cliente " +
        "JOIN mecanico m    ON ot.id_mecanico  = m.id_mecanico " +
        "JOIN usuario u     ON m.id_usuario    = u.id_usuario ";

    private OrdenTrabajo mapear(ResultSet rs) throws SQLException {
        OrdenTrabajo ot = new OrdenTrabajo();
        ot.setIdOrden(rs.getInt("id_orden"));
        ot.setIdRecepcion(rs.getInt("id_recepcion"));
        ot.setIdMecanico(rs.getInt("id_mecanico"));
        ot.setDescripcion(rs.getString("descripcion") != null
                ? rs.getString("descripcion") : "");
        ot.setEstadoTexto(rs.getString("estado"));
        ot.setFechaInicio(rs.getString("fecha_inicio"));
        ot.setFechaFin(rs.getString("fecha_fin") != null
                ? rs.getString("fecha_fin") : "");
        // datos enriquecidos
        try { ot.setPlaca(rs.getString("placa")); }             catch (SQLException ignored) {}
        try { ot.setNombreCliente(rs.getString("nombre_cliente")); } catch (SQLException ignored) {}
        try { ot.setNombreMecanico(rs.getString("nombre_mecanico")); } catch (SQLException ignored) {}
        return ot;
    }

    // ── INSERT ───────────────────────────────────────────────────────────────
    public void insertar(OrdenTrabajo ot) throws SQLException {
        String sql = "INSERT INTO orden_trabajo " +
                     "(id_recepcion, id_mecanico, descripcion, estado) " +
                     "VALUES (?, ?, ?, 'abierta')";

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql,
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, ot.getIdRecepcion());
            ps.setInt(2, ot.getIdMecanico());
            ps.setString(3, ot.getDescripcion());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) ot.setIdOrden(keys.getInt(1));
            }
        }
    }

    // ── SELECT todas ─────────────────────────────────────────────────────────
    public List<OrdenTrabajo> listarTodas() throws SQLException {
        String sql = SELECT_BASE + "ORDER BY ot.fecha_inicio DESC";
        List<OrdenTrabajo> lista = new ArrayList<>();

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    // ── SELECT por mecánico ───────────────────────────────────────────────────
    public List<OrdenTrabajo> listarPorMecanico(int idMecanico) throws SQLException {
        String sql = SELECT_BASE +
                     "WHERE ot.id_mecanico = ? ORDER BY ot.fecha_inicio DESC";
        List<OrdenTrabajo> lista = new ArrayList<>();

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idMecanico);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // ── SELECT por id ────────────────────────────────────────────────────────
    public Optional<OrdenTrabajo> buscarPorId(int idOrden) throws SQLException {
        String sql = SELECT_BASE + "WHERE ot.id_orden = ?";

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idOrden);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    // ── SELECT por recepción ──────────────────────────────────────────────────
    public Optional<OrdenTrabajo> buscarPorRecepcion(int idRecepcion) throws SQLException {
        String sql = SELECT_BASE + "WHERE ot.id_recepcion = ?";

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idRecepcion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    // ── UPDATE estado ─────────────────────────────────────────────────────────
    public boolean actualizarEstado(int idOrden, String estado) throws SQLException {
        String sql = "terminada".equals(estado)
            ? "UPDATE orden_trabajo SET estado = ?, fecha_fin = NOW() WHERE id_orden = ?"
            : "UPDATE orden_trabajo SET estado = ? WHERE id_orden = ?";

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, idOrden);
            return ps.executeUpdate() > 0;
        }
    }
}