package es.safareyes.swapcloset.dto;

import es.safareyes.swapcloset.modelos.EstadoSolicitud;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CambiarEstadoSolicitudDTO (
        @NotNull(message = "El id del perfil no puede ser nulo")
        @Positive(message = "El id del perfil debe ser un número positivo")
        Integer idPerfil,

        @NotNull(message = "El nuevo estado de la solicitud no puede ser nulo")
        EstadoSolicitud estado
){
}
