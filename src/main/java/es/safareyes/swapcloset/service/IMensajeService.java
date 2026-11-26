package es.safareyes.swapcloset.service;

import es.safareyes.swapcloset.dto.ConversacionDTO;
import es.safareyes.swapcloset.dto.EnviarMensajeDTO;
import org.springframework.data.domain.Page;

public interface IMensajeService {
    void enviarMensaje(Integer idSolicitud, EnviarMensajeDTO dto);
    Page<ConversacionDTO> obtenerConversacion(Integer idSolicitud, Integer idUsuario, Integer page, Integer tamano);
}
