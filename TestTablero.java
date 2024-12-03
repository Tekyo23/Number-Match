import java.awt.*;
import javax.swing.*;

public class TestTablero {
    public static void main(String[] args) {
        String entrada = JOptionPane.showInputDialog("Nº de filas:");
        int filas = Integer.parseInt(entrada);
        while (filas < 3) {
            JOptionPane.showMessageDialog(null, "El número de filas debe ser mayor o igual a 3");
            entrada = JOptionPane.showInputDialog("Nº de filas (>= 3):");
            filas = Integer.parseInt(entrada);
        }
        entrada = JOptionPane.showInputDialog("Nº de columnas:");
        int columnas = Integer.parseInt(entrada);

        entrada = JOptionPane.showInputDialog("Nº de vidas:");
        int vidas = Integer.parseInt(entrada);

        JFrame app = new JFrame("Number Match");
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setSize(600, 600);
        app.setLayout(new BorderLayout());

        JLabel labelPuntuacion = new JLabel("Puntuación: 0");
        labelPuntuacion.setFont(new Font("Arial", Font.BOLD, 16));
        labelPuntuacion.setForeground(new Color(34, 139, 34)); // Verde oscuro
        labelPuntuacion.setHorizontalAlignment(JLabel.CENTER);
        
        JLabel labelVidas = new JLabel("Vidas: 0" );
        labelVidas.setFont(new Font("Arial", Font.BOLD, 16));
        labelVidas.setForeground(Color.BLACK); // Negro por defecto
        labelVidas.setHorizontalAlignment(JLabel.CENTER);
        
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Centra los elementos con separación
        panelInfo.setBackground(new Color(220, 220, 220)); // Fondo gris claro
        panelInfo.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Borde negro
        panelInfo.add(labelVidas);
        panelInfo.add(labelPuntuacion);

        app.add(panelInfo, BorderLayout.NORTH);

        // Crear tablero y pasar referencias de los labels
        Tablero t = new Tablero(filas, columnas, vidas, labelPuntuacion, labelVidas);
        app.add(t, BorderLayout.CENTER);

        app.setVisible(true);
    }
}
