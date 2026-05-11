package Vista;

import Controlador.UsuarioControlador;
import Modelo.usuario;
import enums.Rol;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * 
 * @author Anthony Delgado 
 * 
 */
public class UsuarioPanel extends JPanel {

    private JTextField    txtId;
    private JTextField    txtNombre;
    private JTextField    txtEmail;
    private JTextField    txtPassword;
    private JComboBox<String> cmbRol;
    private JTextField    txtBusqueda;

    private JButton btnNuevo;
    private JButton btnGuardar;
    private JButton btnDesactivar;
    private JButton btnActivar;      // ← nuevo
    private JButton btnBuscar;

    private JTable            tabla;
    private DefaultTableModel modeloTabla;

    public UsuarioPanel() {
        iniciarComponentes();
        new UsuarioControlador(this);
    }

    private void iniciarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 240, 240));

        JLabel lblTitulo = new JLabel("Gestión de Usuarios");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTitulo, BorderLayout.NORTH);

        // ── Formulario ───────────────────────────────────────────────────────
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Usuario"));
        panelForm.setPreferredSize(new Dimension(300, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.insets  = new Insets(4, 8, 4, 8);
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;

        txtId = new JTextField();
        txtId.setVisible(false);

        int fila = 0;
        txtNombre   = agregarCampo(panelForm, gbc, "Nombre completo *", fila++);
        txtEmail    = agregarCampo(panelForm, gbc, "Email *",           fila++);
        txtPassword = agregarCampo(panelForm, gbc, "Contraseña *",      fila++);

        gbc.gridy = fila * 2; gbc.gridx = 0;
        panelForm.add(new JLabel("Rol *"), gbc);
        cmbRol = new JComboBox<>(new String[]{
            Rol.ADMIN.getValor(),
            Rol.RECEPCIONISTA.getValor(),
            Rol.MECANICO.getValor()
        });
        gbc.gridy = fila * 2 + 1;
        panelForm.add(cmbRol, gbc);
        fila++;

        // Botones en dos filas
        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 5, 5));
        panelBotones.setBackground(Color.WHITE);
        btnNuevo      = crearBoton("Nuevo",       new Color(108, 117, 125));
        btnGuardar    = crearBoton("Guardar",      new Color(40, 167, 69));
        btnActivar    = crearBoton("Activar",      new Color(23, 162, 184));
        btnDesactivar = crearBoton("Desactivar",   new Color(220, 53, 69));
        panelBotones.add(btnNuevo);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActivar);
        panelBotones.add(btnDesactivar);

        gbc.gridy = fila * 2; gbc.insets = new Insets(12, 8, 8, 8);
        panelForm.add(panelBotones, gbc);

        add(panelForm, BorderLayout.WEST);

        // ── Tabla ────────────────────────────────────────────────────────────
        JPanel panelTabla = new JPanel(new BorderLayout(0, 8));
        panelTabla.setBackground(new Color(240, 240, 240));

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.setBackground(new Color(240, 240, 240));
        txtBusqueda = new JTextField(20);
        txtBusqueda.setToolTipText("Buscar por nombre o email");
        btnBuscar = crearBoton("Buscar", new Color(0, 102, 204));
        panelBusqueda.add(new JLabel("Buscar:"));
        panelBusqueda.add(txtBusqueda);
        panelBusqueda.add(btnBuscar);
        panelTabla.add(panelBusqueda, BorderLayout.NORTH);

        String[] columnas = {"ID", "Nombre", "Email", "Rol", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(25);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        // Color por estado activo/inactivo
        tabla.setDefaultRenderer(Object.class, (t, value, isSelected,
                hasFocus, row, col) -> {
            JLabel cell = new JLabel(value != null ? value.toString() : "");
            cell.setOpaque(true);
            boolean activo = (boolean) modeloTabla.getValueAt(row, 4);
            if (isSelected) {
                cell.setBackground(t.getSelectionBackground());
                cell.setForeground(Color.WHITE);
            } else if (!activo) {
                cell.setBackground(new Color(248, 215, 218));
                cell.setForeground(new Color(150, 0, 0));
            } else {
                cell.setBackground(Color.WHITE);
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
    public void cargarTabla(List<usuario> usuarios) {
        modeloTabla.setRowCount(0);
        for (usuario u : usuarios) {
            modeloTabla.addRow(new Object[]{
                u.getIdUsuario(),
                u.getNombre(),
                u.getEmail(),
                u.getRol().getValor(),
                u.isActivo()
            });
        }
    }

    public usuario obtenerDesdeFormulario() {
        usuario u = new usuario();
        String idStr = txtId.getText();
        if (!idStr.isEmpty()) u.setIdUsuario(Integer.parseInt(idStr));
        u.setNombre(txtNombre.getText().trim());
        u.setEmail(txtEmail.getText().trim());
        u.actualizarPasswordHash(txtPassword.getText().trim());
        u.setRol(enums.Rol.fromValor(cmbRol.getSelectedItem().toString()));
        u.setActivo(true);
        return u;
    }

    public void cargarEnFormulario(usuario u) {
        txtId.setText(String.valueOf(u.getIdUsuario()));
        txtNombre.setText(u.getNombre());
        txtEmail.setText(u.getEmail());
        txtPassword.setText("");
        cmbRol.setSelectedItem(u.getRol().getValor());
    }

    public void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        cmbRol.setSelectedIndex(0);
        tabla.clearSelection();
        txtNombre.requestFocus();
    }

    public int getIdSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return 0;
        return (int) modeloTabla.getValueAt(fila, 0);
    }

    public boolean isUsuarioActivoSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return false;
        return (boolean) modeloTabla.getValueAt(fila, 4);
    }

    public usuario getUsuarioSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return null;
        usuario u = new usuario();
        u.setIdUsuario((int)    modeloTabla.getValueAt(fila, 0));
        u.setNombre((String)    modeloTabla.getValueAt(fila, 1));
        u.setEmail((String)     modeloTabla.getValueAt(fila, 2));
        u.setRol(enums.Rol.fromValor((String) modeloTabla.getValueAt(fila, 3)));
        u.setActivo((boolean)   modeloTabla.getValueAt(fila, 4));
        return u;
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

    public JButton getBtnNuevo()      { return btnNuevo; }
    public JButton getBtnGuardar()    { return btnGuardar; }
    public JButton getBtnActivar()    { return btnActivar; }
    public JButton getBtnDesactivar() { return btnDesactivar; }
    public JButton getBtnBuscar()     { return btnBuscar; }
    public JTable  getTabla()         { return tabla; }

    private JTextField agregarCampo(JPanel panel, GridBagConstraints gbc,
                                    String etiqueta, int fila) {
        gbc.gridy = fila * 2;
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