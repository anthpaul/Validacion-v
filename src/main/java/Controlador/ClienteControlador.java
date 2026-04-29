package Controlador;

import Modelo.cliente;
import service.clienteService;
import Vista.ClientePanel;

import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author User
 */
public class ClienteControlador {
 
    private final clienteService clienteService = new clienteService();
    private final ClientePanel   clientePanel;
 
    public ClienteControlador(ClientePanel clientePanel) {
        this.clientePanel = clientePanel;
        iniciarEventos();
        cargarTabla();
    }
 
    private void iniciarEventos() {

        clientePanel.getBtnNuevo().addActionListener(e -> {
            clientePanel.limpiarFormulario();
        });
 
        clientePanel.getBtnGuardar().addActionListener(e -> guardar());
 

        clientePanel.getBtnEliminar().addActionListener(e -> eliminar());
 

        clientePanel.getBtnBuscar().addActionListener(e -> buscar());
 

        clientePanel.getTabla().getSelectionModel()
                .addListSelectionListener(e -> {
                    if (!e.getValueIsAdjusting()) {
                        cargarEnFormulario();
                    }
                });
    }
 
    
    public void cargarTabla() {
        try {
            List<cliente> clientes = clienteService.listarTodos();
            clientePanel.cargarTabla(clientes);
        } catch (SQLException e) {
            clientePanel.mostrarError("Error al cargar clientes: " + e.getMessage());
        }
    }
 

    private void guardar() {
        try {
            cliente cliente = clientePanel.obtenerDesdeFormulario();
 
            if (cliente.getIdCliente() == 0) {

                clienteService.registrar(cliente);
                clientePanel.mostrarExito("Cliente registrado correctamente");
            } else {

                clienteService.actualizar(cliente);
                clientePanel.mostrarExito("Cliente actualizado correctamente");
            }
 
            clientePanel.limpiarFormulario();
            cargarTabla();
 
        } catch (IllegalArgumentException e) {
            clientePanel.mostrarError(e.getMessage());
        } catch (SQLException e) {
            clientePanel.mostrarError("Error del sistema: " + e.getMessage());
        }
    }
 
   
    private void eliminar() {
        int id = clientePanel.getIdSeleccionado();
 
        if (id == 0) {
            clientePanel.mostrarError("Seleccione un cliente para eliminar");
            return;
        }
 
        boolean confirmar = clientePanel.confirmar(
                "¿Está seguro de eliminar este cliente?");
 
        if (!confirmar) return;
 
        try {
            clienteService.eliminar(id);
            clientePanel.mostrarExito("Cliente eliminado correctamente");
            clientePanel.limpiarFormulario();
            cargarTabla();
 
        } catch (IllegalArgumentException e) {
            clientePanel.mostrarError(e.getMessage());
        } catch (SQLException e) {
            clientePanel.mostrarError("Error del sistema: " + e.getMessage());
        }
    }
 
   
    private void buscar() {
        try {
            String termino = clientePanel.getTerminoBusqueda();
            List<cliente> clientes = clienteService.buscar(termino);
            clientePanel.cargarTabla(clientes);
 
        } catch (SQLException e) {
            clientePanel.mostrarError("Error al buscar: " + e.getMessage());
        }
    }
 
    // ── Cargar cliente seleccionado en el formulario ─────────────────────────
    private void cargarEnFormulario() {
        cliente cliente = clientePanel.getClienteSeleccionado();
        if (cliente != null) {
            clientePanel.cargarEnFormulario(cliente);
        }
    }
}
