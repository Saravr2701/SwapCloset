package es.safareyes.swapcloset;

import es.safareyes.swapcloset.modelos.Solicitud;
import es.safareyes.swapcloset.repositorios.ISolicitudRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestSolicitud {
    @Autowired
    private ISolicitudRepository repository;

    @Test
    void consultarSolicitud() {
        List<Solicitud> todos = repository.findAll();
    }
}
