package Modelo;

/**
 *
 * @author Omar Mite
 */
public class DetalleOrden {
private int idDetalle;
    private int idOrden;
    private String tipo; // Servicio o Repuesto
    private String descripcion;
    private double costoUnitario;
    private int cantidad;

    public DetalleOrden() {}

    public DetalleOrden(int idOrden, String tipo, String descripcion, double costoUnitario, int cantidad) {
        this.idOrden = idOrden;
        this.tipo = validarNoVacio(tipo, "tipo");
        this.descripcion = validarNoVacio(descripcion, "descripcion");
        setCostoUnitario(costoUnitario);
        setCantidad(cantidad);
    }

    // Getters
    public int getIdDetalle() { return idDetalle; }
    public int getIdOrden() { return idOrden; }
    public String getTipo() { return tipo; }
    public String getDescripcion() { return descripcion; }
    public double getCostoUnitario() { return costoUnitario; }
    public int getCantidad() { return cantidad; }
    
    // Cálculo automático
    public double getSubtotal() {
        return this.cantidad * this.costoUnitario;
    }

    // Setters
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }
    
    public void setCostoUnitario(double costo) {
        if (costo < 0) throw new IllegalArgumentException("El costo no puede ser negativo");
        this.costoUnitario = costo;
    }

    public void setCantidad(int cantidad) {
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        this.cantidad = cantidad;
    }

    private String validarNoVacio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El campo '" + campo + "' no puede ser nulo o vacío");
        }
        return valor.trim();
    }
}