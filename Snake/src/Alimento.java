import java.util.Random;

public class Alimento {
    private Posicion posicion;
    private Random azar;

    public Alimento() {
        azar = new Random();
    }

    public Posicion obtenerPosicion() {
        return posicion;
    }

    public void reaparecer(int columnas, int filas, Serpiente serpiente) {
        Posicion nuevaPosicion;

        do {
            int x = azar.nextInt(columnas);
            int y = azar.nextInt(filas);
            nuevaPosicion = new Posicion(x, y);
        } while (serpiente.contiene(nuevaPosicion));

        posicion = nuevaPosicion;
    }
}