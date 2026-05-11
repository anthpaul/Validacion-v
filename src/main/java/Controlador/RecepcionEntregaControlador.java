package Controlador;

import Modelo.Mecanico;
import Modelo.RecepcionEntrega;
import Modelo.vehiculo;
import Modelo.usuario;
import Vista.RecepcionEntregaPanel;
import service.RecepcionEntregaService;
import service.MecanicoService;

import java.sql.SQLException;
import java.util.List;

public class RecepcionEntregaControlador {

    private final RecepcionEntregaService service         = new RecepcionEntregaService();
    private final MecanicoService         mecanicoService = new MecanicoService();
    private final RecepcionEntregaPanel   panel;
    private final usuario                 usuarioSesion;

    public RecepcionEntregaControlador(RecepcionEntregaPanel panel,
                                       usuario usuarioSesion) {
        this.panel         = panel;
        this.usuarioSesion = usuarioSesion;
        iniciarEventos();
        cargarVehiculos();
        cargarMecanicos();
        cargarTabla();
    }

    public RecepcionEntregaControlador(RecepcionEntregaPanel panel) {
        this(panel, null);
    }

    private void iniciarEventos() {
        panel.getBtnNuevo().addActionListener(e -> panel.limpiarFormulario());
        panel.getBtnRegistrarRecepcion().addActionListener(e -> registrarRecepcion());
        panel.getBtnEntregar().addActionListener(e -> entregarVehiculo());
        panel.getBtnBuscar().addActionListener(e -> buscar());

        // Al seleccionar fila → cargar datos completos y verificar si puede entregar
        panel.getTabla().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int idRecepcion = panel.getIdRecepcionSeleccionado();
                if (idRecepcion > 0) {
                    cargarRegistroSeleccionado(idRecepcion);
                }
            }
        });
    }

    private void cargarVehiculos() {
        try {
            List<vehiculo> vehiculos = service.listarVehiculos();
            panel.cargarVehiculos(vehiculos);
        } catch (SQLException e) {
            panel.mostrarError("Error al cargar vehículos: " + e.getMessage());
        }
    }

    private void cargarMecanicos() {
        try {
            List<Mecanico> mecanicos = mecanicoService.listarTodos();
            panel.cargarMecanicos(mecanicos);
        } catch (SQLException e) {
            panel.mostrarError("Error al cargar mecánicos: " + e.getMessage());
        }
    }

    private void cargarTabla() {
        try {
            panel.cargarTabla(service.listarTodos());
        } catch (SQLException e) {
            panel.mostrarError("Error al cargar recepciones: " + e.getMessage());
        }
    }

    // ── Al seleccionar una fila carga todo y habilita/deshabilita entrega ────
    private void cargarRegistroSeleccionado(int idRecepcion) {
        try {
            RecepcionEntrega r = service.buscarPorId(idRecepcion);
            panel.cargarRegistroCompleto(r);

            // Verificar si puede entregar
            boolean puedeEntregar = service.puedeEntregar(idRecepcion)
                                    && !"entregado".equals(r.getEstado());
            panel.habilitarEntrega(puedeEntregar);

        } catch (SQLException e) {
            panel.mostrarError("Error al cargar el registro: " + e.getMessage());
        }
    }

    private void registrarRecepcion() {
        try {
            RecepcionEntrega recepcion = panel.obtenerRecepcionDesdeFormulario();
            int idUsuario = usuarioSesion != null ? usuarioSesion.getIdUsuario() : 1;

            service.registrarRecepcion(recepcion, idUsuario);

            panel.mostrarExito("Recepción registrada y orden de trabajo creada correctamente");
            panel.limpiarFormulario();
            cargarTabla();

        } catch (IllegalArgumentException e) {
            panel.mostrarError(e.getMessage());
        } catch (SQLException e) {
            panel.mostrarError("Error del sistema: " + e.getMessage());
        }
    }

    private void entregarVehiculo() {
        int idRecepcion = panel.getIdRecepcionSeleccionado();
        if (idRecepcion == 0) {
            panel.mostrarError("Seleccione un registro de la tabla para entregar");
            return;
        }

        int kmSalida = panel.getKmSalida();
        if (kmSalida <= 0) {
            panel.mostrarError("Ingrese el kilometraje de salida");
            return;
        }

        if (!panel.confirmar("¿Confirma la entrega del vehículo?")) return;

        try {
            int idUsuario = usuarioSesion != null ? usuarioSesion.getIdUsuario() : 1;
            service.entregarVehiculo(idRecepcion, idUsuario,
                                     kmSalida, panel.getObservacionEntrega());

            panel.mostrarExito("Vehículo entregado correctamente");
            panel.limpiarFormulario();
            cargarTabla();

        } catch (IllegalArgumentException e) {
            panel.mostrarError(e.getMessage());
        } catch (SQLException e) {
            panel.mostrarError("Error del sistema: " + e.getMessage());
        }
    }

    private void buscar() {
        try {
            String placa = panel.getTerminoBusqueda();
            panel.cargarTabla(service.buscarPorPlaca(placa));
        } catch (SQLException e) {
            panel.mostrarError("Error al buscar: " + e.getMessage());
        }
    }
}