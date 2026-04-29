package Controlador;

import Modelo.Mecanico;
import Vista.MecanicoPanel;
import service.MecanicoService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Hamilton
 */
public class MecanicoControlador implements ActionListener {

    private final MecanicoPanel   vista;
    private final MecanicoService servicio;

    public MecanicoControlador(MecanicoPanel vista, MecanicoService servicio) {
        this.vista    = vista;
        this.servicio = servicio;

        vista.getBtnGuardar().addActionListener(this);
        vista.getBtnEliminar().addActionListener(this);
        vista.getBtnNuevo().addActionListener(this);
        vista.getBtnBuscar().addActionListener(this);

        // Cargar en formulario al seleccionar fila
        vista.getTabla().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Mecanico m = vista.getMecanicoSeleccionado();
                if (m != null) vista.cargarEnFormulario(m);
            }
        });

        cargarTabla();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == vista.getBtnGuardar()) {
                registrarMecanico();
            } else if (e.getSource() == vista.getBtnEliminar()) {
                eliminarMecanico();
            } else if (e.getSource() == vista.getBtnNuevo()) {
                vista.limpiarFormulario();
            } else if (e.getSource() == vista.getBtnBuscar()) {
                buscarMecanico();
            }
        } catch (SQLException ex) {
            vista.mostrarError("Error de Base de Datos: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            vista.mostrarError("Validación: " + ex.getMessage());
        } catch (Exception ex) {
            vista.mostrarError("Error inesperado: " + ex.getMessage());
        }
    }

    private void registrarMecanico() throws SQLException {
        Mecanico m = vista.obtenerDesdeFormulario();

        if (m.getNombre().isEmpty() || m.getCedula().isEmpty() ||
            m.getEmail().isEmpty() || m.getPasswordHash().isEmpty()) {
            throw new IllegalArgumentException("Por favor, llene todos los campos obligatorios.");
        }

        servicio.registrar(m);
        vista.mostrarExito("Mecánico registrado exitosamente.");
        cargarTabla();
        vista.limpiarFormulario();
    }

    private void eliminarMecanico() throws SQLException {
        int id = vista.getIdSeleccionado();
        if (id == 0) {
            throw new IllegalArgumentException("Seleccione un mecánico de la tabla para eliminar.");
        }
        if (vista.confirmar("¿Está seguro de dar de baja este mecánico?")) {
            servicio.eliminar(id);
            vista.mostrarExito("Mecánico dado de baja correctamente.");
            cargarTabla();
            vista.limpiarFormulario();
        }
    }

    private void cargarTabla() {
        try {
            List<Mecanico> lista = servicio.listarTodos();
            vista.cargarTabla(lista);
        } catch (SQLException ex) {
            vista.mostrarError("No se pudieron cargar los datos: " + ex.getMessage());
        }
    }

    private void buscarMecanico() throws SQLException {
        String termino = vista.getTerminoBusqueda();
        List<Mecanico> lista = servicio.buscar(termino);
        vista.cargarTabla(lista);
    }
}