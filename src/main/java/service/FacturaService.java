package service;

import dao.FacturaDAO;
import dao.OrdenTrabajodao;
import dao.DetalleOrdendao;
import Modelo.Factura;
import Modelo.OrdenTrabajo;
import Modelo.DetalleOrden;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author Vera Sabando Luis Enrique
 */
public class FacturaService {

    private final FacturaDAO      facturaDAO  = new FacturaDAO();
    private final OrdenTrabajodao ordenDAO    = new OrdenTrabajodao();
    private final DetalleOrdendao detalleDAO  = new DetalleOrdendao();

    // ── Listar órdenes terminadas sin factura — para el combo ────────────────
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

    public List<DetalleOrden> listarDetalles(int idOrden) throws SQLException {
        return detalleDAO.listarPorOrden(idOrden);
    }

   
    public double calcularSubtotal(List<DetalleOrden> detalles) {
        return detalles.stream().mapToDouble(DetalleOrden::getSubtotal).sum();
    }

    public Factura generarFactura(int idOrden, int idCliente,
                                  int idUsuario) throws SQLException {

        // Verificar que la orden existe y está terminada
        OrdenTrabajo orden = ordenDAO.buscarPorId(idOrden)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe la orden con id: " + idOrden));

        if (!"terminada".equals(orden.getEstadoTexto())) {
            throw new IllegalArgumentException(
                "Solo se puede facturar una orden terminada");
        }

        // Verificar que no tenga factura ya
        if (facturaDAO.existeFacturaParaOrden(idOrden)) {
            throw new IllegalArgumentException(
                "Esta orden ya tiene una factura generada");
        }

        // Calcular totales
        List<DetalleOrden> detalles = detalleDAO.listarPorOrden(idOrden);
        if (detalles.isEmpty()) {
            throw new IllegalArgumentException(
                "No se puede facturar una orden sin servicios registrados");
        }

        double subtotal = calcularSubtotal(detalles);

        // Crear factura
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

    public void anular(int idFactura) throws SQLException {
        boolean anulada = facturaDAO.anular(idFactura);
        if (!anulada) {
            throw new IllegalArgumentException("No se encontró la factura a anular");
        }
    }

    public List<Factura> listarTodas() throws SQLException {
        return facturaDAO.listarTodas();
    }
}