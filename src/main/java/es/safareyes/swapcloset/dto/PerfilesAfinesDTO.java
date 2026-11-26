package es.safareyes.swapcloset.dto;
import java.util.List;

public record PerfilesAfinesDTO (
        Integer idCoincidente,
        Integer idUsuario,
        String nombreCompleto,
        String ciudad,
        Long numEstilosComunes,
        Double reputacion,
        List<String> estilos
){ }
