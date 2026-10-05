package es.safareyes.swapcloset;

import es.safareyes.swapcloset.modelos.Prenda;
import es.safareyes.swapcloset.repositorios.IPrendaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestPrenda {
    @Autowired
    private IPrendaRepository repository;

    @Test
    void consultarUsuarios() {
        List<Prenda> todos = repository.findAll();
    }
}