package es.safareyes.swapcloset.contoller;

import es.safareyes.swapcloset.dto.CambiarEstadoSolicitudDTO;
import es.safareyes.swapcloset.dto.CrearPrendaDTO;
import es.safareyes.swapcloset.dto.CrearSolicitudDTO;
import es.safareyes.swapcloset.dto.MisSolicitudesDTO;
import es.safareyes.swapcloset.modelos.EstadoSolicitud;
import es.safareyes.swapcloset.service.ISolicitudService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/solicitudes")
public class SolicitudController {

    private ISolicitudService solicitudService;

    public SolicitudController(ISolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping("/crearSolicitud")
    public void crearSolicitud (@Valid @RequestBody CrearSolicitudDTO dto){
        solicitudService.crearSolicitud(dto);
    }

    @PatchMapping("/{id}/estado/cambiarEstadoSolicitud")
    public void cambiarEstadoSolicitud (@PathVariable Integer id, @Valid @RequestBody CambiarEstadoSolicitudDTO dto){
        solicitudService.cambiarEstadoSolicitud(id, dto);
    }

    @GetMapping("/{idUsuario}")
    public List<MisSolicitudesDTO> misSolicitudesDTO(@PathVariable Integer idUsuario, @RequestParam String bandeja, @RequestParam(required = false) EstadoSolicitud estado){
        return solicitudService.misSolicitudesDTO(idUsuario, bandeja, estado);
    }
}
