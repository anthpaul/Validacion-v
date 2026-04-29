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

    // ── Formulario orden ─────────────────────────────────────────────────────
    private JPanel            panelComboMecanico;
    private JComboBox<String> cmbMecanico;
    private List<Mecanico>    listaMecanicos;
    private JTextField        txtDescripcion;

    // ── Formulario detalle ───────────────────────────────────────────────────
    private JComboBox<String> cmbTipo;
    private JTextField        txtDetDescripcion;
    private JTextField        txtCosto;
    private JTextField        txtCantidad;

    // ── Botones ──────────────────────────────────────────────────────────────
    private JButton btnNuevo;
    private JButton btnAgregarDetalle;
    private JButton btnFinalizar;
    private JButton btnVerDetalles;

    // ── Tabla órdenes ────────────────────────────────────────────────────────
    private JTable            tablaOrdenes;
    private DefaultTableModel modeloOrdenes;

    // ── Tabla detalles ───────────────────────────────────────────────────────
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
        gbcM.insets = new Insets(0, 0, 0, 0);
        gbcM.weightx = 1.0; gbcM.gridwidth = 2;
        gbcM.gridy = 0;
        panelComboMecanico.add(new JLabel("Mecánico *"), gbcM);
        cmbMecanico = new JComboBox<>();
        gbcM.gridy = 1;
        panelComboMecanico.add(cmbMecanico, gbcM);

        gbc.gridy = fila++;
        panelForm.add(panelComboMecanico, gbc);

        // Descripción orden
        gbc.gridy = fila++;
        panelForm.add(new JLabel("Descripción orden"), gbc);
        txtDescripcion = new JTextField();
        gbc.gridy = fila++;
        panelForm.add(txtDescripcion, gbc);

        // Separador
        gbc.gridy = fila++;
        panelForm.add(new JSeparator(), gbc);

        // Tipo
        gbc.gridy = fila++;
        panelForm.add(new JLabel("Tipo *"), gbc);
        cmbTipo = new JComboBox<>(new String[]{"servicio", "repuesto"});
        gbc.gridy = fila++;
        panelForm.add(cmbTipo, gbc);

        // Descripción detalle
        gbc.gridy = fila++;
        panelForm.add(new JLabel("Descripción detalle *"), gbc);
        txtDetDescripcion = new JTextField();
        gbc.gridy = fila++;
        panelForm.add(txtDetDescripcion, gbc);

        // Costo
        gbc.gridy = fila++;
        panelForm.add(new JLabel("Costo unitario *"), gbc);
        txtCosto = new JTextField();
        gbc.gridy = fila++;
        panelForm.add(txtCosto, gbc);

        // Cantidad
        gbc.gridy = fila++;
        panelForm.add(new JLabel("Cantidad *"), gbc);
        txtCantidad = new JTextField();
        gbc.gridy = fila++;
        panelForm.add(txtCantidad, gbc);

        // Botones
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

        // ── Panel derecho — tablas ────────────────────────────────────────────
        JPanel panelDerecho = new JPanel(new GridLayout(2, 1, 0, 10));
        panelDerecho.setBackground(new Color(240, 240, 240));

        // Tabla órdenes
        JPanel panelOrdenes = new JPanel(new BorderLayout());
        panelOrdenes.setBorder(BorderFactory.createTitledBorder("Órdenes de Trabajo"));

        String[] colOrdenes = {"ID", "Recepción", "Mecánico", "Estado", "Fecha Inicio", "Fecha Fin"};
        modeloOrdenes = new DefaultTableModel(colOrdenes, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaOrdenes = new JTable(modeloOrdenes);
        tablaOrdenes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaOrdenes.setRowHeight(25);
        tablaOrdenes.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaOrdenes.getColumnModel().getColumn(0).setMaxWidth(50);
        panelOrdenes.add(new JScrollPane(tablaOrdenes), BorderLayout.CENTER);

        // Tabla detalles
        JPanel panelDetalles = new JPanel(new BorderLayout());
        panelDetalles.setBorder(BorderFactory.createTitledBorder("Detalles de la Orden Seleccionada"));

        String[] colDetalles = {"ID", "Tipo", "Descripción", "Cantidad", "Costo", "Subtotal"};
        modeloDetalles = new DefaultTableModel(colDetalles, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaDetalles = new JTable(modeloDetalles);
        tablaDetalles.setRowHeight(25);
        tablaDetalles.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        panelDetalles.add(new JScrollPane(tablaDetalles), BorderLayout.CENTER);

        panelDerecho.add(panelOrdenes);
        panelDerecho.add(panelDetalles);
        add(panelDerecho, BorderLayout.CENTER);
    }

    // ── Ocultar combo mecánico para el rol mecánico ──────────────────────────
    public void ocultarComboMecanico() {
        panelComboMecanico.setVisible(false);
    }

    // ── Métodos que usa el Controlador ───────────────────────────────────────
    public void cargarMecanicos(List<Mecanico> mecanicos) {
        this.listaMecanicos = mecanicos;
        cmbMecanico.removeAllItems();
        for (Mecanico m : mecanicos) {
            cmbMecanico.addItem(m.getNombre() + " " + m.getApellido() +
                                " — " + m.getCedula());
        }
    }

    public void cargarTabla(List<OrdenTrabajo> lista) {
        modeloOrdenes.setRowCount(0);
        for (OrdenTrabajo ot : lista) {
            modeloOrdenes.addRow(new Object[]{
                ot.getIdOrden(),
                ot.getIdRecepcion(),
                ot.getIdMecanico(),
                ot.getEstadoTexto(),
                ot.getFechaInicio() != null ? ot.getFechaInicio() : "",
                ot.getFechaFin()    != null ? ot.getFechaFin()    : "—"
            });
        }
    }

    public void cargarDetalles(List<DetalleOrden> detalles) {
        modeloDetalles.setRowCount(0);
        for (DetalleOrden d : detalles) {
            modeloDetalles.addRow(new Object[]{
                d.getIdDetalle(), d.getTipo(), d.getDescripcion(),
                d.getCantidad(), d.getCostoUnitario(), d.getSubtotal()
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