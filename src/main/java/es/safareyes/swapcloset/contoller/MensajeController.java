package es.safareyes.swapcloset.contoller;

import es.safareyes.swapcloset.dto.ConversacionDTO;
import es.safareyes.swapcloset.dto.CrearPrendaDTO;
import es.safareyes.swapcloset.dto.EnviarMensajeDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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

    @GetMapping("/conversacionPaginada")
    public Page<ConversacionDTO> obtenerConversacion(@PathVariable Integer id,
                                                        @RequestParam Integer idUsuario,
                                                        @RequestParam(defaultValue = "0") Integer page,
                                                        @RequestParam(defaultValue = "10") Integer size) {
        return mensajeService.obtenerConversacion(
                id, idUsuario, page, size);
    }
}
