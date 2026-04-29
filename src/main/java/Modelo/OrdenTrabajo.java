package Modelo;

import java.util.Objects;

/**
 * @author Omar Mite
 *
 */
public class OrdenTrabajo {

    private int    idOrden;
    private int    idRecepcion;
    private int    idMecanico;
    private String descripcion;
    private String estadoTexto;   // "abierta" | "en_proceso" | "terminada" | "cancelada"
    private String fechaInicio;
    private String fechaFin;

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

    public int    getIdOrden()     { return idOrden; }
    public int    getIdRecepcion() { return idRecepcion; }
    public int    getIdMecanico()  { return idMecanico; }
    public String getDescripcion() { return descripcion; }
    public String getEstadoTexto() { return estadoTexto; }
    public String getFechaInicio() { return fechaInicio; }
    public String getFechaFin()    { return fechaFin; }

    public boolean isEstado() { return !"terminada".equals(estadoTexto) && !"cancelada".equals(estadoTexto); }
    public int getKmEntrada() { return 0; } 
    public void setIdOrden(int idOrden)         { this.idOrden = idOrden; }
    public void setIdRecepcion(int idRecepcion) { this.idRecepcion = idRecepcion; }
    public void setIdMecanico(int idMecanico)   { this.idMecanico = idMecanico; }
    public void setDescripcion(String desc)     { this.descripcion = desc != null ? desc : ""; }
    public void setEstadoTexto(String estado)   { this.estadoTexto = estado; }
    public void setEstado(boolean estado)       { this.estadoTexto = estado ? "abierta" : "terminada"; }
    public void setFechaInicio(String f)        { this.fechaInicio = f; }
    public void setFechaFin(String f)           { this.fechaFin = f; }

    @Override
    public String toString() {
        return "OrdenTrabajo{id=" + idOrden + ", estado='" + estadoTexto + "'}";
    }
}