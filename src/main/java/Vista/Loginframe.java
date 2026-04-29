package Vista;

import Controlador.LoginControlador;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author User
 */
public class Loginframe extends JFrame {
 
  
    private JTextField     txtEmail;
    private JPasswordField txtPassword;
    private JButton        btnIngresar;
    private JLabel         lblError;
 
    public Loginframe() {
        iniciarComponentes();
        new LoginControlador(this);
    }
 
    private void iniciarComponentes() {
        setTitle("Tecnicentro — Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // centrar en pantalla
        setResizable(false);
 
        // Panel principal
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(6, 0, 6, 0);
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JLabel lblTitulo = new JLabel("TECNICENTRO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(lblTitulo, gbc);
 
        JLabel lblSubtitulo = new JLabel("Sistema de Gestión", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(lblSubtitulo, gbc);

        panel.add(new JSeparator(), gbc);
 
        panel.add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField(20);
        panel.add(txtEmail, gbc);
 
        panel.add(new JLabel("Contraseña:"), gbc);
        txtPassword = new JPasswordField(20);
        panel.add(txtPassword, gbc);
 
        lblError = new JLabel(" ", SwingConstants.CENTER);
        lblError.setForeground(Color.RED);
        lblError.setFont(new Font("Arial", Font.PLAIN, 11));
        panel.add(lblError, gbc);
 
        btnIngresar = new JButton("Ingresar");
        btnIngresar.setBackground(new Color(0, 102, 204));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFont(new Font("Arial", Font.BOLD, 13));
        btnIngresar.setPreferredSize(new Dimension(200, 35));
        panel.add(btnIngresar, gbc);
 
        add(panel);
    }
 
    public void mostrarError(String mensaje) {
        lblError.setText(mensaje);
    }
 
    public void limpiarPassword() {
        txtPassword.setText("");
        txtPassword.requestFocus();
    }
 
  
    public JTextField     getTxtEmail()    { return txtEmail; }
    public JPasswordField getTxtPassword() { return txtPassword; }
    public JButton        getBtnIngresar() { return btnIngresar; }
}

