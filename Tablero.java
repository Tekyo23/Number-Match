/**
 * @author Sergio Lombao
 * @author Julián Lechuga
 * @author Raúl Rodríguez
 * @version 1.0
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Tablero extends JPanel {

    private final int[][] tablero; // Esta es la matriz que compone el tablero, no se si el nº de columnas debería
    // ser 8 igual que la figura que nos pasó Juanjo
    private final int filas;
    private final int columnas;
    private int vidas;
    private int puntuacion = 0;
    private int[] primeraSeleccion = null; // celdas en coordenadas [fila, columna]
    private int[] segundaSeleccion = null;
    private boolean jugadaInvalida = false;
    private final JLabel labelPuntuacion;
    private final JLabel labelVidas;
    private boolean[][] iluminadas; // Para saber qué celdas están iluminadas

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

    private boolean jugada(boolean esSugerencia) {
        int f1 = primeraSeleccion[0], c1 = primeraSeleccion[1];
        int f2 = segundaSeleccion[0], c2 = segundaSeleccion[1];
        if (tablero[f1][c1] == 0 || tablero[f2][c2] == 0) { // Estamos seleccionando LA MISMA CELDA dos veces
            return false;
        } else if ((f1 == f2 && c1 == c2) || !validarJugada(f1, c1, f2, c2, esSugerencia)) {
            this.jugadaInvalida = true;
            if (!esSugerencia) { // Solo se restan vidas si NO es una sugerencia
                this.vidas--;
                actualizarEtiquetas();
            }
            return false;
        } else {
            return true;
        }
    }

    private boolean validarJugada(int f1, int c1, int f2, int c2, boolean esSugerencia) {
        int num1 = tablero[f1][c1];
        int num2 = tablero[f2][c2];
        boolean sonIguales = num1 == num2;
        boolean sumanDiez = num1 + num2 == 10;
        if (sonIguales || sumanDiez) {
            if (f1 == f2) { // Si estamos en la fila columna para los dos numeros
                int inicio = Math.min(c1, c2) + 1; // Vemos cual es menor
                int fin = Math.max(c1, c2); // Vemos cual es mayor
    
                for (int i = inicio; i < fin; i++) { // Recorremos la matriz
                    if (tablero[f1][i] != 0) {
                        actualizarEtiquetas();
                        return false; // Si CUALQUIERA de los valores no es 0, es decir, no está vacío, retornamos false, la jugada fue errada
                    }
                }
                if (!esSugerencia) {
                    this.puntuacion += 2 * (fin - inicio) + 1;
                }
                actualizarEtiquetas();
                return true;
            } else if (c1 == c2) { // Si estamos en la misma columna para los dos números
                int inicio = Math.min(f1, f2) + 1; // pos lo mismo
                int fin = Math.max(f1, f2); // x2
                for (int i = inicio; i < fin; i++) {
                    if (tablero[i][c1] != 0) {
                        actualizarEtiquetas();
                        return false;
                    }
                }
                if (!esSugerencia) {
                    this.puntuacion += 2 * (fin - inicio) + 1;
                }
                actualizarEtiquetas();
                return true;
            } else if (Math.abs(f1 - f2) == Math.abs(c1 - c2)) { // Diagonal, para que Sergio no llore tanto
                int pasoFila = (f2 > f1) ? 1 : -1;
                int pasoColumna = (c2 > c1) ? 1 : -1;
    
                for (int i = 1; i < Math.abs(f1 - f2); i++) {
                    if (tablero[f1 + i * pasoFila][c1 + i * pasoColumna] != 0) {
                        actualizarEtiquetas();
                        return false;
                    }
                }
                if (!esSugerencia) {
                    this.puntuacion += 4 * (Math.abs(c2 - c1) - 1) + 1;
                }
                actualizarEtiquetas();
                return true;
            } else {
                int filaMenor = Math.min(f1, f2);
                int filaMayor = Math.max(f1, f2);
                if (filaMenor == f2) {
                    int hold = c1;
                    c1 = c2;
                    c2 = hold;
                }
                for (int i = c1 + 1; i < this.columnas; i++) {
                    if (tablero[filaMenor][i] != 0) {
                        actualizarEtiquetas();
                        return false;
                    }
                }
                for (int j = 0; j < c2; j++) {
                    if (tablero[filaMayor][j] != 0) {
                        actualizarEtiquetas();
                        return false;
                    }
                }
                if (!esSugerencia) {
                    this.puntuacion += ((this.columnas - (c1 + 1)) * 3) + (c2 * 3) + 1;
                }
                actualizarEtiquetas();
                return true;
            }
        } else {
            actualizarEtiquetas();
            return false;
        }
    }

    private void realizarJugada() {
        int f1 = primeraSeleccion[0], c1 = primeraSeleccion[1];
        int f2 = segundaSeleccion[0], c2 = segundaSeleccion[1];

        tablero[f1][c1] = 0;
        tablero[f2][c2] = 0;
    }

    private void actualizarEtiquetas() {
        labelPuntuacion.setText("Puntuación: " + this.puntuacion);
        labelPuntuacion.setFont(new Font("Arial", Font.BOLD, 16));
        labelPuntuacion.setForeground(puntuacion > 5 ? new Color(34, 139, 34) : Color.blue); // Verde oscuro

        labelVidas.setText("Vidas: " + this.vidas);
        labelVidas.setFont(new Font("Arial", Font.BOLD, 16));
        labelVidas.setForeground(vidas > 1 ? Color.BLACK : Color.RED); // Rojo si queda una vida
    }

    void sugerirJugada() {
        for (int f1 = 0; f1 < filas; f1++) {
            for (int c1 = 0; c1 < columnas; c1++) {
                if (tablero[f1][c1] == 0) {
                    continue; // Ignorar celdas vacías
                }
                for (int f2 = 0; f2 < filas; f2++) {
                    for (int c2 = 0; c2 < columnas; c2++) {
                        if ((f1 == f2 && c1 == c2) || tablero[f2][c2] == 0) {
                            continue;
                        }
                        primeraSeleccion = new int[]{f1, c1};
                        segundaSeleccion = new int[]{f2, c2};

                        if (jugada(true)) { // Ahora jugada no resta vidas en modo sugerencia
                            iluminarCeldas(new int[]{f1, c1}, new int[]{f2, c2}, false);
                            JOptionPane.showMessageDialog(this,
                                    "Sugerencia: Intenta combinar el " + tablero[f1][c1] + " y " + tablero[f2][c2] + ".");
                            iluminarCeldas(new int[]{f1, c1}, new int[]{f2, c2}, true);
                            primeraSeleccion = null;
                            segundaSeleccion = null;
                            jugadaInvalida = false;
                            return;
                        }
                    }
                }
            }
        }

        JOptionPane.showMessageDialog(this, "No hay jugadas posibles en el tablero.");
        primeraSeleccion = null;
        segundaSeleccion = null;
    }

    void sugerirTodasJugadas() {
        String[] jugadasValidas = new String[filas * columnas * filas * columnas]; // Arreglo de tamaño máximo posible
        int contador = 0; // Contador de jugadas válidas

        for (int f1 = 0; f1 < filas; f1++) {
            for (int c1 = 0; c1 < columnas; c1++) {
                if (tablero[f1][c1] == 0) {
                    continue; // Ignorar celdas vacías
                }
                for (int f2 = 0; f2 < filas; f2++) {
                    for (int c2 = 0; c2 < columnas; c2++) {
                        if ((f1 == f2 && c1 == c2) || tablero[f2][c2] == 0) {
                            continue; // Ignorar celdas vacías o la misma celda
                        }

                        // Temporalmente marcamos las selecciones
                        primeraSeleccion = new int[]{f1, c1};
                        segundaSeleccion = new int[]{f2, c2};

                        // Llamamos a jugada en modo "sugerencia"
                        if (jugada(true)) {
                            jugadasValidas[contador] = "Combina " + tablero[f1][c1]
                                    + " con " + tablero[f2][c2];
                            contador++;
                        }

                        // Limpiamos las selecciones para evitar interferencias
                        primeraSeleccion = null;
                        segundaSeleccion = null;
                    }
                }
            }
        }

        if (contador > 0) {
            String mensaje = "";
            for (int i = 0; i < contador; i++) {
                mensaje += jugadasValidas[i] + "\n";
            }
            JOptionPane.showMessageDialog(this, "Jugadas posibles encontradas:\n" + mensaje);
        } else {
            JOptionPane.showMessageDialog(this, "No hay jugadas posibles en el tablero.");
        }
    }

    public void AñadirFilas() {
        int[][] tableroNuevo = new int[filas][columnas];
        int x = 0;
        int y = 0;
        int h = 0;
        int filaDeCero = 0;
        int filaEmpezar = 0;
        boolean romper = false;
        if (ComprobarQueNoEsteLleno()) {
            for (int i = 0; i < filas; i++) {
                filaDeCero = 0;
                for (int j = 0; j < columnas; j++) {
                    if (tablero[i][j] != 0) {
                        tableroNuevo[x][y] = tablero[i][j];
                        y++;
                        if (y == columnas) {
                            x++;
                            y = 0;
                        }
                    } else {
                        filaDeCero++;
                        if (filaDeCero == columnas) {
                            filaEmpezar = i;
                            romper = true;
                            break;
                        }
                    }
                }
                if (romper) {
                    break;
                }
            }
            for (int i = filaEmpezar; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    tablero[i][j] = tableroNuevo[h][j];
                }
                h++;
            }
        }
    }

    private void iluminarCeldas(int[] primera, int[] segunda, boolean apagarCeldas) {
        // Inicializar el array iluminadas si es null
        if (iluminadas == null) {
            iluminadas = new boolean[filas][columnas]; // Matriz booleana para gestionar las celdas iluminadas
        }

        // Marcamos las celdas a iluminar
        iluminadas[primera[0]][primera[1]] = true;
        iluminadas[segunda[0]][segunda[1]] = true;

        repaint();

        if (apagarCeldas == true) {
            iluminadas[primera[0]][primera[1]] = false;
            iluminadas[segunda[0]][segunda[1]] = false;
        }
    }

    private boolean ComprobarQueNoEsteLleno() {
        int filasCero = 0;
        for (int i = 0; i < this.filas; i++) {
            for (int j = 0; j < this.columnas; j++) {
                if (tablero[i][j] == 0) {
                    filasCero++;
                    if (filasCero == columnas) {
                        return true;
                    }
                }
            }
            filasCero = 0;
        }
        return false;
    }

    public void moverFilasHaciaAbajo() {
        //Creamos un tablero igual que el original
        int[][] nuevoTablero = new int[filas][columnas];
        int nuevaFila = 0;
        for (int i = 0; i < filas; i++) {
            boolean esFilaDeCeros = true;
            for (int j = 0; j < columnas; j++) {
                if (tablero[i][j] != 0) {
                    esFilaDeCeros = false;
                    break;
                }
            }
            //Si la fila no esta vacia la copiamos
            if (!esFilaDeCeros) {
                nuevoTablero[nuevaFila] = tablero[i];
                nuevaFila++;
            }
        }
        //Rellenamos las filas restantes en el nuevo tablero con ceros
        for (int i = nuevaFila; i < filas; i++) {
            nuevoTablero[i] = new int[columnas];
        }
        //copiamos el nuevo tablero al tablero original
        for (int i = 0; i < filas; i++) {
            tablero[i] = nuevoTablero[i];
        }
    }

    private void FinPartida() {
        if (this.vidas < 1) {
            JOptionPane.showMessageDialog(this, "¡Has perdido!");
            ((JFrame) SwingUtilities.getWindowAncestor(this)).dispose();
        } else {
            boolean tableroLLeno = true;
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    if (tablero[i][j] != 0) {
                        tableroLLeno = false;
                        break;
                    }
                }
            }
            if (tableroLLeno) {
                JOptionPane.showMessageDialog(this, "¡Has ganado!");
                ((JFrame) SwingUtilities.getWindowAncestor(this)).dispose();
            }
        }
    }

    // Métodos de la clase que implementan el juego: básicamente hacer una
    // jugada, dibujar el estado del tablero y comprobar si la partida se acabó
    // Método paint
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int anchoCelda = getWidth() / columnas;
        int altoCelda = getHeight() / filas;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                // Color de la celda según su estado
                if (iluminadas != null && iluminadas[i][j]) {
                    g.setColor(Color.YELLOW); // Iluminar con color amarillo
                } else if (primeraSeleccion != null && primeraSeleccion[0] == i && primeraSeleccion[1] == j) {
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

    // Clase privada para capturar los eventos del ratón
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

                    if (jugada(false)) {
                        realizarJugada();
                        JOptionPane.showMessageDialog(null, "Jugada válida. ¡Sigue así!");
                        moverFilasHaciaAbajo();
                        FinPartida();
                    } else {
                        JOptionPane.showMessageDialog(null, "Jugada inválida. Pierdes una vida.");
                        FinPartida();
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
