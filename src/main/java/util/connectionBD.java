package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
/**
 *
 * @author Anthony Delgado
 */
public class connectionBD {
    private static final String ARCHIVO_PROPERTIES = "/db.properties";

    private static String url;
    private static String usuario;
    private static String password;

    static {
        try (InputStream is = connectionBD.class.getResourceAsStream(ARCHIVO_PROPERTIES)) {

            if (is == null) {
                throw new ExceptionInInitializerError("No se encontró el archivo: " + ARCHIVO_PROPERTIES + 
                    " buscando en el paquete: " + connectionBD.class.getPackage().getName());
            }

            Properties props = new Properties();
            props.load(is);

            Class.forName(props.getProperty("db.driver"));

            url      = props.getProperty("db.url");
            usuario  = props.getProperty("db.usuario");
            password = props.getProperty("db.password");

        } catch (IOException e) {
            throw new ExceptionInInitializerError("Error leyendo db.properties: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("Driver MySQL no encontrado: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, usuario, password);
    }
 

    public static void cerrar(Connection cn) {
        if (cn != null) {
            try { cn.close(); } catch (SQLException e) {
                System.err.println("Error al cerrar Connection: " + e.getMessage());
            }
        }
    }
 
    public static void cerrar(java.sql.PreparedStatement ps) {
        if (ps != null) {
            try { ps.close(); } catch (SQLException e) {
                System.err.println("Error al cerrar PreparedStatement: " + e.getMessage());
            }
        }
    }
 
    public static void cerrar(java.sql.ResultSet rs) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) {
                System.err.println("Error al cerrar ResultSet: " + e.getMessage());
            }
        }
    }

    public static void cerrar(java.sql.ResultSet rs,
                               java.sql.PreparedStatement ps,
                               Connection cn) {
        cerrar(rs);
        cerrar(ps);
        cerrar(cn);
    }
}
