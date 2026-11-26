package es.safareyes.swapcloset.contoller;

import es.safareyes.swapcloset.dto.InformePublicacionesDTO;
import es.safareyes.swapcloset.service.IPrendaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/informes")
public class InformeController {
    private IPrendaService prendaService;

    public InformeController(IPrendaService prendaService) {
        this.prendaService = prendaService;
    }

    @GetMapping("/actividad")
    public List<InformePublicacionesDTO> obtenerInformePublicaciones(
            @RequestParam Integer idUsuario,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate desde,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate hasta) {

        return prendaService.obtenerInformePublicaciones(idUsuario, desde, hasta);
    }
}
