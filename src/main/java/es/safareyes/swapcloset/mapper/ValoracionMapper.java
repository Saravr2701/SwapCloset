package es.safareyes.swapcloset.mapper;

import es.safareyes.swapcloset.dto.ValorarUsuarioDTO;
import es.safareyes.swapcloset.modelos.Valoracion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ValoracionMapper {
    @Mapping(target = "fechaValoracion", ignore = true)
    @Mapping(target = "idValoracion", ignore = true)
    @Mapping(target = "solicitud", ignore = true)
    @Mapping(target = "perfil", ignore = true)

    Valoracion valorarUsuario(ValorarUsuarioDTO dto);
}
