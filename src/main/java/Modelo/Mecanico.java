package Modelo;

import java.util.Objects;

public class Mecanico {

    private int    id;           
    private int    idUsuario;    
    private String cedula;
    private String especialidad;
    private String telefono;

    private String  nombre;
    private String apellido;
    private String  email;
    private String  passwordHash;
    private boolean activo;

    public Mecanico() {
        this.activo = true;
    }

    public Mecanico(String nombre,String apellido, String email, String passwordHash,
                    String cedula, String especialidad, String telefono) {
        this.nombre       = nombre;
        this.apellido      = apellido;
        this.email        = email;
        this.passwordHash = passwordHash;
        this.cedula       = cedula;
        this.especialidad = especialidad;
        this.telefono     = telefono;
        this.activo       = true;
    }

    public int     getId()           { return id; }
    public int     getIdUsuario()    { return idUsuario; }
    public String  getNombre()       { return nombre; }
    public String  getApellido()     { return apellido;}
    public String  getEmail()        { return email; }
    public String  getPasswordHash() { return passwordHash; }
    public String  getCedula()       { return cedula; }
    public String  getEspecialidad() { return especialidad; }
    public String  getTelefono()     { return telefono; }
    public boolean isActivo()        { return activo; }

    public void setId(int id)                     { this.id = id; }
    public void setIdUsuario(int idUsuario)        { this.idUsuario = idUsuario; }
    public void setNombre(String nombre)           { this.nombre = nombre; }
    public void setApellido(String apellido)       { this.apellido = apellido;}
    public void setEmail(String email)             { this.email = email; }
    public void setPasswordHash(String hash)       { this.passwordHash = hash; }
    public void setCedula(String cedula)           { this.cedula = cedula; }
    public void setEspecialidad(String esp)        { this.especialidad = esp; }
    public void setTelefono(String telefono)       { this.telefono = telefono; }
    public void setActivo(boolean activo)          { this.activo = activo; }

    public String getNombreCompleto() { return nombre; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mecanico)) return false;
        Mecanico m = (Mecanico) o;
        return Objects.equals(cedula, m.cedula);
    }

    @Override
    public int hashCode() { return Objects.hash(cedula); }

    @Override
    public String toString() {
        return "Mecanico{id=" + id + ", nombre='" + nombre + ", apellido='"+ apellido+
               "', cedula='" + cedula + "', especialidad='" + especialidad + "'}";
    }
}