package es.safareyes.swapcloset.repositorios;

import es.safareyes.swapcloset.modelos.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Query(value =
            "SELECT \n" +
                    "p.id AS id_perfil_coincidente, " +
                    "CONCAT(p.nombre, ' ', p.ape1, ' ', p.ape2) AS nombre_completo, " +
                    "p.ciudad, " +
                    "STRING_AGG(e.nombre, ', ') AS estilos_comunes, " +
                    "rep.reputacion " +
                    "FROM perfil p " +
                        "JOIN estilos_perfil ep ON p.id = ep.id_perfil " +
                        "JOIN estilos e ON ep.id_estilo = e.id " +
                        "JOIN reputacion_perfil rep ON p.id = rep.id_perfil\n " +
                    "WHERE p.id != :id_perfil " +
                    "  AND ep.id_estilo IN ( " +
                    "      SELECT id_estilo " +
                    "      FROM estilos_perfil " +
                    "      WHERE id_perfil = :id_perfil) " +
                    "  AND (rep.media_puntuacion >= 4.0 OR rep.media_puntuacion IS NOT NULL) " +
                    "GROUP BY p.id, rep.reputacion, rep.media_puntuacion " +
                    "ORDER BY rep.media_puntuacion DESC;",
            nativeQuery = true)
    List<PerfilCoincidenteProyeccion> obtenerPerfilCoincidente(@Param("id_perfil") Integer idPerfil);
}
