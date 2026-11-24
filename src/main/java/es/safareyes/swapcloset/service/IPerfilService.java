package es.safareyes.swapcloset.service;

import es.safareyes.swapcloset.dto.ModificarPerfilDTO;

public interface IPerfilService {
    void modificarPerfil(Integer idUsuario, ModificarPerfilDTO modificarPerfilDTO);
    void visualizarPerfil();
}
