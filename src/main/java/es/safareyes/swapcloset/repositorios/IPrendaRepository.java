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
    List<ConsultaPrendasCompletasProyeccion> obtenerPrendasCompletas();
}
