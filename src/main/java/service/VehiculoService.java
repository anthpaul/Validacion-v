package service;

import Modelo.vehiculo;
import dao.Vehiculodao;
import dao.clientedao;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Anthony Delgado
 */
public class VehiculoService {

    private final Vehiculodao vehiculoDAO = new Vehiculodao();
    private final clientedao  clienteDAO  = new clientedao();

    public void registrar(vehiculo vehiculo) throws SQLException {
        if (vehiculoDAO.existePlaca(vehiculo.getPlaca())) {
            throw new IllegalArgumentException(
                "Ya existe un vehículo con la placa: " + vehiculo.getPlaca());
        }

        if (!clienteDAO.buscarPorId(vehiculo.getIdCliente()).isPresent()) {
            throw new IllegalArgumentException(
                "No existe el cliente con id: " + vehiculo.getIdCliente());
        }

        vehiculoDAO.insertar(vehiculo);
    }

    public vehiculo buscarPorId(int idVehiculo) throws SQLException {
        return vehiculoDAO.buscarPorId(idVehiculo)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe vehículo con id: " + idVehiculo));
    }

    public vehiculo buscarPorPlaca(String placa) throws SQLException {
        return vehiculoDAO.buscarPorPlaca(placa)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe vehículo con placa: " + placa));
    }

    public List<vehiculo> listarTodos() throws SQLException {
        return vehiculoDAO.listarTodos();
    }

    public List<vehiculo> listarPorCliente(int idCliente) throws SQLException {
        return vehiculoDAO.listarPorCliente(idCliente);
    }

    public void actualizar(vehiculo vehiculo) throws SQLException {
        vehiculoDAO.buscarPorPlaca(vehiculo.getPlaca()).ifPresent(existente -> {
            if (existente.getIdVehiculo() != vehiculo.getIdVehiculo()) {
                throw new IllegalArgumentException(
                    "La placa ya está registrada en otro vehículo");
            }
        });

        boolean actualizado = vehiculoDAO.actualizar(vehiculo);
        if (!actualizado) {
            throw new IllegalArgumentException("No se encontró el vehículo a actualizar");
        }
    }

    public void actualizarKilometraje(int idVehiculo, int nuevoKm) throws SQLException {
        vehiculo vehiculo = buscarPorId(idVehiculo);
        vehiculo.setKilometraje(nuevoKm);
        vehiculoDAO.actualizar(vehiculo);
    }

    public void eliminar(int idVehiculo) throws SQLException {
        boolean eliminado = vehiculoDAO.eliminar(idVehiculo);
        if (!eliminado) {
            throw new IllegalArgumentException("No se encontró el vehículo a eliminar");
        }
    }
}