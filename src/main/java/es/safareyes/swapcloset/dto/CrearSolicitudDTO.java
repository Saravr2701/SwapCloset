package es.safareyes.swapcloset.dto;

import es.safareyes.swapcloset.modelos.TipoSolicitud;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record CrearSolicitudDTO (
        @NotNull(message = "Es obligatorio indicar el perfil solicitante")
        @Positive(message = "El id del perfil solicitante debe ser positivo")
        Integer idPerfil,

        @NotNull(message = "El obligatorio solicitar una prenda")
        @Positive(message = "El id de la prenda solicitada debe ser positivo")
        Integer idPrendaSolicitada,

        @Positive(message = "El id de la prenda ofrecida debe ser positivo")
        Integer idPrendaOfrecida,

        @NotNull(message = "Debes indicar el tipo de solicitud")
        TipoSolicitud tipoSolicitud,

        @NotNull(message = "Debes indicar la fecha de inicio de la solicitud")
        LocalDate fechaInicio,

        @NotNull(message = "Debes indicar la fecha de fin de la solicitud")
        LocalDate fechaFin
)
{}
