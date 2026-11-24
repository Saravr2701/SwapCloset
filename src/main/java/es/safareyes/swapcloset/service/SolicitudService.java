package es.safareyes.swapcloset.service;

import es.safareyes.swapcloset.dto.CambiarEstadoSolicitudDTO;
import es.safareyes.swapcloset.dto.CrearSolicitudDTO;
import es.safareyes.swapcloset.dto.MisSolicitudesDTO;
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
import java.util.ArrayList;
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

        if (Boolean.FALSE.equals(prendaSolicitada.getActivo())) {
            throw new PrendaNoDisponible("La prenda solicitada está desactivada");
        }

        long solicitudPendiente = solicitudRepository.contarSolicitudesPendientes(dto.idPerfil(), prendaSolicitada.getIdPrenda(), RolPrenda.SOLICITADA, EstadoSolicitud.PENDIENTE);

        if(solicitudPendiente > 0){
            throw new SolicitudDuplicada("Ya has creado una solicitud para esta prenda, está pendiente de aprobación");
        }

        if(dto.tipoSolicitud() == TipoSolicitud.PRESTAMO && dto.idPrendaOfrecida() != null) {
            throw new TipoSolicitudNoPermitido("En un préstamo no se ofrecen prendas");
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

            if (Boolean.FALSE.equals(prendaOfrecida.getActivo())) {
                throw new PrendaNoDisponible("La prenda ofrecida está desactivada");
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

    @Transactional
    public void cambiarEstadoSolicitud(Integer id, CambiarEstadoSolicitudDTO dto) {
        Solicitud nuevaSolicitud = solicitudRepository.findById(id).orElse(null);
        if (nuevaSolicitud == null){
            throw new SolicitudNoExiste("La solicitud no existe");
        }

        SolicitudPrenda nuevaSolicitudPrenda = solicitudPrendaRepository.findBySolicitud_IdSolicitudAndRolPrenda(id,RolPrenda.SOLICITADA);
        SolicitudPrenda prendaOfrecida = solicitudPrendaRepository.findBySolicitud_IdSolicitudAndRolPrenda(id, RolPrenda.OFRECIDA);
        Perfil solicitante;
        Perfil propietario;

        if (nuevaSolicitud.getPerfil().getIdPerfil().equals(dto.idPerfil())){
            solicitante = nuevaSolicitud.getPerfil();
            if (nuevaSolicitud.getEstadoSolicitud() != EstadoSolicitud.PENDIENTE){
                throw new EstadoNoValido("Esta solicitud no admite cambios de estado");
            }

            if (dto.estado() != EstadoSolicitud.CANCELADA){
                throw new EstadoNoValido("El nuevo estado no es válido");
            }
        }else if (dto.idPerfil().equals(nuevaSolicitudPrenda.getPrenda().getPerfil().getIdPerfil())){
            propietario = nuevaSolicitudPrenda.getPrenda().getPerfil();

            if (nuevaSolicitud.getEstadoSolicitud().equals(EstadoSolicitud.PENDIENTE)){
                if (dto.estado() != EstadoSolicitud.ACEPTADA && dto.estado() != EstadoSolicitud.RECHAZADA){
                    throw new EstadoNoValido("El nuevo estado no es válido");
                }

                if (dto.estado().equals(EstadoSolicitud.ACEPTADA)
                    && nuevaSolicitud.getTipoSolicitud().equals(TipoSolicitud.INTERCAMBIO)){

                    if (nuevaSolicitudPrenda.getPrenda().getEstado() != EstadoPrenda.DISPONIBLE) {
                        throw new PrendaNoDisponible("La prenda solicitada no está disponible");
                    }

                    //Reservamos tanto la prenda solicitada como la ofrecida
                    nuevaSolicitudPrenda.getPrenda().setEstado(EstadoPrenda.RESERVADA);


                    if (prendaOfrecida == null){
                        throw new RelacionNoEncontrada("No se ha encontrado la prenda ofrecida");
                    }

                    if (prendaOfrecida.getPrenda().getEstado() != EstadoPrenda.DISPONIBLE) {
                        throw new PrendaNoDisponible("La prenda ofrecida no está disponible");
                    }

                    prendaOfrecida.getPrenda().setEstado(EstadoPrenda.RESERVADA);

                }

                if (dto.estado().equals(EstadoSolicitud.ACEPTADA)
                    && nuevaSolicitud.getTipoSolicitud().equals(TipoSolicitud.PRESTAMO)) {
                    if (nuevaSolicitudPrenda.getPrenda().getEstado() != (EstadoPrenda.DISPONIBLE )){
                        throw new PrendaNoDisponible("La prenda no está disponible");
                    }

                    nuevaSolicitudPrenda.getPrenda().setEstado(EstadoPrenda.PRESTADA);
                }

                nuevaSolicitud.setFechaRespuesta(LocalDate.now());

            }else if (nuevaSolicitud.getEstadoSolicitud().equals(EstadoSolicitud.ACEPTADA) ||
                      nuevaSolicitud.getEstadoSolicitud().equals(EstadoSolicitud.RETRASADA)){
                if (dto.estado() != EstadoSolicitud.FINALIZADA){
                    throw new EstadoNoValido("El nuevo estado no es válido");
                }

                if (nuevaSolicitud.getTipoSolicitud().equals(TipoSolicitud.INTERCAMBIO)){
                    prendaOfrecida.getPrenda().setEstado(EstadoPrenda.DISPONIBLE);
                }

                nuevaSolicitudPrenda.getPrenda().setEstado(EstadoPrenda.DISPONIBLE);
            }else {
                throw new EstadoNoValido("El estado de la solicitud no permite cambios");
            }
        }else {
            throw new PerfilNoEstaEnLaSolicitud("El perfil no corresponde al solicitante ni al solicitado");
        }

        if (dto.estado().equals(EstadoSolicitud.ACEPTADA)) {
            solicitudPrendaRepository.solicitudEnPendiente(nuevaSolicitudPrenda.getPrenda().getIdPrenda(), id);
            if (nuevaSolicitud.getTipoSolicitud() == TipoSolicitud.INTERCAMBIO) {
                solicitudPrendaRepository.solicitudEnPendiente(prendaOfrecida.getPrenda().getIdPrenda(), id);
            }
        }

        nuevaSolicitud.setEstadoSolicitud(dto.estado());
        solicitudRepository.save(nuevaSolicitud);
    }

    @Transactional(readOnly = true)
    public List<MisSolicitudesDTO> misSolicitudesDTO(Integer idUsuario, String bandeja, EstadoSolicitud estado){
        Perfil perfil = perfilRepository.findByUsuario_IdUsuario(idUsuario).orElse(null);

        if (perfil == null) {
            throw new RelacionNoEncontrada("El perfil indicado no existe");
        }

        List<Solicitud> solicitudes;


        if (bandeja.equalsIgnoreCase("enviadas")){
            solicitudes = solicitudRepository.findByPerfilIdPerfil(perfil.getIdPerfil());
        }else if (bandeja.equalsIgnoreCase("recibidas")){
            solicitudes = solicitudRepository.obtenerSolicitudesRecibidas(perfil.getIdPerfil(), RolPrenda.SOLICITADA);
        }else {
            throw new BandejaNoValida("La bandeja indicada no es válida, introduce enviadas o recibidas");
        }

        List<MisSolicitudesDTO> resultado = new ArrayList<>();

        for (Solicitud solicitud : solicitudes) {
            if (estado != null && solicitud.getEstadoSolicitud() != estado) {
                continue;
            }

            SolicitudPrenda solicitada = solicitudPrendaRepository.findBySolicitud_IdSolicitudAndRolPrenda(solicitud.getIdSolicitud(), RolPrenda.SOLICITADA);

            if (solicitada == null) {
                throw new RelacionNoEncontrada("No se ha encontrado la prenda solicitada");
            }

            SolicitudPrenda ofrecida = solicitudPrendaRepository.findBySolicitud_IdSolicitudAndRolPrenda(solicitud.getIdSolicitud(), RolPrenda.OFRECIDA);

            Prenda prendaSolicitada = solicitada.getPrenda();

            List<String> fotosSolicitada =
                    prendaRepository.fotos(prendaSolicitada.getIdPrenda());

            String fotoSolicitada = null;

            if (!fotosSolicitada.isEmpty()) {
                fotoSolicitada = fotosSolicitada.get(0);
            }

            String nombreOfrecida = null;
            String fotoOfrecida = null;

            if (ofrecida != null) {
                Prenda prendaOfrecida = ofrecida.getPrenda();

                nombreOfrecida = prendaOfrecida.getTitulo();

                List<String> fotosOfrecida =
                        prendaRepository.fotos(prendaOfrecida.getIdPrenda());

                if (!fotosOfrecida.isEmpty()) {
                    fotoOfrecida = fotosOfrecida.get(0);
                }
            }

            MisSolicitudesDTO dto = new MisSolicitudesDTO(
                    solicitud.getIdSolicitud(),
                    nombreOfrecida,
                    fotoOfrecida,
                    prendaSolicitada.getTitulo(),
                    fotoSolicitada,
                    solicitud.getEstadoSolicitud().name()
            );

            resultado.add(dto);
        }

        return resultado;
    }
}
