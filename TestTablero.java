import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TestTablero {
    public static void main(String[] args) {
        String entrada = JOptionPane.showInputDialog("Nº de filas:");
        int filas = Integer.parseInt(entrada);
		while (filas < 3) {
			JOptionPane.showMessageDialog(null,"El número de filas debe ser mayor o igual a 3");	
			entrada = JOptionPane.showInputDialog("Nº de filas (>= 3):");
        	filas = Integer.parseInt(entrada);
		}
        entrada = JOptionPane.showInputDialog("Nº de columnas:");
        int columnas = Integer.parseInt(entrada);

        entrada = JOptionPane.showInputDialog("Nº de vidas:");
        int vidas = Integer.parseInt(entrada);

        Tablero t = new Tablero(filas, columnas, vidas);

        JFrame app = new JFrame("Number Match");
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setSize(600, 600); // Tamaño de la ventana por defecto, se puede rezisear una vez creada
        app.add(t);
        app.setVisible(true);
    }
}