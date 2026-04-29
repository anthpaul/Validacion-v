package dao;

import Modelo.Factura;
import util.connectionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Vera Sabando Luis Enrique
 */
public class FacturaDAO {

    private Factura mapear(ResultSet rs) throws SQLException {
        Factura f = new Factura();
        f.setIdFactura(rs.getInt("id_factura"));
        f.setIdOrden(rs.getInt("id_orden"));
        f.setIdCliente(rs.getInt("id_cliente"));
        f.setIdUsuario(rs.getInt("id_usuario"));
        f.setNumFactura(rs.getString("numero_factura"));
        f.setFechaEmision(rs.getDate("fecha_emision").toLocalDate());
        f.calcularTotales(rs.getDouble("subtotal"));
        f.setEstado(rs.getString("estado"));
        return f;
    }

    public void insertar(Factura f) throws SQLException {
        String sql = "INSERT INTO factura " +
                     "(id_orden, id_cliente, id_usuario, numero_factura, " +
                     " fecha_emision, subtotal, iva, total, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, f.getIdOrden());
            ps.setInt(2, f.getIdCliente());
            ps.setInt(3, f.getIdUsuario());
            ps.setString(4, f.getNumFactura());
            ps.setDate(5, java.sql.Date.valueOf(f.getFechaEmision()));
            ps.setDouble(6, f.getSubtotal());
            ps.setDouble(7, f.getIva());
            ps.setDouble(8, f.getTotal());
            ps.setString(9, f.getEstado());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) f.setIdFactura(keys.getInt(1));
            }
        }
    }

    public Optional<Factura> buscarPorId(int idFactura) throws SQLException {
        String sql = "SELECT * FROM factura WHERE id_factura = ?";

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idFactura);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    public Optional<Factura> buscarPorOrden(int idOrden) throws SQLException {
        String sql = "SELECT * FROM factura WHERE id_orden = ?";

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idOrden);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    public List<Factura> listarTodas() throws SQLException {
        String sql = "SELECT * FROM factura ORDER BY fecha_emision DESC";
        List<Factura> lista = new ArrayList<>();

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public boolean anular(int idFactura) throws SQLException {
        String sql = "UPDATE factura SET estado = 'anulada', " +
                     "fecha_anulacion = NOW() WHERE id_factura = ?";

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idFactura);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean existeFacturaParaOrden(int idOrden) throws SQLException {
        String sql = "SELECT 1 FROM factura WHERE id_orden = ?";

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idOrden);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}