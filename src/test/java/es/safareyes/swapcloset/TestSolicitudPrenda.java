package es.safareyes.swapcloset;

import es.safareyes.swapcloset.modelos.SolicitudPrenda;
import es.safareyes.swapcloset.repositorios.ISolicitudPrendaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestSolicitudPrenda {
    @Autowired
    private ISolicitudPrendaRepository repository;

    @Test
    void consultarSolicitudPrenda() {
        List<SolicitudPrenda> todos = repository.findAll();
    }

    @Test
    void consultarMisPrendasSolicitadas() {
        List<SolicitudPrenda> todos = repository.prendasQueHanSolicitado(2);
    }
}
