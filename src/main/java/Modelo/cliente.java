package Modelo;
import java.util.Objects;
/**
 *
 * @author Anthony Delgado 
 */
public class cliente {
 
    private int    idCliente;
    private String nombres;
    private String apellidos;
    private String cedula;
    private String telefono;
    private String email;      
    private String direccion;  
    private String createdAt;
 
    public cliente() {}

    public cliente(String nombres, String apellidos, String cedula, String telefono) {
        setNombres(nombres);
        setApellidos(apellidos);
        setCedula(cedula);
        setTelefono(telefono);
    }

    public int    getIdCliente()  { return idCliente; }
    public String getNombres()    { return nombres; }
    public String getApellidos()  { return apellidos; }
    public String getCedula()     { return cedula; }
    public String getTelefono()   { return telefono; }
    public String getEmail()      { return email; }
    public String getDireccion()  { return direccion; }
    public String getCreatedAt()  { return createdAt; }

    public void setIdCliente(int idCliente) {
        if (idCliente < 0) throw new IllegalArgumentException("idCliente no puede ser negativo");
        this.idCliente = idCliente;
    }
 
    public void setNombres(String nombres) {
        this.nombres = validarNoVacio(nombres, "nombres");
    }
 
    public void setApellidos(String apellidos) {
        this.apellidos = validarNoVacio(apellidos, "apellidos");
    }
 
    public void setCedula(String cedula) {
        validarNoVacio(cedula, "cedula");
        if (!cedula.matches("\\d{10}")) {
            throw new IllegalArgumentException("Cédula inválida — debe tener exactamente 10 dígitos: " + cedula);
        }
        this.cedula = cedula;
    }
 
    public void setTelefono(String telefono) {
        validarNoVacio(telefono, "telefono");
        if (!telefono.matches("\\d{7,15}")) {
            throw new IllegalArgumentException("Teléfono inválido: " + telefono);
        }
        this.telefono = telefono;
    }

    public void setEmail(String email) {
        if (email != null && !email.isBlank() && !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido: " + email);
        }
        this.email = (email != null && !email.isBlank()) ? email.toLowerCase().trim() : null;
    }

    public void setDireccion(String direccion) {
        this.direccion = (direccion != null && !direccion.isBlank()) ? direccion.trim() : null;
    }
 
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
 

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
 
    public boolean tieneEmail() {
        return email != null && !email.isBlank();
    }
 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof cliente)) return false;
        cliente c = (cliente) o;
        return Objects.equals(cedula, c.cedula);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(cedula);
    }
 
    @Override
    public String toString() {
        return "Cliente{id=" + idCliente + ", cedula='" + cedula +
               "', nombre='" + getNombreCompleto() + "'}";
    }

    private String validarNoVacio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El campo '" + campo + "' no puede ser nulo o vacío");
        }
        return valor.trim();
    }
}
