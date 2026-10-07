package es.safareyes.swapcloset.repositorios;

import es.safareyes.swapcloset.modelos.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ICategoriaRepository extends JpaRepository<Categoria, Integer> {
    @Query(value =
            "SELECT " +
                "c.nombre, " +
                "COUNT(p.id) AS prendas_disponibles " +
            "FROM categorias c " +
                "LEFT JOIN prenda p ON p.id_categorias = c.id AND p.estado = 'DISPONIBLE' " +
            "GROUP BY c.id, c.nombre " +
            "ORDER BY c.nombre ASC",
             nativeQuery = true)
    List<CantidadCategoriaProyeccion> obtenerCantidadCategoria();
}
