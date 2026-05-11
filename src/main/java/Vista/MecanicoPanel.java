package Vista;

import Modelo.Mecanico;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * @author Hamilton
 */
public class MecanicoPanel extends JPanel {

    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtCedula;
    private JTextField txtTelefono;
    private JTextField txtEmail;
    private JTextField txtPassword;
    private JTextField txtBusqueda;
    private JComboBox<String> cbxEspecialidad;

    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnBuscar;
    private JButton btnNuevo;

    private JTable tblMecanicos;
    private DefaultTableModel modeloTabla;

    public MecanicoPanel() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 240, 240));

        JLabel lblTitulo = new JLabel("Gestión de Mecánicos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Mecánico"));
        panelForm.setPreferredSize(new Dimension(300, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.weightx = 1.0;

        int fila = 0;
        txtNombre    = agregarCampo(panelForm, gbc, "Nombre *",      fila++);
        txtApellido  = agregarCampo(panelForm, gbc, "Apellido *",    fila++);
        txtCedula    = agregarCampo(panelForm, gbc, "Cédula *",      fila++);
        txtTelefono  = agregarCampo(panelForm, gbc, "Teléfono *",    fila++);
        txtEmail     = agregarCampo(panelForm, gbc, "Email *",       fila++);
        txtPassword  = agregarCampo(panelForm, gbc, "Contraseña *",  fila++);

        gbc.gridy = fila * 2; gbc.gridx = 0; gbc.gridwidth = 2;
        panelForm.add(new JLabel("Especialidad *"), gbc);
        cbxEspecialidad = new JComboBox<>(new String[]{
            "Mecánica General", "Frenos", "Electricidad", "Suspensión", "Motor"
        });
        gbc.gridy = fila * 2 + 1;
        panelForm.add(cbxEspecialidad, gbc);
        fila++;

        JPanel panelBotones = new JPanel(new GridLayout(1, 3, 5, 0));
        panelBotones.setBackground(Color.WHITE);
        btnNuevo    = crearBoton("Nuevo",    new Color(108, 117, 125));
        btnGuardar  = crearBoton("Guardar",  new Color(40, 167, 69));
        btnEliminar = crearBoton("Eliminar", new Color(220, 53, 69));
        panelBotones.add(btnNuevo);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);

        gbc.gridy = fila * 2; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 8, 8, 8);
        panelForm.add(panelBotones, gbc);
        add(panelForm, BorderLayout.WEST);

        // ── Tabla ────────────────────────────────────────────────────────────
        JPanel panelTabla = new JPanel(new BorderLayout(0, 8));
        panelTabla.setBackground(new Color(240, 240, 240));

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.setBackground(new Color(240, 240, 240));
        txtBusqueda = new JTextField(15);
        btnBuscar = crearBoton("Buscar", new Color(0, 102, 204));
        panelBusqueda.add(new JLabel("Buscar:"));
        panelBusqueda.add(txtBusqueda);
        panelBusqueda.add(btnBuscar);
        panelTabla.add(panelBusqueda, BorderLayout.NORTH);

        String[] columnas = {"ID", "Cédula", "Nombre", "Apellido", "Especialidad", "Teléfono", "Email"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblMecanicos = new JTable(modeloTabla);
        tblMecanicos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblMecanicos.setRowHeight(25);
        tblMecanicos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        panelTabla.add(new JScrollPane(tblMecanicos), BorderLayout.CENTER);
        
        javax.swing.SwingUtilities.invokeLater(() ->{
            tblMecanicos.getColumnModel().getColumn(0).setMinWidth(0);
            tblMecanicos.getColumnModel().getColumn(0).setMaxWidth(0);
            tblMecanicos.getColumnModel().getColumn(0).setWidth(0);
            tblMecanicos.getColumnModel().getColumn(0).setPreferredWidth(0);
        });
        add(panelTabla, BorderLayout.CENTER);
    }

    public void cargarTabla(List<Mecanico> mecanicos) {
        modeloTabla.setRowCount(0);
        for (Mecanico m : mecanicos) {
            modeloTabla.addRow(new Object[]{
                m.getId(),
                m.getCedula(),
                m.getNombre(),
                m.getApellido(),
                m.getEspecialidad(),
                m.getTelefono() != null ? m.getTelefono() : "",
                m.getEmail() != null ? m.getEmail() : ""
            });
        }
    }

    public Mecanico obtenerDesdeFormulario() {
        Mecanico m = new Mecanico();
        m.setNombre(txtNombre.getText().trim());
        m.setApellido(txtApellido.getText().trim());
        m.setCedula(txtCedula.getText().trim());
        m.setTelefono(txtTelefono.getText().trim());
        m.setEmail(txtEmail.getText().trim());
        m.setPasswordHash(txtPassword.getText().trim());
        m.setEspecialidad(cbxEspecialidad.getSelectedItem().toString());
        return m;
    }

    public void cargarEnFormulario(Mecanico m) {
        txtNombre.setText(m.getNombre());
        txtApellido.setText(m.getApellido());
        txtCedula.setText(m.getCedula());
        txtTelefono.setText(m.getTelefono() != null ? m.getTelefono() : "");
        txtEmail.setText(m.getEmail() != null ? m.getEmail() : "");
        txtPassword.setText("");
        cbxEspecialidad.setSelectedItem(m.getEspecialidad());
    }

    public void limpiarFormulario() {
        txtNombre.setText(""); txtApellido.setText("");
        txtCedula.setText(""); txtTelefono.setText("");
        txtEmail.setText(""); txtPassword.setText("");
        if (cbxEspecialidad.getItemCount() > 0) cbxEspecialidad.setSelectedIndex(0);
        tblMecanicos.clearSelection();
        txtNombre.requestFocus();
    }

    public int getIdSeleccionado() {
        int fila = tblMecanicos.getSelectedRow();
        if (fila == -1) return 0;
        return (int) modeloTabla.getValueAt(fila, 0);
    }

    public Mecanico getMecanicoSeleccionado() {
        int fila = tblMecanicos.getSelectedRow();
        if (fila == -1) return null;
        Mecanico m = new Mecanico();
        m.setId((int)         modeloTabla.getValueAt(fila, 0));
        m.setCedula((String)  modeloTabla.getValueAt(fila, 1));
        m.setNombre((String)  modeloTabla.getValueAt(fila, 2));
        m.setApellido((String)modeloTabla.getValueAt(fila, 3));
        m.setEspecialidad((String) modeloTabla.getValueAt(fila, 4));
        m.setTelefono((String)modeloTabla.getValueAt(fila, 5));
        m.setEmail((String)   modeloTabla.getValueAt(fila, 6));
        return m;
    }

    public String getTerminoBusqueda() { return txtBusqueda.getText().trim(); }

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

    public JButton getBtnNuevo()    { return btnNuevo; }
    public JButton getBtnGuardar()  { return btnGuardar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnBuscar()   { return btnBuscar; }
    public JTable  getTabla()       { return tblMecanicos; }

    private JTextField agregarCampo(JPanel panel, GridBagConstraints gbc,
                                    String etiqueta, int fila) {
        gbc.gridy = fila * 2; gbc.gridx = 0; gbc.gridwidth = 2;
        panel.add(new JLabel(etiqueta), gbc);
        JTextField campo = new JTextField();
        gbc.gridy = fila * 2 + 1;
        panel.add(campo, gbc);
        return campo;
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