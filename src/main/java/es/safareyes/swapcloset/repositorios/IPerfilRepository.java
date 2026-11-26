package es.safareyes.swapcloset.repositorios;

import es.safareyes.swapcloset.dto.PerfilesAfinesDTO;
import es.safareyes.swapcloset.modelos.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Repository

public interface IPerfilRepository extends JpaRepository<Perfil, Integer> {
    @Query(value =
            "SELECT " +
                    "CONCAT (p.nombre, ' ', p.ape1, ' ', p.ape2) AS nombre_completo, " +
                    "p.ciudad," +
                    "STRING_AGG(e.nombre, ', ') AS estilos, " +
                    "p.biografia, " +
                    "CONCAT ('Talla pantalón: ', p.\"talla_pantalon\"  " +
                        ",' | Talla camiseta:', p.\"talla_camiseta\"  " +
                        ",' | Talla zapato:',p.\"talla_zapatos\") AS tallas, " +
                    "rep.reputacion " +
            "FROM perfil p " +
                "LEFT JOIN reputacion_perfil rep ON rep.id_perfil = p.id " +
                "JOIN estilos_perfil ep ON p.id = ep.id_perfil " +
                "JOIN estilos e ON ep.id_estilo = e.id " +
            "GROUP BY p.id, rep.reputacion ",
            nativeQuery = true)
    List<ConsultaPerfilProyeccion> obtenerPerfil();

    @Query(value = """
        SELECT
            p.id AS "idCoincidente",
            p.id_usuario AS "idUsuario",
            CONCAT(p.nombre, ' ', p.ape1, ' ', p.ape2) AS "nombreCompleto",
            p.ciudad,
            COUNT(e.id) AS "numEstilosComunes",
            CAST(rep.media_puntuacion AS DOUBLE PRECISION) AS reputacion,
            STRING_AGG(e.nombre, ', ') AS "estilosComunes"
        FROM swapcloset.perfil p
        JOIN swapcloset.estilos_perfil ep
            ON p.id = ep.id_perfil
        JOIN swapcloset.estilos e
            ON ep.id_estilo = e.id
        JOIN swapcloset.reputacion_perfil rep
            ON p.id = rep.id_perfil
        WHERE p.id != :id_perfil
        AND ep.id_estilo IN (
            SELECT id_estilo
            FROM swapcloset.estilos_perfil
            WHERE id_perfil = :id_perfil
        )
        AND rep.media_puntuacion >= 4.0
        GROUP BY p.id, rep.media_puntuacion
        ORDER BY COUNT(e.id) DESC, rep.media_puntuacion DESC, p.id ASC
        """, nativeQuery = true)
    List<PerfilCoincidenteProyeccion> obtenerPerfilCoincidente(
            @Param("id_perfil") Integer idPerfil);

    Optional<Perfil> findByUsuario_IdUsuario(Integer idUsuario);

    @Query("SELECT CONCAT(p.nombre, ' ', UPPER(SUBSTRING(p.ape1, 1, 1)), '.')" +
            " FROM Perfil p " +
            "WHERE p.usuario.idUsuario = :idUsuario")
    String obtenerNombreCorto(@Param("idUsuario") Integer idUsuario);
}
