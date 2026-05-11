public enum Direccion {
    ARRIBA,
    ABAJO,
    IZQUIERDA,
    DERECHA;

    public boolean esOpuesta(Direccion otra) {
        return (this == ARRIBA && otra == ABAJO)
                || (this == ABAJO && otra == ARRIBA)
                || (this == IZQUIERDA && otra == DERECHA)
                || (this == DERECHA && otra == IZQUIERDA);
    }
}