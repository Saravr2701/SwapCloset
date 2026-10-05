package es.safareyes.swapcloset.repositorios;

import es.safareyes.swapcloset.modelos.Prenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface IPrendaRepository extends JpaRepository<Prenda, Integer> {
}
