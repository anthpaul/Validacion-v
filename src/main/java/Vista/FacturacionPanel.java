package Vista;

import Modelo.DetalleOrden;
import Modelo.Factura;
import Modelo.OrdenTrabajo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * @author Vera Sabando Luis Enrique
 */
public class FacturacionPanel extends JPanel {

    private JComboBox<String>  cmbOrden;
    private List<OrdenTrabajo> listaOrdenes;

    private JTable            tablaDetalles;
    private DefaultTableModel modeloDetalles;

    private JTable            tablaFacturas;
    private DefaultTableModel modeloFacturas;

    private JLabel lblSubtotal;
    private JLabel lblIVA;
    private JLabel lblTotal;
    private JLabel lblEstado;
    private JLabel lblNumFactura;
    private JLabel lblCliente;

    private JTextField txtMonto;

    private JButton btnCargar;
    private JButton btnGenerar;
    private JButton btnAnular;
    private JButton btnPagar;

    // Factura seleccionada — puede ser la recién generada o del historial
    private Factura facturaActual;

    public FacturacionPanel() {
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 240, 240));

        JLabel lblTitulo = new JLabel("Facturación");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTitulo, BorderLayout.NORTH);

        // ── Panel izquierdo ───────────────────────────────────────────────────
        JPanel panelIzq = new JPanel(new BorderLayout(0, 10));
        panelIzq.setBackground(new Color(240, 240, 240));
        panelIzq.setPreferredSize(new Dimension(320, 0));

        // Selección de orden
        JPanel panelOrden = new JPanel(new GridBagLayout());
        panelOrden.setBackground(Color.WHITE);
        panelOrden.setBorder(BorderFactory.createTitledBorder("Generar Nueva Factura"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;

        gbc.gridy = 0;
        panelOrden.add(new JLabel("Orden terminada:"), gbc);
        cmbOrden = new JComboBox<>();
        gbc.gridy = 1;
        panelOrden.add(cmbOrden, gbc);

        btnCargar = crearBoton("Cargar Detalles", new Color(0, 102, 204));
        gbc.gridy = 2;
        panelOrden.add(btnCargar, gbc);

        panelIzq.add(panelOrden, BorderLayout.NORTH);

        // Resumen
        JPanel panelTotales = new JPanel(new GridBagLayout());
        panelTotales.setBackground(Color.WHITE);
        panelTotales.setBorder(BorderFactory.createTitledBorder("Resumen de Factura"));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(4, 10, 4, 10);
        g.weightx = 1.0;

        lblNumFactura = agregarEtiqueta(panelTotales, g, "N° Factura:", "—", 0);
        lblCliente    = agregarEtiqueta(panelTotales, g, "Cliente:",    "—", 1);
        lblSubtotal   = agregarEtiqueta(panelTotales, g, "Subtotal:",   "$0.00", 2);
        lblIVA        = agregarEtiqueta(panelTotales, g, "IVA (12%):",  "$0.00", 3);
        lblTotal      = agregarEtiqueta(panelTotales, g, "TOTAL:",      "$0.00", 4);
        lblEstado     = agregarEtiqueta(panelTotales, g, "Estado:",     "—", 5);

        g.gridy = 6; g.gridx = 0; g.gridwidth = 1;
        panelTotales.add(new JLabel("Monto recibido:"), g);
        txtMonto = new JTextField();
        g.gridx = 1;
        panelTotales.add(txtMonto, g);

        JPanel panelBotones = new JPanel(new GridLayout(1, 3, 5, 0));
        panelBotones.setBackground(Color.WHITE);
        btnGenerar = crearBoton("Generar",  new Color(40, 167, 69));
        btnPagar   = crearBoton("Cobrar",   new Color(0, 102, 204));
        btnAnular  = crearBoton("Anular",   new Color(220, 53, 69));
        panelBotones.add(btnGenerar);
        panelBotones.add(btnPagar);
        panelBotones.add(btnAnular);

        g.gridy = 7; g.gridx = 0; g.gridwidth = 2;
        g.insets = new Insets(12, 8, 8, 8);
        panelTotales.add(panelBotones, g);

        panelIzq.add(panelTotales, BorderLayout.CENTER);
        add(panelIzq, BorderLayout.WEST);

        // ── Panel derecho ─────────────────────────────────────────────────────
        JPanel panelDer = new JPanel(new GridLayout(2, 1, 0, 10));
        panelDer.setBackground(new Color(240, 240, 240));

        // Tabla detalles
        JPanel panelDetalle = new JPanel(new BorderLayout());
        panelDetalle.setBorder(BorderFactory.createTitledBorder("Servicios y Repuestos"));
        String[] colDet = {"Tipo", "Descripción", "Cantidad", "Costo", "Subtotal"};
        modeloDetalles = new DefaultTableModel(colDet, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaDetalles = new JTable(modeloDetalles);
        tablaDetalles.setRowHeight(25);
        tablaDetalles.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        panelDetalle.add(new JScrollPane(tablaDetalles), BorderLayout.CENTER);

        // Tabla historial — al hacer clic carga la factura para anular
        JPanel panelHistorial = new JPanel(new BorderLayout());
        panelHistorial.setBorder(BorderFactory.createTitledBorder(
                "Historial de Facturas — clic para seleccionar"));
        String[] colFac = {"ID", "N° Factura", "Cliente", "Orden",
                           "Subtotal", "IVA", "Total", "Estado", "Fecha"};
        modeloFacturas = new DefaultTableModel(colFac, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaFacturas = new JTable(modeloFacturas);
        tablaFacturas.setRowHeight(25);
        tablaFacturas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaFacturas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        // Color por estado
        tablaFacturas.setDefaultRenderer(Object.class, (t, value, isSelected,
                hasFocus, row, col) -> {
            JLabel cell = new JLabel(value != null ? value.toString() : "");
            cell.setOpaque(true);
            String estado = (String) modeloFacturas.getValueAt(row, 7);
            if (isSelected) {
                cell.setBackground(t.getSelectionBackground());
                cell.setForeground(Color.WHITE);
            } else if ("anulada".equals(estado)) {
                cell.setBackground(new Color(248, 215, 218));
                cell.setForeground(new Color(120, 0, 0));
            } else {
                cell.setBackground(new Color(212, 237, 218));
                cell.setForeground(Color.BLACK);
            }
            cell.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            return cell;
        });

        SwingUtilities.invokeLater(() -> {
            tablaFacturas.getColumnModel().getColumn(0).setMinWidth(0);
            tablaFacturas.getColumnModel().getColumn(0).setMaxWidth(0);
            tablaFacturas.getColumnModel().getColumn(0).setWidth(0);
            tablaFacturas.getColumnModel().getColumn(0).setPreferredWidth(0);
        });

        panelHistorial.add(new JScrollPane(tablaFacturas), BorderLayout.CENTER);

        panelDer.add(panelDetalle);
        panelDer.add(panelHistorial);
        add(panelDer, BorderLayout.CENTER);
    }

    // ── Métodos que usa el Controlador ───────────────────────────────────────
    public void cargarOrdenes(List<OrdenTrabajo> ordenes) {
        this.listaOrdenes = ordenes;
        cmbOrden.removeAllItems();
        for (OrdenTrabajo ot : ordenes) {
            cmbOrden.addItem("Orden #" + ot.getIdOrden() +
                             " | " + ot.getNombreCliente() +
                             " | " + ot.getPlaca());
        }
    }

    public int getIdOrdenSeleccionada() {
        int idx = cmbOrden.getSelectedIndex();
        if (idx < 0 || listaOrdenes == null || listaOrdenes.isEmpty()) return 0;
        return listaOrdenes.get(idx).getIdOrden();
    }

    public void cargarDetalles(List<DetalleOrden> detalles) {
        modeloDetalles.setRowCount(0);
        for (DetalleOrden d : detalles) {
            modeloDetalles.addRow(new Object[]{
                d.getTipo(), d.getDescripcion(), d.getCantidad(),
                String.format("$%.2f", d.getCostoUnitario()),
                String.format("$%.2f", d.getSubtotal())
            });
        }
    }

    public void cargarHistorial(List<Factura> facturas) {
        modeloFacturas.setRowCount(0);
        for (Factura f : facturas) {
            modeloFacturas.addRow(new Object[]{
                f.getIdFactura(),
                f.getNumFactura(),
                f.getIdCliente(),   // el controlador puede enriquecer con nombre
                f.getIdOrden(),
                String.format("$%.2f", f.getSubtotal()),
                String.format("$%.2f", f.getIva()),
                String.format("$%.2f", f.getTotal()),
                f.getEstado(),
                f.getFechaEmision()
            });
        }
    }

    public void mostrarTotales(Factura factura, String nombreCliente) {
        this.facturaActual = factura;
        lblNumFactura.setText(factura.getNumFactura());
        lblCliente.setText(nombreCliente);
        lblSubtotal.setText(String.format("$%.2f", factura.getSubtotal()));
        lblIVA.setText(String.format("$%.2f", factura.getIva()));
        lblTotal.setText(String.format("$%.2f", factura.getTotal()));
        lblEstado.setText(factura.getEstado().toUpperCase());
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
    }

    public void mostrarTotalesPrevios(double subtotal, String nombreCliente) {
        lblCliente.setText(nombreCliente);
        double iva   = subtotal * 0.12;
        double total = subtotal + iva;
        lblSubtotal.setText(String.format("$%.2f", subtotal));
        lblIVA.setText(String.format("$%.2f", iva));
        lblTotal.setText(String.format("$%.2f", total));
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
    }

    // ── ID de factura seleccionada en el historial ────────────────────────────
    public int getIdFacturaSeleccionadaHistorial() {
        int fila = tablaFacturas.getSelectedRow();
        if (fila == -1) return 0;
        return (int) modeloFacturas.getValueAt(fila, 0);
    }

    public JTable getTablaFacturas() { return tablaFacturas; }

    public void limpiar() {
        if (cmbOrden.getItemCount() > 0) cmbOrden.setSelectedIndex(0);
        modeloDetalles.setRowCount(0);
        lblNumFactura.setText("—");
        lblCliente.setText("—");
        lblSubtotal.setText("$0.00");
        lblIVA.setText("$0.00");
        lblTotal.setText("$0.00");
        lblEstado.setText("—");
        txtMonto.setText("");
        facturaActual = null;
    }

    public double getMontoRecibido() {
        try { return Double.parseDouble(txtMonto.getText().trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    public Factura getFacturaActual() { return facturaActual; }
    public void setFacturaActual(Factura f) { this.facturaActual = f; }

    public void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
    public void mostrarExito(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
    public boolean confirmar(String msg) {
        return JOptionPane.showConfirmDialog(this, msg, "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public JButton getBtnCargar()  { return btnCargar; }
    public JButton getBtnGenerar() { return btnGenerar; }
    public JButton getBtnPagar()   { return btnPagar; }
    public JButton getBtnAnular()  { return btnAnular; }

    private JLabel agregarEtiqueta(JPanel panel, GridBagConstraints g,
                                   String etiqueta, String valor, int fila) {
        g.gridy = fila; g.gridx = 0; g.gridwidth = 1;
        panel.add(new JLabel(etiqueta), g);
        JLabel lbl = new JLabel(valor);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        g.gridx = 1;
        panel.add(lbl, g);
        return lbl;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}