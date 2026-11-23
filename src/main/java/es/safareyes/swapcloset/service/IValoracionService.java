package es.safareyes.swapcloset.service;

import es.safareyes.swapcloset.dto.ValorarUsuarioDTO;
import es.safareyes.swapcloset.modelos.Solicitud;

public interface IValoracionService {
    public void valorarUsuario(Integer idSolicitud, ValorarUsuarioDTO valorarUsuarioDTO);
}
