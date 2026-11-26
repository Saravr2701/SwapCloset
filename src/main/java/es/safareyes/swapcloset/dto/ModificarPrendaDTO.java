package es.safareyes.swapcloset.dto;

import es.safareyes.swapcloset.modelos.Color;
import es.safareyes.swapcloset.modelos.CondicionPrenda;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ModificarPrendaDTO (
        @NotNull(message = "El id del usuario es obligatorio")
        @Positive(message = "El id del usuario debe ser un número positivo")
        Integer idUsuario,

        String titulo,

        String descripcion,

        String marca,

        String talla,

        CondicionPrenda condicion,

        Color color,

        Boolean permiteIntercambio,

        Boolean permitePrestamo,

        @Positive(message = "El id de la categoría debe ser un número positivo")
        Integer idCategoria
) { }
