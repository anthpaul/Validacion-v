package Controlador;

import Modelo.DetalleOrden;
import Modelo.Mecanico;
import Modelo.OrdenTrabajo;
import Modelo.usuario;
import Vista.MantenimientoPanel;
import service.MantenimientoService;
import service.MecanicoService;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Omar Mite
 */
public class MantenimientoControlador {

    private final MantenimientoService service         = new MantenimientoService();
    private final MecanicoService      mecanicoService = new MecanicoService();
    private final MantenimientoPanel   panel;
    private final usuario              usuarioSesion;
    private int                        idMecanicoSesion = 0;

    public MantenimientoControlador(MantenimientoPanel panel, usuario usuarioSesion) {
        this.panel         = panel;
        this.usuarioSesion = usuarioSesion;
        iniciarEventos();
        resolverIdMecanico();
        configurarVistaPorRol();
        cargarTabla();
    }

    public MantenimientoControlador(MantenimientoPanel panel) {
        this(panel, null);
    }

    // ── Buscar id_mecanico del usuario en sesión ─────────────────────────────
    private void resolverIdMecanico() {
        if (usuarioSesion == null || !usuarioSesion.esMecanico()) return;

        try {
            List<Mecanico> todos = mecanicoService.listarTodos();
            for (Mecanico m : todos) {
                if (m.getIdUsuario() == usuarioSesion.getIdUsuario()) {
                    idMecanicoSesion = m.getId();
                    break;
                }
            }
        } catch (SQLException e) {
            panel.mostrarError("Error al identificar el mecánico: " + e.getMessage());
        }
    }

    // ── Configurar la vista según el rol ─────────────────────────────────────
    private void configurarVistaPorRol() {
        if (usuarioSesion != null && usuarioSesion.esMecanico()) {
            // Mecánico: ocultar el combo de mecánicos
            panel.ocultarComboMecanico();
        } else {
            // Admin: cargar todos los mecánicos en el combo
            try {
                List<Mecanico> mecanicos = mecanicoService.listarTodos();
                panel.cargarMecanicos(mecanicos);
            } catch (SQLException e) {
                panel.mostrarError("Error al cargar mecánicos: " + e.getMessage());
            }
        }
    }

    private void iniciarEventos() {
        panel.getBtnNuevo().addActionListener(e -> panel.limpiarFormulario());
        panel.getBtnAgregarDetalle().addActionListener(e -> agregarDetalle());
        panel.getBtnFinalizar().addActionListener(e -> finalizar());
        panel.getBtnVerDetalles().addActionListener(e -> verDetalles());

        panel.getTabla().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) verDetalles();
        });
    }

    public void cargarTabla() {
        try {
            List<OrdenTrabajo> lista;

            if (usuarioSesion != null && usuarioSesion.esMecanico()
                    && idMecanicoSesion > 0) {
                // Mecánico → SOLO sus órdenes
                lista = service.listarPorMecanico(idMecanicoSesion);
            } else {
                // Admin/Recepcionista → todas
                lista = service.listarTodas();
            }

            panel.cargarTabla(lista);

        } catch (Exception e) {
            panel.mostrarError("Error al cargar órdenes: " + e.getMessage());
        }
    }

    private void agregarDetalle() {
        try {
            if (panel.getIdSeleccionado() == 0) {
                panel.mostrarError("Seleccione una orden de la tabla primero");
                return;
            }

            DetalleOrden det = panel.obtenerDetalleFormulario();

            // Si es mecánico el id_orden ya viene del panel
            // Si es admin usa el id del mecánico seleccionado en el combo
            service.agregarDetalle(det);

            panel.mostrarExito("Detalle agregado correctamente");
            panel.limpiarFormularioDetalle();
            verDetalles();
            cargarTabla();

        } catch (NumberFormatException e) {
            panel.mostrarError("Costo y Cantidad deben ser números válidos");
        } catch (IllegalArgumentException e) {
            panel.mostrarError(e.getMessage());
        } catch (Exception e) {
            panel.mostrarError("Error al agregar detalle: " + e.getMessage());
        }
    }

    private void finalizar() {
        int id = panel.getIdSeleccionado();
        if (id == 0) {
            panel.mostrarError("Seleccione una orden para finalizar");
            return;
        }

        if (!panel.confirmar("¿Está seguro de finalizar esta orden?")) return;

        try {
            service.finalizarOrden(id);
            panel.mostrarExito("Orden finalizada correctamente");
            cargarTabla();
            panel.limpiarFormulario();

        } catch (IllegalArgumentException e) {
            panel.mostrarError(e.getMessage());
        } catch (Exception e) {
            panel.mostrarError("Error al finalizar: " + e.getMessage());
        }
    }

    private void verDetalles() {
        int id = panel.getIdSeleccionado();
        if (id == 0) return;

        try {
            List<DetalleOrden> detalles = service.listarDetalles(id);
            panel.cargarDetalles(detalles);
        } catch (Exception e) {
            panel.mostrarError("Error al cargar detalles: " + e.getMessage());
        }
    }
}