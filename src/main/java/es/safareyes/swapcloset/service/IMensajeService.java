package es.safareyes.swapcloset.service;

import es.safareyes.swapcloset.dto.EnviarMensajeDTO;

public interface IMensajeService {
    public void enviarMensaje(Integer idSolicitud, EnviarMensajeDTO dto);
}
