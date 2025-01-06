import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TestTablero {
    private static Tablero t;

    public static void main(String[] args) {
        // ícono personalizado para los input
        ImageIcon iconoQ = new ImageIcon("resources\\placeholder1.png");
        Image iconoF = iconoQ.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        ImageIcon iconoRedimensionado = new ImageIcon(iconoF);

        // Establecer el ícono personalizado para los cuadros de diálogo
        UIManager.put("OptionPane.questionIcon", iconoRedimensionado);
        String entrada = JOptionPane.showInputDialog("Nº de filas (>= 3):");
        int filas = Integer.parseInt(entrada);
        while (filas < 3) {
            JOptionPane.showMessageDialog(null, "El número de filas debe ser mayor o igual a 3");
            entrada = JOptionPane.showInputDialog("Nº de filas (>= 3):");
            filas = Integer.parseInt(entrada);
        }
        entrada = JOptionPane.showInputDialog("Nº de columnas:");
        int columnas = Integer.parseInt(entrada);
        while (columnas < 1) {
            JOptionPane.showMessageDialog(null, "El número de columnas debe ser mayor que 0");
            entrada = JOptionPane.showInputDialog("Nº de columnas:");
            columnas = Integer.parseInt(entrada);
        }

        entrada = JOptionPane.showInputDialog("Nº de vidas:");
        int vidas = Integer.parseInt(entrada);
        while (vidas < 1) {
            JOptionPane.showMessageDialog(null, "El número de vidas debe ser mayor que 0");
            entrada = JOptionPane.showInputDialog("Nº de vidas:");
            vidas = Integer.parseInt(entrada);
        }

        JFrame app = new JFrame("Number Match");
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setSize(600, 600);
        app.setLayout(new BorderLayout());

        // Cargar imágenes
        ImageIcon unaJugada = new ImageIcon("resources\\1jugada.png");
        ImageIcon todasJugadas = new ImageIcon("resources\\todasJugadas.png");
        ImageIcon agregarNumeros = new ImageIcon("resources\\agregarNumeros.png");
        Image unaJugadaImagen = unaJugada.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        ImageIcon unaJugadaRedimensionada = new ImageIcon(unaJugadaImagen);
        Image imagenTodasJugadas = todasJugadas.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        ImageIcon todasJugadasRedimensionado = new ImageIcon(imagenTodasJugadas);
        Image imagenAgregarNumeros = agregarNumeros.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        ImageIcon imagenAgregarNumerosRedimensionado = new ImageIcon(imagenAgregarNumeros);


        JButton botonAgregarNumeros = new JButton(imagenAgregarNumerosRedimensionado);
        JButton botonUnaJugada = new JButton(unaJugadaRedimensionada);
        JButton botonTodasJugadas = new JButton(todasJugadasRedimensionado);
        configurarBotonComoIcono(botonAgregarNumeros);
        configurarBotonComoIcono(botonUnaJugada);
        configurarBotonComoIcono(botonTodasJugadas);
        
        botonAgregarNumeros.addActionListener(e -> {
            if (t != null) { // Verificar que el tablero esté inicializado
                t.AñadirFilas();
            }
        });

        botonUnaJugada.addActionListener(e -> {
            if (t != null) { // Verificar que el tablero esté inicializado
                t.sugerirJugada();
            }
        });

        botonTodasJugadas.addActionListener(e -> {
            if (t != null) { // Verificar que el tablero esté inicializado
                t.sugerirTodasJugadas();
            }
        });

        // panel de botones
        JPanel panelIconos = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelIconos.add(botonAgregarNumeros);
        panelIconos.add(botonUnaJugada);
        panelIconos.add(botonTodasJugadas);

        // JLabel para puntuación
        JLabel labelPuntuacion = new JLabel("Puntuación: 0", JLabel.CENTER);
        labelPuntuacion.setFont(new Font("Arial", Font.BOLD, 22));
        labelPuntuacion.setForeground(new Color(34, 139, 34));
        labelPuntuacion.setBorder(new EmptyBorder(0, 5, 0, 0));

        // Crear JLabel para vidas
        JLabel labelVidas = new JLabel("Vidas: 0");
        labelVidas.setFont(new Font("Arial", Font.BOLD, 22));
        labelVidas.setHorizontalAlignment(JLabel.CENTER);

        // Crear panel para información
        JPanel panelInfo = new JPanel(new BorderLayout());
        panelInfo.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        panelInfo.add(labelPuntuacion, BorderLayout.WEST);
        panelInfo.add(labelVidas, BorderLayout.CENTER);
        panelInfo.add(panelIconos, BorderLayout.EAST);
        app.add(panelInfo, BorderLayout.NORTH);

        // Icono de la app
        ImageIcon icono = new ImageIcon("resources\\numberMatch.png");
        app.setIconImage(icono.getImage());

        // Inicializar el tablero y asignarlo a la variable estática
        t = new Tablero(filas, columnas, vidas, labelPuntuacion, labelVidas);
        app.add(t, BorderLayout.CENTER);

        app.setVisible(true);
    }

    private static void configurarBotonComoIcono(JButton boton) {
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
    }
}
