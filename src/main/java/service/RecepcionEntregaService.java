package service;

import dao.Recepcionentregadao;
import dao.OrdenTrabajodao;
import Modelo.RecepcionEntrega;
import Modelo.OrdenTrabajo;
import Modelo.vehiculo;

import java.sql.SQLException;
import java.util.List;

public class RecepcionEntregaService {

    private final Recepcionentregadao recepcionDAO  = new Recepcionentregadao();
    private final OrdenTrabajodao     ordenDAO      = new OrdenTrabajodao();
    private final VehiculoService     vehiculoService = new VehiculoService();

    // ── Listar vehículos para el combo ───────────────────────────────────────
    public List<vehiculo> listarVehiculos() throws SQLException {
        return vehiculoService.listarTodos();
    }

    // ── Registrar recepción + crear orden automáticamente ────────────────────
    public void registrarRecepcion(RecepcionEntrega recepcion,
                                   int idUsuario) throws SQLException {
        if (recepcionDAO.tieneRecepcionActiva(recepcion.getIdVehiculo())) {
            throw new IllegalArgumentException(
                "Este vehículo ya tiene una recepción activa");
        }

        int idRecepcion = recepcionDAO.insertar(recepcion, idUsuario);

        if (idRecepcion > 0 && recepcion.getIdMecanico() > 0) {
            OrdenTrabajo orden = new OrdenTrabajo(
                idRecepcion,
                recepcion.getIdMecanico(),
                recepcion.getMotivoIngreso()
            );
            ordenDAO.insertar(orden);
        }
    }

    // ── Verificar si la orden de una recepción está terminada ─────────────────
    public boolean puedeEntregar(int idRecepcion) throws SQLException {
        return recepcionDAO.ordenTerminada(idRecepcion);
    }

    // ── Registrar entrega ────────────────────────────────────────────────────
    public void entregarVehiculo(int idRecepcion, int idUsuario,
                                  int kmSalida, String observaciones) throws SQLException {

        // Verificar que la orden esté terminada
        if (!recepcionDAO.ordenTerminada(idRecepcion)) {
            throw new IllegalArgumentException(
                "La orden de trabajo debe estar terminada antes de entregar el vehículo. " +
                "El mecánico aún no ha finalizado el mantenimiento.");
        }

        OrdenTrabajo orden = ordenDAO.buscarPorRecepcion(idRecepcion)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe orden de trabajo para esta recepción"));

        recepcionDAO.registrarEntrega(orden.getIdOrden(), idUsuario,
                                      kmSalida, observaciones);
        recepcionDAO.actualizarEstado(idRecepcion, "entregado");
    }

    // ── Listar todas ─────────────────────────────────────────────────────────
    public List<RecepcionEntrega> listarTodos() throws SQLException {
        return recepcionDAO.listarTodas();
    }

    // ── Buscar por placa ─────────────────────────────────────────────────────
    public List<RecepcionEntrega> buscarPorPlaca(String placa) throws SQLException {
        if (placa == null || placa.isBlank()) return listarTodos();
        return recepcionDAO.buscarPorPlaca(placa);
    }

    // ── Buscar por id ─────────────────────────────────────────────────────────
    public RecepcionEntrega buscarPorId(int idRecepcion) throws SQLException {
        return recepcionDAO.buscarPorId(idRecepcion)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe la recepción con id: " + idRecepcion));
    }
}