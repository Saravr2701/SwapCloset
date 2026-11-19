package es.safareyes.swapcloset.service;

import es.safareyes.swapcloset.dto.CrearSolicitudDTO;
import es.safareyes.swapcloset.exception.*;
import es.safareyes.swapcloset.mapper.SolicitudMapper;
import es.safareyes.swapcloset.modelos.*;
import es.safareyes.swapcloset.repositorios.IPerfilRepository;
import es.safareyes.swapcloset.repositorios.IPrendaRepository;
import es.safareyes.swapcloset.repositorios.ISolicitudPrendaRepository;
import es.safareyes.swapcloset.repositorios.ISolicitudRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class SolicitudService implements ISolicitudService{
    private SolicitudMapper solicitudMapper;
    private ISolicitudRepository solicitudRepository;
    private IPerfilRepository perfilRepository;
    private IPrendaRepository prendaRepository;
    private ISolicitudPrendaRepository solicitudPrendaRepository;

    @Transactional
    public void crearSolicitud(CrearSolicitudDTO dto){
        Solicitud nuevaSolicitud = solicitudMapper.crearSolicitud(dto);



        Perfil perfil = perfilRepository.findById(dto.idPerfil()).orElse(null);
        if (perfil == null){
            throw new DatoNoEncontrado("El perfil indicado no existe");
        }
        nuevaSolicitud.setPerfil(perfil);



        Prenda prendaSolicitada = prendaRepository.findById(dto.idPrendaSolicitada()).orElse(null);
        if (prendaSolicitada == null){
            throw new DatoNoEncontrado("La prenda solicitada no existe");
        }

        if (prendaSolicitada.getPerfil().getIdPerfil().equals(dto.idPerfil())){
            throw new SolicitudPrendaPropia("No puedes solicitar una prenda propia");
        }

        if (dto.tipoSolicitud() == TipoSolicitud.INTERCAMBIO && prendaSolicitada.getPermiteIntercambio() == false){
            throw new TipoSolicitudNoPermitido("Esta prenda no admite intercambios");
        }

        if (dto.tipoSolicitud() == TipoSolicitud.PRESTAMO && prendaSolicitada.getPermitePrestamo() == false){
            throw new TipoSolicitudNoPermitido("Esta prenda no admite prestamos");
        }

        if (prendaSolicitada.getEstado() != EstadoPrenda.DISPONIBLE) {
            throw new PrendaNoDisponible("La prenda solicitada no está disponible");
        }

        long solicitudPendiente = solicitudRepository.contarSolicitudesPendientes(dto.idPerfil(), prendaSolicitada.getIdPrenda(), RolPrenda.SOLICITADA, EstadoSolicitud.PENDIENTE);

        if(solicitudPendiente > 0){
            throw new SolicitudDuplicada("Ya has creado una solicitud para esta prenda, está pendiente de aprobación");
        }


        Prenda prendaOfrecida = null;
        if (dto.tipoSolicitud() == TipoSolicitud.INTERCAMBIO) {
            if (dto.idPrendaOfrecida() == null) {
                throw new DatoNoEncontrado("Debes seleccionar una prenda para ofrecer en el intercambio");
            }
            prendaOfrecida = prendaRepository.findById(dto.idPrendaOfrecida()).orElse(null);


            if (prendaOfrecida == null) {
                throw new DatoNoEncontrado("La prenda ofrecida no existe");
            }

            if (prendaOfrecida.getEstado() != EstadoPrenda.DISPONIBLE) {
                throw new PrendaNoDisponible("La prenda ofrecida no está disponible");
            }

            if (!prendaOfrecida.getPerfil().getIdPerfil().equals(dto.idPerfil())) {
                throw new SolicitudPrendaPropia("Debes ofrecer una prenda que te pertenezca");
            }
        }

        if (dto.fechaInicio().isBefore(LocalDate.now())) {
            throw new FechaNoValida("La fecha de inicio no puede ser anterior a la fecha actual");
        }

        if (dto.fechaFin().isBefore(dto.fechaInicio()) || dto.fechaInicio().equals(dto.fechaFin())) {
            throw new FechaNoValida("La fecha de fin no puede ser anterior ni igual a la fecha de inicio");
        }

        if (dto.tipoSolicitud().equals(TipoSolicitud.PRESTAMO)) {
            if (dto.fechaFin().isAfter(dto.fechaInicio().plusDays(30))){
                throw new DuracionNoValida("La duración de la solicitud no puede exceder de los 30 días");
            }

            long prestamosActivos = solicitudRepository
                    .countByPerfil_IdPerfilAndTipoSolicitudAndEstadoSolicitudIn(
                            dto.idPerfil(),
                            TipoSolicitud.PRESTAMO,
                            List.of(EstadoSolicitud.ACEPTADA,
                                    EstadoSolicitud.RETRASADA)

                    );
            if (prestamosActivos >= 3) {
                throw new PrestamosActivosExcedidos("No puedes tener más de 3 préstamos activos a la vez");
            }
        }

        nuevaSolicitud.setFechaCreacion(LocalDate.now());

        nuevaSolicitud.setEstadoSolicitud(EstadoSolicitud.PENDIENTE);

        solicitudRepository.save(nuevaSolicitud);


        SolicitudPrenda relacionSolicitada = new SolicitudPrenda();

        relacionSolicitada.setSolicitud(nuevaSolicitud);

        relacionSolicitada.setPrenda(prendaSolicitada);

        relacionSolicitada.setRolPrenda(RolPrenda.SOLICITADA);

        solicitudPrendaRepository.save(relacionSolicitada);


        if (dto.tipoSolicitud() == TipoSolicitud.INTERCAMBIO) {

            SolicitudPrenda relacionOfrecida = new SolicitudPrenda();

            relacionOfrecida.setSolicitud(nuevaSolicitud);
            relacionOfrecida.setPrenda(prendaOfrecida);
            relacionOfrecida.setRolPrenda(RolPrenda.OFRECIDA);

            solicitudPrendaRepository.save(relacionOfrecida);
        }
    }
}
