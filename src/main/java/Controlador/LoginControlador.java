package Controlador;

import Modelo.usuario;
import service.UsuarioService;
import Vista.Loginframe;
import Vista.InicioFramer;

import java.sql.SQLException;        
/**
 *
 * @author User
 */
public class LoginControlador {
 
    private final UsuarioService usuarioService = new UsuarioService();
    private final Loginframe     loginFrame;
 
    public LoginControlador(Loginframe loginFrame) {
        this.loginFrame = loginFrame;
        iniciarEventos();
    }
 
    private void iniciarEventos() {
        // Evento del botón Ingresar
        loginFrame.getBtnIngresar().addActionListener(e -> login());
 
        // Evento Enter en el campo contraseña
        loginFrame.getTxtPassword().addActionListener(e -> login());
    }
 
    private void login() {
        String email    = loginFrame.getTxtEmail().getText().trim();
        String password = new String(loginFrame.getTxtPassword().getPassword());
        System.out.println("Hash generado: " + util.HashUtil.md5(password));
        // Validación básica
        if (email.isEmpty() || password.isEmpty()) {
            loginFrame.mostrarError("Email y contraseña son obligatorios");
            return;
        }
 
        try {
            usuario usuario = usuarioService.login(email, password);
 
            // Login exitoso — abrir ventana principal
            loginFrame.dispose();
            InicioFramer inicioFrame = new InicioFramer(usuario);
            inicioFrame.setVisible(true);
 
        } catch (IllegalArgumentException e) {
            loginFrame.mostrarError(e.getMessage());
            loginFrame.limpiarPassword();
 
        } catch (SQLException e) {
            loginFrame.mostrarError("Error del sistema: " + e.getMessage());
        }
    }
}
