public class RegistroPuntuacion {
    private String nombre;
    private int puntuacion;

    public RegistroPuntuacion(String nombre, int puntuacion) {
        this.nombre = nombre;
        this.puntuacion = puntuacion;
    }

    public String obtenerNombre() {
        return nombre;
    }

    public int obtenerPuntuacion() {
        return puntuacion;
    }

    public String convertirLineaArchivo() {
        String nombreLimpio = nombre.replace("|", " ").trim();
        return nombreLimpio + "|" + puntuacion;
    }

    public static RegistroPuntuacion desdeLineaArchivo(String linea) {
        String[] partes = linea.split("\\|", 2);

        if (partes.length != 2) {
            throw new IllegalArgumentException("Formato inválido en archivo de puntuaciones.");
        }

        String nombre = partes[0].trim();
        int puntuacion = Integer.parseInt(partes[1].trim());

        return new RegistroPuntuacion(nombre, puntuacion);
    }
}