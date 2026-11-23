package es.safareyes.swapcloset.contoller;

import es.safareyes.swapcloset.dto.CrearPrendaDTO;
import es.safareyes.swapcloset.service.IPrendaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/prendas")
public class PrendaController {

    private IPrendaService prendaService;

    public PrendaController(IPrendaService prendaService) {
        this.prendaService = prendaService;
    }

    @PostMapping("/crearPrenda")
    public void crearPrenda (@Valid @RequestBody CrearPrendaDTO dto){
        prendaService.crearPrenda(dto);
    }

    @PatchMapping("/eliminarPrendaFisico/{id}")
    public void eliminarPrendaFisico(@PathVariable Integer id){
        prendaService.eliminarPrendaFisico(id);
    }
}
