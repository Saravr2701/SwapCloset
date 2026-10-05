package es.safareyes.swapcloset;

import es.safareyes.swapcloset.modelos.Notificacion;
import es.safareyes.swapcloset.repositorios.INotificacionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestNotificacion {
    @Autowired
    private INotificacionRepository repository;

    @Test
    void consultarNotificacion() {
        List<Notificacion> todos = repository.findAll();
    }
}