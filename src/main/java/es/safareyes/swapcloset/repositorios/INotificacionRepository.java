package es.safareyes.swapcloset.repositorios;

import es.safareyes.swapcloset.modelos.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface INotificacionRepository extends JpaRepository<Notificacion, Integer> {

}