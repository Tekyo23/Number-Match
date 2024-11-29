
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Tablero extends JPanel {

    //Aquí irían los atributos necesarios
    // 1er draft
    private int[][] tablero; // Esta es la matriz que compone el tablero, no se si el nº de columnas debería ser 8 igual que la figura que nos pasó Juanjo
    private int filas;
    private int columnas;
    private int vidas = 5; // Podemos cambiarlo, incluso darle la opción al jugador
    private int puntuacion = 0;

    //Constructores
    public Tablero(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.tablero = new int[filas][columnas];

        inicializarTablero();

        addMouseListener(new MouseHandler());
    }

    private void inicializarTablero() {
        Random random = new Random();
        for (int i = 0; i < Math.min(filas, 3); i++) { // Esto SOLO INICIALIZA LAS PRIMERAS 3 FILAS, según el enunciado
            for (int j = 0; j < columnas; j++) {
                tablero[i][j] = random.nextInt(9) + 1; // Se asignan nunmeros random a cada celda del tablero
            }
        }
    }

    //Métodos de la clase que implementan el juego: básicamente hacer una
    //jugada, dibujar el estado del tablero y comprobar si la partida se acabó
    //Método paint
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // El ancho se obtiene dividiendo el ancho total del tablero (VENTANA) por el número de columnas.
        int anchoCelda = getWidth() / columnas;
        // La altura se calcula dividiendo el alto total del tablero (VENTATA) por el número de filas.
        int altoCelda = getHeight() / filas;

        // Dibujar las celdas y sus números
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                // Dibujar celda

                // fondo de la celda
                g.setColor(Color.lightGray);
                g.fillRect(j * anchoCelda, i * altoCelda, anchoCelda, altoCelda);

                // borde de la celda.
                g.setColor(Color.black);
                g.drawRect(j * anchoCelda, i * altoCelda, anchoCelda, altoCelda);

                // Dibujar número si la celda no está vacía, comparamos con 0 porque 0 no puede ser parte de los nº
                if (tablero[i][j] > 0 && tablero[i][j] <= 9) {
			g.setFont(new Font("Arial", Font.BOLD, 20)); // Configura la fuente para el texto.
			// Dibuja el número centrado aproximadamente en la celda.
		    g.drawString(String.valueOf(tablero[i][j]),
			    j * anchoCelda + anchoCelda / 2 - 4, 
			    i * altoCelda + altoCelda / 2 + 4);// + y - 4 puestos a ojo para que quede mas bonito centrado
                }
            }
        }
    }

    //Clase privada para capturar los eventos del ratón
    private class MouseHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            int anchoCelda = getWidth() / columnas;  // el getWidth es una función del componente que te da un nº, al dividir por el nº de columnas las hacemos equiformes
            int altoCelda = getHeight() / filas; // pos mas de lo mismo

            // Determina en qué celda ocurrió el clic dividiendo las coordenadas del clic entre el tamaño de las celdas.
            // e.getX()` y `e.getY()` te dicen las coordenadas del clic en PIXELES. Con eso podmos usar MATEMATICAS para sacar las coordenadas
            int columna = e.getX() / anchoCelda;
            int fila = e.getY() / altoCelda;

            if (fila >= 0 && fila < filas && columna >= 0 && columna < columnas) { // Check para que el click tenga sentido, sino no hago nada
                JOptionPane.showMessageDialog(null,
                        String.format("Click en %d, %d", fila, columna)); // Por ahora solo te dice que hiciste click
            }
        }
    }
}
