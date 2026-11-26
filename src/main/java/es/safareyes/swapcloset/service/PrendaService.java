package es.safareyes.swapcloset.service;

import es.safareyes.swapcloset.dto.*;
import es.safareyes.swapcloset.exception.*;
import es.safareyes.swapcloset.mapper.PrendaMapper;
import es.safareyes.swapcloset.modelos.*;
import es.safareyes.swapcloset.repositorios.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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
    private IUsuariosRepository usuariosRepository;

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

        if(nuevaPrenda.getColor() == null){
            throw new SinColor("La prenda necesita un color para poder ser creada");
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

        if (dto.color() != null){
            prenda.setColor(dto.color());
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
        Prenda prenda = prendaRepository.findByIdPrenda(id).orElse(null);

        if (prenda == null){
            throw new RelacionNoEncontrada("La prenda indicada no existe");
        }

        if(prenda.getActivo().equals(FALSE)){
            throw new PrendaNoDisponible("La prenda está dada de baja");
        }

        if (prenda.getEstado() == EstadoPrenda.BORRADOR){
            throw new PrendaNoDisponible("La prenda está en borrador asi que sus datos no son visibles");
        }

        if (prenda.getEstado() == EstadoPrenda.RETIRADA) {
            throw new PrendaNoDisponible("La prenda está retirada y sus datos no son visibles");
        }

        List<Foto> fotosOrdenadas = new ArrayList<>(prenda.getFotos());

        fotosOrdenadas.sort(
                Comparator.comparing(
                        Foto::getOrden,
                        Comparator.nullsLast(Comparator.naturalOrder())
                )
        );

        List<String> fotos = new ArrayList<>();

        for (Foto foto : fotosOrdenadas) {
            fotos.add(foto.getUrlFoto());
        }

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

    @Transactional(readOnly = true)
    public Page<PrendaTarjetaDTO> listarPrendas(Integer idCategoria,
                                                String talla,
                                                CondicionPrenda condicion,
                                                Integer idEstilo,
                                                String ciudad,
                                                TipoSolicitud modalidad,
                                                String marca,
                                                Color color,
                                                Pageable pageable) {

                                            Specification<Prenda> filtros = PrendaSpecifications.filtrar(
                                                    idCategoria,
                                                    talla,
                                                    condicion,
                                                    idEstilo,
                                                    ciudad,
                                                    modalidad,
                                                    marca,
                                                    color
                                            );

                                            return prendaRepository.findAll(filtros, pageable)
                                                    .map(prenda -> {
                                                        List<String> fotos =
                                                                prendaRepository.fotos(prenda.getIdPrenda());

                                                        String fotoPrincipal = null;

                                                        if (!fotos.isEmpty()) {
                                                            fotoPrincipal = fotos.get(0);
                                                        }

                                                        return prendaMapper.aTarjeta(prenda, fotoPrincipal);
                                                    });
                                        }

    @Transactional(readOnly = true)
    public List<InformePublicacionesDTO> obtenerInformePublicaciones(Integer idUsuario, LocalDate desde, LocalDate hasta) {
        Usuario usuario = usuariosRepository.findById(idUsuario).orElse(null);

        if (usuario == null) {
            throw new RelacionNoEncontrada("El usuario indicado no existe");
        }

        if (usuario.getRol() != RolUsuario.ADMINISTRADOR && usuario.getRol() != RolUsuario.MODERADOR) {
            throw new AccesoInformeNoPermitido("Solo los administradores y moderadores pueden consultar el informe");
        }

        if (desde.isAfter(hasta)) {
            throw new FechaNoValida("La fecha de inicio no puede ser posterior a la fecha de fin");
        }

        List<InformePublicacionesProyeccion> resultados =
                prendaRepository.obtenerInformePublicaciones(desde, hasta);

        List<InformePublicacionesDTO> informes = new ArrayList<>();

        for (InformePublicacionesProyeccion resultado : resultados) {

            InformePublicacionesDTO dto = new InformePublicacionesDTO(
                    resultado.getMes(),
                    resultado.getCategoria(),
                    resultado.getPublicaciones(),
                    resultado.getIntercambios(),
                    resultado.getPrestamos()
            );
            informes.add(dto);
        }
        return informes;
    }
}
