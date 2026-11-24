package es.safareyes.swapcloset.dto;

public record DetallesUsuarioPrendaDTO (
        String nombreUsuario,
        String ubicacion,
        Double reputacion,
        Integer numeroResenas
) {
}
