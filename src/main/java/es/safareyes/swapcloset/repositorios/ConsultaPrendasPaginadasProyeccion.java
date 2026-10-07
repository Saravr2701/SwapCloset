package es.safareyes.swapcloset.repositorios;

import es.safareyes.swapcloset.modelos.CondicionPrenda;
import es.safareyes.swapcloset.modelos.EstadoPrenda;

import java.time.LocalDate;

public interface ConsultaPrendasPaginadasProyeccion {
    Integer getId();
    String getTitulo();
    String getDescripcion();
    String getMarca();
    String getTalla();
    String getCondicion();
    String getEstado();
    Boolean getPermite_intercambio();
    Boolean getPermite_prestamo();
    LocalDate getFecha_publicacion();
}
