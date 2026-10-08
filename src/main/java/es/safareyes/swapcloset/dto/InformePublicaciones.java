package es.safareyes.swapcloset.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class InformePublicaciones {
    private String mes;
    private Long publicaciones;
    private Long intercambios;
    private Long prestamos;
}
