package es.safareyes.swapcloset.service;

import es.safareyes.swapcloset.dto.ModificarPerfilDTO;
import es.safareyes.swapcloset.dto.VisualizarPerfilDTO;

public interface IPerfilService {
    void modificarPerfil(Integer idUsuario, ModificarPerfilDTO modificarPerfilDTO);
    VisualizarPerfilDTO visualizarPerfil(Integer id);
}
