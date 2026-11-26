package es.safareyes.swapcloset.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InformePublicacionesDTO {
    private String mes;
    private String categoria;
    private Long publicaciones;
    private Long intercambios;
    private Long prestamos;
}