package es.safareyes.swapcloset.contoller;

import es.safareyes.swapcloset.dto.CrearSolicitudDTO;
import es.safareyes.swapcloset.dto.ValorarUsuarioDTO;
import es.safareyes.swapcloset.service.IValoracionService;
import es.safareyes.swapcloset.service.ValoracionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/solicitudes/{id}")
public class ValoracionController {

    private IValoracionService valoracionService;

    public ValoracionController(IValoracionService valoracionService) {
        this.valoracionService = valoracionService;
    }

    @PostMapping("/valoraciones")
    public void valorarUsuario (@PathVariable Integer id, @Valid @RequestBody ValorarUsuarioDTO dto){
        valoracionService.valorarUsuario(id,dto);
    }
}
