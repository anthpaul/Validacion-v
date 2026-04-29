package Modelo;

import enums.Rol;
import java.util.Objects;
/**
 *
 * @author Anthony Delgado
 */
public class usuario {
 
    private int     idUsuario;
    private String  nombre;
    private String  email;
    private String  passwordHash;
    private Rol     rol;
    private boolean activo;
 


    public usuario() {}

    public usuario(String nombre, String email, String passwordHash, Rol rol) {
        setNombre(nombre);
        setEmail(email);
        this.passwordHash = validarNoVacio(passwordHash, "passwordHash");
        setRol(rol);
        this.activo = true;
    }

    public int     getIdUsuario()  { return idUsuario; }
    public String  getNombre()     { return nombre; }
    public String  getEmail()      { return email; }
    public String  getPasswordHash(){ return passwordHash; }
    public Rol     getRol()        { return rol; }
    public boolean isActivo()      { return activo; }

    public void setIdUsuario(int idUsuario) {
        if (idUsuario < 0) throw new IllegalArgumentException("idUsuario no puede ser negativo");
        this.idUsuario = idUsuario;
    }
 
    public void setNombre(String nombre) {
        this.nombre = validarNoVacio(nombre, "nombre");
    }
 
    public void setEmail(String email) {
        validarNoVacio(email, "email");
        if (!email.contains("@")) throw new IllegalArgumentException("Email inválido: " + email);
        this.email = email.toLowerCase().trim();
    }

    public void actualizarPasswordHash(String passwordHash) {
        this.passwordHash = validarNoVacio(passwordHash, "passwordHash");
    }
 
    public void setRol(Rol rol) {
        if (rol == null) throw new IllegalArgumentException("El rol no puede ser nulo");
        this.rol = rol;
    }
 
    public void setActivo(boolean activo)      { this.activo = activo; }


    public boolean esAdmin()         { return Rol.ADMIN.equals(this.rol); }
    public boolean esRecepcionista() { return Rol.RECEPCIONISTA.equals(this.rol); }
    public boolean esMecanico()      { return Rol.MECANICO.equals(this.rol); }
    public boolean estaActivo()      { return this.activo; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof usuario)) return false;
        usuario u = (usuario) o;
        return idUsuario == u.idUsuario;
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(idUsuario);
    }
 
    @Override
    public String toString() {
        return "Usuario{id=" + idUsuario + ", email='" + email +
               "', rol=" + rol + ", activo=" + activo + "}";
    }

    private String validarNoVacio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El campo '" + campo + "' no puede ser nulo o vacío");
        }
        return valor.trim();
    }
}
