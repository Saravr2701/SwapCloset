package es.safareyes.swapcloset.dto;

import es.safareyes.swapcloset.modelos.Color;
import es.safareyes.swapcloset.modelos.EstadoPrenda;

import java.util.List;

public record MostrarDetallesPrendasDTO (
        String titulo,
        String descripcion,
        String marca,
        String talla,
        EstadoPrenda estado,
        Color color,
        List<String> fotos,
        DetallesUsuarioPrendaDTO usuario
){
}
