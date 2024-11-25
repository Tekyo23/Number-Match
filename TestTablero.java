import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TestTablero {

	public static void main(String[] args) {
		Tablero t = new Tablero();
		
		JFrame app = new JFrame("Tablero");
		
		String entrada= JOptionPane.showInputDialog("Filas:");
		int filas=Integer.parseInt(entrada);

		String salida=String.format("Has elegido %d filas", filas);
        JOptionPane.showMessageDialog(null, salida);

		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setBounds(100, 100, 440, 470);
		app.add(t);
		app.setVisible(true);
	}
}
