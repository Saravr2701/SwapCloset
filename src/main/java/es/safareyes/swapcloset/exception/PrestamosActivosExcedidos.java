package es.safareyes.swapcloset.exception;

public class PrestamosActivosExcedidos extends RuntimeException {
    public PrestamosActivosExcedidos(String message) {
        super(message);
    }
}
