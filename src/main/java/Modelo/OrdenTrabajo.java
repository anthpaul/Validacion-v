package Modelo;

/**
 * @author Omar Mite
 * Estados: abierta | en_proceso | terminada | cancelada
 */
public class OrdenTrabajo {

    private int    idOrden;
    private int    idRecepcion;
    private int    idMecanico;
    private String descripcion;
    private String estadoTexto;
    private String fechaInicio;
    private String fechaFin;

    // ── Datos enriquecidos desde JOIN — no se persisten ──────────────────────
    private String placa;
    private String nombreCliente;
    private String nombreMecanico;

    public OrdenTrabajo() {}

    public OrdenTrabajo(int idRecepcion, int idMecanico, String descripcion) {
        this.idRecepcion = idRecepcion;
        this.idMecanico  = idMecanico;
        this.descripcion = descripcion != null ? descripcion : "";
        this.estadoTexto = "abierta";
    }

    public OrdenTrabajo(int idOrden, int idRecepcion, int idMecanico,
                        int kmEntrada, String descripcion) {
        this.idOrden     = idOrden;
        this.idRecepcion = idRecepcion;
        this.idMecanico  = idMecanico;
        this.descripcion = descripcion != null ? descripcion : "";
        this.estadoTexto = "abierta";
    }

    // ── Getters ──────────────────────────────────────────────────────────────
    public int    getIdOrden()       { return idOrden; }
    public int    getIdRecepcion()   { return idRecepcion; }
    public int    getIdMecanico()    { return idMecanico; }
    public String getDescripcion()   { return descripcion; }
    public String getEstadoTexto()   { return estadoTexto; }
    public String getFechaInicio()   { return fechaInicio; }
    public String getFechaFin()      { return fechaFin; }
    public String getPlaca()         { return placa != null ? placa : ""; }
    public String getNombreCliente() { return nombreCliente != null ? nombreCliente : ""; }
    public String getNombreMecanico(){ return nombreMecanico != null ? nombreMecanico : ""; }

    // compatibilidad
    public boolean isEstado() {
        return !"terminada".equals(estadoTexto) && !"cancelada".equals(estadoTexto);
    }
    public int getKmEntrada() { return 0; }

    // ── Setters ──────────────────────────────────────────────────────────────
    public void setIdOrden(int v)          { this.idOrden = v; }
    public void setIdRecepcion(int v)      { this.idRecepcion = v; }
    public void setIdMecanico(int v)       { this.idMecanico = v; }
    public void setDescripcion(String v)   { this.descripcion = v != null ? v : ""; }
    public void setEstadoTexto(String v)   { this.estadoTexto = v; }
    public void setEstado(boolean v)       { this.estadoTexto = v ? "abierta" : "terminada"; }
    public void setFechaInicio(String v)   { this.fechaInicio = v; }
    public void setFechaFin(String v)      { this.fechaFin = v; }
    public void setPlaca(String v)         { this.placa = v; }
    public void setNombreCliente(String v) { this.nombreCliente = v; }
    public void setNombreMecanico(String v){ this.nombreMecanico = v; }

    @Override
    public String toString() {
        return "OrdenTrabajo{id=" + idOrden + ", estado='" + estadoTexto + "'}";
    }
}