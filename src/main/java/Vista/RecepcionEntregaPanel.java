package Vista;

import Controlador.RecepcionEntregaControlador;
import Modelo.Mecanico;
import Modelo.RecepcionEntrega;
import Modelo.vehiculo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RecepcionEntregaPanel extends JPanel {

    // ── Formulario ───────────────────────────────────────────────────────────
    private JComboBox<String> cmbVehiculo;
    private List<vehiculo>    listaVehiculos;
    private JComboBox<String> cmbMecanico;
    private List<Mecanico>    listaMecanicos;

    private JTextField txtKmEntrada;
    private JTextField txtKmSalida;
    private JTextField txtBusqueda;

    private JTextArea txtMotivoIngreso;
    private JTextArea txtObservacionesRecepcion;
    private JTextArea txtObservacionesEntrega;

    // ── Checklist ────────────────────────────────────────────────────────────
    private JCheckBox chkCarroceria;
    private JCheckBox chkVidrios;
    private JCheckBox chkLlantas;
    private JCheckBox chkLuces;
    private JCheckBox chkDocumentos;
    private JCheckBox chkMotor;
    private JCheckBox chkInterior;
    private JCheckBox chkAceite;

    // ── Botones ──────────────────────────────────────────────────────────────
    private JButton btnNuevo;
    private JButton btnRegistrarRecepcion;
    private JButton btnEntregar;
    private JButton btnBuscar;

    // ── Tabla ────────────────────────────────────────────────────────────────
    private JTable            tabla;
    private DefaultTableModel modeloTabla;

    public RecepcionEntregaPanel() {
        iniciarComponentes();
        new RecepcionEntregaControlador(this);
    }

    private void iniciarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 240, 240));

        JLabel lblTitulo = new JLabel("Recepción / Entrega de Vehículos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTitulo, BorderLayout.NORTH);

        // ── Panel izquierdo con scroll ────────────────────────────────────────
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;

        int fila = 0;

        // ── Sección Recepción ─────────────────────────────────────────────────
        agregarTitulo(panelForm, gbc, "DATOS DE RECEPCIÓN", fila++,
                      new Color(0, 102, 204));

        // Vehículo
        gbc.gridy = fila++;
        panelForm.add(new JLabel("Vehículo *"), gbc);
        cmbVehiculo = new JComboBox<>();
        gbc.gridy = fila++;
        panelForm.add(cmbVehiculo, gbc);

        // Mecánico
        gbc.gridy = fila++;
        panelForm.add(new JLabel("Mecánico asignado *"), gbc);
        cmbMecanico = new JComboBox<>();
        gbc.gridy = fila++;
        panelForm.add(cmbMecanico, gbc);

        // KM entrada
        gbc.gridy = fila++;
        panelForm.add(new JLabel("Kilometraje entrada *"), gbc);
        txtKmEntrada = new JTextField();
        gbc.gridy = fila++;
        panelForm.add(txtKmEntrada, gbc);

        // Motivo
        gbc.gridy = fila++;
        panelForm.add(new JLabel("Motivo de ingreso *"), gbc);
        txtMotivoIngreso = new JTextArea(2, 20);
        txtMotivoIngreso.setLineWrap(true);
        gbc.gridy = fila++;
        panelForm.add(new JScrollPane(txtMotivoIngreso), gbc);

        // Observaciones recepción
        gbc.gridy = fila++;
        panelForm.add(new JLabel("Observaciones"), gbc);
        txtObservacionesRecepcion = new JTextArea(2, 20);
        txtObservacionesRecepcion.setLineWrap(true);
        gbc.gridy = fila++;
        panelForm.add(new JScrollPane(txtObservacionesRecepcion), gbc);

        // ── Checklist ─────────────────────────────────────────────────────────
        agregarTitulo(panelForm, gbc, "CHECKLIST DE ENTRADA", fila++,
                      new Color(40, 167, 69));

        JPanel panelChecklist = new JPanel(new GridLayout(4, 2, 5, 5));
        panelChecklist.setBackground(Color.WHITE);
        chkCarroceria  = new JCheckBox("Carrocería OK");
        chkVidrios     = new JCheckBox("Vidrios OK");
        chkLlantas     = new JCheckBox("Llantas OK");
        chkLuces       = new JCheckBox("Luces OK");
        chkDocumentos  = new JCheckBox("Documentos OK");
        chkMotor       = new JCheckBox("Motor OK");
        chkInterior    = new JCheckBox("Interior OK");
        chkAceite      = new JCheckBox("Aceite OK");
        panelChecklist.add(chkCarroceria);
        panelChecklist.add(chkVidrios);
        panelChecklist.add(chkLlantas);
        panelChecklist.add(chkLuces);
        panelChecklist.add(chkDocumentos);
        panelChecklist.add(chkMotor);
        panelChecklist.add(chkInterior);
        panelChecklist.add(chkAceite);

        gbc.gridy = fila++;
        panelForm.add(panelChecklist, gbc);

        // ── Sección Entrega ───────────────────────────────────────────────────
        agregarTitulo(panelForm, gbc, "DATOS DE ENTREGA", fila++,
                      new Color(220, 53, 69));

        // KM salida
        gbc.gridy = fila++;
        panelForm.add(new JLabel("Kilometraje salida"), gbc);
        txtKmSalida = new JTextField();
        gbc.gridy = fila++;
        panelForm.add(txtKmSalida, gbc);

        // Observaciones entrega
        gbc.gridy = fila++;
        panelForm.add(new JLabel("Observaciones entrega"), gbc);
        txtObservacionesEntrega = new JTextArea(2, 20);
        txtObservacionesEntrega.setLineWrap(true);
        gbc.gridy = fila++;
        panelForm.add(new JScrollPane(txtObservacionesEntrega), gbc);

        // Botones
        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 5, 5));
        panelBotones.setBackground(Color.WHITE);
        btnNuevo              = crearBoton("Nuevo",               new Color(108, 117, 125));
        btnRegistrarRecepcion = crearBoton("Registrar Recepción", new Color(40, 167, 69));
        btnEntregar           = crearBoton("Entregar Vehículo",   new Color(0, 102, 204));
        btnBuscar             = crearBoton("Buscar por Placa",    new Color(255, 153, 0));
        panelBotones.add(btnNuevo);
        panelBotones.add(btnRegistrarRecepcion);
        panelBotones.add(btnEntregar);
        panelBotones.add(btnBuscar);

        gbc.gridy = fila++;
        gbc.insets = new Insets(12, 8, 8, 8);
        panelForm.add(panelBotones, gbc);

        // Scroll para el formulario
        JScrollPane scrollForm = new JScrollPane(panelForm);
        scrollForm.setPreferredSize(new Dimension(320, 0));
        scrollForm.setBorder(BorderFactory.createTitledBorder("Formulario"));
        scrollForm.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollForm, BorderLayout.WEST);

        // ── Tabla ────────────────────────────────────────────────────────────
        JPanel panelTabla = new JPanel(new BorderLayout(0, 8));
        panelTabla.setBackground(new Color(240, 240, 240));

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.setBackground(new Color(240, 240, 240));
        txtBusqueda = new JTextField(15);
        txtBusqueda.setToolTipText("Buscar por placa");
        panelBusqueda.add(new JLabel("Placa:"));
        panelBusqueda.add(txtBusqueda);
        panelTabla.add(panelBusqueda, BorderLayout.NORTH);

        String[] columnas = {"ID", "Placa", "Vehículo", "Motivo",
                             "Estado", "Fecha Entrada", "Fecha Entrega"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(25);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);

        panelTabla.add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelTabla, BorderLayout.CENTER);
    }

    // ── Construir texto del checklist ────────────────────────────────────────
    public String getChecklist() {
        StringBuilder sb = new StringBuilder();
        if (chkCarroceria.isSelected()) sb.append("Carrocería OK | ");
        if (chkVidrios.isSelected())    sb.append("Vidrios OK | ");
        if (chkLlantas.isSelected())    sb.append("Llantas OK | ");
        if (chkLuces.isSelected())      sb.append("Luces OK | ");
        if (chkDocumentos.isSelected()) sb.append("Documentos OK | ");
        if (chkMotor.isSelected())      sb.append("Motor OK | ");
        if (chkInterior.isSelected())   sb.append("Interior OK | ");
        if (chkAceite.isSelected())     sb.append("Aceite OK | ");
        return sb.toString();
    }

    // ── Métodos que usa el Controlador ───────────────────────────────────────
    public void cargarVehiculos(List<vehiculo> vehiculos) {
        this.listaVehiculos = vehiculos;
        cmbVehiculo.removeAllItems();
        for (vehiculo v : vehiculos) {
            cmbVehiculo.addItem(v.getDescripcion());
        }
    }

    public void cargarMecanicos(List<Mecanico> mecanicos) {
        this.listaMecanicos = mecanicos;
        cmbMecanico.removeAllItems();
        for (Mecanico m : mecanicos) {
            cmbMecanico.addItem(m.getNombre() + " " + m.getApellido() +
                                " — " + m.getCedula());
        }
    }

    public RecepcionEntrega obtenerRecepcionDesdeFormulario() {
        int idxV = cmbVehiculo.getSelectedIndex();
        if (idxV < 0 || listaVehiculos == null || listaVehiculos.isEmpty()) {
            throw new IllegalArgumentException("Seleccione un vehículo");
        }

        vehiculo v = listaVehiculos.get(idxV);
        RecepcionEntrega r = new RecepcionEntrega(
            v.getIdVehiculo(),
            v.getPlaca(),
            v.getDescripcion(),
            txtMotivoIngreso.getText().trim(),
            txtObservacionesRecepcion.getText().trim(),
            getChecklist()                   
        );

        // Mecánico
        int idxM = cmbMecanico.getSelectedIndex();
        if (idxM >= 0 && listaMecanicos != null) {
            r.setIdMecanico(listaMecanicos.get(idxM).getId());
        }

        // KM entrada
        try {
            r.setKmEntrada(Integer.parseInt(txtKmEntrada.getText().trim()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Kilometraje de entrada debe ser un número");
        }

        // Checklist
        r.setChecklist(getChecklist());

        return r;
    }

    public int getIdRecepcionSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return 0;
        return (int) modeloTabla.getValueAt(fila, 0);
    }

    public int getKmSalida() {
        try {
            return Integer.parseInt(txtKmSalida.getText().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public String getObservacionEntrega() {
        return txtObservacionesEntrega.getText().trim();
    }

    public String getTerminoBusqueda() { return txtBusqueda.getText().trim(); }

    public void cargarTabla(List<RecepcionEntrega> registros) {
        modeloTabla.setRowCount(0);
        for (RecepcionEntrega r : registros) {
            modeloTabla.addRow(new Object[]{
                r.getIdRecepcion(), r.getPlaca(),
                r.getDescripcionVehiculo(), r.getMotivoIngreso(),
                r.getEstado(), r.getFechaRecepcionTexto(),
                r.getFechaEntregaTexto()
            });
        }
    }

    public void cargarRegistroSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return;
        txtMotivoIngreso.setText(
            String.valueOf(modeloTabla.getValueAt(fila, 3)));
    }

    public void limpiarFormulario() {
        if (cmbVehiculo.getItemCount() > 0)  cmbVehiculo.setSelectedIndex(0);
        if (cmbMecanico.getItemCount() > 0)  cmbMecanico.setSelectedIndex(0);
        txtKmEntrada.setText("");
        txtMotivoIngreso.setText("");
        txtObservacionesRecepcion.setText("");
        txtObservacionesEntrega.setText("");
        txtKmSalida.setText("");
        txtBusqueda.setText("");
        // Limpiar checklist
        chkCarroceria.setSelected(false);
        chkVidrios.setSelected(false);
        chkLlantas.setSelected(false);
        chkLuces.setSelected(false);
        chkDocumentos.setSelected(false);
        chkMotor.setSelected(false);
        chkInterior.setSelected(false);
        chkAceite.setSelected(false);
        tabla.clearSelection();
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

    public JButton getBtnNuevo()              { return btnNuevo; }
    public JButton getBtnRegistrarRecepcion() { return btnRegistrarRecepcion; }
    public JButton getBtnEntregar()           { return btnEntregar; }
    public JButton getBtnBuscar()             { return btnBuscar; }
    public JTable  getTabla()                 { return tabla; }

    private void agregarTitulo(JPanel panel, GridBagConstraints gbc,
                               String texto, int fila, Color color) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        lbl.setForeground(color);
        lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, color));
        gbc.gridy = fila;
        gbc.insets = new Insets(10, 8, 4, 8);
        panel.add(lbl, gbc);
        gbc.insets = new Insets(4, 8, 4, 8);
    }

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