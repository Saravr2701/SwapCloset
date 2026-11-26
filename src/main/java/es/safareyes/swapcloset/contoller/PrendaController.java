package es.safareyes.swapcloset.contoller;

import es.safareyes.swapcloset.dto.*;
import es.safareyes.swapcloset.modelos.Color;
import es.safareyes.swapcloset.modelos.CondicionPrenda;
import es.safareyes.swapcloset.modelos.TipoSolicitud;
import es.safareyes.swapcloset.service.IPrendaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

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

    @GetMapping("/prendasPaginadas")
    public Page<PrendaTarjetaDTO> listarPrendas(
            @RequestParam(required = false) Integer idCategoria,
            @RequestParam(required = false) String talla,
            @RequestParam(name = "estado", required = false) CondicionPrenda condicion,
            @RequestParam(required = false) Integer idEstilo,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) TipoSolicitud modalidad,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) Color color,
            @PageableDefault(
                    size = 10,
                    sort = {"fechaPublicacion", "idPrenda"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {

        return prendaService.listarPrendas(
                idCategoria, talla, condicion,
                idEstilo, ciudad, modalidad,
                marca, color, pageable
        );
    }
}
