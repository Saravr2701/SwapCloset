package es.safareyes.swapcloset.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record EnviarMensajeDTO (
    @NotNull(message = "El ID del emisor no puede estar vacío")
    @Positive(message = "El ID del emisor no puede ser negativo")
    Integer idEmisor,

    @NotBlank(message = "El contenido del mensaje no puede estar en blanco")
    @Size(max = 1000, message = "El mensaje contiene más de 1000 carácteres")
    String contenidoMensaje

)
{}

