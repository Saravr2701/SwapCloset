package es.safareyes.swapcloset.repositorios;

import es.safareyes.swapcloset.modelos.SolicitudPrenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ISolicitudPrendaRepository extends JpaRepository<SolicitudPrenda, Integer> {

}
