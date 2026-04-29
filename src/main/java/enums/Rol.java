package enums;

/**
 *
 * @author Anthony Delgado 
 */
public enum Rol {
    ADMIN("admin"),
    RECEPCIONISTA("recepcionista"),
    MECANICO("mecanico");
 
    private final String valor;
 
    Rol(String valor) {
        this.valor = valor;
    }
 
    public String getValor() {
        return valor;
    }
    public static Rol fromValor(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El rol no puede ser nulo o vacio");
        }
        for (Rol rol : values()) {
            if (rol.valor.equalsIgnoreCase(valor)) {
                return rol;
            }
        }
        throw new IllegalArgumentException("Rol no reconocido: " + valor);
    }
 
    @Override
    public String toString() {
        return valor;
    }
}
