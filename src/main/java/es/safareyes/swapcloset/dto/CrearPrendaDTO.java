package es.safareyes.swapcloset.dto;

import es.safareyes.swapcloset.modelos.Color;
import es.safareyes.swapcloset.modelos.CondicionPrenda;
import jakarta.validation.constraints.*;

public record CrearPrendaDTO(
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 100)
        String titulo,

        @NotBlank(message = "La descripción es obligatoria")
        @Size(max = 500)
        String descripcion,

        @Size(max = 150)
        String marca,

        @Size(max = 10)
        String talla,

        @NotNull(message = "La condición es obligatoria")
        CondicionPrenda condicion,

        @NotNull(message = "El intercambio es obligatorio")
        Boolean permiteIntercambio,

        @NotNull(message = "El préstamo es obligatorio")
        Boolean permitePrestamo,

        @NotNull(message = "El color es obligatorio")
        Color color,

        @NotNull(message = "La categoría es obligatoria")
        @Positive(message = "El id de la categoría debe ser positivo")
        Integer idCategoria,

        @NotNull(message = "El perfil es obligatorio")
        @Positive(message = "El id del perfil debe ser positivo")
        Integer idPerfil
) {}
