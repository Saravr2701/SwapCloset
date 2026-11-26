package es.safareyes.swapcloset.service;

import es.safareyes.swapcloset.dto.ConversacionDTO;
import es.safareyes.swapcloset.dto.EnviarMensajeDTO;
import es.safareyes.swapcloset.exception.*;
import es.safareyes.swapcloset.mapper.MensajeMapper;
import es.safareyes.swapcloset.modelos.*;
import es.safareyes.swapcloset.repositorios.ConversacionProyeccion;
import es.safareyes.swapcloset.repositorios.IMensajeRepository;
import es.safareyes.swapcloset.repositorios.IPerfilRepository;
import es.safareyes.swapcloset.repositorios.ISolicitudRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
            throw new RelacionNoEncontrada("La solicitud de la prenda no existe");
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

    @Transactional
    public Page<ConversacionDTO> obtenerConversacion (Integer idSolicitud, Integer idUsuario, Integer page, Integer tamano){
        Solicitud solicitud = solicitudRepository.findById(idSolicitud).orElse(null);

        if (solicitud == null) {
            throw new SolicitudNoExiste("La solicitud indicada no existe");
        }

        Perfil perfil = perfilRepository.findByUsuario_IdUsuario(idUsuario)
                .orElse(null);

        if (perfil == null) {
            throw new RelacionNoEncontrada("El perfil indicado no existe");
        }

        if (page < 0) {
            throw new PaginacionNoValida("El número de página no puede ser negativo");
        }

        if (tamano < 1 || tamano > 100) {
            throw new PaginacionNoValida("El tamaño de página debe ser de entre 1 y 100");
        }

        Perfil solicitante = solicitud.getPerfil();

        SolicitudPrenda solicitudPrenda = null;

        for (SolicitudPrenda sp : solicitud.getSolicitudesPrendas()) {
            if (sp.getRolPrenda() == RolPrenda.SOLICITADA) {
                solicitudPrenda = sp;
                break;
            }
        }

        if (solicitudPrenda == null) {
            throw new SolicitudNoExiste("La solicitud de la prenda no existe");
        }

        Perfil propietario = solicitudPrenda.getPrenda().getPerfil();

        if (!perfil.getIdPerfil().equals(solicitante.getIdPerfil()) && !perfil.getIdPerfil().equals(propietario.getIdPerfil())) {
            throw new PerfilNoEstaEnLaSolicitud("No puedes consultar una conversación en la que no participas");
        }

        Pageable pageable = PageRequest.of(page, tamano);
        Page<ConversacionProyeccion> mensajes = mensajeRepository.obtenerConversaciones(idSolicitud, pageable);

        return mensajes.map(mensajeMapper::toDTO);
    }
}