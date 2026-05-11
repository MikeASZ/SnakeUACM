import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GestorPuntuaciones {
    private Path archivoPuntuaciones;
    private List<RegistroPuntuacion> puntuaciones;

    public GestorPuntuaciones(String nombreArchivo) {
        archivoPuntuaciones = Paths.get(nombreArchivo);
        puntuaciones = new ArrayList<>();
        cargarPuntuaciones();
    }

    public void cargarPuntuaciones() {
        puntuaciones.clear();

        if (!Files.exists(archivoPuntuaciones)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(archivoPuntuaciones)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                try {
                    puntuaciones.add(RegistroPuntuacion.desdeLineaArchivo(linea));
                } catch (IllegalArgumentException ex) {
                    System.err.println("Línea ignorada en puntuaciones: " + linea);
                }
            }

            ordenarYLimitarPuntuaciones();
        } catch (IOException ex) {
            System.err.println("Error al leer puntuaciones: " + ex.getMessage());
        }
    }

    public void guardarPuntuaciones() {
        try (BufferedWriter escritor = Files.newBufferedWriter(archivoPuntuaciones)) {
            for (RegistroPuntuacion registro : puntuaciones) {
                escritor.write(registro.convertirLineaArchivo());
                escritor.newLine();
            }
        } catch (IOException ex) {
            System.err.println("Error al guardar puntuaciones: " + ex.getMessage());
        }
    }

    public void agregarPuntuacion(String nombre, int puntuacion) {
        if (nombre == null || nombre.trim().isEmpty()) {
            nombre = "Jugador";
        }

        puntuaciones.add(new RegistroPuntuacion(nombre.trim(), puntuacion));
        ordenarYLimitarPuntuaciones();
        guardarPuntuaciones();
    }

    public List<RegistroPuntuacion> obtenerMejoresPuntuaciones() {
        return new ArrayList<>(puntuaciones);
    }

    private void ordenarYLimitarPuntuaciones() {
        puntuaciones.sort(Comparator.comparingInt(RegistroPuntuacion::obtenerPuntuacion).reversed());

        while (puntuaciones.size() > 10) {
            puntuaciones.remove(puntuaciones.size() - 1);
        }
    }
}