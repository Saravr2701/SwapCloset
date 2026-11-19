package es.safareyes.swapcloset.repositorios;

import es.safareyes.swapcloset.modelos.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ISolicitudRepository extends JpaRepository<Solicitud, Integer> {
    //Consulta de las prendas que ha solicitado
    List<Solicitud> findByPerfilIdPerfil(Integer idPerfil);

    long countByPerfil_IdPerfilAndTipoSolicitudAndEstadoSolicitudIn(
            Integer idPerfil,
            TipoSolicitud tipoSolicitud,
            List<EstadoSolicitud> estadoSolicitud
    );

    @Query("""
    SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END
    FROM Solicitud s
    LEFT JOIN s.SolicitudesPrendas sp
    WHERE s.idSolicitud = :idSolicitud
    AND (s.perfil.idPerfil = :idPerfil
    OR sp.prenda.perfil.idPerfil = :idPerfil)
    """)
    boolean perfilEnSolicitud(
            @Param("idSolicitud") Integer idSolicitud,
            @Param("idPerfil") Integer idPerfil
    );

    @Query("""
    SELECT COUNT(sp)
    FROM SolicitudPrenda sp
    WHERE sp.solicitud.perfil.idPerfil = :idPerfil
    AND sp.prenda.idPrenda = :idPrenda
    AND sp.rolPrenda = :rol
    AND sp.solicitud.estadoSolicitud = :estado
    """)
    long contarSolicitudesPendientes(@Param("idPerfil") Integer idPerfil, @Param("idPrenda") Integer idPrenda, @Param("rol") RolPrenda rol, @Param("estado") EstadoSolicitud estado);
}