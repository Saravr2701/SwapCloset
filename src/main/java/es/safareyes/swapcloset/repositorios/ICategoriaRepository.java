package es.safareyes.swapcloset.repositorios;

import es.safareyes.swapcloset.modelos.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ICategoriaRepository extends JpaRepository<Categoria, Integer> {

}
