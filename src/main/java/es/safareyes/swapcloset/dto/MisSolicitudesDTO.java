package es.safareyes.swapcloset.dto;

public record MisSolicitudesDTO (
        Integer idSolicitud,
        String prendaOfrecida,
        String fotoPrendaOfrecida,
        String prendaSolicitada,
        String fotoPrendaSolicitada,
        String estadoSolicitud
){ }
