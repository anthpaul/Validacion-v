package Controlador;

import Modelo.usuario;
import Vista.UsuarioPanel;
import service.UsuarioService;
import util.HashUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * 
 * @author Anthony Delgado
 * 
 */
public class UsuarioControlador {

    private final UsuarioService usuarioService = new UsuarioService();
    private final UsuarioPanel   panel;

    public UsuarioControlador(UsuarioPanel panel) {
        this.panel = panel;
        iniciarEventos();
        cargarTabla();
    }

    private void iniciarEventos() {
        panel.getBtnNuevo().addActionListener(e -> panel.limpiarFormulario());
        panel.getBtnGuardar().addActionListener(e -> guardar());
        panel.getBtnActivar().addActionListener(e -> activar());
        panel.getBtnDesactivar().addActionListener(e -> desactivar());
        panel.getBtnBuscar().addActionListener(e -> buscar());

        panel.getTabla().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                usuario u = panel.getUsuarioSeleccionado();
                if (u != null) panel.cargarEnFormulario(u);
            }
        });
    }

    private void cargarTabla() {
        try {
            List<usuario> usuarios = usuarioService.listarTodos();
            panel.cargarTabla(usuarios);
        } catch (SQLException e) {
            panel.mostrarError("Error al cargar usuarios: " + e.getMessage());
        }
    }

    private void guardar() {
        try {
            usuario u = panel.obtenerDesdeFormulario();

            if (u.getNombre().isEmpty() || u.getEmail().isEmpty()) {
                panel.mostrarError("Nombre y email son obligatorios");
                return;
            }

            String password = u.getPasswordHash();
            if (password.isEmpty()) {
                panel.mostrarError("La contraseña es obligatoria");
                return;
            }

            usuarioService.registrar(
                u.getNombre(), u.getEmail(), password, u.getRol());

            panel.mostrarExito("Usuario registrado correctamente");
            panel.limpiarFormulario();
            cargarTabla();

        } catch (IllegalArgumentException e) {
            panel.mostrarError(e.getMessage());
        } catch (SQLException e) {
            panel.mostrarError("Error del sistema: " + e.getMessage());
        }
    }

    // ── Activar usuario ──────────────────────────────────────────────────────
    private void activar() {
        int id = panel.getIdSeleccionado();
        if (id == 0) {
            panel.mostrarError("Seleccione un usuario para activar");
            return;
        }

        if (panel.isUsuarioActivoSeleccionado()) {
            panel.mostrarError("El usuario ya está activo");
            return;
        }

        if (!panel.confirmar("¿Está seguro de activar este usuario?")) return;

        try {
            usuarioService.activar(id);
            panel.mostrarExito("Usuario activado correctamente");
            panel.limpiarFormulario();
            cargarTabla();

        } catch (IllegalArgumentException e) {
            panel.mostrarError(e.getMessage());
        } catch (SQLException e) {
            panel.mostrarError("Error del sistema: " + e.getMessage());
        }
    }

    // ── Desactivar usuario ───────────────────────────────────────────────────
    private void desactivar() {
        int id = panel.getIdSeleccionado();
        if (id == 0) {
            panel.mostrarError("Seleccione un usuario para desactivar");
            return;
        }

        if (!panel.isUsuarioActivoSeleccionado()) {
            panel.mostrarError("El usuario ya está inactivo");
            return;
        }

        if (!panel.confirmar("¿Está seguro de desactivar este usuario?")) return;

        try {
            usuarioService.desactivar(id);
            panel.mostrarExito("Usuario desactivado correctamente");
            panel.limpiarFormulario();
            cargarTabla();

        } catch (IllegalArgumentException e) {
            panel.mostrarError(e.getMessage());
        } catch (SQLException e) {
            panel.mostrarError("Error del sistema: " + e.getMessage());
        }
    }

    private void buscar() {
        try {
            String termino = panel.getTerminoBusqueda();
            List<usuario> todos = usuarioService.listarTodos();

            if (termino.isEmpty()) {
                panel.cargarTabla(todos);
                return;
            }

            List<usuario> filtrados = todos.stream()
                .filter(u -> u.getNombre().toLowerCase()
                              .contains(termino.toLowerCase())
                          || u.getEmail().toLowerCase()
                              .contains(termino.toLowerCase()))
                .collect(java.util.stream.Collectors.toList());

            panel.cargarTabla(filtrados);

        } catch (SQLException e) {
            panel.mostrarError("Error al buscar: " + e.getMessage());
        }
    }
}