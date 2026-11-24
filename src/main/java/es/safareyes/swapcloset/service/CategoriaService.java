package es.safareyes.swapcloset.service;

import es.safareyes.swapcloset.dto.MostrarNumPrendasPorCategoria;
import es.safareyes.swapcloset.repositorios.CantidadCategoriaProyeccion;
import es.safareyes.swapcloset.repositorios.ICategoriaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CategoriaService implements ICategoriaService{

    ICategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<MostrarNumPrendasPorCategoria> obtenerCantidadCategoria(){
        List<CantidadCategoriaProyeccion> cantidadCategoria = categoriaRepository.obtenerCantidadCategoria();
        List<MostrarNumPrendasPorCategoria> categorias = new ArrayList<>();

        for (CantidadCategoriaProyeccion cantidad : cantidadCategoria) {
            MostrarNumPrendasPorCategoria dto = new MostrarNumPrendasPorCategoria(
                    cantidad.getPrendas_disponibles().intValue(),
                    cantidad.getNombre()
            );
            categorias.add(dto);
        }

        return categorias;
    }
}
