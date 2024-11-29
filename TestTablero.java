import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TestTablero {
    public static void main(String[] args) {
        String entrada = JOptionPane.showInputDialog("Nº de filas:");
        int filas = Integer.parseInt(entrada);

        entrada = JOptionPane.showInputDialog("Nº de columnas:");
        int columnas = Integer.parseInt(entrada);

        Tablero t = new Tablero(filas, columnas);

        JFrame app = new JFrame("Number Match");
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setSize(600, 600); // Tamaño de la ventana por defecto, se puede rezisear una vez creada
        app.add(t);
        app.setVisible(true);
    }
}