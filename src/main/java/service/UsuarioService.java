package service;

import dao.Usuariodao;
import enums.Rol;
import Modelo.usuario;

import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UsuarioService {

    private final Usuariodao usuarioDAO = new Usuariodao();

    // ── Hash MD5 simple ──────────────────────────────────────────────────────
    private String hashMD5(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(texto.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error al hashear: " + e.getMessage());
        }
    }

    public void registrar(String nombre, String email,
                          String password, Rol rol) throws SQLException {
        if (usuarioDAO.existeEmail(email)) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + email);
        }
        String hash = hashMD5(password);
        usuario usuario = new usuario(nombre, email, hash, rol);
        usuarioDAO.insertar(usuario);
    }

    public usuario login(String email, String password) throws SQLException {
        Optional<usuario> optional = usuarioDAO.buscarPorEmail(email);

        if (optional.isEmpty()) {
            throw new IllegalArgumentException("Credenciales incorrectas");
        }

        usuario usuario = optional.get();

        if (!hashMD5(password).equals(usuario.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciales incorrectas");
        }

        if (!usuario.isActivo()) {
            throw new IllegalArgumentException("Usuario inactivo — contacte al administrador");
        }

        return usuario;
    }

    public usuario buscarPorId(int idUsuario) throws SQLException {
        return usuarioDAO.buscarPorId(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe usuario con id: " + idUsuario));
    }

    public List<usuario> listarTodos() throws SQLException {
        return usuarioDAO.listarTodos();
    }

    public List<usuario> listarPorRol(Rol rol) throws SQLException {
        return usuarioDAO.listarPorRol(rol);
    }

    public void actualizar(usuario usuario) throws SQLException {
        Optional<usuario> existente = usuarioDAO.buscarPorEmail(usuario.getEmail());
        if (existente.isPresent() && existente.get().getIdUsuario() != usuario.getIdUsuario()) {
            throw new IllegalArgumentException("El email ya está en uso por otro usuario");
        }
        usuarioDAO.actualizar(usuario);
    }

    public void desactivar(int idUsuario) throws SQLException {
        usuarioDAO.desactivar(idUsuario);
    }
}