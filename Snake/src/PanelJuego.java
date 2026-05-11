import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class PanelJuego extends JPanel implements KeyListener, Runnable {
    private static final int TAMANO_CUADRO = 25;
    private static final int FILAS = 10;
    private static final int COLUMNAS = 20;
    private static final int RETARDO = 500;

    private Serpiente serpiente;
    private Alimento alimento;
    private GestorPuntuaciones gestorPuntuaciones;
    private int puntuacion;
    private boolean enEjecucion;
    private boolean juegoTerminado;
    private Thread hiloJuego;

    public PanelJuego() {
        setPreferredSize(new Dimension(COLUMNAS * TAMANO_CUADRO, FILAS * TAMANO_CUADRO + 60));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        gestorPuntuaciones = new GestorPuntuaciones("puntuaciones.txt");
        iniciarNuevoJuego();
    }

    private void iniciarNuevoJuego() {
        serpiente = new Serpiente(new Posicion(COLUMNAS / 2, FILAS / 2));
        alimento = new Alimento();
        alimento.reaparecer(COLUMNAS, FILAS, serpiente);
        puntuacion = 0;
        juegoTerminado = false;
        enEjecucion = true;

        if (hiloJuego == null || !hiloJuego.isAlive()) {
            hiloJuego = new Thread(this);
            hiloJuego.start();
        }

        requestFocusInWindow();
    }

    @Override
    public void run() {
        while (true) {
            if (enEjecucion && !juegoTerminado) {
                actualizarJuego();
                repaint();
            }

            try {
                Thread.sleep(RETARDO);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    private void actualizarJuego() {
        Posicion siguienteCabeza = serpiente.obtenerSiguienteCabeza();

        boolean chocaPared = siguienteCabeza.obtenerX() < 0
                || siguienteCabeza.obtenerX() >= COLUMNAS
                || siguienteCabeza.obtenerY() < 0
                || siguienteCabeza.obtenerY() >= FILAS;

        boolean comeraAlimento = siguienteCabeza.equals(alimento.obtenerPosicion());
        boolean chocaConsigoMisma = serpiente.chocaraConsigoMisma(siguienteCabeza, comeraAlimento);

        if (chocaPared || chocaConsigoMisma) {
            terminarJuego();
            return;
        }

        serpiente.mover(comeraAlimento);

        if (comeraAlimento) {
            puntuacion++;
            alimento.reaparecer(COLUMNAS, FILAS, serpiente);
        }
    }
    
    private void terminarJuego() {
        enEjecucion = false;
        juegoTerminado = true;
        repaint();

        SwingUtilities.invokeLater(() -> {
            String nombre = JOptionPane.showInputDialog(
                    this,
                    "Perdiste. Tu puntuación fue: " + puntuacion + ". Escribe tu nombre:",
                    "Fin del juego",
                    JOptionPane.PLAIN_MESSAGE
            );

            gestorPuntuaciones.agregarPuntuacion(nombre, puntuacion);
            repaint();
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        dibujarReticula(g);
        dibujarAlimento(g);
        dibujarSerpiente(g);
        dibujarInformacion(g);

        if (juegoTerminado) {
            dibujarJuegoTerminado(g);
        }
    }
    
    private void dibujarReticula(Graphics g) {
        g.setColor(new Color(35, 35, 35));

        for (int x = 0; x <= COLUMNAS; x++) {
            g.drawLine(x * TAMANO_CUADRO, 0, x * TAMANO_CUADRO, FILAS * TAMANO_CUADRO);
        }

        for (int y = 0; y <= FILAS; y++) {
            g.drawLine(0, y * TAMANO_CUADRO, COLUMNAS * TAMANO_CUADRO, y * TAMANO_CUADRO);
        }
    }

    private void dibujarSerpiente(Graphics g) {
        boolean esCabeza = true;

        for (Posicion parte : serpiente.obtenerCuerpo()) {
            if (esCabeza) {
                g.setColor(new Color(0, 220, 80));
                esCabeza = false;
            } else {
                g.setColor(new Color(0, 150, 60));
            }

            g.fillRect(
                    parte.obtenerX() * TAMANO_CUADRO,
                    parte.obtenerY() * TAMANO_CUADRO,
                    TAMANO_CUADRO,
                    TAMANO_CUADRO
            );

            g.setColor(Color.BLACK);
            g.drawRect(
                    parte.obtenerX() * TAMANO_CUADRO,
                    parte.obtenerY() * TAMANO_CUADRO,
                    TAMANO_CUADRO,
                    TAMANO_CUADRO
            );
        }
    }

    private void dibujarAlimento(Graphics g) {
    Posicion posicionAlimento = alimento.obtenerPosicion();
    g.setColor(Color.RED);
    g.fillOval(
            posicionAlimento.obtenerX() * TAMANO_CUADRO + 4,
            posicionAlimento.obtenerY() * TAMANO_CUADRO + 4,
            TAMANO_CUADRO - 8,
            TAMANO_CUADRO - 8
    );
    }

    private void dibujarInformacion(Graphics g) {
    int posicionY = FILAS * TAMANO_CUADRO + 20;

    g.setColor(Color.WHITE);
    g.setFont(new Font("Arial", Font.BOLD, 14));
    g.drawString("Puntuación: " + puntuacion, 10, posicionY);
    g.drawString("Enter para reiniciar", 10, posicionY + 25);

    dibujarMejoresPuntuaciones(g, 300, posicionY);
    }

    private void dibujarMejoresPuntuaciones(Graphics g, int x, int y) {
    g.setFont(new Font("Arial", Font.PLAIN, 12));
    g.drawString("Top 10:", x, y);

    int desplazamiento = 15;
    int indice = 1;

    for (RegistroPuntuacion registro : gestorPuntuaciones.obtenerMejoresPuntuaciones()) {
        g.drawString(
                indice + ". " + registro.obtenerNombre() + " - " + registro.obtenerPuntuacion(),
                x,
                y + desplazamiento
        );
        desplazamiento += 14;
        indice++;

        if (indice > 5) {
            break;
        	}
    	}
    }

    private void dibujarJuegoTerminado(Graphics g) {
    g.setColor(new Color(0, 0, 0, 180));
    g.fillRect(0, 0, COLUMNAS * TAMANO_CUADRO, FILAS * TAMANO_CUADRO);

    g.setColor(Color.WHITE);
    g.setFont(new Font("Arial", Font.BOLD, 30));
    g.drawString("Estrellaste la culebra xd", 80, 110);

    g.setFont(new Font("Arial", Font.PLAIN, 16));
    g.drawString("Presiona ENTER para jugar otra vez", 135, 140);
    }

    @Override
    public void keyPressed(KeyEvent e) {
    int tecla = e.getKeyCode();

    if (tecla == KeyEvent.VK_ENTER && juegoTerminado) {
        iniciarNuevoJuego();
        return;
    }

    if (tecla == KeyEvent.VK_UP || tecla == KeyEvent.VK_W) {
        serpiente.cambiarDireccion(Direccion.ARRIBA);
    } else if (tecla == KeyEvent.VK_DOWN || tecla == KeyEvent.VK_S) {
        serpiente.cambiarDireccion(Direccion.ABAJO);
    } else if (tecla == KeyEvent.VK_LEFT || tecla == KeyEvent.VK_A) {
        serpiente.cambiarDireccion(Direccion.IZQUIERDA);
    } else if (tecla == KeyEvent.VK_RIGHT || tecla == KeyEvent.VK_D) {
        serpiente.cambiarDireccion(Direccion.DERECHA);
    }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    // No se necesita para este juego.
    }

    @Override
    public void keyTyped(KeyEvent e) {
    // No se necesita para este juego.
    }
}