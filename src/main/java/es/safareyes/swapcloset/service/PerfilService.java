package es.safareyes.swapcloset.service;

import es.safareyes.swapcloset.dto.ModificarPerfilDTO;
import es.safareyes.swapcloset.dto.PrendasPerfilDTO;
import es.safareyes.swapcloset.dto.VisualizarPerfilDTO;
import es.safareyes.swapcloset.exception.RelacionNoEncontrada;
import es.safareyes.swapcloset.modelos.*;
import es.safareyes.swapcloset.repositorios.IEstilosRepository;
import es.safareyes.swapcloset.repositorios.IPerfilRepository;
import es.safareyes.swapcloset.repositorios.IPrendaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PerfilService implements IPerfilService {

    IPerfilRepository perfilRepository;
    IEstilosRepository estilosRepository;
    IPrendaRepository prendaRepository;

    @Transactional
    public void modificarPerfil(Integer idUsuario, ModificarPerfilDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO de modificación del perfil es nulo");
        }

        Perfil perfil = perfilRepository.findByUsuario_IdUsuario(idUsuario).orElse(null);

        if (perfil == null) {
            throw new RelacionNoEncontrada("El perfil indicado no existe");
        }

        if (dto.nombre() != null && !dto.nombre().isEmpty()) {
            perfil.setNombre(dto.nombre());
        }

        if (dto.ape1() != null && !dto.ape1().isEmpty()) {
            perfil.setApe1(dto.ape1());
        }

        if (dto.ape2() != null && !dto.ape2().isEmpty()) {
            perfil.setApe2(dto.ape2());
        }

        if (dto.ciudad() != null && !dto.ciudad().isEmpty()) {
            perfil.setCiudad(dto.ciudad());
        }

        if (dto.biografia() != null && !dto.biografia().isEmpty()) {
            perfil.setBiografia(dto.biografia());
        }

        if (dto.tallaCamiseta() != null) {
            perfil.setTallaCamiseta(dto.tallaCamiseta());
        }

        if (dto.tallaPantalon() != null) {
            perfil.setTallaPantalon(dto.tallaPantalon());
        }

        if (dto.tallaZapatos() != null) {
            perfil.setTallaZapatos(dto.tallaZapatos());
        }

        if (dto.idEstilos() != null) {
            List<Estilo> estilos = estilosRepository.findAllById(dto.idEstilos());
            if (estilos.size() != dto.idEstilos().size()) {
                throw new RelacionNoEncontrada("Alguno de los estilos indicados no existe");
            }
            perfil.setEstilos(new HashSet<>(estilos));
        }

        perfilRepository.save(perfil);
    }

    @Transactional(readOnly = true)
    public VisualizarPerfilDTO visualizarPerfil(Integer id){

        Perfil perfil = perfilRepository.findByUsuario_IdUsuario(id).orElse(null);

        if (perfil == null) {
            throw new RelacionNoEncontrada("El perfil indicado no existe");
        }

        List<String> estilos = new ArrayList<>();

        for (Estilo estilo : perfil.getEstilos()) {
            estilos.add(estilo.getNombre());
        }

        int numeroResenas = perfil.getValoraciones().size();
        double suma = 0.0;

        for (Valoracion valoracion : perfil.getValoraciones()) {
            suma += valoracion.getPuntuacion();
        }

        Double reputacion = null;
        if (numeroResenas > 0) {
            reputacion = Math.round((suma / numeroResenas) * 10.0) / 10.0;
        }

        Integer tallaZapatos = Integer.parseInt(
                perfil.getTallaZapatos().name().substring(1)
        );

        Integer tallaPantalon = Integer.parseInt(
                perfil.getTallaPantalon().name().substring(1)
        );

        String tallaCamiseta = perfil.getTallaCamiseta().name();

        String nombreCompleto = perfilRepository.obtenerNombreCorto(id);

        List<PrendasPerfilDTO> prendas = new ArrayList<>();

        for (Prenda prenda : perfil.getPrendas()) {
            if (Boolean.TRUE.equals(prenda.getActivo()) && prenda.getEstado() != EstadoPrenda.BORRADOR) {
                List<String> fotos = prendaRepository.fotos(prenda.getIdPrenda());
                String fotoPrincipal = null;

                if (!fotos.isEmpty()) {
                    fotoPrincipal = fotos.get(0);
                }

                PrendasPerfilDTO dto = new PrendasPerfilDTO(
                        prenda.getIdPrenda(),
                        prenda.getTitulo(),
                        prenda.getMarca(),
                        prenda.getTalla(),
                        fotoPrincipal
                );
                prendas.add(dto);
            }
        }
        return new VisualizarPerfilDTO(
                nombreCompleto,
                perfil.getCiudad(),
                perfil.getBiografia(),
                tallaZapatos,
                tallaPantalon,
                tallaCamiseta,
                estilos,
                reputacion,
                numeroResenas,
                prendas
        );
    }
}
