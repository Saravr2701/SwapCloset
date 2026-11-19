package es.safareyes.swapcloset.contoller;

import es.safareyes.swapcloset.dto.CrearPrendaDTO;
import es.safareyes.swapcloset.dto.EnviarMensajeDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import es.safareyes.swapcloset.service.IMensajeService;

@RestController
@RequestMapping("/api/v1/solicitudes/{id}/mensajes")
public class MensajeController {

    private IMensajeService mensajeService;

    public MensajeController(IMensajeService mensajeService){
        this.mensajeService = mensajeService;
    }

    @PostMapping
    public void enviarMensaje (@PathVariable Integer id, @Valid @RequestBody EnviarMensajeDTO dto){
        mensajeService.enviarMensaje(id, dto);
    }
}
