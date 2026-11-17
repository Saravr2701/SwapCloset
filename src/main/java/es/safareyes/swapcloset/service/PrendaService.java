package es.safareyes.swapcloset.service;

import es.safareyes.swapcloset.exception.RelacionNoEncontrada;
import es.safareyes.swapcloset.exception.SinTipoSolicitud;
import es.safareyes.swapcloset.mapper.PrendaMapper;
import es.safareyes.swapcloset.dto.CrearPrendaDTO;
import es.safareyes.swapcloset.modelos.*;
import es.safareyes.swapcloset.repositorios.ICategoriaRepository;
import es.safareyes.swapcloset.repositorios.IFotoRepository;
import es.safareyes.swapcloset.repositorios.IPerfilRepository;
import es.safareyes.swapcloset.repositorios.IPrendaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class PrendaService implements IPrendaService{

    private PrendaMapper prendaMapper;
    private IPrendaRepository prendaRepository;
    private ICategoriaRepository categoriaRepository;
    private IPerfilRepository perfilRepository;

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


        prendaRepository.save(nuevaPrenda);
    }
}
