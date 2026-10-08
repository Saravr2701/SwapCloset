package es.safareyes.swapcloset.repositorios;

import es.safareyes.swapcloset.modelos.Prenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface IPrendaRepository extends JpaRepository<Prenda, Integer> {
    @Query(value =
            "SELECT " +
                "pr.id, " +
                "pr.titulo, " +
                "pr.descripcion, " +
                "pr.marca, " +
                "pr.talla, " +
                "pr.condicion, " +
                "pr.estado, " +
                "pr.permite_intercambio, " +
                "pr.permite_prestamo, " +
                "pr.fecha_publicacion " +
            "FROM prenda pr " +
            "ORDER BY pr.fecha_publicacion DESC",
             nativeQuery = true)
    List<ConsultaPrendasPaginadasProyeccion> obtenerPrendasPaginadas();

    @Query(value =
            "SELECT " +
                    "pr.id AS idPrenda, " +
                    "pr.titulo, " +
                    "pr.descripcion, " +
                    "pr.marca, " +
                    "pr.talla, " +
                    "pr.condicion, " +
                    "pr.estado, " +
                    "pr.permite_intercambio, " +
                    "pr.permite_prestamo, " +
                    "pr.fecha_publicacion, " +
                    "c.nombre, " +
                    "p.id AS idPerfil, " +
                    "CONCAT(p.nombre, ' ', p.ape1, ' ', p.ape2), " +
                    "rep.reputacion, " +
                    "STRING_AGG(f.url, ', ' ORDER BY f.orden) "+
                    "FROM prenda pr " +
                    "JOIN categorias c ON pr.id_categorias = c.id " +
                    "JOIN perfil p ON pr.id_perfil = p.id " +
                    "JOIN reputacion_perfil rep ON p.id = rep.id_perfil " +
                    "JOIN foto f ON pr.id = f.id_prenda " +
                    "GROUP BY pr.id, p.id, c.nombre, rep.reputacion " +
                    "ORDER BY pr.fecha_publicacion DESC",
            nativeQuery = true)
    List<ConsultaPrendasCompletasProyeccion> obtenerPrendasCompletas();
}
