package es.safareyes.swapcloset;

import es.safareyes.swapcloset.modelos.Valoracion;
import es.safareyes.swapcloset.repositorios.IValoracionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestValoracion {
    @Autowired
    private IValoracionRepository repository;

    @Test
    void consultarValoraciones() {
        List<Valoracion> todos = repository.findAll();
    }
}