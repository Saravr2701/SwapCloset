package es.safareyes.swapcloset.repositorios;

import es.safareyes.swapcloset.modelos.Solicitud;
import es.safareyes.swapcloset.modelos.SolicitudPrenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ISolicitudRepository extends JpaRepository<Solicitud, Integer> {
    //Consulta de las prendas que ha solicitado
    List<Solicitud> findByPerfilIdPerfil(Integer idPerfil);
}