package service;

import dao.Mecanicodao;
import Modelo.Mecanico;
import util.HashUtil;

import java.sql.SQLException;
import java.util.List;

public class MecanicoService {

    private final Mecanicodao mecanicoDAO = new Mecanicodao();

    public void registrar(Mecanico mecanico) throws SQLException {
        if (mecanico.getCedula() == null || !mecanico.getCedula().matches("\\d{10}")) {
            throw new IllegalArgumentException("La cédula debe tener exactamente 10 dígitos");
        }

        if (mecanicoDAO.existeCedula(mecanico.getCedula())) {
            throw new IllegalArgumentException(
                "Ya existe un mecánico con la cédula: " + mecanico.getCedula());
        }

        // Hashear la contraseña antes de guardar
        String hash = HashUtil.md5(mecanico.getPasswordHash());
        mecanico.setPasswordHash(hash);

        mecanicoDAO.insertar(mecanico);
    }

    public Mecanico buscarPorId(int idMecanico) throws SQLException {
        return mecanicoDAO.buscarPorId(idMecanico)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe mecánico con id: " + idMecanico));
    }

    public List<Mecanico> listarTodos() throws SQLException {
        return mecanicoDAO.listarTodos();
    }

    public List<Mecanico> buscar(String termino) throws SQLException {
        if (termino == null || termino.isBlank()) {
            return mecanicoDAO.listarTodos();
        }
        return mecanicoDAO.buscar(termino.trim());
    }

    public void actualizar(Mecanico mecanico) throws SQLException {
        if (mecanico.getCedula() == null || !mecanico.getCedula().matches("\\d{10}")) {
            throw new IllegalArgumentException("La cédula debe tener exactamente 10 dígitos");
        }
        mecanicoDAO.actualizar(mecanico);
    }

    public void eliminar(int idMecanico) throws SQLException {
        boolean eliminado = mecanicoDAO.eliminar(idMecanico);
        if (!eliminado) {
            throw new IllegalArgumentException("No se encontró el mecánico a eliminar");
        }
    }
}