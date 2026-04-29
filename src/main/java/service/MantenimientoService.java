package service;

import dao.OrdenTrabajodao;
import dao.DetalleOrdendao;
import Modelo.OrdenTrabajo;
import Modelo.DetalleOrden;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Omar Mite
 */
public class MantenimientoService {

    private final OrdenTrabajodao  ordenDAO   = new OrdenTrabajodao();
    private final DetalleOrdendao  detalleDAO = new DetalleOrdendao();

    public List<OrdenTrabajo> listarTodas() throws SQLException {
        return ordenDAO.listarTodas();
    }

    public List<OrdenTrabajo> listarPorMecanico(int idMecanico) throws SQLException {
        return ordenDAO.listarPorMecanico(idMecanico);
    }

    public void registrarOrden(OrdenTrabajo ot) throws SQLException {
        if (ot.getIdRecepcion() <= 0) {
            throw new IllegalArgumentException("La recepción no es válida");
        }
        if (ot.getIdMecanico() <= 0) {
            throw new IllegalArgumentException("Debe asignar un mecánico");
        }
        ordenDAO.insertar(ot);
    }

    public void agregarDetalle(DetalleOrden det) throws SQLException {
        if (det.getIdOrden() <= 0) {
            throw new IllegalArgumentException("Seleccione una orden válida");
        }
        if (det.getDescripcion() == null || det.getDescripcion().isBlank()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }
        if (det.getCostoUnitario() <= 0) {
            throw new IllegalArgumentException("El costo debe ser mayor a 0");
        }

        ordenDAO.buscarPorId(det.getIdOrden()).ifPresent(orden -> {
            if ("terminada".equals(orden.getEstadoTexto())) {
                throw new IllegalArgumentException("No se puede agregar detalle a una orden terminada");
            }
        });

        detalleDAO.insertar(det);

        // Cambiar estado a en_proceso si estaba abierta
        ordenDAO.buscarPorId(det.getIdOrden()).ifPresent(orden -> {
            if ("abierta".equals(orden.getEstadoTexto())) {
                try {
                    ordenDAO.actualizarEstado(det.getIdOrden(), "en_proceso");
                } catch (SQLException e) {
                    System.err.println("Error al actualizar estado: " + e.getMessage());
                }
            }
        });
    }

    public void finalizarOrden(int idOrden) throws SQLException {
        OrdenTrabajo orden = ordenDAO.buscarPorId(idOrden)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe la orden con id: " + idOrden));

        if ("terminada".equals(orden.getEstadoTexto())) {
            throw new IllegalArgumentException("La orden ya está terminada");
        }
        if ("cancelada".equals(orden.getEstadoTexto())) {
            throw new IllegalArgumentException("La orden está cancelada");
        }

        ordenDAO.actualizarEstado(idOrden, "terminada");
    }

    public void cambiarEstado(int idOrden, String nuevoEstado) throws SQLException {
        ordenDAO.actualizarEstado(idOrden, nuevoEstado);
    }

    public List<DetalleOrden> listarDetalles(int idOrden) throws SQLException {
        return detalleDAO.listarPorOrden(idOrden);
    }
}