package es.safareyes.swapcloset.contoller;

import es.safareyes.swapcloset.dto.CrearSolicitudDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/solicitudes")
public class ValoracionController {

    private IValoracionService valorancionService;

    public ValoracionController(IValoracionService valoracionService) {
        this.valoracionService = valoracionService;
    }

    @PostMapping("/valoraciones")
    public void valorarUsuario (@Valid @RequestBody valorarUsuarioDTO dto){
        valoracionService.valorarUsuario(dto);
    }
}
