package Vista;

import Controlador.MantenimientoControlador;
import Modelo.DetalleOrden;
import Modelo.Mecanico;
import Modelo.OrdenTrabajo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * @author Omar Mite
 */
public class MantenimientoPanel extends JPanel {

    private JPanel            panelComboMecanico;
    private JComboBox<String> cmbMecanico;
    private List<Mecanico>    listaMecanicos;
    private JTextField        txtDescripcion;

    private JComboBox<String> cmbTipo;
    private JTextField        txtDetDescripcion;
    private JTextField        txtCosto;
    private JTextField        txtCantidad;

    private JButton btnNuevo;
    private JButton btnAgregarDetalle;
    private JButton btnFinalizar;
    private JButton btnVerDetalles;

    private JTable            tablaOrdenes;
    private DefaultTableModel modeloOrdenes;

    private JTable            tablaDetalles;
    private DefaultTableModel modeloDetalles;

    public MantenimientoPanel() {
        iniciarComponentes();
        new MantenimientoControlador(this);
    }

    private void iniciarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 240, 240));

        JLabel lblTitulo = new JLabel("Gestión de Mantenimiento");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTitulo, BorderLayout.NORTH);

        // ── Panel izquierdo ───────────────────────────────────────────────────
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createTitledBorder("Agregar detalle a la orden"));
        panelForm.setPreferredSize(new Dimension(280, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;

        int fila = 0;

        // Combo mecánico — solo visible para admin
        panelComboMecanico = new JPanel(new GridBagLayout());
        panelComboMecanico.setBackground(Color.WHITE);
        GridBagConstraints gbcM = new GridBagConstraints();
        gbcM.fill = GridBagConstraints.HORIZONTAL;
        gbcM.weightx = 1.0; gbcM.gridwidth = 2;
        gbcM.gridy = 0;
        panelComboMecanico.add(new JLabel("Mecánico *"), gbcM);
        cmbMecanico = new JComboBox<>();
        gbcM.gridy = 1;
        panelComboMecanico.add(cmbMecanico, gbcM);
        gbc.gridy = fila++;
        panelForm.add(panelComboMecanico, gbc);

        gbc.gridy = fila++;
        panelForm.add(new JLabel("Descripción orden"), gbc);
        txtDescripcion = new JTextField();
        gbc.gridy = fila++;
        panelForm.add(txtDescripcion, gbc);

        gbc.gridy = fila++;
        panelForm.add(new JSeparator(), gbc);

        gbc.gridy = fila++;
        panelForm.add(new JLabel("Tipo *"), gbc);
        cmbTipo = new JComboBox<>(new String[]{"servicio", "repuesto"});
        gbc.gridy = fila++;
        panelForm.add(cmbTipo, gbc);

        gbc.gridy = fila++;
        panelForm.add(new JLabel("Descripción detalle *"), gbc);
        txtDetDescripcion = new JTextField();
        gbc.gridy = fila++;
        panelForm.add(txtDetDescripcion, gbc);

        gbc.gridy = fila++;
        panelForm.add(new JLabel("Costo unitario *"), gbc);
        txtCosto = new JTextField();
        gbc.gridy = fila++;
        panelForm.add(txtCosto, gbc);

        gbc.gridy = fila++;
        panelForm.add(new JLabel("Cantidad *"), gbc);
        txtCantidad = new JTextField();
        gbc.gridy = fila++;
        panelForm.add(txtCantidad, gbc);

        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 5, 5));
        panelBotones.setBackground(Color.WHITE);
        btnNuevo          = crearBoton("Nuevo",           new Color(108, 117, 125));
        btnAgregarDetalle = crearBoton("Agregar Detalle", new Color(40, 167, 69));
        btnVerDetalles    = crearBoton("Ver Detalles",    new Color(0, 102, 204));
        btnFinalizar      = crearBoton("Finalizar Orden", new Color(220, 53, 69));
        panelBotones.add(btnNuevo);
        panelBotones.add(btnAgregarDetalle);
        panelBotones.add(btnVerDetalles);
        panelBotones.add(btnFinalizar);

        gbc.gridy = fila++;
        gbc.insets = new Insets(12, 8, 8, 8);
        panelForm.add(panelBotones, gbc);
        add(panelForm, BorderLayout.WEST);

        // ── Panel derecho ─────────────────────────────────────────────────────
        JPanel panelDerecho = new JPanel(new GridLayout(2, 1, 0, 10));
        panelDerecho.setBackground(new Color(240, 240, 240));

        // Tabla órdenes — ID oculto, muestra cliente, placa, mecánico, estado
        JPanel panelOrdenes = new JPanel(new BorderLayout());
        panelOrdenes.setBorder(BorderFactory.createTitledBorder("Órdenes de Trabajo"));

        String[] colOrdenes = {"ID", "Cliente", "Placa", "Mecánico",
                               "Estado", "Fecha Inicio", "Fecha Fin"};
        modeloOrdenes = new DefaultTableModel(colOrdenes, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaOrdenes = new JTable(modeloOrdenes);
        tablaOrdenes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaOrdenes.setRowHeight(25);
        tablaOrdenes.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        // Color por estado
        tablaOrdenes.setDefaultRenderer(Object.class, (t, value, isSelected,
                hasFocus, row, col) -> {
            JLabel cell = new JLabel(value != null ? value.toString() : "");
            cell.setOpaque(true);
            String estado = (String) modeloOrdenes.getValueAt(row, 4);
            if (isSelected) {
                cell.setBackground(t.getSelectionBackground());
                cell.setForeground(Color.WHITE);
            } else {
                switch (estado != null ? estado : "") {
                    case "abierta":    cell.setBackground(new Color(255, 243, 205)); break;
                    case "en_proceso": cell.setBackground(new Color(209, 236, 241)); break;
                    case "terminada":  cell.setBackground(new Color(212, 237, 218)); break;
                    case "cancelada":  cell.setBackground(new Color(248, 215, 218)); break;
                    default:           cell.setBackground(Color.WHITE);
                }
                cell.setForeground(Color.BLACK);
            }
            cell.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            return cell;
        });

        panelOrdenes.add(new JScrollPane(tablaOrdenes), BorderLayout.CENTER);

        SwingUtilities.invokeLater(() -> {
            tablaOrdenes.getColumnModel().getColumn(0).setMinWidth(0);
            tablaOrdenes.getColumnModel().getColumn(0).setMaxWidth(0);
            tablaOrdenes.getColumnModel().getColumn(0).setWidth(0);
            tablaOrdenes.getColumnModel().getColumn(0).setPreferredWidth(0);
        });

        // Tabla detalles
        JPanel panelDetalles = new JPanel(new BorderLayout());
        panelDetalles.setBorder(
                BorderFactory.createTitledBorder("Detalles de la Orden Seleccionada"));

        String[] colDetalles = {"ID", "Tipo", "Descripción", "Cantidad", "Costo", "Subtotal"};
        modeloDetalles = new DefaultTableModel(colDetalles, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaDetalles = new JTable(modeloDetalles);
        tablaDetalles.setRowHeight(25);
        tablaDetalles.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        panelDetalles.add(new JScrollPane(tablaDetalles), BorderLayout.CENTER);

        SwingUtilities.invokeLater(() -> {
            tablaDetalles.getColumnModel().getColumn(0).setMinWidth(0);
            tablaDetalles.getColumnModel().getColumn(0).setMaxWidth(0);
            tablaDetalles.getColumnModel().getColumn(0).setWidth(0);
            tablaDetalles.getColumnModel().getColumn(0).setPreferredWidth(0);
        });

        panelDerecho.add(panelOrdenes);
        panelDerecho.add(panelDetalles);
        add(panelDerecho, BorderLayout.CENTER);
    }

    public void ocultarComboMecanico() {
        panelComboMecanico.setVisible(false);
    }

    public void cargarMecanicos(List<Mecanico> mecanicos) {
        this.listaMecanicos = mecanicos;
        cmbMecanico.removeAllItems();
        for (Mecanico m : mecanicos)
            cmbMecanico.addItem(m.getNombre() + " " + m.getApellido() +
                                " — " + m.getCedula());
    }

    public void cargarTabla(List<OrdenTrabajo> lista) {
        modeloOrdenes.setRowCount(0);
        for (OrdenTrabajo ot : lista) {
            modeloOrdenes.addRow(new Object[]{
                ot.getIdOrden(),
                ot.getNombreCliente(),
                ot.getPlaca(),
                ot.getNombreMecanico(),
                ot.getEstadoTexto(),
                ot.getFechaInicio() != null ? ot.getFechaInicio() : "",
                ot.getFechaFin()    != null && !ot.getFechaFin().isEmpty()
                        ? ot.getFechaFin() : "—"
            });
        }
    }

    public void cargarDetalles(List<DetalleOrden> detalles) {
        modeloDetalles.setRowCount(0);
        for (DetalleOrden d : detalles) {
            modeloDetalles.addRow(new Object[]{
                d.getIdDetalle(), d.getTipo(), d.getDescripcion(),
                d.getCantidad(),
                String.format("$%.2f", d.getCostoUnitario()),
                String.format("$%.2f", d.getSubtotal())
            });
        }
    }

    public DetalleOrden obtenerDetalleFormulario() {
        int idOrden = getIdSeleccionado();
        return new DetalleOrden(
            idOrden,
            cmbTipo.getSelectedItem().toString(),
            txtDetDescripcion.getText().trim(),
            Double.parseDouble(txtCosto.getText().trim()),
            Integer.parseInt(txtCantidad.getText().trim())
        );
    }

    public int getMecanicoIdSeleccionado() {
        int idx = cmbMecanico.getSelectedIndex();
        if (idx < 0 || listaMecanicos == null) return 0;
        return listaMecanicos.get(idx).getId();
    }

    public String getDescripcionOrden() { return txtDescripcion.getText().trim(); }

    public int getIdSeleccionado() {
        int fila = tablaOrdenes.getSelectedRow();
        if (fila == -1) return 0;
        return (int) modeloOrdenes.getValueAt(fila, 0);
    }

    public void limpiarFormulario() {
        txtDescripcion.setText("");
        limpiarFormularioDetalle();
        tablaOrdenes.clearSelection();
        modeloDetalles.setRowCount(0);
    }

    public void limpiarFormularioDetalle() {
        txtDetDescripcion.setText("");
        txtCosto.setText("");
        txtCantidad.setText("");
        cmbTipo.setSelectedIndex(0);
    }

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

    public JButton getBtnNuevo()          { return btnNuevo; }
    public JButton getBtnGuardar()        { return btnNuevo; }
    public JButton getBtnAgregarDetalle() { return btnAgregarDetalle; }
    public JButton getBtnFinalizar()      { return btnFinalizar; }
    public JButton getBtnVerDetalles()    { return btnVerDetalles; }
    public JTable  getTabla()             { return tablaOrdenes; }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}