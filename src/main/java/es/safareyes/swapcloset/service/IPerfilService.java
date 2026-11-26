package es.safareyes.swapcloset.service;

import es.safareyes.swapcloset.dto.ModificarPerfilDTO;
import es.safareyes.swapcloset.dto.PerfilesAfinesDTO;
import es.safareyes.swapcloset.dto.VisualizarPerfilDTO;

import java.util.List;

public interface IPerfilService {
    void modificarPerfil(Integer idUsuario, ModificarPerfilDTO modificarPerfilDTO);
    VisualizarPerfilDTO visualizarPerfil(Integer id);
    List<PerfilesAfinesDTO> perfilesAfines(Integer idUsuario);
}
