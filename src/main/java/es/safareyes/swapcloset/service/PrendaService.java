package es.safareyes.swapcloset.service;

import es.safareyes.swapcloset.dto.DetallesUsuarioPrendaDTO;
import es.safareyes.swapcloset.dto.ModificarPrendaDTO;
import es.safareyes.swapcloset.dto.MostrarDetallesPrendasDTO;
import es.safareyes.swapcloset.exception.BajaNoPermitida;
import es.safareyes.swapcloset.exception.PrendaNoDisponible;
import es.safareyes.swapcloset.exception.RelacionNoEncontrada;
import es.safareyes.swapcloset.exception.SinTipoSolicitud;
import es.safareyes.swapcloset.mapper.PrendaMapper;
import es.safareyes.swapcloset.dto.CrearPrendaDTO;
import es.safareyes.swapcloset.modelos.*;
import es.safareyes.swapcloset.repositorios.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static java.lang.Boolean.FALSE;

@Service
@AllArgsConstructor
@Slf4j
public class PrendaService implements IPrendaService{

    private PrendaMapper prendaMapper;
    private IPrendaRepository prendaRepository;
    private ICategoriaRepository categoriaRepository;
    private IPerfilRepository perfilRepository;
    private ISolicitudPrendaRepository solicitudPrendaRepository;

    public void crearPrenda(CrearPrendaDTO dto){
        Prenda nuevaPrenda = prendaMapper.toEntity(dto);

        if (dto.permiteIntercambio().equals(false) && dto.permitePrestamo().equals(false)){
            throw new SinTipoSolicitud("La prenda necesita al menos un tipo de solicitud para ser creada");
        }

        Categoria categoria = categoriaRepository
                .findById(dto.idCategoria())
                .orElse(null);

        if (categoria == null){
            throw new RelacionNoEncontrada("La categoria introducida no existe");
        }
        nuevaPrenda.setCategoria(categoria);



        Perfil perfil = perfilRepository
                .findById(dto.idPerfil())
                .orElse(null);

        if (perfil == null){
            throw new RelacionNoEncontrada("El perfil indicado no existe");
        }
        nuevaPrenda.setPerfil(perfil);

        nuevaPrenda.setFechaPublicacion(LocalDate.now());

        nuevaPrenda.setEstado(EstadoPrenda.BORRADOR);

        nuevaPrenda.setActivo(true);
        
        prendaRepository.save(nuevaPrenda);
    }

    @Transactional
    public void eliminarPrendaLogico(Integer id, Integer idUsuario){
        Prenda nuevaPrenda = prendaRepository.findById(id).orElse(null);
        if(nuevaPrenda == null){
            throw new RelacionNoEncontrada("La prenda indicada no existe");
        }

        if(nuevaPrenda.getEstado().equals(EstadoPrenda.RESERVADA)){
            throw new BajaNoPermitida("La prenda indicada no se puede dar de baja porque está reservada");
        }

        if(nuevaPrenda.getEstado().equals(EstadoPrenda.PRESTADA)){
            throw new BajaNoPermitida("La prenda indicada no se puede dar de baja porque está prestada");
        }

        if(!nuevaPrenda.getPerfil().getUsuario().getIdUsuario().equals(idUsuario)){
            throw new BajaNoPermitida("La prenda solo puede ser eliminada por el usuario propietario");
        }

        if (FALSE.equals(nuevaPrenda.getActivo())) {
            throw new BajaNoPermitida("La prenda ya está dada de baja");
        }

        nuevaPrenda.setActivo(false);

        prendaRepository.save(nuevaPrenda);

        prendaRepository.cancelarSolicitudesPendientes(id);

    }

    @Transactional
    public void modificarPrenda(Integer id, ModificarPrendaDTO dto){
        Prenda prenda = prendaRepository.findById(id).orElse(null);
        if(prenda == null){
            throw new RelacionNoEncontrada("La prenda indicada no existe");
        }

        if(!prenda.getPerfil().getUsuario().getIdUsuario().equals(dto.idUsuario())){
            throw new BajaNoPermitida("La prenda solo puede ser modificada por el usuario propietario");
        }

        if (dto.titulo() != null && !dto.titulo().isEmpty()){
            prenda.setTitulo(dto.titulo());
        }

        if (dto.descripcion() != null && !dto.descripcion().isEmpty()){
            prenda.setDescripcion(dto.descripcion());
        }

        if (dto.marca() != null && !dto.marca().isEmpty()){
            prenda.setMarca(dto.marca());
        }

        if (dto.talla() != null && !dto.talla().isEmpty()){
            prenda.setTalla(dto.talla());
        }

        if (dto.condicion() != null){
            prenda.setCondicion(dto.condicion());
        }

        if (dto.permiteIntercambio() != null){
            prenda.setPermiteIntercambio(dto.permiteIntercambio());
        }

        if (dto.permitePrestamo() != null){
            prenda.setPermitePrestamo(dto.permitePrestamo());
        }

        if (prenda.getPermiteIntercambio().equals(FALSE) &&
            prenda.getPermitePrestamo().equals(FALSE)) {
            throw new SinTipoSolicitud("La prenda necesita al menos un tipo de solicitud");
        }

        if (dto.idCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(dto.idCategoria())
                    .orElse(null);
            if (categoria == null) {
                throw new RelacionNoEncontrada("La categoría indicada no existe");
            }
            prenda.setCategoria(categoria);
        }

        prendaRepository.save(prenda);
    }

    @Transactional(readOnly = true)
    public MostrarDetallesPrendasDTO mostrarDetallesPrenda(Integer id){
        Prenda prenda = prendaRepository.findById(id).orElse(null);

        if (prenda == null){
            throw new RelacionNoEncontrada("La prenda indicada no existe");
        }

        if(prenda.getActivo().equals(FALSE)){
            throw new PrendaNoDisponible("La prenda está dada de baja");
        }

        if (prenda.getEstado() == EstadoPrenda.BORRADOR){
            throw new PrendaNoDisponible("La prenda está en borrador asi que sus datos no son visibles");
        }

        List<String> fotos = prendaRepository.fotos(id);

        Perfil perfil = prenda.getPerfil();

        int numeroResenas = perfil.getValoraciones().size();
        double suma = 0.0;

        for (Valoracion valoracion : perfil.getValoraciones()) {
            suma += valoracion.getPuntuacion();
        }

        Double reputacion = null;
        if (numeroResenas > 0) {
            reputacion = Math.round((suma / numeroResenas) * 10.0) / 10.0;
        }

        DetallesUsuarioPrendaDTO usuario = new DetallesUsuarioPrendaDTO(
                perfil.getUsuario().getNombreusu(),
                perfil.getCiudad(),
                reputacion,
                numeroResenas
        );

        MostrarDetallesPrendasDTO dto = new MostrarDetallesPrendasDTO(
                prenda.getTitulo(),
                prenda.getDescripcion(),
                prenda.getMarca(),
                prenda.getTalla(),
                prenda.getEstado(),
                prenda.getColor(),
                fotos,
                usuario
        );
        return dto;
    }
}
