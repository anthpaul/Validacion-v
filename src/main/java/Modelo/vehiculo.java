package Modelo;
 
import java.time.Year;
import java.util.Objects;
/**
 *
 * @author Anthony Delgado
 */
public class vehiculo {
 
    private int    idVehiculo;
    private int    idCliente;
    private String placa;
    private String marca;
    private String modelo;
    private int    anio;
    private String color;  
    private int    kilometraje;
 
    public vehiculo() {}
 
    public vehiculo(int idCliente, String placa, String marca, String modelo, int anio) {
        setIdCliente(idCliente);
        setPlaca(placa);
        setMarca(marca);
        setModelo(modelo);
        setAnio(anio);
        this.kilometraje = 0;
    }
 
    public int    getIdVehiculo()  { return idVehiculo; }
    public int    getIdCliente()   { return idCliente; }
    public String getPlaca()       { return placa; }
    public String getMarca()       { return marca; }
    public String getModelo()      { return modelo; }
    public int    getAnio()        { return anio; }
    public String getColor()       { return color; }
    public int    getKilometraje() { return kilometraje; }
 
    // validaciones
    public void setIdVehiculo(int idVehiculo) {
        if (idVehiculo < 0) throw new IllegalArgumentException("idVehiculo no puede ser negativo");
        this.idVehiculo = idVehiculo;
    }
 
    public void setIdCliente(int idCliente) {
        if (idCliente <= 0) throw new IllegalArgumentException("idCliente debe ser mayor a cero");
        this.idCliente = idCliente;
    }
 
    public void setPlaca(String placa) {
        validarNoVacio(placa, "placa");
        if (!placa.matches("[A-Z]{3}-\\d{3,4}")) {
            throw new IllegalArgumentException("Formato de placa inválido: " + placa +
                    " — se esperaba formato AAA-1234");
        }
        this.placa = placa.toUpperCase().trim();
    }
 
    public void setMarca(String marca) {
        this.marca = validarNoVacio(marca, "marca");
    }
 
    public void setModelo(String modelo) {
        this.modelo = validarNoVacio(modelo, "modelo");
    }
 
    public void setAnio(int anio) {
        int anioActual = Year.now().getValue();
        if (anio < 1900 || anio > anioActual + 1) {
            throw new IllegalArgumentException(
                "Anio invalido: " + anio + " debe estar entre 1900 y " + (anioActual + 1));
        }
        this.anio = anio;
    }
 
    public void setColor(String color) {
        this.color = (color != null && !color.isBlank()) ? color.trim() : null;
    }
 
    public void setKilometraje(int kilometraje) {
        if (kilometraje < 0) throw new IllegalArgumentException("El kilometraje no puede ser negativo");
        if (kilometraje < this.kilometraje) {
            throw new IllegalArgumentException(
                "El kilometraje no puede decrecer. Actual: " + this.kilometraje + ", nuevo: " + kilometraje);
        }
        this.kilometraje = kilometraje;
    }


    public String getDescripcion() {
        return anio + " " + marca + " " + modelo + " [" + placa + "]";
    }
    

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof vehiculo)) return false;
        vehiculo v = (vehiculo) o;
        return Objects.equals(placa, v.placa);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(placa);
    }
 
    @Override
    public String toString() {
        return "Vehiculo{id=" + idVehiculo + ", placa='" + placa +
               "', descripcion='" + getDescripcion() + "', km=" + kilometraje + "}";
    }
 
    private String validarNoVacio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El campo '" + campo + "' no puede ser nulo o vacío");
        }
        return valor.trim();
    }
}
