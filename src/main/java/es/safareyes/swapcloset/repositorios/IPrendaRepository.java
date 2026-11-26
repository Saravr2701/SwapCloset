package es.safareyes.swapcloset.repositorios;

import es.safareyes.swapcloset.modelos.Prenda;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository

public interface IPrendaRepository extends JpaRepository<Prenda, Integer>, JpaSpecificationExecutor<Prenda> {
    @Query(value = """
        SELECT
            TO_CHAR(a.fecha, 'YYYY-MM') AS mes,
            c.nombre AS categoria,
            SUM(a.publicaciones) AS publicaciones,
            SUM(a.intercambios) AS intercambios,
            SUM(a.prestamos) AS prestamos
        FROM (SELECT
                p.fecha_publicacion AS fecha,
                p.id_categorias AS id_categoria,
                1 AS publicaciones,
                0 AS intercambios,
                0 AS prestamos,
                NULL AS id_solicitud
            FROM swapcloset.prenda p
            WHERE p.estado != 0

            UNION ALL

            SELECT DISTINCT
                s.fecha_creacion AS fecha,
                p.id_categorias AS id_categoria,
                0 AS publicaciones,
                CASE WHEN s.tipo_solicitud = 1
                    THEN 1 ELSE 0 END AS intercambios,
                CASE WHEN s.tipo_solicitud = 0
                    THEN 1 ELSE 0 END AS prestamos,
                s.id AS id_solicitud
            FROM swapcloset.solicitud s
            JOIN swapcloset.solicitud_prenda sp
                ON s.id = sp.id_solicitud
            JOIN swapcloset.prenda p
                ON sp.id_prenda = p.id
            WHERE sp.rol_prenda = 0
                AND s.estado IN (1, 5, 6)
        ) a
        JOIN swapcloset.categorias c
            ON a.id_categoria = c.id
        WHERE a.fecha BETWEEN :desde AND :hasta
        GROUP BY TO_CHAR(a.fecha, 'YYYY-MM'), c.nombre
        ORDER BY mes DESC, categoria ASC
        """, nativeQuery = true)
    List<InformePublicacionesProyeccion> obtenerInformePublicaciones(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);

    @Modifying
    @Query("UPDATE Solicitud s " +
            "SET s.estadoSolicitud = es.safareyes.swapcloset.modelos.EstadoSolicitud.CANCELADA " +
            "WHERE s.estadoSolicitud = es.safareyes.swapcloset.modelos.EstadoSolicitud.PENDIENTE " +
                "AND s.idSolicitud IN (SELECT sp.solicitud.idSolicitud " +
                                        "FROM SolicitudPrenda sp " +
                                        "WHERE sp.prenda.idPrenda = :idPrenda)")
    Integer cancelarSolicitudesPendientes(@Param("idPrenda") Integer idPrenda);

    @Query("SELECT f.urlFoto " +
            "FROM Foto f " +
            "WHERE f.prenda.idPrenda = :idPrenda " +
            "ORDER BY f.orden")
    List<String> fotos(@Param ("idPrenda") Integer idPrenda);

    @EntityGraph(attributePaths = {
            "fotos",
            "perfil",
            "perfil.usuario",
            "perfil.valoraciones"
    })
    Optional<Prenda> findByIdPrenda(Integer id);
}
