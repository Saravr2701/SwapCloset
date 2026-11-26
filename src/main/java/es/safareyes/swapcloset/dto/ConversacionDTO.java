package es.safareyes.swapcloset.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ConversacionDTO (
        Integer idMensaje,
        Integer idSolicitud,
        Integer idEmisor,
        Integer idReceptor,
        String emisor,
        String receptor,
        String contenido,
        LocalDate fechaEnvio,
        LocalTime horaEnvio
){ }
