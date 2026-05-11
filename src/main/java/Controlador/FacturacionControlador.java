package Controlador;

import Modelo.DetalleOrden;
import Modelo.Factura;
import Modelo.OrdenTrabajo;
import Modelo.usuario;
import Vista.FacturacionPanel;
import service.FacturaService;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Vera Sabando Luis Enrique
 */
public class FacturacionControlador {

    private final FacturaService   facturaService = new FacturaService();
    private final FacturacionPanel panel;
    private final usuario          usuarioSesion;

    // Orden actualmente cargada en pantalla
    private OrdenTrabajo ordenActual;

    public FacturacionControlador(FacturacionPanel panel, usuario usuarioSesion) {
        this.panel         = panel;
        this.usuarioSesion = usuarioSesion;
        iniciarEventos();
        cargarOrdenes();
        cargarHistorial();
    }

    private void iniciarEventos() {
        panel.getBtnCargar().addActionListener(e -> cargarDetalles());
        panel.getBtnGenerar().addActionListener(e -> generarFactura());
        panel.getBtnPagar().addActionListener(e -> cobrar());
        panel.getBtnAnular().addActionListener(e -> anular());

        // Al hacer clic en el historial → cargar la factura para anular
        panel.getTablaFacturas().getSelectionModel()
                .addListSelectionListener(e -> {
                    if (!e.getValueIsAdjusting()) {
                        seleccionarFacturaHistorial();
                    }
                });
    }

    // ── Cargar órdenes terminadas sin factura ────────────────────────────────
    private void cargarOrdenes() {
        try {
            List<OrdenTrabajo> ordenes = facturaService.listarOrdenesParaFacturar();
            panel.cargarOrdenes(ordenes);
        } catch (SQLException e) {
            panel.mostrarError("Error al cargar órdenes: " + e.getMessage());
        }
    }

    // ── Cargar detalles de la orden seleccionada ─────────────────────────────
    private void cargarDetalles() {
        int idOrden = panel.getIdOrdenSeleccionada();
        if (idOrden == 0) {
            panel.mostrarError("Seleccione una orden");
            return;
        }

        try {
            // Guardar la orden actual para usar su nombre de cliente
            List<OrdenTrabajo> ordenes = facturaService.listarOrdenesParaFacturar();
            ordenActual = ordenes.stream()
                    .filter(o -> o.getIdOrden() == idOrden)
                    .findFirst().orElse(null);

            List<DetalleOrden> detalles = facturaService.listarDetalles(idOrden);
            panel.cargarDetalles(detalles);

            double subtotal = facturaService.calcularSubtotal(detalles);
            String nombreCliente = ordenActual != null
                    ? ordenActual.getNombreCliente() : "—";
            panel.mostrarTotalesPrevios(subtotal, nombreCliente);

        } catch (SQLException e) {
            panel.mostrarError("Error al cargar detalles: " + e.getMessage());
        }
    }

    // ── Generar factura — idCliente real desde la orden ──────────────────────
    private void generarFactura() {
        int idOrden = panel.getIdOrdenSeleccionada();
        if (idOrden == 0) {
            panel.mostrarError("Seleccione una orden para facturar");
            return;
        }

        try {
            Factura factura = facturaService.generarFactura(
                    idOrden, usuarioSesion.getIdUsuario());

            String nombreCliente = ordenActual != null
                    ? ordenActual.getNombreCliente() : "—";
            panel.mostrarTotales(factura, nombreCliente);
            panel.mostrarExito("Factura generada: " + factura.getNumFactura());
            cargarOrdenes();
            cargarHistorial();

        } catch (IllegalArgumentException e) {
            panel.mostrarError(e.getMessage());
        } catch (SQLException e) {
            panel.mostrarError("Error del sistema: " + e.getMessage());
        }
    }

    // ── Cobrar ───────────────────────────────────────────────────────────────
    private void cobrar() {
        Factura factura = panel.getFacturaActual();
        if (factura == null) {
            panel.mostrarError("Primero genere o seleccione una factura");
            return;
        }

        if ("anulada".equals(factura.getEstado())) {
            panel.mostrarError("No se puede cobrar una factura anulada");
            return;
        }

        double monto = panel.getMontoRecibido();
        if (monto <= 0) {
            panel.mostrarError("Ingrese el monto recibido");
            return;
        }

        if (monto < factura.getTotal()) {
            panel.mostrarError(String.format(
                "Monto insuficiente.\nTotal: $%.2f\nRecibido: $%.2f",
                factura.getTotal(), monto));
            return;
        }

        double cambio = monto - factura.getTotal();
        panel.mostrarExito(String.format(
            "✓ Pago registrado correctamente\n" +
            "Factura: %s\n" +
            "Total cobrado: $%.2f\n" +
            "Cambio: $%.2f",
            factura.getNumFactura(), factura.getTotal(), cambio));

        panel.limpiar();
        cargarHistorial();
    }

    // ── Anular — desde factura generada o desde el historial ─────────────────
    private void anular() {
        Factura factura = panel.getFacturaActual();
        if (factura == null) {
            panel.mostrarError(
                "Seleccione una factura del historial para anular");
            return;
        }

        if ("anulada".equals(factura.getEstado())) {
            panel.mostrarError("Esta factura ya está anulada");
            return;
        }

        if (!panel.confirmar("¿Está seguro de anular la factura " +
                              factura.getNumFactura() + "?\n" +
                              "Esta acción no se puede deshacer.")) return;

        try {
            facturaService.anular(factura.getIdFactura());
            panel.mostrarExito("Factura " + factura.getNumFactura() +
                               " anulada correctamente");
            panel.limpiar();
            cargarOrdenes();  // la orden vuelve a estar disponible
            cargarHistorial();

        } catch (IllegalArgumentException e) {
            panel.mostrarError(e.getMessage());
        } catch (SQLException e) {
            panel.mostrarError("Error al anular: " + e.getMessage());
        }
    }

    // ── Seleccionar factura del historial ────────────────────────────────────
    private void seleccionarFacturaHistorial() {
        int idFactura = panel.getIdFacturaSeleccionadaHistorial();
        if (idFactura == 0) return;

        try {
            Factura factura = facturaService.buscarPorId(idFactura);
            panel.setFacturaActual(factura);

            // Mostrar sus totales en el resumen
            panel.mostrarTotales(factura, "Ver historial");

            // Cargar sus detalles
            List<DetalleOrden> detalles =
                    facturaService.listarDetalles(factura.getIdOrden());
            panel.cargarDetalles(detalles);

        } catch (SQLException e) {
            panel.mostrarError("Error al cargar factura: " + e.getMessage());
        }
    }

    // ── Historial ────────────────────────────────────────────────────────────
    private void cargarHistorial() {
        try {
            List<Factura> facturas = facturaService.listarTodas();
            panel.cargarHistorial(facturas);
        } catch (SQLException e) {
            panel.mostrarError("Error al cargar historial: " + e.getMessage());
        }
    }
}