import java.util.Objects;

public class Posicion {
    private int x;
    private int y;

    public Posicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int obtenerX() {
        return x;
    }

    public int obtenerY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Posicion)) {
            return false;
        }

        Posicion otra = (Posicion) obj;
        return x == otra.x && y == otra.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}