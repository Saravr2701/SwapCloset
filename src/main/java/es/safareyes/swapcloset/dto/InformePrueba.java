package es.safareyes.swapcloset.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class InformePrueba {
    private String tipo_organismo;
    //Count devuelve long en vez de integer
    private Long recuento;

    /*En el repositorio List[InformePrueba]
    * En el test List <InformePrueba> todos = pruebaRepositorio.nombreDeLaColumnaDelSelect*/
}
