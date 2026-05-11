import javax.swing.SwingUtilities;

public class AplicacionSnake {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaJuego());
    }
}