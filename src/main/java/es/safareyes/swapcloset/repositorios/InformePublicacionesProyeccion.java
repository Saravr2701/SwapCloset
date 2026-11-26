package es.safareyes.swapcloset.repositorios;

public interface InformePublicacionesProyeccion {
    String getMes();
    String getCategoria();
    Long getPublicaciones();
    Long getIntercambios();
    Long getPrestamos();
}