package es.safareyes.swapcloset.service;

import es.safareyes.swapcloset.dto.ValorarUsuarioDTO;
import es.safareyes.swapcloset.exception.*;
import es.safareyes.swapcloset.mapper.ValoracionMapper;
import es.safareyes.swapcloset.modelos.*;
import es.safareyes.swapcloset.repositorios.ISolicitudPrendaRepository;
import es.safareyes.swapcloset.repositorios.ISolicitudRepository;
import es.safareyes.swapcloset.repositorios.IValoracionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@AllArgsConstructor
@Slf4j
public class ValoracionService implements IValoracionService{
    private IValoracionRepository valoracionRepository;
    private ValoracionMapper valoracionMapper;
    private ISolicitudRepository solicitudRepository;

    @Transactional
    public void valorarUsuario(Integer idSolicitud, ValorarUsuarioDTO dto){
        Valoracion nuevaValoracion = valoracionMapper.valorarUsuario(dto);

        Solicitud solicitud = solicitudRepository.findById(idSolicitud).orElse(null);

        if(solicitud == null){
            throw new SolicitudNoExiste("No existe una solicitud a valorar");
        }

        if(solicitud.getEstadoSolicitud() == EstadoSolicitud.RECHAZADA){
            throw new SolicitudRechazada("No se puede valorar una solicitud rechazada");
        }

        if(solicitud.getEstadoSolicitud() != EstadoSolicitud.FINALIZADA){
            throw new SolicitudEnCurso("No se puede valorar una solicitud que no ha finalizado");
        }

        if(dto.idValorador() == null){
            throw new ValoracionNoValida("No se ha indicado el valorador");
        }

        SolicitudPrenda solicitudPrenda = null;

        for (SolicitudPrenda sp : solicitud.getSolicitudesPrendas()) {
            if (sp.getRolPrenda() == RolPrenda.SOLICITADA) {
                solicitudPrenda = sp;
                break;
            }
        }

        if (solicitudPrenda == null) {
                throw new RelacionNoEncontrada("No se encontró la prenda solicitada");
        }

        Perfil perfilValorado;

        if (dto.idValorador().equals(solicitud.getPerfil().getIdPerfil())) {
            perfilValorado = solicitudPrenda.getPrenda().getPerfil();
        } else if (dto.idValorador().equals(solicitudPrenda.getPrenda().getPerfil().getIdPerfil())) {
            perfilValorado = solicitud.getPerfil();
        } else {
            throw new PerfilNoEstaEnLaSolicitud("El perfil no participa en esta solicitud");
        }

        boolean yaValorado = valoracionRepository.existsBySolicitud_IdSolicitudAndPerfil_IdPerfil(idSolicitud, perfilValorado.getIdPerfil());

        if (yaValorado == true) {
            throw new ValoracionNoValida("Ya has valorado a esta persona en esta solicitud");
        }

        nuevaValoracion.setSolicitud(solicitud);

        nuevaValoracion.setFechaValoracion(LocalDate.now());

        nuevaValoracion.setPerfil(perfilValorado);

        valoracionRepository.save(nuevaValoracion);
    }
}
