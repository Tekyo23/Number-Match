import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Tablero extends JPanel  {

	//Aquí irían los atributos necesarios

	//Constructores
	Tablero() {
		//El constructor debe tener los parámetros oportunos 
		//para inicializar el tablero y el juego

		// Añadimos el 'escuchador' de ratón
		addMouseListener(new MouseHandler());
	}

	//Métodos de la clase que implementan el juego: básicamente hacer una
	//jugada, dibujar el estado del tablero y comprobar si la partida se acabó

	//Método paint
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Aquí iría el código para pintar el estado del tablero
	
		// Pintar rectángulo
		g.setColor(Color.blue);
		g.fillRect(10, 10, 40, 40);

		// Pintar línea
		g.setColor(Color.lightGray);
		g.drawLine(55, 10, 55, 80);
	
		// Pintar un texto
		Font fuente = new Font("Arial", Font.PLAIN, 20);
		g.setFont(fuente);
		g.setColor(Color.black);
		g.drawString("Texto", 10,100);
	}	

	//Clase privada para capturar los eventos del ratón
	private class MouseHandler extends MouseAdapter {
		public void mouseClicked (MouseEvent e) {
			//Mostramos un diálogo con la posición del ratón 
			//para ver un ejemplo de cómo se obtienen las coordenadas
			//donde se produjo el click
			JOptionPane.showMessageDialog(null, String.format("Ratón %d,%d \n",e.getX(),e.getY()));

			//Aquí irían las instrucciones para comprobar si el 
			//click del ratón se produjo en una posición correcta
			//y hacer la jugada correspondiente
			
			//Se pueden llamar a los métodos públicos de la clase

			//Seguramente habrá que repintar el tablero si se realizó
			//una jugada válida
			repaint();
		}
	}
}

