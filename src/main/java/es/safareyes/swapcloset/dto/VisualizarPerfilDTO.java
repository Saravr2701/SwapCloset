package es.safareyes.swapcloset.dto;

import es.safareyes.swapcloset.modelos.Estilo;

import java.util.List;

public record VisualizarPerfilDTO (
        String nombre,
        String ubicacion,
        String descripcion,
        Integer tallaZapatos,
        Integer tallaPantalon,
        String tallaCamiseta,
        List<String> estilos,
        Double reputacion,
        Integer numeroResenas,
        List<PrendasPerfilDTO> prendas
) { }
