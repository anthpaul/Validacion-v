package dao;

import Modelo.DetalleOrden;
import util.connectionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Omar Mite
 */
public class DetalleOrdendao {
    private DetalleOrden mapear(ResultSet rs) throws SQLException {
        DetalleOrden d = new DetalleOrden(
            rs.getInt("id_orden"),
            rs.getString("tipo"),
            rs.getString("descripcion"),
            rs.getDouble("costo_unitario"),
            rs.getInt("cantidad")
        );
        d.setIdDetalle(rs.getInt("id_detalle"));
        return d;
    }

    public void insertar(DetalleOrden detalle) throws SQLException {
        String sql = "INSERT INTO detalle_orden (id_orden, tipo, descripcion, costo_unitario, cantidad) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, detalle.getIdOrden());
            ps.setString(2, detalle.getTipo());
            ps.setString(3, detalle.getDescripcion());
            ps.setDouble(4, detalle.getCostoUnitario());
            ps.setInt(5, detalle.getCantidad());

            ps.executeUpdate();
        }
    }

    public List<DetalleOrden> listarPorOrden(int idOrden) throws SQLException {
        String sql = "SELECT * FROM detalle_orden WHERE id_orden = ?";
        List<DetalleOrden> lista = new ArrayList<>();

        try (Connection cn = connectionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idOrden);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }
}