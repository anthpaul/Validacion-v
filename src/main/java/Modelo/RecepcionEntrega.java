package Modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Modelo — combina recepcion + entrega
 */
public class RecepcionEntrega {

    private int    idRecepcion;
    private int    idVehiculo;
    private int    idMecanico;
    private int    kmEntrada;
    private int    kmSalida;
    private String placa;
    private String descripcionVehiculo;
    private String motivoIngreso;
    private String checklist;
    private String observacionesRecepcion;
    private String observacionesEntrega;
    private String estado;
    private String fechaRecepcionStr;
    private String fechaEntregaStr;

    // Para manejo interno en memoria (sin BD)
    private LocalDateTime fechaRecepcion;
    private LocalDateTime fechaEntrega;

    public RecepcionEntrega() {
        this.estado = "pendiente";
        this.fechaRecepcion = LocalDateTime.now();
    }

    public RecepcionEntrega(int idVehiculo, String placa, String descripcionVehiculo,
                            String motivoIngreso,String checklist, String observacionesRecepcion) {
        setIdVehiculo(idVehiculo);
        setPlaca(placa);
        setDescripcionVehiculo(descripcionVehiculo);
        setMotivoIngreso(motivoIngreso);
        setObservacionesRecepcion(observacionesRecepcion);
        this.estado = "pendiente";
        this.fechaRecepcion = LocalDateTime.now();
    }

    // ── Getters ──────────────────────────────────────────────────────────────
    public int    getIdRecepcion()           { return idRecepcion; }
    public int    getIdVehiculo()            { return idVehiculo; }
    public int    getIdMecanico()            { return idMecanico; }
    public int    getKmEntrada()             { return kmEntrada; }
    public int    getKmSalida()              { return kmSalida; }
    public String getChecklist() { return checklist != null ? checklist : ""; }
    public String getPlaca()                 { return placa; }
    public String getDescripcionVehiculo()   { return descripcionVehiculo; }
    public String getMotivoIngreso()         { return motivoIngreso; }
    public String getObservacionesRecepcion(){ return observacionesRecepcion; }
    public String getObservacionesEntrega()  { return observacionesEntrega; }
    public String getEstado()               { return estado; }

    public String getFechaRecepcionTexto() {
        if (fechaRecepcionStr != null) return fechaRecepcionStr;
        if (fechaRecepcion != null)
            return fechaRecepcion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        return "";
    }

    public String getFechaEntregaTexto() {
        if (fechaEntregaStr != null) return fechaEntregaStr;
        if (fechaEntrega != null)
            return fechaEntrega.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        return "Pendiente";
    }

    // ── Setters ──────────────────────────────────────────────────────────────
    public void setIdRecepcion(int id) {
        if (id < 0) throw new IllegalArgumentException("idRecepcion no puede ser negativo");
        this.idRecepcion = id;
    }

    public void setIdVehiculo(int id) {
        if (id <= 0) throw new IllegalArgumentException("Debe seleccionar un vehículo válido");
        this.idVehiculo = id;
    }

    public void setIdMecanico(int idMecanico)     { this.idMecanico = idMecanico; }
    public void setKmEntrada(int kmEntrada)        { this.kmEntrada = kmEntrada; }
    public void setKmSalida(int kmSalida)          { this.kmSalida = kmSalida; }
    public void setChecklist(String checklist) { this.checklist = checklist; }
    public void setPlaca(String placa) {
        if (placa == null || placa.isBlank())
            throw new IllegalArgumentException("La placa no puede estar vacía");
        this.placa = placa.trim().toUpperCase();
    }

    public void setDescripcionVehiculo(String desc) {
        if (desc == null || desc.isBlank())
            throw new IllegalArgumentException("La descripción no puede estar vacía");
        this.descripcionVehiculo = desc.trim();
    }

    public void setMotivoIngreso(String motivo) {
        if (motivo == null || motivo.isBlank())
            throw new IllegalArgumentException("El motivo de ingreso es obligatorio");
        this.motivoIngreso = motivo.trim();
    }

    public void setObservacionesRecepcion(String obs) {
        this.observacionesRecepcion = obs != null ? obs.trim() : "";
    }

    public void setObservacionesEntrega(String obs) {
        this.observacionesEntrega = obs != null ? obs.trim() : "";
    }

    public void setEstadoDesdeDB(String estado)         { this.estado = estado; }
    public void setFechaRecepcionTextoDB(String fecha)  { this.fechaRecepcionStr = fecha; }
    public void setFechaEntregaTextoDb(String fecha)    { this.fechaEntregaStr = fecha; }

    public void marcarEntregado(String observaciones) {
        if ("entregado".equals(this.estado))
            throw new IllegalArgumentException("Este vehículo ya fue entregado");
        this.estado = "entregado";
        this.fechaEntrega = LocalDateTime.now();
        setObservacionesEntrega(observaciones);
    }
}