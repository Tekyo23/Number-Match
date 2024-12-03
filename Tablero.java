
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.JLabel;
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
    private JLabel labelPuntuacion;
    private JLabel labelVidas;

    public Tablero(int filas, int columnas, int vidas, JLabel labelPuntuacion, JLabel labelVidas) {
        this.filas = filas;
        this.columnas = columnas;
        this.vidas = vidas;
        this.tablero = new int[filas][columnas];
        this.labelPuntuacion = labelPuntuacion;
        this.labelVidas = labelVidas;
        inicializarTablero();
        addMouseListener(new MouseHandler());
        actualizarEtiquetas();
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
                    this.jugadaInvalida = true;
                    vidas--;
                    actualizarEtiquetas();
                    return false; // Si CUALQUIERA de los valores no es 0, es decir, no está vacio, retornamos false, la jugada fue errada
                }
            }
            this.puntuacion += 2 * (fin - inicio);
            actualizarEtiquetas();
            return true;
        } else if (c1 == c2) { // Si estamos en la misma columna para los dos numeros
            int inicio = Math.min(f1, f2) + 1; // pos lo mismo
            int fin = Math.max(f1, f2); // x2
            for (int i = inicio; i < fin; i++) {
                if (tablero[i][c1] != 0) {
                    this.jugadaInvalida = true;
                    vidas--;
                    actualizarEtiquetas();
                    return false;
                }
            }
            this.puntuacion += 2 * (fin - inicio);
            actualizarEtiquetas();
            return true;
        } else if (Math.abs(f1 - f2) == Math.abs(c1 - c2)) { // Diagonal, para que Sergio no llore tanto
            int pasoFila = (f2 > f1) ? 1 : -1;
            int pasoColumna = (c2 > c1) ? 1 : -1;

            for (int i = 1; i < Math.abs(f1 - f2); i++) {
                if (tablero[f1 + i * pasoFila][c1 + i * pasoColumna] != 0) {
                    this.jugadaInvalida = true;
                    vidas--;
                    actualizarEtiquetas();
                    return false;
                }
            }
            this.puntuacion += 4 * (Math.abs(c2 - c1) - 1);
            actualizarEtiquetas();
            return true;
        } else {
            this.vidas -= 1;
            actualizarEtiquetas();
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

    private void actualizarEtiquetas() {
        labelPuntuacion.setText("Puntuación: " + this.puntuacion);
        labelPuntuacion.setFont(new Font("Arial", Font.BOLD, 16));
        labelPuntuacion.setForeground(new Color(34, 139, 34)); // Verde oscuro
    
        labelVidas.setText("Vidas: " + this.vidas);
        labelVidas.setFont(new Font("Arial", Font.BOLD, 16));
        labelVidas.setForeground(vidas > 1 ? Color.BLACK : Color.RED); // Rojo si queda una vida
    }

    //Métodos de la clase que implementan el juego: básicamente hacer una
    //jugada, dibujar el estado del tablero y comprobar si la partida se acabó
    //Método paint
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    
        int anchoCelda = getWidth() / columnas;
        int altoCelda = getHeight() / filas;
    
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                // Color de la celda según su estado
                if (primeraSeleccion != null && primeraSeleccion[0] == i && primeraSeleccion[1] == j) {
                    g.setColor(new Color(144, 238, 144)); // Verde claro para primera selección
                } else if (segundaSeleccion != null && segundaSeleccion[0] == i && segundaSeleccion[1] == j) {
                    g.setColor(jugadaInvalida ? Color.RED : new Color(144, 238, 144)); // Rojo o verde para segunda selección
                } else {
                    g.setColor(Color.LIGHT_GRAY); // Gris claro para celdas inactivas
                }
    
                g.fillRect(j * anchoCelda, i * altoCelda, anchoCelda, altoCelda); // Rellenar celda
    
                g.setColor(Color.BLACK); // Borde negro
                g.drawRect(j * anchoCelda, i * altoCelda, anchoCelda, altoCelda);
    
                // Dibujar el número
                if (tablero[i][j] > 0) {
                    g.setFont(new Font("Arial", Font.BOLD, 24)); // Fuente grande
                    g.setColor(Color.DARK_GRAY); // Número en gris oscuro
                    String numero = String.valueOf(tablero[i][j]);
                    int x = j * anchoCelda + anchoCelda / 2 - g.getFontMetrics().stringWidth(numero) / 2;
                    int y = i * altoCelda + altoCelda / 2 + g.getFontMetrics().getHeight() / 3;
                    g.drawString(numero, x, y);
                }
            }
        }
    }
    

    //Clase privada para capturar los eventos del ratón
    private class MouseHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int anchoCelda = getWidth() / columnas;
            int altoCelda = getHeight() / filas;
    
            int columna = e.getX() / anchoCelda;
            int fila = e.getY() / altoCelda;
    
            if (fila >= 0 && fila < filas && columna >= 0 && columna < columnas) {
                if (tablero[fila][columna] == 0) {
                    JOptionPane.showMessageDialog(null, "Selecciona una casilla válida.");
                    return;
                }
    
                if (primeraSeleccion == null) {
                    primeraSeleccion = new int[]{fila, columna};
                } else if (segundaSeleccion == null) {
                    segundaSeleccion = new int[]{fila, columna};
                    repaint(); // Refrescar visual antes de evaluar
    
                    if (jugada()) {
                        realizarJugada();
                        JOptionPane.showMessageDialog(null, "Jugada válida. ¡Sigue así!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Jugada inválida. Pierdes una vida.");
                    }
    
                    // Reiniciar selecciones
                    primeraSeleccion = null;
                    segundaSeleccion = null;
                    jugadaInvalida = false;
                    actualizarEtiquetas();
                }
                repaint(); // Refrescar tablero
            }
        }
    }
    
}
