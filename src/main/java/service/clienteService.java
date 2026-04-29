package service;

import dao.clientedao;
import Modelo.cliente;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Anthony Delgado
 */
public class clienteService {

    private final clientedao clienteDAO = new clientedao();

    public void registrar(cliente cliente) throws SQLException {
        if (clienteDAO.existeCedula(cliente.getCedula())) {
            throw new IllegalArgumentException(
                "Ya existe un cliente con la cédula: " + cliente.getCedula());
        }
        clienteDAO.insertar(cliente);
    }

    public cliente buscarPorId(int idCliente) throws SQLException {
        return clienteDAO.buscarPorId(idCliente)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe cliente con id: " + idCliente));
    }

    public cliente buscarPorCedula(String cedula) throws SQLException {
        return clienteDAO.buscarPorCedula(cedula)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe cliente con cédula: " + cedula));
    }

    public List<cliente> listarTodos() throws SQLException {
        return clienteDAO.listarTodos();
    }

    public List<cliente> buscar(String termino) throws SQLException {
        if (termino == null || termino.isBlank()) {
            return clienteDAO.listarTodos();
        }
        return clienteDAO.buscar(termino.trim());
    }

    public void actualizar(cliente cliente) throws SQLException {
        clienteDAO.buscarPorCedula(cliente.getCedula()).ifPresent(existente -> {
            if (existente.getIdCliente() != cliente.getIdCliente()) {
                throw new IllegalArgumentException(
                    "La cédula ya está registrada en otro cliente");
            }
        });

        boolean actualizado = clienteDAO.actualizar(cliente);
        if (!actualizado) {
            throw new IllegalArgumentException("No se encontró el cliente a actualizar");
        }
    }

    public void eliminar(int idCliente) throws SQLException {
        if (clienteDAO.tieneVehiculos(idCliente)) {
            throw new IllegalArgumentException(
                "No se puede eliminar — el cliente tiene vehículos registrados");
        }

        boolean eliminado = clienteDAO.eliminar(idCliente);
        if (!eliminado) {
            throw new IllegalArgumentException("No se encontró el cliente a eliminar");
        }
    }
}