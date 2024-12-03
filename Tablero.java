
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
    private int vidas = 5; // Por defecto 5, el jugador puede elegir el nº
    private int puntuacion = 0;
    private int[] primeraSeleccion = null; // celdas en coordenadas [fila, columna]
    private int[] segundaSeleccion = null;
    private boolean jugadaInvalida = false;

    //Constructores
    public Tablero(int filas, int columnas, int vidas) {
        this.filas = filas;
        this.columnas = columnas;
        this.tablero = new int[filas][columnas];
        this.vidas = vidas;
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

    private boolean jugada() {
        int f1 = primeraSeleccion[0], c1 = primeraSeleccion[1];
        int f2 = segundaSeleccion[0], c2 = segundaSeleccion[1];

        if (tablero[f1][c1] == 0 || tablero[f2][c2] == 0) {
            return false; // No se puede seleccionar casillas vacías, el 0 está fuera de parametros
        }

        // buscamos los valores en el tablero / mátriz
        int num1 = tablero[f1][c1];
        int num2 = tablero[f2][c2];

        boolean sonIguales = num1 == num2;
        boolean sumanDiez = num1 + num2 == 10;
        if (sonIguales || sumanDiez) {
            return validarJugada(f1, c1, f2, c2);
        } else {
            this.jugadaInvalida = true;
            return false;
        }
    }

    private boolean validarJugada(int f1, int c1, int f2, int c2) {
        if (f1 == f2) { // Si estamos en la fila columna para los dos numeros
            int inicio = Math.min(c1, c2) + 1; // Vemos cual es menor
            int fin = Math.max(c1, c2); // Vemos cual es mayor

            for (int i = inicio; i < fin; i++) { // Recorremos la matriz
                if (tablero[f1][i] != 0) {
                    return false; // Si CUALQUIERA de los valores no es 0, es decir, no está vacio, retornamos false, la jugada fue errada
                }
            }
            this.puntuacion += 2 * (fin - inicio);
            return true;
        } else if (c1 == c2) { // Si estamos en la misma columna para los dos numeros
            int inicio = Math.min(f1, f2) + 1; // pos lo mismo
            int fin = Math.max(f1, f2); // x2
            for (int i = inicio; i < fin; i++) {
                if (tablero[i][c1] != 0) {
                    return false; // toca mejorar
                }
            }
            this.puntuacion += 2 * (fin - inicio);
            return true;
        } else if (Math.abs(f1 - f2) == Math.abs(c1 - c2)) { // Diagonal, para que Sergio no llore tanto
            int pasoFila = (f2 > f1) ? 1 : -1;
            int pasoColumna = (c2 > c1) ? 1 : -1;

            for (int i = 1; i < Math.abs(f1 - f2); i++) {
                if (tablero[f1 + i * pasoFila][c1 + i * pasoColumna] != 0) {
                    return false;
                }
            }
            this.puntuacion += 4 * (Math.abs(c2 - c1) - 1);
            return true;
        } else {
            this.vidas -= 1;
            return false; // No fue válida baja ninguna condición
        }
    }

    private void realizarJugada() {
        int f1 = primeraSeleccion[0], c1 = primeraSeleccion[1];
        int f2 = segundaSeleccion[0], c2 = segundaSeleccion[1];

        tablero[f1][c1] = 0;
        tablero[f2][c2] = 0;

        this.puntuacion += 1;

        // Hasta acá todo ok, el prox problema sería validar que haya jugadas posibles o sugerirle directamente al jugador que agregue filas
    }

    //Métodos de la clase que implementan el juego: básicamente hacer una
    //jugada, dibujar el estado del tablero y comprobar si la partida se acabó
    //Método paint
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // El ancho se obtiene dividiendo el ancho total del tablero (VENTANA) por el número de columnas.
        int anchoCelda = getWidth() / columnas;
        // La altura se calcula dividiendo el alto total del tablero (VENTANA) por el número de filas.
        int altoCelda = getHeight() / filas;

        // Dibujar las celdas y sus números
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                // fondo de la celda
                if (primeraSeleccion != null && primeraSeleccion[0] == i && primeraSeleccion[1] == j) {
                    g.setColor(new Color(144, 238, 144)); // Verde claro para la primera selección
                } else if (segundaSeleccion != null && segundaSeleccion[0] == i && segundaSeleccion[1] == j) {
                    if (this.jugadaInvalida) {
                        g.setColor(Color.RED); // Rojo para una jugada inválida
                    } else {
                        g.setColor(new Color(144, 238, 144)); // Verde claro para la segunda selección
                    }
                } else {
                    g.setColor(Color.LIGHT_GRAY); // Color por defecto para las demás celdas
                }

                g.fillRect(j * anchoCelda, i * altoCelda, anchoCelda, altoCelda);

                // borde de la celda.
                g.setColor(Color.BLACK);

                g.drawRect(j * anchoCelda, i * altoCelda, anchoCelda, altoCelda);

                // Dibujar número si la celda no está vacía, comparamos con 0 porque 0 no puede ser parte de los nº
                if (tablero[i][j] > 0 && tablero[i][j] <= 9) {
                    g.setFont(new Font("Arial", Font.BOLD, 26)); // fuente para el texto.
                    // Dibuja el número "centrado" aproximadamente en la celda.
                    g.drawString(String.valueOf(tablero[i][j]),
                            j * anchoCelda + anchoCelda / 2 - 4,
                            i * altoCelda + altoCelda / 2 + 4); // + y - 4 puestos a ojo para que quede más bonito centrado
                }
            }
        }

    }

    //Clase privada para capturar los eventos del ratón
    private class MouseHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            int anchoCelda = getWidth() / columnas; // el ancho de cada celda
            int altoCelda = getHeight() / filas; // la altura de cada celda
            int columna = e.getX() / anchoCelda; // columna donde se hizo clic
            int fila = e.getY() / altoCelda; // fila donde se hizo clic

            if (fila >= 0 && fila < filas && columna >= 0 && columna < columnas) { // Check para clicks válidos en casillas que NO estén vacias
                if (tablero[fila][columna] == 0) {
                    JOptionPane.showMessageDialog(null, "Lo de leer instrucciones no es lo tuyo.");
                    return;
                }

                if (primeraSeleccion == null) {
                    primeraSeleccion = new int[]{fila, columna}; // guarda la primera selección
                } else if (segundaSeleccion == null) {
                    segundaSeleccion = new int[]{fila, columna}; // guarda la segunda selección
                    repaint(); // actualiza la visualización antes de evaluar la jugada

                    if (jugada()) { // comprueba si la jugada es válida
                        realizarJugada();
                        JOptionPane.showMessageDialog(null, "Bien. Puntuación: " + puntuacion);
                    } else {
                        Tablero.this.jugadaInvalida = true;
                        repaint(); // Pinta las celdas roj
                        vidas--; // resta una vida
                        JOptionPane.showMessageDialog(null, "Toca mejorar chaval. Vidas: " + vidas + " Puntuación: " + puntuacion);
                    }

                    // Reinicia las selecciones después de procesar TODA la jugada, aca null es válido SOLO por las validaciones que hacemos antes de != null
                    primeraSeleccion = null;
                    segundaSeleccion = null;
                    jugadaInvalida = false;
                }

                repaint(); // reinstancia el tablero de nuevo con los valores actualizados
            }
        }
    }
}
