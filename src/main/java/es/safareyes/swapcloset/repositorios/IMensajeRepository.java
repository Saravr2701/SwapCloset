package es.safareyes.swapcloset.repositorios;

import es.safareyes.swapcloset.modelos.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface IMensajeRepository extends JpaRepository<Mensaje, Integer> {
    @Query(value =
            "SELECT " +
            "    m.id AS id_mensaje, " +
            "    m.id_solicitud, " +
            "    p_emisor.id AS id_emisor, " +
            "    CONCAT(p_emisor.nombre, ' ', p_emisor.ape1) AS emisor," +
            "    p_receptor.id AS id_receptor, " +
            "    CONCAT(p_receptor.nombre, ' ', p_receptor.ape1) AS receptor, " +
            "    m.contenido, " +
            "    m.fecha_envio, " +
            "    m.hora_envio " +
            "FROM mensaje m " +
                "JOIN envia_mensaje em ON m.id = em.id_mensaje " +
                "JOIN perfil p_emisor ON em.id_perfil = p_emisor.id " +
                "JOIN recibe_mensaje rm ON m.id = rm.id_mensaje " +
                "JOIN perfil p_receptor ON rm.id_perfil = p_receptor.id " +
            "WHERE m.id_solicitud = :id_solicitud " +
            "ORDER BY m.fecha_envio DESC, m.hora_envio DESC " ,
            nativeQuery = true)
    List<ConversacionProyeccion> obtenerConversaciones(@Param("id_solicitud") Integer idSolicitud);
}