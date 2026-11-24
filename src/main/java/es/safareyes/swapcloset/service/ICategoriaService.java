package es.safareyes.swapcloset.service;

import es.safareyes.swapcloset.dto.MostrarNumPrendasPorCategoria;

import java.util.List;

public interface ICategoriaService {
    List<MostrarNumPrendasPorCategoria> obtenerCantidadCategoria();
}
