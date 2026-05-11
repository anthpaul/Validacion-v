package dao;

import Modelo.RecepcionEntrega;
import util.connectionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Recepcionentregadao {

    private RecepcionEntrega mapear(ResultSet rs) throws SQLException {
        RecepcionEntrega r = new RecepcionEntrega();
        r.setIdRecepcion(rs.getInt("id_recepcion"));
        r.setIdVehiculo(rs.getInt("id_vehiculo"));
        r.setPlaca(rs.getString("placa") != null ? rs.getString("placa") : "");
        r.setDescripcionVehiculo(rs.getString("descripcion_vehiculo") != null
                ? rs.getString("descripcion_vehiculo") : "");
        r.setMotivoIngreso(rs.getString("observaciones") != null
                ? rs.getString("observaciones") : "Sin observaciones");
        r.setObservacionesRecepcion(rs.getString("checklist") != null
                ? rs.getString("checklist") : "");
        r.setEstadoDesdeDB(rs.getString("estado"));
        r.setFechaRecepcionTextoDB(rs.getString("fecha_entrada"));
        // nombre del cliente desde JOIN
        try {
            r.setNombreCliente(rs.getString("nombre_cliente"));
        } catch (SQLException ignored) {}
        return r;
    }

    // ── INSERT ───────────────────────────────────────────────────────────────
    public int insertar(RecepcionEntrega recepcion, int idUsuario) throws SQLException {
        String sql = "INSERT INTO recepcion " +
                     "(id_vehiculo, id_usuario, km_entrada, observaciones, checklist, estado) " +
                     "VALUES (?, ?, ?, ?, ?, 'pendiente')";

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, recepcion.getIdVehiculo());
            ps.setInt(2, idUsuario);
            ps.setInt(3, recepcion.getKmEntrada());
            ps.setString(4, recepcion.getMotivoIngreso());
            ps.setString(5, recepcion.getChecklist());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    recepcion.setIdRecepcion(id);
                    return id;
                }
            }
        }
        return 0;
    }

    // ── SELECT todas con JOIN ─────────────────────────────────────────────────
    public List<RecepcionEntrega> listarTodas() throws SQLException {
        String sql = "SELECT r.*, v.placa, " +
                     "CONCAT(v.marca, ' ', v.modelo, ' [', v.placa, ']') AS descripcion_vehiculo, " +
                     "CONCAT(c.nombres, ' ', c.apellidos) AS nombre_cliente " +
                     "FROM recepcion r " +
                     "JOIN vehiculo v ON r.id_vehiculo = v.id_vehiculo " +
                     "JOIN cliente c ON v.id_cliente = c.id_cliente " +
                     "ORDER BY r.fecha_entrada DESC";

        List<RecepcionEntrega> lista = new ArrayList<>();
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    // ── SELECT por id ────────────────────────────────────────────────────────
    public Optional<RecepcionEntrega> buscarPorId(int idRecepcion) throws SQLException {
        String sql = "SELECT r.*, v.placa, " +
                     "CONCAT(v.marca, ' ', v.modelo, ' [', v.placa, ']') AS descripcion_vehiculo, " +
                     "CONCAT(c.nombres, ' ', c.apellidos) AS nombre_cliente " +
                     "FROM recepcion r " +
                     "JOIN vehiculo v ON r.id_vehiculo = v.id_vehiculo " +
                     "JOIN cliente c ON v.id_cliente = c.id_cliente " +
                     "WHERE r.id_recepcion = ?";

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idRecepcion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    // ── SELECT por placa ─────────────────────────────────────────────────────
    public List<RecepcionEntrega> buscarPorPlaca(String placa) throws SQLException {
        String sql = "SELECT r.*, v.placa, " +
                     "CONCAT(v.marca, ' ', v.modelo, ' [', v.placa, ']') AS descripcion_vehiculo, " +
                     "CONCAT(c.nombres, ' ', c.apellidos) AS nombre_cliente " +
                     "FROM recepcion r " +
                     "JOIN vehiculo v ON r.id_vehiculo = v.id_vehiculo " +
                     "JOIN cliente c ON v.id_cliente = c.id_cliente " +
                     "WHERE v.placa LIKE ? ORDER BY r.fecha_entrada DESC";

        List<RecepcionEntrega> lista = new ArrayList<>();
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, "%" + placa.toUpperCase().trim() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // ── UPDATE estado ────────────────────────────────────────────────────────
    public boolean actualizarEstado(int idRecepcion, String estado) throws SQLException {
        String sql = "UPDATE recepcion SET estado = ? WHERE id_recepcion = ?";
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, idRecepcion);
            return ps.executeUpdate() > 0;
        }
    }

    // ── INSERT entrega ───────────────────────────────────────────────────────
    public void registrarEntrega(int idOrden, int idUsuario,
                                  int kmSalida, String observaciones) throws SQLException {
        String sql = "INSERT INTO entrega (id_orden, id_usuario, km_salida, observaciones, condicion) " +
                     "VALUES (?, ?, ?, ?, 'bueno')";
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idOrden);
            ps.setInt(2, idUsuario);
            ps.setInt(3, kmSalida);
            ps.setString(4, observaciones);
            ps.executeUpdate();
        }
    }

    // ── Verificar si tiene recepción activa ──────────────────────────────────
    public boolean tieneRecepcionActiva(int idVehiculo) throws SQLException {
        String sql = "SELECT 1 FROM recepcion WHERE id_vehiculo = ? " +
                     "AND estado NOT IN ('entregado') LIMIT 1";
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idVehiculo);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    // ── Verificar si la orden de una recepción está terminada ─────────────────
    public boolean ordenTerminada(int idRecepcion) throws SQLException {
        String sql = "SELECT 1 FROM orden_trabajo " +
                     "WHERE id_recepcion = ? AND estado = 'terminada'";
        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idRecepcion);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }
}