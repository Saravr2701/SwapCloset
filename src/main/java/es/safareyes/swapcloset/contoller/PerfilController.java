package es.safareyes.swapcloset.contoller;

import es.safareyes.swapcloset.dto.ModificarPerfilDTO;
import es.safareyes.swapcloset.dto.VisualizarPerfilDTO;
import es.safareyes.swapcloset.service.IPerfilService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/usuarios")
public class PerfilController {
    IPerfilService perfilService;

    public PerfilController(IPerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @PutMapping("/me")
    public void modificarPerfil(@RequestParam Integer idUsuario, @Valid @RequestBody ModificarPerfilDTO dto) {
        perfilService.modificarPerfil(idUsuario, dto);
    }

    @GetMapping("/{id}")
    public VisualizarPerfilDTO visualizarPerfil(@PathVariable Integer id){
        return perfilService.visualizarPerfil(id);
    }
}
