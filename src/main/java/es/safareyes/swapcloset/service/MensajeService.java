package es.safareyes.swapcloset.service;

import es.safareyes.swapcloset.dto.EnviarMensajeDTO;
import es.safareyes.swapcloset.exception.Inexistente;
import es.safareyes.swapcloset.exception.NoDisponible;
import es.safareyes.swapcloset.exception.PerfilNoEstaEnLaSolicitud;
import es.safareyes.swapcloset.exception.SolicitudNoExiste;
import es.safareyes.swapcloset.mapper.MensajeMapper;
import es.safareyes.swapcloset.modelos.*;
import es.safareyes.swapcloset.repositorios.IMensajeRepository;
import es.safareyes.swapcloset.repositorios.IPerfilRepository;
import es.safareyes.swapcloset.repositorios.ISolicitudRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@AllArgsConstructor
@Slf4j
public class MensajeService implements IMensajeService{
    private ISolicitudRepository solicitudRepository;
    private IPerfilRepository perfilRepository;
    private MensajeMapper mensajeMapper;
    private IMensajeRepository mensajeRepository;

    @Transactional
    public void enviarMensaje(Integer idSolicitud, EnviarMensajeDTO dto){
        Solicitud solicitud = solicitudRepository.findById(idSolicitud).orElse(null);

        if (solicitud == null){
            throw new Inexistente("La solicitud de la prenda no existe");
        }

        if (solicitud.getEstadoSolicitud().equals(EstadoSolicitud.RECHAZADA) ||
                solicitud.getEstadoSolicitud().equals(EstadoSolicitud.CADUCADA) ||
                solicitud.getEstadoSolicitud().equals(EstadoSolicitud.CANCELADA)){
            throw new NoDisponible("Debido al estado de la solicitud esta conversación no está disponible");
        }

        Perfil perfilEmisor = perfilRepository.findById(dto.idEmisor()).orElse(null);

        if (perfilEmisor == null){
            throw new Inexistente("El perfil del emisor no existe");
        }

        Perfil solicitante = solicitud.getPerfil();

        SolicitudPrenda solicitudPrenda = null;

        for (SolicitudPrenda sp : solicitud.getSolicitudesPrendas()) {
            if (sp.getRolPrenda() == RolPrenda.SOLICITADA) {
                solicitudPrenda = sp;
                break;
            }
        }

        if(solicitudPrenda == null){
            throw new SolicitudNoExiste("La solicitud de la prenda no existe");
        }

        Perfil propietario = solicitudPrenda.getPrenda().getPerfil();

        Perfil perfilReceptor;

        if (perfilEmisor.getIdPerfil().equals(solicitante.getIdPerfil())) {
            perfilReceptor = propietario;
        } else if (perfilEmisor.getIdPerfil().equals(propietario.getIdPerfil())) {
            perfilReceptor = solicitante;
        } else {
            throw new PerfilNoEstaEnLaSolicitud("El emisor no participa en esta conversación");
        }

        Mensaje nuevoMensaje = mensajeMapper.enviarMensaje(dto);

        nuevoMensaje.setFechaEnvio(LocalDate.now());

        nuevoMensaje.setHoraEnvio(LocalTime.now());

        nuevoMensaje.getEmisores().add(perfilEmisor);

        nuevoMensaje.getReceptores().add(perfilReceptor);

        nuevoMensaje.setSolicitud(solicitud);

        mensajeRepository.save(nuevoMensaje);
    }
}