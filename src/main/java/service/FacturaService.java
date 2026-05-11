package service;

import dao.FacturaDAO;
import dao.OrdenTrabajodao;
import dao.DetalleOrdendao;
import dao.Recepcionentregadao;
import Modelo.Factura;
import Modelo.OrdenTrabajo;
import Modelo.DetalleOrden;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Vera Sabando Luis Enrique
 */
public class FacturaService {

    private final FacturaDAO          facturaDAO   = new FacturaDAO();
    private final OrdenTrabajodao     ordenDAO     = new OrdenTrabajodao();
    private final DetalleOrdendao     detalleDAO   = new DetalleOrdendao();
    private final Recepcionentregadao recepcionDAO = new Recepcionentregadao();

    // ── Listar órdenes terminadas sin factura ────────────────────────────────
    public List<OrdenTrabajo> listarOrdenesParaFacturar() throws SQLException {
        return ordenDAO.listarTodas().stream()
                .filter(o -> "terminada".equals(o.getEstadoTexto()))
                .filter(o -> {
                    try {
                        return !facturaDAO.existeFacturaParaOrden(o.getIdOrden());
                    } catch (SQLException e) {
                        return false;
                    }
                })
                .collect(java.util.stream.Collectors.toList());
    }

    // ── Listar detalles de una orden ─────────────────────────────────────────
    public List<DetalleOrden> listarDetalles(int idOrden) throws SQLException {
        return detalleDAO.listarPorOrden(idOrden);
    }

    // ── Calcular subtotal ────────────────────────────────────────────────────
    public double calcularSubtotal(List<DetalleOrden> detalles) {
        return detalles.stream().mapToDouble(DetalleOrden::getSubtotal).sum();
    }

    // ── Generar factura — obtiene idCliente desde la recepción ───────────────
    public Factura generarFactura(int idOrden, int idUsuario) throws SQLException {

        OrdenTrabajo orden = ordenDAO.buscarPorId(idOrden)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe la orden con id: " + idOrden));

        if (!"terminada".equals(orden.getEstadoTexto())) {
            throw new IllegalArgumentException(
                "Solo se puede facturar una orden terminada");
        }

        if (facturaDAO.existeFacturaParaOrden(idOrden)) {
            throw new IllegalArgumentException(
                "Esta orden ya tiene una factura generada");
        }

        // Obtener idCliente desde la recepción → vehículo → cliente
        int idCliente = obtenerIdClienteDesdeOrden(orden);

        List<DetalleOrden> detalles = detalleDAO.listarPorOrden(idOrden);
        if (detalles.isEmpty()) {
            throw new IllegalArgumentException(
                "No se puede facturar una orden sin servicios registrados");
        }

        double subtotal = calcularSubtotal(detalles);

        Factura factura = new Factura();
        factura.setIdOrden(idOrden);
        factura.setIdCliente(idCliente);
        factura.setIdUsuario(idUsuario);
        factura.setNumFactura("FAC-" + System.currentTimeMillis());
        factura.setFechaEmision(LocalDate.now());
        factura.calcularTotales(subtotal);
        factura.setEstado("activa");

        facturaDAO.insertar(factura);
        return factura;
    }

    // ── Obtener idCliente a través de la cadena orden→recepcion→vehiculo ─────
    private int obtenerIdClienteDesdeOrden(OrdenTrabajo orden) throws SQLException {
        String sql = "SELECT v.id_cliente " +
                     "FROM orden_trabajo ot " +
                     "JOIN recepcion r ON ot.id_recepcion = r.id_recepcion " +
                     "JOIN vehiculo v  ON r.id_vehiculo   = v.id_vehiculo " +
                     "WHERE ot.id_orden = ?";

        try (java.sql.Connection cn = util.connectionBD.getConnection();
             java.sql.PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, orden.getIdOrden());
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id_cliente");
            }
        }
        throw new IllegalArgumentException(
            "No se pudo obtener el cliente de la orden " + orden.getIdOrden());
    }

    // ── Anular factura por id ────────────────────────────────────────────────
    public void anular(int idFactura) throws SQLException {
        Factura f = facturaDAO.buscarPorId(idFactura)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe la factura con id: " + idFactura));

        if ("anulada".equals(f.getEstado())) {
            throw new IllegalArgumentException("La factura ya está anulada");
        }

        facturaDAO.anular(idFactura);
    }

    // ── Listar todas las facturas ─────────────────────────────────────────────
    public List<Factura> listarTodas() throws SQLException {
        return facturaDAO.listarTodas();
    }

    // ── Buscar factura por id ─────────────────────────────────────────────────
    public Factura buscarPorId(int idFactura) throws SQLException {
        return facturaDAO.buscarPorId(idFactura)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe la factura con id: " + idFactura));
    }
}