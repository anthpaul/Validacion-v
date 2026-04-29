package Controlador;

import Modelo.cliente;
import Modelo.vehiculo;
import service.clienteService;
import service.VehiculoService;
import Vista.VehiculoPanel;
 
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author User
 */
public class VehiculoControlador {
 
    private final VehiculoService vehiculoService = new VehiculoService();
    private final clienteService  clienteService  = new clienteService();
    private final VehiculoPanel   vehiculoPanel;
 
    public VehiculoControlador(VehiculoPanel vehiculoPanel) {
        this.vehiculoPanel = vehiculoPanel;
        iniciarEventos();
        cargarClientes();
        cargarTabla();
    }
 
    private void iniciarEventos() {
 

        vehiculoPanel.getBtnNuevo().addActionListener(e -> {
            vehiculoPanel.limpiarFormulario();
        });
 

        vehiculoPanel.getBtnGuardar().addActionListener(e -> guardar());
 

        vehiculoPanel.getBtnEliminar().addActionListener(e -> eliminar());
 

        vehiculoPanel.getBtnBuscar().addActionListener(e -> buscar());

        vehiculoPanel.getTabla().getSelectionModel()
                .addListSelectionListener(e -> {
                    if (!e.getValueIsAdjusting()) {
                        cargarEnFormulario();
                    }
                });
    }
 
    private void cargarClientes() {
        try {
            List<cliente> clientes = clienteService.listarTodos();
            vehiculoPanel.cargarClientes(clientes);
        } catch (SQLException e) {
            vehiculoPanel.mostrarError("Error al cargar clientes: " + e.getMessage());
        }
    }
 
    public void cargarTabla() {
        try {
            List<vehiculo> vehiculos = vehiculoService.listarTodos();
            vehiculoPanel.cargarTabla(vehiculos);
        } catch (SQLException e) {
            vehiculoPanel.mostrarError("Error al cargar vehículos: " + e.getMessage());
        }
    }
 
    private void guardar() {
        try {
            vehiculo vehiculo = vehiculoPanel.obtenerDesdeFormulario();
 
            if (vehiculo.getIdVehiculo() == 0) {
                vehiculoService.registrar(vehiculo);
                vehiculoPanel.mostrarExito("Vehículo registrado correctamente");
            } else {
                vehiculoService.actualizar(vehiculo);
                vehiculoPanel.mostrarExito("Vehículo actualizado correctamente");
            }
 
            vehiculoPanel.limpiarFormulario();
            cargarTabla();
 
        } catch (IllegalArgumentException e) {
            vehiculoPanel.mostrarError(e.getMessage());
        } catch (SQLException e) {
            vehiculoPanel.mostrarError("Error del sistema: " + e.getMessage());
        }
    }
 
  
    private void eliminar() {
        int id = vehiculoPanel.getIdSeleccionado();
 
        if (id == 0) {
            vehiculoPanel.mostrarError("Seleccione un vehículo para eliminar");
            return;
        }
 
        boolean confirmar = vehiculoPanel.confirmar(
                "¿Está seguro de eliminar este vehículo?");
 
        if (!confirmar) return;
 
        try {
            vehiculoService.eliminar(id);
            vehiculoPanel.mostrarExito("Vehículo eliminado correctamente");
            vehiculoPanel.limpiarFormulario();
            cargarTabla();
 
        } catch (IllegalArgumentException e) {
            vehiculoPanel.mostrarError(e.getMessage());
        } catch (SQLException e) {
            vehiculoPanel.mostrarError("Error del sistema: " + e.getMessage());
        }
    }
 
    private void buscar() {
        try {
            String placa = vehiculoPanel.getTerminoBusqueda();
            if (placa.isEmpty()) {
                cargarTabla();
                return;
            }
            vehiculo vehiculo = vehiculoService.buscarPorPlaca(placa);
            vehiculoPanel.cargarTabla(List.of(vehiculo));
 
        } catch (IllegalArgumentException e) {
            vehiculoPanel.mostrarError(e.getMessage());
        } catch (SQLException e) {
            vehiculoPanel.mostrarError("Error al buscar: " + e.getMessage());
        }
    }
 
    private void cargarEnFormulario() {
        vehiculo vehiculo = vehiculoPanel.getVehiculoSeleccionado();
        if (vehiculo != null) {
            vehiculoPanel.cargarEnFormulario(vehiculo);
        }
    }
}
