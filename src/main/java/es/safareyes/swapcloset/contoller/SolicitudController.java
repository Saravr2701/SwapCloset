package es.safareyes.swapcloset.contoller;

import es.safareyes.swapcloset.dto.CrearPrendaDTO;
import es.safareyes.swapcloset.dto.CrearSolicitudDTO;
import es.safareyes.swapcloset.service.ISolicitudService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
