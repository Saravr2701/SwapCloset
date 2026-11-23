package es.safareyes.swapcloset.mapper;

import es.safareyes.swapcloset.dto.CrearSolicitudDTO;
import es.safareyes.swapcloset.modelos.Solicitud;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SolicitudMapper {
    @Mapping(target = "idSolicitud", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaRespuesta", ignore = true)

    Solicitud crearSolicitud(CrearSolicitudDTO crearSolicitudDTO);
}
