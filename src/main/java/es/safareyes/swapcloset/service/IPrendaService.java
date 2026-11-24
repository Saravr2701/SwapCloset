package es.safareyes.swapcloset.service;

import es.safareyes.swapcloset.dto.CrearPrendaDTO;
import es.safareyes.swapcloset.dto.ModificarPrendaDTO;
import es.safareyes.swapcloset.dto.MostrarDetallesPrendasDTO;

public interface IPrendaService {

    void crearPrenda(CrearPrendaDTO dto);
    void eliminarPrendaLogico(Integer idPrenda, Integer idUsuario);
    void modificarPrenda(Integer id, ModificarPrendaDTO dto);
    MostrarDetallesPrendasDTO mostrarDetallesPrenda(Integer id);
}
