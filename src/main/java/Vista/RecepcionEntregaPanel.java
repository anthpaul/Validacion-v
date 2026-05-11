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

    // ── Formulario recepción ─────────────────────────────────────────────────
    private JComboBox<String> cmbVehiculo;
    private List<vehiculo>    listaVehiculos;
    private JComboBox<String> cmbMecanico;
    private List<Mecanico>    listaMecanicos;
    private JTextField        txtKmEntrada;
    private JTextArea         txtMotivoIngreso;
    private JTextArea         txtObservacionesRecepcion;

    // ── Checklist ────────────────────────────────────────────────────────────
    private JCheckBox chkCarroceria, chkVidrios, chkLlantas, chkLuces;
    private JCheckBox chkDocumentos, chkMotor, chkInterior, chkAceite;

    // ── Formulario entrega ───────────────────────────────────────────────────
    private JTextField txtKmSalida;
    private JTextArea  txtObservacionesEntrega;
    private JLabel     lblEstadoOrden; // muestra si la orden está terminada o no

    // ── Búsqueda ─────────────────────────────────────────────────────────────
    private JTextField txtBusqueda;

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

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;

        int fila = 0;

        // ── SECCIÓN RECEPCIÓN ─────────────────────────────────────────────────
        agregarTitulo(panelForm, gbc, "DATOS DE RECEPCIÓN", fila++, new Color(0, 102, 204));

        gbc.gridy = fila++;
        panelForm.add(new JLabel("Vehículo *"), gbc);
        cmbVehiculo = new JComboBox<>();
        gbc.gridy = fila++;
        panelForm.add(cmbVehiculo, gbc);

        gbc.gridy = fila++;
        panelForm.add(new JLabel("Mecánico asignado *"), gbc);
        cmbMecanico = new JComboBox<>();
        gbc.gridy = fila++;
        panelForm.add(cmbMecanico, gbc);

        gbc.gridy = fila++;
        panelForm.add(new JLabel("Kilometraje entrada *"), gbc);
        txtKmEntrada = new JTextField();
        gbc.gridy = fila++;
        panelForm.add(txtKmEntrada, gbc);

        gbc.gridy = fila++;
        panelForm.add(new JLabel("Motivo de ingreso *"), gbc);
        txtMotivoIngreso = new JTextArea(2, 20);
        txtMotivoIngreso.setLineWrap(true);
        gbc.gridy = fila++;
        panelForm.add(new JScrollPane(txtMotivoIngreso), gbc);

        gbc.gridy = fila++;
        panelForm.add(new JLabel("Observaciones"), gbc);
        txtObservacionesRecepcion = new JTextArea(2, 20);
        txtObservacionesRecepcion.setLineWrap(true);
        gbc.gridy = fila++;
        panelForm.add(new JScrollPane(txtObservacionesRecepcion), gbc);

        // ── CHECKLIST ─────────────────────────────────────────────────────────
        agregarTitulo(panelForm, gbc, "CHECKLIST DE ENTRADA", fila++, new Color(40, 167, 69));

        JPanel panelChecklist = new JPanel(new GridLayout(4, 2, 5, 5));
        panelChecklist.setBackground(Color.WHITE);
        chkCarroceria = new JCheckBox("Carrocería OK");
        chkVidrios    = new JCheckBox("Vidrios OK");
        chkLlantas    = new JCheckBox("Llantas OK");
        chkLuces      = new JCheckBox("Luces OK");
        chkDocumentos = new JCheckBox("Documentos OK");
        chkMotor      = new JCheckBox("Motor OK");
        chkInterior   = new JCheckBox("Interior OK");
        chkAceite     = new JCheckBox("Aceite OK");
        panelChecklist.add(chkCarroceria); panelChecklist.add(chkVidrios);
        panelChecklist.add(chkLlantas);    panelChecklist.add(chkLuces);
        panelChecklist.add(chkDocumentos); panelChecklist.add(chkMotor);
        panelChecklist.add(chkInterior);   panelChecklist.add(chkAceite);
        gbc.gridy = fila++;
        panelForm.add(panelChecklist, gbc);

        // ── SECCIÓN ENTREGA ───────────────────────────────────────────────────
        agregarTitulo(panelForm, gbc, "ENTREGA DEL VEHÍCULO", fila++, new Color(220, 53, 69));

        // Estado de la orden — muestra si puede entregar
        lblEstadoOrden = new JLabel("Seleccione un registro para ver el estado");
        lblEstadoOrden.setFont(new Font("Arial", Font.BOLD, 11));
        lblEstadoOrden.setForeground(new Color(108, 117, 125));
        gbc.gridy = fila++;
        panelForm.add(lblEstadoOrden, gbc);

        gbc.gridy = fila++;
        panelForm.add(new JLabel("Kilometraje salida *"), gbc);
        txtKmSalida = new JTextField();
        gbc.gridy = fila++;
        panelForm.add(txtKmSalida, gbc);

        gbc.gridy = fila++;
        panelForm.add(new JLabel("Observaciones entrega"), gbc);
        txtObservacionesEntrega = new JTextArea(2, 20);
        txtObservacionesEntrega.setLineWrap(true);
        gbc.gridy = fila++;
        panelForm.add(new JScrollPane(txtObservacionesEntrega), gbc);

        // ── BOTONES ───────────────────────────────────────────────────────────
        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 5, 5));
        panelBotones.setBackground(Color.WHITE);
        btnNuevo              = crearBoton("Nuevo",               new Color(108, 117, 125));
        btnRegistrarRecepcion = crearBoton("Registrar Recepción", new Color(40, 167, 69));
        btnEntregar           = crearBoton("Entregar Vehículo",   new Color(0, 102, 204));
        btnBuscar             = crearBoton("Buscar por Placa",    new Color(255, 153, 0));
        btnEntregar.setEnabled(false); // deshabilitado por defecto
        panelBotones.add(btnNuevo);
        panelBotones.add(btnRegistrarRecepcion);
        panelBotones.add(btnEntregar);
        panelBotones.add(btnBuscar);

        gbc.gridy = fila++;
        gbc.insets = new Insets(12, 8, 8, 8);
        panelForm.add(panelBotones, gbc);

        JScrollPane scrollForm = new JScrollPane(panelForm);
        scrollForm.setPreferredSize(new Dimension(320, 0));
        scrollForm.setBorder(BorderFactory.createTitledBorder("Formulario"));
        scrollForm.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollForm, BorderLayout.WEST);

        // ── TABLA ────────────────────────────────────────────────────────────
        JPanel panelTabla = new JPanel(new BorderLayout(0, 8));
        panelTabla.setBackground(new Color(240, 240, 240));

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.setBackground(new Color(240, 240, 240));
        txtBusqueda = new JTextField(15);
        txtBusqueda.setToolTipText("Buscar por placa");
        panelBusqueda.add(new JLabel("Placa:"));
        panelBusqueda.add(txtBusqueda);
        panelTabla.add(panelBusqueda, BorderLayout.NORTH);

        // ID oculto en col 0
        String[] columnas = {"ID", "Cliente", "Placa", "Vehículo",
                             "Motivo", "Estado", "Fecha Entrada", "Fecha Entrega"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(25);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        // Color por estado
        tabla.setDefaultRenderer(Object.class, (t, value, isSelected, hasFocus, row, col) -> {
            JLabel cell = new JLabel(value != null ? value.toString() : "");
            cell.setOpaque(true);
            String estado = (String) modeloTabla.getValueAt(row, 5);
            if (isSelected) {
                cell.setBackground(t.getSelectionBackground());
                cell.setForeground(Color.WHITE);
            } else {
                switch (estado != null ? estado : "") {
                    case "pendiente":   cell.setBackground(new Color(255, 243, 205)); break;
                    case "en_proceso":  cell.setBackground(new Color(209, 236, 241)); break;
                    case "listo":       cell.setBackground(new Color(212, 237, 218)); break;
                    case "entregado":   cell.setBackground(new Color(240, 240, 240)); break;
                    default:            cell.setBackground(Color.WHITE);
                }
                cell.setForeground(Color.BLACK);
            }
            cell.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            return cell;
        });

        panelTabla.add(new JScrollPane(tabla), BorderLayout.CENTER);

        SwingUtilities.invokeLater(() -> {
            tabla.getColumnModel().getColumn(0).setMinWidth(0);
            tabla.getColumnModel().getColumn(0).setMaxWidth(0);
            tabla.getColumnModel().getColumn(0).setWidth(0);
            tabla.getColumnModel().getColumn(0).setPreferredWidth(0);
        });

        add(panelTabla, BorderLayout.CENTER);
    }

    // ── Métodos que usa el Controlador ───────────────────────────────────────

    public void cargarVehiculos(List<vehiculo> vehiculos) {
        this.listaVehiculos = vehiculos;
        cmbVehiculo.removeAllItems();
        for (vehiculo v : vehiculos) cmbVehiculo.addItem(v.getDescripcion());
    }

    public void cargarMecanicos(List<Mecanico> mecanicos) {
        this.listaMecanicos = mecanicos;
        cmbMecanico.removeAllItems();
        for (Mecanico m : mecanicos)
            cmbMecanico.addItem(m.getNombre() + " " + m.getApellido() + " — " + m.getCedula());
    }

    public RecepcionEntrega obtenerRecepcionDesdeFormulario() {
        int idxV = cmbVehiculo.getSelectedIndex();
        if (idxV < 0 || listaVehiculos == null || listaVehiculos.isEmpty())
            throw new IllegalArgumentException("Seleccione un vehículo");

        vehiculo v = listaVehiculos.get(idxV);
        RecepcionEntrega r = new RecepcionEntrega(
            v.getIdVehiculo(), v.getPlaca(), v.getDescripcion(),
            txtMotivoIngreso.getText().trim(),
            txtObservacionesRecepcion.getText().trim(),
            getChecklist()
        );

        int idxM = cmbMecanico.getSelectedIndex();
        if (idxM >= 0 && listaMecanicos != null)
            r.setIdMecanico(listaMecanicos.get(idxM).getId());

        try {
            r.setKmEntrada(Integer.parseInt(txtKmEntrada.getText().trim()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Kilometraje de entrada debe ser un número");
        }
        return r;
    }

    // ── Cargar datos completos del registro seleccionado ─────────────────────
    public void cargarRegistroCompleto(RecepcionEntrega r) {
        txtMotivoIngreso.setText(r.getMotivoIngreso());
        txtObservacionesRecepcion.setText(r.getObservacionesRecepcion());
        txtKmSalida.setText("");
        txtObservacionesEntrega.setText("");
    }

    // ── Habilitar o deshabilitar botón de entrega ─────────────────────────────
    public void habilitarEntrega(boolean puede) {
        btnEntregar.setEnabled(puede);
        if (puede) {
            lblEstadoOrden.setText("✓ Orden terminada — puede entregar el vehículo");
            lblEstadoOrden.setForeground(new Color(40, 167, 69));
        } else {
            lblEstadoOrden.setText("✗ El mecánico aún no ha terminado la orden");
            lblEstadoOrden.setForeground(new Color(220, 53, 69));
        }
    }

    public void cargarTabla(List<RecepcionEntrega> registros) {
        modeloTabla.setRowCount(0);
        for (RecepcionEntrega r : registros) {
            modeloTabla.addRow(new Object[]{
                r.getIdRecepcion(),
                r.getNombreCliente(),
                r.getPlaca(),
                r.getDescripcionVehiculo(),
                r.getMotivoIngreso(),
                r.getEstado(),
                r.getFechaRecepcionTexto(),
                r.getFechaEntregaTexto()
            });
        }
    }

    public void cargarRegistroSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return;
        txtMotivoIngreso.setText(String.valueOf(modeloTabla.getValueAt(fila, 4)));
    }

    public int getIdRecepcionSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return 0;
        return (int) modeloTabla.getValueAt(fila, 0);
    }

    public int getKmSalida() {
        try { return Integer.parseInt(txtKmSalida.getText().trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    public String getObservacionEntrega() { return txtObservacionesEntrega.getText().trim(); }
    public String getTerminoBusqueda()    { return txtBusqueda.getText().trim(); }

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

    public void limpiarFormulario() {
        if (cmbVehiculo.getItemCount() > 0) cmbVehiculo.setSelectedIndex(0);
        if (cmbMecanico.getItemCount() > 0) cmbMecanico.setSelectedIndex(0);
        txtKmEntrada.setText("");
        txtMotivoIngreso.setText("");
        txtObservacionesRecepcion.setText("");
        txtObservacionesEntrega.setText("");
        txtKmSalida.setText("");
        txtBusqueda.setText("");
        chkCarroceria.setSelected(false); chkVidrios.setSelected(false);
        chkLlantas.setSelected(false);    chkLuces.setSelected(false);
        chkDocumentos.setSelected(false); chkMotor.setSelected(false);
        chkInterior.setSelected(false);   chkAceite.setSelected(false);
        tabla.clearSelection();
        btnEntregar.setEnabled(false);
        lblEstadoOrden.setText("Seleccione un registro para ver el estado");
        lblEstadoOrden.setForeground(new Color(108, 117, 125));
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