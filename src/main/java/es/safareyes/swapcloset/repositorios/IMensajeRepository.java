package es.safareyes.swapcloset.repositorios;

import es.safareyes.swapcloset.modelos.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface IMensajeRepository extends JpaRepository<Mensaje, Integer> {

}