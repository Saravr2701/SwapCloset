package es.safareyes.swapcloset.repositorios;

import es.safareyes.swapcloset.modelos.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface IFotoRepository extends JpaRepository<Foto, Integer> {

}