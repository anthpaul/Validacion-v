
package Modelo;
import java.time.LocalDate;

/**
 *
 * @author Vera Sabando Luis Enrique
 */
public class Factura {

    private int idFactura;
    private int idOrden;
    private int idCliente;
    private int idUsuario;
    private String numFactura;
    private LocalDate fechaEmision;
    private double subtotal;
    private double iva;
    private double total;

    private String estado; // GENERADA, PAGADA, ANULADA

    public void calcularTotales(double subtotal) {
        this.subtotal = subtotal;
        this.iva = subtotal * 0.12;
        this.total = subtotal + iva;
    }

    // getters y setters
    public void setIdOrden(int idOrden) { this.idOrden = idOrden; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public void setNumFactura(String numFactura) { this.numFactura = numFactura; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setIdFactura(int idFactura) {this.idFactura = idFactura;}

    public int getIdFactura() {return idFactura;}
    public double getSubtotal() { return subtotal; }
    public double getIva() { return iva; }
    public double getTotal() { return total; }
    public String getEstado() { return estado; }
    public int getIdOrden(){return idOrden;}
    public int getIdCliente(){return idCliente;}
    public int getIdUsuario(){return idUsuario;}
    public String getNumFactura(){return numFactura;}
    public LocalDate getFechaEmision(){return fechaEmision;}
}
