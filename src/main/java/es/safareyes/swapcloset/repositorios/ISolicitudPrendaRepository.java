package es.safareyes.swapcloset.repositorios;

import es.safareyes.swapcloset.modelos.SolicitudPrenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ISolicitudPrendaRepository extends JpaRepository<SolicitudPrenda, Integer> {
    //Consulta de las prendas que le han solicitado
    @Query("SELECT sp FROM SolicitudPrenda sp JOIN sp.prenda p WHERE p.perfil.idPerfil = :idPerfil")
    List<SolicitudPrenda> prendasQueHanSolicitado(@Param("idPerfil") Integer idPerfil);
}
