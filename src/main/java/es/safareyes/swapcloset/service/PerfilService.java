package es.safareyes.swapcloset.service;

import es.safareyes.swapcloset.dto.ModificarPerfilDTO;
import es.safareyes.swapcloset.exception.RelacionNoEncontrada;
import es.safareyes.swapcloset.modelos.Estilo;
import es.safareyes.swapcloset.modelos.Perfil;
import es.safareyes.swapcloset.repositorios.IEstilosRepository;
import es.safareyes.swapcloset.repositorios.IPerfilRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PerfilService implements IPerfilService {

    IPerfilRepository perfilRepository;
    IEstilosRepository estilosRepository;

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
    public void visualizarPerfil(){}
}
