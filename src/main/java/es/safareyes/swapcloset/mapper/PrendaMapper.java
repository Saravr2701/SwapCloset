package es.safareyes.swapcloset.mapper;

import es.safareyes.swapcloset.dto.CrearPrendaDTO;
import es.safareyes.swapcloset.dto.PrendaTarjetaDTO;
import es.safareyes.swapcloset.modelos.Prenda;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrendaMapper {
    @Mapping(target = "idPrenda", ignore = true)
    @Mapping(target = "fechaPublicacion", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "perfil", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "fotos", ignore = true)
    @Mapping(target = "solicitudes", ignore = true)



    Prenda toEntity(CrearPrendaDTO dto);



    @Mapping(target = "fotoPrincipal", source = "fotoPrincipal")
    PrendaTarjetaDTO aTarjeta(Prenda prenda, String fotoPrincipal);
}
