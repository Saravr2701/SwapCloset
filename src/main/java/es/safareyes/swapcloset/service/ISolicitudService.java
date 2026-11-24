package es.safareyes.swapcloset.service;

import es.safareyes.swapcloset.dto.CambiarEstadoSolicitudDTO;
import es.safareyes.swapcloset.dto.CrearSolicitudDTO;
import es.safareyes.swapcloset.dto.MisSolicitudesDTO;
import es.safareyes.swapcloset.modelos.EstadoSolicitud;

import java.util.List;

public interface ISolicitudService {
    public void crearSolicitud(CrearSolicitudDTO dto);
    public void cambiarEstadoSolicitud(Integer id, CambiarEstadoSolicitudDTO dto);
    public List<MisSolicitudesDTO> misSolicitudesDTO(Integer idUsuario, String bandeja, EstadoSolicitud estado);
}
