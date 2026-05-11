package Vista;

import dao.clientedao;
import dao.Vehiculodao;
import dao.OrdenTrabajodao;
import dao.FacturaDAO;
import dao.Usuariodao;
import Modelo.OrdenTrabajo;
import Modelo.Factura;
import Modelo.usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * 
 * @author Anthony Delgado 
 * 
 */
public class Reportespanel extends JPanel {

    private final clientedao      clienteDAO  = new clientedao();
    private final Vehiculodao     vehiculoDAO = new Vehiculodao();
    private final OrdenTrabajodao ordenDAO    = new OrdenTrabajodao();
    private final FacturaDAO      facturaDAO  = new FacturaDAO();
    private final Usuariodao      usuarioDAO  = new Usuariodao();

    // ── Tarjetas resumen ─────────────────────────────────────────────────────
    private JLabel lblTotalClientes;
    private JLabel lblTotalVehiculos;
    private JLabel lblUsuariosActivos;
    private JLabel lblOrdenesAbiertas;
    private JLabel lblOrdenesEnProceso;
    private JLabel lblOrdenesTerminadas;
    private JLabel lblTotalFacturado;
    private JLabel lblFacturasAnuladas;

    // ── Tablas ───────────────────────────────────────────────────────────────
    private JTable            tablaOrdenes;
    private DefaultTableModel modeloOrdenes;

    private JTable            tablaFacturas;
    private DefaultTableModel modeloFacturas;

    private JTable            tablaUsuarios;
    private DefaultTableModel modeloUsuarios;

    public Reportespanel() {
        iniciarComponentes();
        cargarDatos();
    }

    private void iniciarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 240, 240));

        // ── Título + botón actualizar ─────────────────────────────────────────
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setBackground(new Color(240, 240, 240));
        JLabel lblTitulo = new JLabel("Reportes del Sistema");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelTitulo.add(lblTitulo, BorderLayout.WEST);

        JButton btnRefrescar = new JButton("↻ Actualizar");
        btnRefrescar.setBackground(new Color(0, 102, 204));
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.setBorderPainted(false);
        btnRefrescar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefrescar.addActionListener(e -> cargarDatos());
        panelTitulo.add(btnRefrescar, BorderLayout.EAST);
        add(panelTitulo, BorderLayout.NORTH);

        // ── Panel principal con scroll ────────────────────────────────────────
        JPanel panelMain = new JPanel();
        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.Y_AXIS));
        panelMain.setBackground(new Color(240, 240, 240));

        // ── Tarjetas resumen ──────────────────────────────────────────────────
        JPanel panelTarjetas = new JPanel(new GridLayout(2, 4, 10, 10));
        panelTarjetas.setBackground(new Color(240, 240, 240));
        panelTarjetas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        lblTotalClientes    = crearTarjeta(panelTarjetas, "Clientes",
                                           "—", new Color(0, 102, 204));
        lblTotalVehiculos   = crearTarjeta(panelTarjetas, "Vehículos",
                                           "—", new Color(40, 167, 69));
        lblUsuariosActivos  = crearTarjeta(panelTarjetas, "Usuarios Activos",
                                           "—", new Color(23, 162, 184));
        lblOrdenesAbiertas  = crearTarjeta(panelTarjetas, "Órdenes Abiertas",
                                           "—", new Color(255, 193, 7));
        lblOrdenesEnProceso = crearTarjeta(panelTarjetas, "En Proceso",
                                           "—", new Color(23, 162, 184));
        lblOrdenesTerminadas= crearTarjeta(panelTarjetas, "Terminadas",
                                           "—", new Color(40, 167, 69));
        lblTotalFacturado   = crearTarjeta(panelTarjetas, "Total Facturado",
                                           "$0.00", new Color(102, 16, 242));
        lblFacturasAnuladas = crearTarjeta(panelTarjetas, "Facturas Anuladas",
                                           "—", new Color(220, 53, 69));

        panelMain.add(panelTarjetas);
        panelMain.add(Box.createVerticalStrut(15));

        // ── Tablas en grid ────────────────────────────────────────────────────
        JPanel panelTablas = new JPanel(new GridLayout(1, 2, 10, 0));
        panelTablas.setBackground(new Color(240, 240, 240));
        panelTablas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));

        // Tabla órdenes
        JPanel panelOrdenes = new JPanel(new BorderLayout());
        panelOrdenes.setBorder(BorderFactory.createTitledBorder("Órdenes de Trabajo"));
        panelOrdenes.setBackground(Color.WHITE);

        String[] colOrdenes = {"ID", "Cliente", "Placa", "Mecánico",
                               "Estado", "Fecha Inicio", "Fecha Fin"};
        modeloOrdenes = new DefaultTableModel(colOrdenes, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaOrdenes = new JTable(modeloOrdenes);
        tablaOrdenes.setRowHeight(24);
        tablaOrdenes.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));

        tablaOrdenes.setDefaultRenderer(Object.class, (t, value, isSelected,
                hasFocus, row, col) -> {
            JLabel cell = new JLabel(value != null ? value.toString() : "");
            cell.setOpaque(true);
            String estado = (String) modeloOrdenes.getValueAt(row, 4);
            if (isSelected) {
                cell.setBackground(t.getSelectionBackground());
            } else {
                switch (estado != null ? estado : "") {
                    case "abierta":    cell.setBackground(new Color(255, 243, 205)); break;
                    case "en_proceso": cell.setBackground(new Color(209, 236, 241)); break;
                    case "terminada":  cell.setBackground(new Color(212, 237, 218)); break;
                    case "cancelada":  cell.setBackground(new Color(248, 215, 218)); break;
                    default:           cell.setBackground(Color.WHITE);
                }
            }
            cell.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            return cell;
        });

        SwingUtilities.invokeLater(() -> {
            tablaOrdenes.getColumnModel().getColumn(0).setMinWidth(0);
            tablaOrdenes.getColumnModel().getColumn(0).setMaxWidth(0);
            tablaOrdenes.getColumnModel().getColumn(0).setWidth(0);
            tablaOrdenes.getColumnModel().getColumn(0).setPreferredWidth(0);
        });

        panelOrdenes.add(new JScrollPane(tablaOrdenes), BorderLayout.CENTER);

        // Tabla facturas
        JPanel panelFacturas = new JPanel(new BorderLayout());
        panelFacturas.setBorder(BorderFactory.createTitledBorder("Historial de Facturas"));
        panelFacturas.setBackground(Color.WHITE);

        String[] colFacturas = {"ID", "N° Factura", "Orden",
                                "Subtotal", "IVA", "Total", "Estado", "Fecha"};
        modeloFacturas = new DefaultTableModel(colFacturas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaFacturas = new JTable(modeloFacturas);
        tablaFacturas.setRowHeight(24);
        tablaFacturas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));

        tablaFacturas.setDefaultRenderer(Object.class, (t, value, isSelected,
                hasFocus, row, col) -> {
            JLabel cell = new JLabel(value != null ? value.toString() : "");
            cell.setOpaque(true);
            String estado = (String) modeloFacturas.getValueAt(row, 6);
            if (isSelected) {
                cell.setBackground(t.getSelectionBackground());
            } else if ("anulada".equals(estado)) {
                cell.setBackground(new Color(248, 215, 218));
            } else {
                cell.setBackground(new Color(212, 237, 218));
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

        panelFacturas.add(new JScrollPane(tablaFacturas), BorderLayout.CENTER);

        panelTablas.add(panelOrdenes);
        panelTablas.add(panelFacturas);
        panelMain.add(panelTablas);
        panelMain.add(Box.createVerticalStrut(15));

        // ── Tabla usuarios ────────────────────────────────────────────────────
        JPanel panelUsuarios = new JPanel(new BorderLayout());
        panelUsuarios.setBorder(BorderFactory.createTitledBorder("Usuarios del Sistema"));
        panelUsuarios.setBackground(Color.WHITE);
        panelUsuarios.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        String[] colUsuarios = {"ID", "Nombre", "Email", "Rol", "Estado"};
        modeloUsuarios = new DefaultTableModel(colUsuarios, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaUsuarios = new JTable(modeloUsuarios);
        tablaUsuarios.setRowHeight(24);
        tablaUsuarios.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));

        tablaUsuarios.setDefaultRenderer(Object.class, (t, value, isSelected,
                hasFocus, row, col) -> {
            JLabel cell = new JLabel(value != null ? value.toString() : "");
            cell.setOpaque(true);
            boolean activo = (boolean) modeloUsuarios.getValueAt(row, 4);
            if (isSelected) {
                cell.setBackground(t.getSelectionBackground());
                cell.setForeground(Color.WHITE);
            } else if (!activo) {
                cell.setBackground(new Color(248, 215, 218));
                cell.setForeground(new Color(120, 0, 0));
            } else {
                cell.setBackground(Color.WHITE);
                cell.setForeground(Color.BLACK);
            }
            cell.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            return cell;
        });

        SwingUtilities.invokeLater(() -> {
            tablaUsuarios.getColumnModel().getColumn(0).setMinWidth(0);
            tablaUsuarios.getColumnModel().getColumn(0).setMaxWidth(0);
            tablaUsuarios.getColumnModel().getColumn(0).setWidth(0);
            tablaUsuarios.getColumnModel().getColumn(0).setPreferredWidth(0);
        });

        panelUsuarios.add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);
        panelMain.add(panelUsuarios);

        add(new JScrollPane(panelMain), BorderLayout.CENTER);
    }

    // ── Cargar todos los datos ────────────────────────────────────────────────
    private void cargarDatos() {
        try {
            // Tarjetas
            lblTotalClientes.setText(
                String.valueOf(clienteDAO.listarTodos().size()));
            lblTotalVehiculos.setText(
                String.valueOf(vehiculoDAO.listarTodos().size()));

            List<usuario> usuarios = usuarioDAO.listarTodos();
            long activos = usuarios.stream().filter(usuario::isActivo).count();
            lblUsuariosActivos.setText(String.valueOf(activos));

            // Órdenes
            List<OrdenTrabajo> ordenes = ordenDAO.listarTodas();
            long abiertas    = ordenes.stream()
                .filter(o -> "abierta".equals(o.getEstadoTexto())).count();
            long enProceso   = ordenes.stream()
                .filter(o -> "en_proceso".equals(o.getEstadoTexto())).count();
            long terminadas  = ordenes.stream()
                .filter(o -> "terminada".equals(o.getEstadoTexto())).count();

            lblOrdenesAbiertas.setText(String.valueOf(abiertas));
            lblOrdenesEnProceso.setText(String.valueOf(enProceso));
            lblOrdenesTerminadas.setText(String.valueOf(terminadas));

            // Tabla órdenes
            modeloOrdenes.setRowCount(0);
            for (OrdenTrabajo ot : ordenes) {
                modeloOrdenes.addRow(new Object[]{
                    ot.getIdOrden(),
                    ot.getNombreCliente(),
                    ot.getPlaca(),
                    ot.getNombreMecanico(),
                    ot.getEstadoTexto(),
                    ot.getFechaInicio() != null ? ot.getFechaInicio() : "",
                    ot.getFechaFin() != null && !ot.getFechaFin().isEmpty()
                            ? ot.getFechaFin() : "—"
                });
            }

            // Facturas
            List<Factura> facturas = facturaDAO.listarTodas();
            double totalFacturado = facturas.stream()
                    .filter(f -> "activa".equals(f.getEstado()))
                    .mapToDouble(Factura::getTotal).sum();
            long anuladas = facturas.stream()
                    .filter(f -> "anulada".equals(f.getEstado())).count();

            lblTotalFacturado.setText(String.format("$%.2f", totalFacturado));
            lblFacturasAnuladas.setText(String.valueOf(anuladas));

            // Tabla facturas
            modeloFacturas.setRowCount(0);
            for (Factura f : facturas) {
                modeloFacturas.addRow(new Object[]{
                    f.getIdFactura(), f.getNumFactura(), f.getIdOrden(),
                    String.format("$%.2f", f.getSubtotal()),
                    String.format("$%.2f", f.getIva()),
                    String.format("$%.2f", f.getTotal()),
                    f.getEstado(), f.getFechaEmision()
                });
            }

            // Tabla usuarios
            modeloUsuarios.setRowCount(0);
            for (usuario u : usuarios) {
                modeloUsuarios.addRow(new Object[]{
                    u.getIdUsuario(),
                    u.getNombre(),
                    u.getEmail(),
                    u.getRol().getValor(),
                    u.isActivo()
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar datos: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel crearTarjeta(JPanel panel, String titulo,
                                String valor, Color color) {
        JPanel tarjeta = new JPanel(new BorderLayout(0, 5));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JPanel barraColor = new JPanel();
        barraColor.setBackground(color);
        barraColor.setPreferredSize(new Dimension(0, 5));
        tarjeta.add(barraColor, BorderLayout.NORTH);

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 11));
        lblTitulo.setForeground(new Color(100, 100, 100));
        tarjeta.add(lblTitulo, BorderLayout.CENTER);

        JLabel lblValor = new JLabel(valor, SwingConstants.CENTER);
        lblValor.setFont(new Font("Arial", Font.BOLD, 22));
        lblValor.setForeground(color);
        tarjeta.add(lblValor, BorderLayout.SOUTH);

        panel.add(tarjeta);
        return lblValor;
    }
}