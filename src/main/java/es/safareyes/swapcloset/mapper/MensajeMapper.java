package es.safareyes.swapcloset.mapper;

import es.safareyes.swapcloset.dto.ConversacionDTO;
import es.safareyes.swapcloset.dto.EnviarMensajeDTO;
import es.safareyes.swapcloset.modelos.Mensaje;
import es.safareyes.swapcloset.repositorios.ConversacionProyeccion;
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
    ConversacionDTO toDTO(ConversacionProyeccion mensaje);
}
