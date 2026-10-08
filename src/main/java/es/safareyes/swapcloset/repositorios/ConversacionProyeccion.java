package es.safareyes.swapcloset.repositorios;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ConversacionProyeccion {
    Integer getIdMensaje();
    Integer getIdSolicitud();
    Integer getIdEmisor();
    Integer getIdReceptor();
    String getReceptor();
    String getEmisor();
    String getContenido();
    LocalDate getFechaEnvio();
    LocalTime getHoraEnvio();
}
