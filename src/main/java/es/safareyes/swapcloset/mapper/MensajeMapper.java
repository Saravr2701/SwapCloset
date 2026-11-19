package es.safareyes.swapcloset.mapper;

import es.safareyes.swapcloset.dto.EnviarMensajeDTO;
import es.safareyes.swapcloset.modelos.Mensaje;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MensajeMapper {
    @Mapping(target = "idMensaje", ignore = true)
    @Mapping(target = "solicitud", ignore = true)
    @Mapping(target = "fechaEnvio", ignore = true)
    @Mapping(target = "horaEnvio", ignore = true)
    @Mapping(target = "emisores", ignore = true)
    @Mapping(target = "receptores", ignore = true)
    @Mapping(target = "contenido", source = "contenidoMensaje")

    Mensaje enviarMensaje(EnviarMensajeDTO dto);
}
