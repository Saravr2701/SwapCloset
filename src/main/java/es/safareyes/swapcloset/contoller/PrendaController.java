package es.safareyes.swapcloset.contoller;

import es.safareyes.swapcloset.dto.BajaLogicaPrendaDTO;
import es.safareyes.swapcloset.dto.CrearPrendaDTO;
import es.safareyes.swapcloset.dto.ModificarPrendaDTO;
import es.safareyes.swapcloset.dto.MostrarDetallesPrendasDTO;
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

    @PatchMapping("/eliminarPrendaLogico/{id}")
    public void eliminarPrendaLogico(@PathVariable Integer id, @Valid @RequestBody BajaLogicaPrendaDTO dto){
        prendaService.eliminarPrendaLogico(id, dto.idUsuario());
    }

    @PutMapping("{id}/modificarPrenda")
    public void modificarPrenda(@PathVariable Integer id, @Valid @RequestBody ModificarPrendaDTO dto){
        prendaService.modificarPrenda(id, dto);
    }

    @GetMapping("/{id}")
    public MostrarDetallesPrendasDTO detallesPrenda(@PathVariable Integer id) {
        return prendaService.mostrarDetallesPrenda(id);
    }

}
