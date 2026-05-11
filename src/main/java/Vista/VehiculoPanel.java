package Vista;

import Controlador.VehiculoControlador;
import Modelo.cliente;
import Modelo.vehiculo;
 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 *
 * @author Anthony Delgado 
 */
public class VehiculoPanel extends JPanel {

    private JTextField txtId;
    private JComboBox<String> cmbCliente;
    private List<cliente>     listaClientes;
    private JTextField txtPlaca;
    private JTextField txtMarca;
    private JTextField txtModelo;
    private JTextField txtAnio;
    private JTextField txtColor;
    private JTextField txtKilometraje;
    private JTextField txtBusqueda;
 

    private JButton btnNuevo;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnBuscar;
 

    private JTable            tabla;
    private DefaultTableModel modeloTabla;
 
    public VehiculoPanel() {
        iniciarComponentes();
        new VehiculoControlador(this);
    }
 
    private void iniciarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 240, 240));
 
        JLabel lblTitulo = new JLabel("Gestión de Vehículos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Vehículo"));
        panelForm.setPreferredSize(new Dimension(300, 0));
 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.insets  = new Insets(4, 8, 4, 8);
        gbc.weightx = 1.0;
 
        txtId = new JTextField();
        txtId.setVisible(false);
 
        // Cliente combo
        gbc.gridy = 0; gbc.gridx = 0; gbc.gridwidth = 2;
        panelForm.add(new JLabel("Cliente *"), gbc);
        cmbCliente = new JComboBox<>();
        gbc.gridy = 1;
        panelForm.add(cmbCliente, gbc);
 
        int fila = 1;
        txtPlaca       = agregarCampo(panelForm, gbc, "Placa *",        fila++);
        txtMarca       = agregarCampo(panelForm, gbc, "Marca *",        fila++);
        txtModelo      = agregarCampo(panelForm, gbc, "Modelo *",       fila++);
        txtAnio        = agregarCampo(panelForm, gbc, "Año *",          fila++);
        txtColor       = agregarCampo(panelForm, gbc, "Color",          fila++);
        txtKilometraje = agregarCampo(panelForm, gbc, "Kilometraje *",  fila++);
 
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

        JPanel panelTabla = new JPanel(new BorderLayout(0, 8));
        panelTabla.setBackground(new Color(240, 240, 240));
 
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.setBackground(new Color(240, 240, 240));
        txtBusqueda = new JTextField(15);
        txtBusqueda.setToolTipText("Buscar por placa");
        btnBuscar = crearBoton("Buscar", new Color(0, 102, 204));
        panelBusqueda.add(new JLabel("Placa:"));
        panelBusqueda.add(txtBusqueda);
        panelBusqueda.add(btnBuscar);
        panelTabla.add(panelBusqueda, BorderLayout.NORTH);
 
        String[] columnas = {"ID", "Cliente", "Placa", "Marca", "Modelo", "Año", "Color", "Km"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(25);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
 
        panelTabla.add(new JScrollPane(tabla), BorderLayout.CENTER);
        javax.swing.SwingUtilities.invokeLater(() ->{
            tabla.getColumnModel().getColumn(0).setMinWidth(0);
            tabla.getColumnModel().getColumn(0).setMaxWidth(0);
            tabla.getColumnModel().getColumn(0).setWidth(0);
            tabla.getColumnModel().getColumn(0).setPreferredWidth(0);
            tabla.getColumnModel().getColumn(1).setMinWidth(0);
            tabla.getColumnModel().getColumn(1).setMaxWidth(0);
            tabla.getColumnModel().getColumn(1).setWidth(0);
            tabla.getColumnModel().getColumn(1).setPreferredWidth(0);
        });
        
        add(panelTabla, BorderLayout.CENTER);
    }

    public void cargarClientes(List<cliente> clientes) {
        this.listaClientes = clientes;
        cmbCliente.removeAllItems();
        for (cliente c : clientes) {
            cmbCliente.addItem(c.getNombreCompleto() + " — " + c.getCedula());
        }
    }
 
    public void cargarTabla(List<vehiculo> vehiculos) {
        modeloTabla.setRowCount(0);
        for (vehiculo v : vehiculos) {
            modeloTabla.addRow(new Object[]{
                v.getIdVehiculo(),
                v.getIdCliente(), 
                v.getPlaca(),
                v.getMarca(),
                v.getModelo(),
                v.getAnio(),
                v.getColor() != null ? 
                        v.getColor() : "",
                v.getKilometraje()
            });
        }
    }
 
    public vehiculo obtenerDesdeFormulario() {
        vehiculo v = new vehiculo();
        String idStr = txtId.getText();
        if (!idStr.isEmpty()) v.setIdVehiculo(Integer.parseInt(idStr));
 
        // Obtener id del cliente seleccionado en el combo
        int idxCliente = cmbCliente.getSelectedIndex();
        if (idxCliente >= 0 && listaClientes != null) {
            v.setIdCliente(listaClientes.get(idxCliente).getIdCliente());
        }
 
        v.setPlaca(txtPlaca.getText().trim());
        v.setMarca(txtMarca.getText().trim());
        v.setModelo(txtModelo.getText().trim());
        v.setAnio(Integer.parseInt(txtAnio.getText().trim()));
        v.setColor(txtColor.getText().trim());
        v.setKilometraje(Integer.parseInt(txtKilometraje.getText().trim()));
        return v;
    }
 
    public void cargarEnFormulario(vehiculo v) {
        txtId.setText(String.valueOf(v.getIdVehiculo()));
        txtPlaca.setText(v.getPlaca());
        txtMarca.setText(v.getMarca());
        txtModelo.setText(v.getModelo());
        txtAnio.setText(String.valueOf(v.getAnio()));
        txtColor.setText(v.getColor() != null ? v.getColor() : "");
        txtKilometraje.setText(String.valueOf(v.getKilometraje()));

        if (listaClientes != null) {
           for (int i = 0; i < listaClientes.size(); i++) {
               if (listaClientes.get(i).getIdCliente() == v.getIdCliente()) {
                   cmbCliente.setSelectedIndex(i);
                   break;
               }
           }
       }
    }
 
    public void limpiarFormulario() {
        txtId.setText("");
        txtPlaca.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtAnio.setText("");
        txtColor.setText("");
        txtKilometraje.setText("");
        if (cmbCliente.getItemCount() > 0) cmbCliente.setSelectedIndex(0);
        tabla.clearSelection();
        txtPlaca.requestFocus();
    }
 
    public int getIdSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return 0;
        return (int) modeloTabla.getValueAt(fila, 0);
    }
 
    public vehiculo getVehiculoSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return null;
        vehiculo v = new vehiculo();
        v.setIdVehiculo((int)  modeloTabla.getValueAt(fila, 0));
        v.setIdCliente((int)   modeloTabla.getValueAt(fila, 1)); 
        v.setPlaca((String)    modeloTabla.getValueAt(fila, 2));
        v.setMarca((String)    modeloTabla.getValueAt(fila, 3));
        v.setModelo((String)   modeloTabla.getValueAt(fila, 4));
        v.setAnio((int)        modeloTabla.getValueAt(fila, 5));
        v.setColor((String)    modeloTabla.getValueAt(fila, 6));
        v.setKilometraje((int) modeloTabla.getValueAt(fila, 7));
        return v;
    }
 
    public String  getTerminoBusqueda()  { return txtBusqueda.getText().trim(); }
 
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
