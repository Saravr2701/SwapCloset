package es.safareyes.swapcloset;

import es.safareyes.swapcloset.modelos.Categorias;
import es.safareyes.swapcloset.repositorios.ICategoriaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestCategoria {
    @Autowired
    private ICategoriaRepository repository;

    @Test
    void consultarCategoria() {
        List<Categorias> todos = repository.findAll();
    }
}
