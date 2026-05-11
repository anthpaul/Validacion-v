package Vista;

import Controlador.ClienteControlador;
import Modelo.cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
/**
 *
 * @author User
 */
public class ClientePanel extends JPanel {

    private JTextField txtId;
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtCedula;
    private JTextField txtTelefono;
    private JTextField txtEmail;
    private JTextField txtDireccion;
    private JTextField txtBusqueda;
 
    private JButton btnNuevo;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnBuscar;

    private JTable            tabla;
    private DefaultTableModel modeloTabla;
 
    public ClientePanel() {
        iniciarComponentes();
        new ClienteControlador(this);
    }
 
    private void iniciarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 240, 240));
        txtId = new JTextField();
        txtId.setVisible(false);

        JLabel lblTitulo = new JLabel("Gestión de Clientes");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTitulo, BorderLayout.NORTH);
 

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));
        panelForm.setPreferredSize(new Dimension(300, 0));
 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.insets  = new Insets(4, 8, 4, 8);
        gbc.weightx = 1.0;
 
        
        int fila = 0;
        txtNombres   = agregarCampo(panelForm, gbc, "Nombres *",    fila++);
        txtApellidos = agregarCampo(panelForm, gbc, "Apellidos *",  fila++);
        txtCedula    = agregarCampo(panelForm, gbc, "Cédula *",     fila++);
        txtTelefono  = agregarCampo(panelForm, gbc, "Teléfono *",   fila++);
        txtEmail     = agregarCampo(panelForm, gbc, "Email",        fila++);
        txtDireccion = agregarCampo(panelForm, gbc, "Dirección",    fila++);
 
        // Botones
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
 
        // ── Panel derecho — Tabla ─────────────────────────────────────────────
        JPanel panelTabla = new JPanel(new BorderLayout(0, 8));
        panelTabla.setBackground(new Color(240, 240, 240));
 
        // Barra de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.setBackground(new Color(240, 240, 240));
        txtBusqueda = new JTextField(20);
        txtBusqueda.setToolTipText("Buscar por nombre o cédula");
        btnBuscar   = crearBoton("Buscar", new Color(0, 102, 204));
        panelBusqueda.add(new JLabel("Buscar:"));
        panelBusqueda.add(txtBusqueda);
        panelBusqueda.add(btnBuscar);
        panelTabla.add(panelBusqueda, BorderLayout.NORTH);

 
        // Tabla
        String[] columnas = {"ID","Nombres", "Apellidos", "Cédula", "Teléfono", "Email", "Dirección"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(25);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        

        panelTabla.add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelTabla, BorderLayout.CENTER);
        
        javax.swing.SwingUtilities.invokeLater(() -> {
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(0);
    });

    add(panelTabla, BorderLayout.CENTER);
    }
 
    public void cargarTabla(List<cliente> clientes) {
        modeloTabla.setRowCount(0);
        for (cliente c : clientes) {
            modeloTabla.addRow(new Object[]{
                c.getIdCliente(),
                c.getNombres(),
                c.getApellidos(),
                c.getCedula(),
                c.getTelefono(),
                c.getEmail() != null ? c.getEmail() : "",
                c.getDireccion()
            });
        }
    }
 
    public cliente obtenerDesdeFormulario() {
        cliente c = new cliente();
        String idStr = txtId.getText();
        if (!idStr.isEmpty()) c.setIdCliente(Integer.parseInt(idStr));
        c.setNombres(txtNombres.getText().trim());
        c.setApellidos(txtApellidos.getText().trim());
        c.setCedula(txtCedula.getText().trim());
        c.setTelefono(txtTelefono.getText().trim());
        c.setEmail(txtEmail.getText().trim());
        c.setDireccion(txtDireccion.getText().trim());
        return c;
    }
 
    public void cargarEnFormulario(cliente c) {
        txtId.setText(String.valueOf(c.getIdCliente())); 
        txtNombres.setText(c.getNombres());
        txtApellidos.setText(c.getApellidos());
        txtCedula.setText(c.getCedula());
        txtTelefono.setText(c.getTelefono());
        txtEmail.setText(c.getEmail() != null ? c.getEmail() : "");
        txtDireccion.setText(c.getDireccion() != null ? c.getDireccion() : "");
    }
 
    public void limpiarFormulario() {
        txtNombres.setText("");
        txtApellidos.setText("");
        txtCedula.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtDireccion.setText("");
        tabla.clearSelection();
        txtNombres.requestFocus();
    }
 
    public int getIdSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return 0;
        return (int) modeloTabla.getValueAt(fila, 0);
    }
 
    public cliente getClienteSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return null;
        cliente c = new cliente();
        c.setIdCliente((int)    modeloTabla.getValueAt(fila, 0));
        c.setNombres((String)   modeloTabla.getValueAt(fila, 1));
        c.setApellidos((String) modeloTabla.getValueAt(fila, 2));
        c.setCedula((String)    modeloTabla.getValueAt(fila, 3));
        c.setTelefono((String)  modeloTabla.getValueAt(fila, 4));
        c.setEmail((String)     modeloTabla.getValueAt(fila, 5));
        c.setDireccion((String) modeloTabla.getValueAt(fila, 6));
        return c;
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
    public JTable  getTabla()       { return tabla; }

    private JTextField agregarCampo(JPanel panel, GridBagConstraints gbc,
                                    String etiqueta, int fila) {
        gbc.gridy    = fila * 2;
        gbc.gridx    = 0;
        gbc.gridwidth = 2;
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
