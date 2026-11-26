package es.safareyes.swapcloset.service;

import es.safareyes.swapcloset.dto.*;
import es.safareyes.swapcloset.modelos.Color;
import es.safareyes.swapcloset.modelos.CondicionPrenda;
import es.safareyes.swapcloset.modelos.TipoSolicitud;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface IPrendaService {

    void crearPrenda(CrearPrendaDTO dto);
    void eliminarPrendaLogico(Integer idPrenda, Integer idUsuario);
    void modificarPrenda(Integer id, ModificarPrendaDTO dto);
    MostrarDetallesPrendasDTO mostrarDetallesPrenda(Integer id);
    Page<PrendaTarjetaDTO> listarPrendas(
            Integer idCategoria,
            String talla,
            CondicionPrenda condicion,
            Integer idEstilo,
            String ciudad,
            TipoSolicitud modalidad,
            String marca,
            Color color,
            Pageable pageable
    );
    List<InformePublicacionesDTO> obtenerInformePublicaciones(Integer idUsuario, LocalDate desde, LocalDate hasta);
}
