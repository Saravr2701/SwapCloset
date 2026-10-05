package es.safareyes.swapcloset.repositorios;

import es.safareyes.swapcloset.modelos.Valoracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface IValoracionRepository extends JpaRepository<Valoracion, Integer> {

}
