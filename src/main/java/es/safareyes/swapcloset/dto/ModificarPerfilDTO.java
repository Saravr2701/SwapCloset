package es.safareyes.swapcloset.dto;

import es.safareyes.swapcloset.modelos.TallaCamiseta;
import es.safareyes.swapcloset.modelos.TallaPantalon;
import es.safareyes.swapcloset.modelos.TallaZapatos;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record ModificarPerfilDTO (
        String nombre,

        String ape1,

        String ape2,

        String ciudad,

        String biografia,

        TallaCamiseta tallaCamiseta,

        TallaPantalon tallaPantalon,

        TallaZapatos tallaZapatos,

        @Size(min = 1, max = 5, message = "Debes seleccionar entre 1 y 5 estilos")
        Set<@Positive Integer> idEstilos
) { }
