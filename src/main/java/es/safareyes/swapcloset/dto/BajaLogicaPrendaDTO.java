package es.safareyes.swapcloset.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BajaLogicaPrendaDTO (
        @NotNull(message = "El id del usuario es obligatorio")
        @Positive(message = "El id del usuario debe ser un número positivo")
        Integer idUsuario
){ }
