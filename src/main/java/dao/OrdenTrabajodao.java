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

    private OrdenTrabajo mapear(ResultSet rs) throws SQLException {
        OrdenTrabajo ot = new OrdenTrabajo();
        ot.setIdOrden(rs.getInt("id_orden"));
        ot.setIdRecepcion(rs.getInt("id_recepcion"));
        ot.setIdMecanico(rs.getInt("id_mecanico"));
        ot.setDescripcion(rs.getString("descripcion") != null ? rs.getString("descripcion") : "");
        ot.setEstadoTexto(rs.getString("estado"));
        ot.setFechaInicio(rs.getString("fecha_inicio"));
        ot.setFechaFin(rs.getString("fecha_fin") != null ? rs.getString("fecha_fin") : "");
        return ot;
    }

    
    public void insertar(OrdenTrabajo ot) throws SQLException {
        String sql = "INSERT INTO orden_trabajo (id_recepcion, id_mecanico, descripcion, estado) " +
                     "VALUES (?, ?, ?, 'abierta')";

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, ot.getIdRecepcion());
            ps.setInt(2, ot.getIdMecanico());
            ps.setString(3, ot.getDescripcion());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) ot.setIdOrden(keys.getInt(1));
            }
        }
    }

    public List<OrdenTrabajo> listarTodas() throws SQLException {
        String sql = "SELECT * FROM orden_trabajo ORDER BY fecha_inicio DESC";
        List<OrdenTrabajo> lista = new ArrayList<>();

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<OrdenTrabajo> listarPorMecanico(int idMecanico) throws SQLException {
        String sql = "SELECT * FROM orden_trabajo WHERE id_mecanico = ? " +
                     "ORDER BY fecha_inicio DESC";
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

    public Optional<OrdenTrabajo> buscarPorId(int idOrden) throws SQLException {
        String sql = "SELECT * FROM orden_trabajo WHERE id_orden = ?";

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idOrden);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    public boolean actualizarEstado(int idOrden, String estado) throws SQLException {
        String sql = estado.equals("terminada")
            ? "UPDATE orden_trabajo SET estado = ?, fecha_fin = NOW() WHERE id_orden = ?"
            : "UPDATE orden_trabajo SET estado = ? WHERE id_orden = ?";

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idOrden);
            return ps.executeUpdate() > 0;
        }
    }

    
    public Optional<OrdenTrabajo> buscarPorRecepcion(int idRecepcion) throws SQLException {
        String sql = "SELECT * FROM orden_trabajo WHERE id_recepcion = ?";

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idRecepcion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }
}