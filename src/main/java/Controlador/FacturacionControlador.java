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

    private final FacturaService    facturaService = new FacturaService();
    private final FacturacionPanel  panel;
    private final usuario           usuarioSesion;

    public FacturacionControlador(FacturacionPanel panel, usuario usuarioSesion) {
        this.panel         = panel;
        this.usuarioSesion = usuarioSesion;
        iniciarEventos();
        cargarOrdenes();
        cargarHistorial();
    }

    private void iniciarEventos() {

        // Cargar detalles al seleccionar orden
        panel.getBtnCargar().addActionListener(e -> cargarDetalles());

        // Generar factura
        panel.getBtnGenerar().addActionListener(e -> generarFactura());

        // Cobrar
        panel.getBtnPagar().addActionListener(e -> cobrar());

        // Anular
        panel.getBtnAnular().addActionListener(e -> anular());
    }

    private void cargarOrdenes() {
        try {
            List<OrdenTrabajo> ordenes = facturaService.listarOrdenesParaFacturar();
            panel.cargarOrdenes(ordenes);
        } catch (SQLException e) {
            panel.mostrarError("Error al cargar órdenes: " + e.getMessage());
        }
    }

    private void cargarDetalles() {
        int idOrden = panel.getIdOrdenSeleccionada();
        if (idOrden == 0) {
            panel.mostrarError("Seleccione una orden");
            return;
        }

        try {
            List<DetalleOrden> detalles = facturaService.listarDetalles(idOrden);
            panel.cargarDetalles(detalles);

            double subtotal = facturaService.calcularSubtotal(detalles);
            panel.mostrarTotalesPrevios(subtotal);

        } catch (SQLException e) {
            panel.mostrarError("Error al cargar detalles: " + e.getMessage());
        }
    }


    private void generarFactura() {
        int idOrden = panel.getIdOrdenSeleccionada();
        if (idOrden == 0) {
            panel.mostrarError("Seleccione una orden para facturar");
            return;
        }

        try {
            // idCliente = 1 temporalmente
            // TODO: obtener idCliente desde la recepción de la orden
            Factura factura = facturaService.generarFactura(
                    idOrden, 1, usuarioSesion.getIdUsuario());

            panel.mostrarTotales(factura);
            panel.mostrarExito("Factura generada: " + factura.getNumFactura());
            cargarOrdenes();
            cargarHistorial();

        } catch (IllegalArgumentException e) {
            panel.mostrarError(e.getMessage());
        } catch (SQLException e) {
            panel.mostrarError("Error del sistema: " + e.getMessage());
        }
    }

    private void cobrar() {
        Factura factura = panel.getFacturaActual();
        if (factura == null) {
            panel.mostrarError("Primero genere la factura");
            return;
        }

        double monto = panel.getMontoRecibido();
        if (monto <= 0) {
            panel.mostrarError("Ingrese el monto recibido");
            return;
        }

        if (monto < factura.getTotal()) {
            panel.mostrarError(String.format(
                "Monto insuficiente. Total: $%.2f — Recibido: $%.2f",
                factura.getTotal(), monto));
            return;
        }

        double cambio = monto - factura.getTotal();
        panel.mostrarExito(String.format(
            "Pago registrado.\nCambio: $%.2f", cambio));
        panel.limpiar();
        cargarHistorial();
    }

    private void anular() {
        Factura factura = panel.getFacturaActual();
        if (factura == null) {
            panel.mostrarError("No hay factura seleccionada para anular");
            return;
        }

        if (!panel.confirmar("¿Está seguro de anular la factura " +
                              factura.getNumFactura() + "?")) return;

        try {
            facturaService.anular(factura.getIdFactura());
            panel.mostrarExito("Factura anulada correctamente");
            panel.limpiar();
            cargarHistorial();

        } catch (SQLException e) {
            panel.mostrarError("Error al anular: " + e.getMessage());
        }
    }

    // ── Historial de facturas ────────────────────────────────────────────────
    private void cargarHistorial() {
        try {
            List<Factura> facturas = facturaService.listarTodas();
            panel.cargarHistorial(facturas);
        } catch (SQLException e) {
            panel.mostrarError("Error al cargar historial: " + e.getMessage());
        }
    }
}