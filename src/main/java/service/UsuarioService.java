package service;

import dao.Usuariodao;
import enums.Rol;
import Modelo.usuario;
import util.HashUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UsuarioService {

    private final Usuariodao usuarioDAO = new Usuariodao();

    private String hashMD5(String texto) {
        return HashUtil.md5(texto);
    }

    public void registrar(String nombre, String email,
                          String password, Rol rol) throws SQLException {
        if (usuarioDAO.existeEmail(email)) {
            throw new IllegalArgumentException(
                "Ya existe un usuario con el email: " + email);
        }
        String hash = hashMD5(password);
        usuario u = new usuario(nombre, email, hash, rol);
        usuarioDAO.insertar(u);
    }

    public usuario login(String email, String password) throws SQLException {
        Optional<usuario> optional = usuarioDAO.buscarPorEmail(email);

        if (optional.isEmpty()) {
            throw new IllegalArgumentException("Credenciales incorrectas");
        }

        usuario u = optional.get();

        if (!hashMD5(password).equals(u.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciales incorrectas");
        }

        if (!u.isActivo()) {
            throw new IllegalArgumentException(
                "Usuario inactivo — contacte al administrador");
        }

        return u;
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
        if (existente.isPresent() &&
            existente.get().getIdUsuario() != usuario.getIdUsuario()) {
            throw new IllegalArgumentException(
                "El email ya está en uso por otro usuario");
        }
        usuarioDAO.actualizar(usuario);
    }

    public void desactivar(int idUsuario) throws SQLException {
        usuario u = buscarPorId(idUsuario);
        if (!u.isActivo()) {
            throw new IllegalArgumentException("El usuario ya está inactivo");
        }
        usuarioDAO.desactivar(idUsuario);
    }

    // ── NUEVO — activar ──────────────────────────────────────────────────────
    public void activar(int idUsuario) throws SQLException {
        usuario u = buscarPorId(idUsuario);
        if (u.isActivo()) {
            throw new IllegalArgumentException("El usuario ya está activo");
        }
        usuarioDAO.activar(idUsuario);
    }
}