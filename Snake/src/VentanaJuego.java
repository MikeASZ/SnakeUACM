import javax.swing.JFrame;

public class VentanaJuego extends JFrame {
    private PanelJuego panelJuego;

    public VentanaJuego() {
        panelJuego = new PanelJuego();

        setTitle("Snake Java");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(panelJuego);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}