package es.safareyes.swapcloset.mapper;

import es.safareyes.swapcloset.dto.PerfilesAfinesDTO;
import es.safareyes.swapcloset.repositorios.PerfilCoincidenteProyeccion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PerfilMapper {
    @Mapping(target = "estilos", source = "estilosComunes")
    PerfilesAfinesDTO toDTO(PerfilCoincidenteProyeccion perfil);

    default List<String> convertirEstilos(String estilos) {
        return Arrays.asList(estilos.split(", "));
    }
}