package Vista;
import Vista.Loginframe;
import javax.swing.*;

/**
 * Punto de entrada de la aplicación
 */
public class Main {
    public static void main(String[] args) {
        // Ejecutar en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Si falla usa el look por defecto
            }
            Loginframe loginFrame = new Loginframe();
            loginFrame.setVisible(true);
        });
    }
}

