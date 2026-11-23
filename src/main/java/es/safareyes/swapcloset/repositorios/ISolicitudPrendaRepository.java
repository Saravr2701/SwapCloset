package es.safareyes.swapcloset.repositorios;

import es.safareyes.swapcloset.modelos.RolPrenda;
import es.safareyes.swapcloset.modelos.SolicitudPrenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ISolicitudPrendaRepository extends JpaRepository<SolicitudPrenda, Integer> {
    //Consulta de las prendas que le han solicitado
    @Query("SELECT sp FROM SolicitudPrenda sp JOIN sp.prenda p WHERE p.perfil.idPerfil = :idPerfil")
    List<SolicitudPrenda> prendasQueHanSolicitado(@Param("idPerfil") Integer idPerfil);

    SolicitudPrenda findBySolicitud_IdSolicitudAndRolPrenda(
            Integer idSolicitud,
            RolPrenda rolPrenda
    );

    @Modifying
    @Query("UPDATE Solicitud s SET s.estadoSolicitud = RECHAZADA, " +
            "s.fechaRespuesta = CURRENT_DATE " +
            "WHERE s.estadoSolicitud = PENDIENTE " +
                "AND s.idSolicitud <> :idSolicitud " +
                "AND s.idSolicitud IN (SELECT sp.solicitud.idSolicitud " +
                                        "FROM SolicitudPrenda sp " +
                                        "WHERE sp.prenda.idPrenda = :idPrenda)")
    Integer solicitudEnPendiente(@Param("idPrenda") Integer idPrenda, @Param("idSolicitud") Integer idSolicitud);
}
