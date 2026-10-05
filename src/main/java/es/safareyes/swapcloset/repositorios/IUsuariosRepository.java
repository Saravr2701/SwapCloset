package es.safareyes.swapcloset.repositorios;

import es.safareyes.swapcloset.modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface IUsuariosRepository extends JpaRepository<Usuario, Integer> {

}
