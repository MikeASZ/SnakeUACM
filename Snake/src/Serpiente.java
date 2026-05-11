import java.util.LinkedList;
import java.util.List;

public class Serpiente {
    private LinkedList<Posicion> cuerpo;
    private Direccion direccionActual;
    private Direccion direccionPendiente;

    public Serpiente(Posicion inicio) {
        cuerpo = new LinkedList<>();
        cuerpo.add(inicio);
        cuerpo.add(new Posicion(inicio.obtenerX() - 1, inicio.obtenerY()));
        cuerpo.add(new Posicion(inicio.obtenerX() - 2, inicio.obtenerY()));

        direccionActual = Direccion.DERECHA;
        direccionPendiente = Direccion.DERECHA;
    }

    public void cambiarDireccion(Direccion nuevaDireccion) {
        if (nuevaDireccion != null && !direccionActual.esOpuesta(nuevaDireccion)) {
            direccionPendiente = nuevaDireccion;
        }
    }

    public Direccion obtenerDireccion() {
        return direccionActual;
    }

    public Posicion obtenerCabeza() {
        return cuerpo.getFirst();
    }

    public List<Posicion> obtenerCuerpo() {
        return cuerpo;
    }

    public Posicion obtenerSiguienteCabeza() {
        return calcularCabezaDesdeDireccion(direccionPendiente);
    }

    public void mover(boolean crecer) {
        direccionActual = direccionPendiente;
        Posicion nuevaCabeza = calcularCabezaDesdeDireccion(direccionActual);
        cuerpo.addFirst(nuevaCabeza);

        if (!crecer) {
            cuerpo.removeLast();
        }
    }

    public boolean contiene(Posicion posicion) {
        return cuerpo.contains(posicion);
    }

    public boolean chocaraConsigoMisma(Posicion siguienteCabeza, boolean crecer) {
        int limite = crecer ? cuerpo.size() : cuerpo.size() - 1;

        for (int i = 0; i < limite; i++) {
            if (cuerpo.get(i).equals(siguienteCabeza)) {
                return true;
            }
        }

        return false;
    }

    private Posicion calcularCabezaDesdeDireccion(Direccion direccion) {
        Posicion cabeza = obtenerCabeza();
        int x = cabeza.obtenerX();
        int y = cabeza.obtenerY();

        switch (direccion) {
            case ARRIBA:
                y--;
                break;
            case ABAJO:
                y++;
                break;
            case IZQUIERDA:
                x--;
                break;
            case DERECHA:
                x++;
                break;
        }

        return new Posicion(x, y);
    }
}

