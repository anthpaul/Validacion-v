package service;

import dao.Recepcionentregadao;
import dao.OrdenTrabajodao;
import dao.Vehiculodao;
import Modelo.RecepcionEntrega;
import Modelo.OrdenTrabajo;
import Modelo.vehiculo;

import java.sql.SQLException;
import java.util.List;

/**
 */
public class RecepcionEntregaService {

    private final Recepcionentregadao recepcionDAO = new Recepcionentregadao();
    private final OrdenTrabajodao     ordenDAO     = new OrdenTrabajodao();
    private final VehiculoService     vehiculoService = new VehiculoService();

    
    public List<vehiculo> listarVehiculos() throws SQLException {
        return vehiculoService.listarTodos();
    }

    public void registrarRecepcion(RecepcionEntrega recepcion,
                                   int idUsuario) throws SQLException {

        // Validar que no tenga recepción activa
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

    
    public void entregarVehiculo(int idRecepcion, int idUsuario,
                                  int kmSalida, String observaciones) throws SQLException {


        OrdenTrabajo orden = ordenDAO.buscarPorRecepcion(idRecepcion)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe orden de trabajo para esta recepción"));

        if (!"terminada".equals(orden.getEstadoTexto())) {
            throw new IllegalArgumentException(
                "La orden de trabajo debe estar terminada antes de entregar el vehículo");
        }

        recepcionDAO.registrarEntrega(orden.getIdOrden(), idUsuario,
                                      kmSalida, observaciones);


        recepcionDAO.actualizarEstado(idRecepcion, "entregado");
    }

    public List<RecepcionEntrega> listarTodos() throws SQLException {
        return recepcionDAO.listarTodas();
    }

    
    public List<RecepcionEntrega> buscarPorPlaca(String placa) throws SQLException {
        if (placa == null || placa.isBlank()) return listarTodos();
        return recepcionDAO.buscarPorPlaca(placa);
    }
}