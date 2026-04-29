package Vista;

import Modelo.usuario;

import javax.swing.*;
import java.awt.*;

/**
 */
public class InicioFramer extends JFrame {

    private final usuario usuario;
    private JPanel panelContenido;

    public InicioFramer(usuario usuario) {
        this.usuario = usuario;
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        setTitle("Tecnicentro — " + usuario.getNombre() +
                 " [" + usuario.getRol().getValor() + "]");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ── Barra superior ───────────────────────────────────────────────────
        JPanel barraTop = new JPanel(new BorderLayout());
        barraTop.setBackground(new Color(0, 102, 204));
        barraTop.setPreferredSize(new Dimension(0, 45));

        JLabel lblSistema = new JLabel("  TECNICENTRO — Sistema de Gestión");
        lblSistema.setForeground(Color.WHITE);
        lblSistema.setFont(new Font("Arial", Font.BOLD, 14));
        barraTop.add(lblSistema, BorderLayout.WEST);

        JButton btnCerrar = new JButton("Cerrar Sesión");
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBackground(new Color(0, 80, 160));
        btnCerrar.setBorderPainted(false);
        btnCerrar.addActionListener(e -> cerrarSesion());
        barraTop.add(btnCerrar, BorderLayout.EAST);

        add(barraTop, BorderLayout.NORTH);

        // ── Menú lateral ─────────────────────────────────────────────────────
        JPanel menuLateral = new JPanel();
        menuLateral.setLayout(new BoxLayout(menuLateral, BoxLayout.Y_AXIS));
        menuLateral.setBackground(new Color(45, 45, 45));
        menuLateral.setPreferredSize(new Dimension(180, 0));
        menuLateral.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Info del usuario
        JLabel lblUsuario = new JLabel("  " + usuario.getNombre());
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 12));
        lblUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuLateral.add(lblUsuario);

        JLabel lblRol = new JLabel("  " + usuario.getRol().getValor().toUpperCase());
        lblRol.setForeground(new Color(180, 180, 180));
        lblRol.setFont(new Font("Arial", Font.PLAIN, 11));
        lblRol.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuLateral.add(lblRol);
        menuLateral.add(Box.createVerticalStrut(15));
        menuLateral.add(new JSeparator());
        menuLateral.add(Box.createVerticalStrut(10));

        // ── MÓDULOS SEGÚN ROL ─────────────────────────────────────────────────

        if (usuario.esAdmin()) {
            // Admin ve TODO
            agregarSeccion(menuLateral, "GESTIÓN");
            agregarBotonMenu(menuLateral, "Clientes",
                e -> cargarPanel(new ClientePanel()));
            agregarBotonMenu(menuLateral, "Vehículos",
                e -> cargarPanel(new VehiculoPanel()));
            agregarBotonMenu(menuLateral, "Recepción/Entrega", e -> {
                RecepcionEntregaPanel p = new RecepcionEntregaPanel();
                new Controlador.RecepcionEntregaControlador(p, usuario);
                cargarPanel(p);
            });
            agregarBotonMenu(menuLateral, "Mecánicos", e -> {
                MecanicoPanel p = new MecanicoPanel();
                new Controlador.MecanicoControlador(p, new service.MecanicoService());
                cargarPanel(p);
            });
            agregarBotonMenu(menuLateral, "Mantenimiento", e -> {
                MantenimientoPanel p = new MantenimientoPanel();
                new Controlador.MantenimientoControlador(p, usuario);
                cargarPanel(p);
            });
            agregarBotonMenu(menuLateral, "Facturación", e -> {
                FacturacionPanel fp = new FacturacionPanel();
                new Controlador.FacturacionControlador(fp, usuario);
                cargarPanel(fp);
            });

            menuLateral.add(Box.createVerticalStrut(10));
            menuLateral.add(new JSeparator());
            menuLateral.add(Box.createVerticalStrut(10));
            agregarSeccion(menuLateral, "ADMINISTRACIÓN");
            agregarBotonMenu(menuLateral, "Usuarios",
                e -> cargarPanel(new UsuarioPanel()));
            agregarBotonMenu(menuLateral, "Reportes",
                e -> cargarPanel(new Reportespanel()));
            
        }

        if (usuario.esRecepcionista()) {
            // Recepcionista: clientes, vehículos, recepción/entrega, facturación
            agregarSeccion(menuLateral, "GESTIÓN");
            agregarBotonMenu(menuLateral, "Clientes",
                e -> cargarPanel(new ClientePanel()));
            agregarBotonMenu(menuLateral, "Vehículos",
                e -> cargarPanel(new VehiculoPanel()));
            agregarBotonMenu(menuLateral, "Recepción/Entrega", e -> {
                RecepcionEntregaPanel p = new RecepcionEntregaPanel();
                new Controlador.RecepcionEntregaControlador(p, usuario);
                cargarPanel(p);
            });
            agregarBotonMenu(menuLateral, "Facturación", e -> {
               FacturacionPanel fp = new FacturacionPanel();
               new Controlador.FacturacionControlador(fp, usuario);
               cargarPanel(fp);
            });
        }

        if (usuario.esMecanico()) {
            agregarSeccion(menuLateral, "MI TRABAJO");
        agregarBotonMenu(menuLateral, "Mis Órdenes", e -> {
            MantenimientoPanel p = new MantenimientoPanel();
            new Controlador.MantenimientoControlador(p, usuario);
            cargarPanel(p);
        });
        }

        menuLateral.add(Box.createVerticalGlue());
        add(menuLateral, BorderLayout.WEST);

        
        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(new Color(240, 240, 240));

        JLabel lblBienvenida = new JLabel(
                "Bienvenido, " + usuario.getNombre(), SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 18));
        lblBienvenida.setForeground(new Color(100, 100, 100));
        panelContenido.add(lblBienvenida, BorderLayout.CENTER);

        add(panelContenido, BorderLayout.CENTER);
    }

    public void cargarPanel(JPanel panel) {
        panelContenido.removeAll();
        panelContenido.add(panel, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de cerrar sesión?",
                "Cerrar Sesión",
                JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            dispose();
            Loginframe loginFrame = new Loginframe();
            loginFrame.setVisible(true);
        }
    }

    private void agregarSeccion(JPanel menu, String texto) {
        JLabel lbl = new JLabel("  " + texto);
        lbl.setForeground(new Color(150, 150, 150));
        lbl.setFont(new Font("Arial", Font.PLAIN, 10));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        menu.add(lbl);
        menu.add(Box.createVerticalStrut(3));
    }

    private void agregarBotonMenu(JPanel menu, String texto,
                                  java.awt.event.ActionListener accion) {
        JButton btn = new JButton("  " + texto);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(180, 38));
        btn.setPreferredSize(new Dimension(180, 38));
        btn.setBackground(new Color(45, 45, 45));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.PLAIN, 13));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(0, 102, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(45, 45, 45));
            }
        });

        btn.addActionListener(accion);
        menu.add(btn);
        menu.add(Box.createVerticalStrut(2));
    }
}