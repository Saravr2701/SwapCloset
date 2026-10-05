package es.safareyes.swapcloset;

import es.safareyes.swapcloset.modelos.Estilo;
import es.safareyes.swapcloset.repositorios.IEstilosRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestEstilos {
    @Autowired
    private IEstilosRepository repository;

    @Test
    void consultarEstilo() {
        List<Estilo> todos = repository.findAll();
    }
}