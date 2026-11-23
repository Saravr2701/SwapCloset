package es.safareyes.swapcloset.dto;

import jakarta.validation.constraints.*;

public record ValorarUsuarioDTO (
        @NotNull
        @Positive
        Integer idValorador,

        @Size(max = 255, message = "El comentario no puede tener más de 255 caracteres")
        String comentario,

        @Min(1) @Max(5)
        @NotNull(message = "La puntuación no puede ser nula")
        Integer puntuacion
){ }
