package es.safareyes.swapcloset.service;

import es.safareyes.swapcloset.dto.CambiarEstadoSolicitudDTO;
import es.safareyes.swapcloset.dto.CrearSolicitudDTO;

public interface ISolicitudService {
    public void crearSolicitud(CrearSolicitudDTO dto);
    public void cambiarEstadoSolicitud(Integer id, CambiarEstadoSolicitudDTO dto);
}
