package es.safareyes.swapcloset.contoller;

import es.safareyes.swapcloset.dto.MostrarNumPrendasPorCategoria;
import es.safareyes.swapcloset.service.ICategoriaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/categorias")
public class CategoriaController {
    ICategoriaService categoriaService;

    public CategoriaController(ICategoriaService categoriaService){
        this.categoriaService = categoriaService;
    }

    @GetMapping("/numPrendas")
    public List<MostrarNumPrendasPorCategoria> numPrendasPorCategoria(){
        return categoriaService.obtenerCantidadCategoria();
    };
}
