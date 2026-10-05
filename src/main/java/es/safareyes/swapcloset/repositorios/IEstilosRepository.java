package es.safareyes.swapcloset.repositorios;

import es.safareyes.swapcloset.modelos.Estilo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface IEstilosRepository extends JpaRepository<Estilo, Integer> {

}
