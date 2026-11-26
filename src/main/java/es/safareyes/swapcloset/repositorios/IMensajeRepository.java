package es.safareyes.swapcloset.repositorios;

import es.safareyes.swapcloset.modelos.Mensaje;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface IMensajeRepository extends JpaRepository<Mensaje, Integer> {
    @Query(value = """
        SELECT
            m.id AS "idMensaje",
            m.id_solicitud AS "idSolicitud",
            p_emisor.id AS "idEmisor",
            CONCAT(p_emisor.nombre, ' ', p_emisor.ape1) AS emisor,
            p_receptor.id AS "idReceptor",
            CONCAT(p_receptor.nombre, ' ', p_receptor.ape1) AS receptor,
            m.contenido,
            m.fecha_envio AS "fechaEnvio",
            m.hora_envio AS "horaEnvio"
        FROM swapcloset.mensaje m
        JOIN swapcloset.envia_mensaje em 
            ON m.id = em.id_mensaje
        JOIN swapcloset.perfil p_emisor 
            ON em.id_perfil = p_emisor.id
        JOIN swapcloset.recibe_mensaje rm 
            ON m.id = rm.id_mensaje
        JOIN swapcloset.perfil p_receptor 
            ON rm.id_perfil = p_receptor.id
        WHERE m.id_solicitud = :idSolicitud
        ORDER BY m.fecha_envio DESC, m.hora_envio DESC, m.id DESC
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM swapcloset.mensaje m
        JOIN swapcloset.envia_mensaje em 
            ON m.id = em.id_mensaje
        JOIN swapcloset.perfil p_emisor 
            ON em.id_perfil = p_emisor.id
        JOIN swapcloset.recibe_mensaje rm 
            ON m.id = rm.id_mensaje
        JOIN swapcloset.perfil p_receptor 
            ON rm.id_perfil = p_receptor.id
        WHERE m.id_solicitud = :idSolicitud
        """,
            nativeQuery = true)
    Page<ConversacionProyeccion> obtenerConversaciones(
            @Param("idSolicitud") Integer idSolicitud,
            Pageable pageable);
}