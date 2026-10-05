package es.safareyes.swapcloset;

import es.safareyes.swapcloset.modelos.Foto;
import es.safareyes.swapcloset.repositorios.IFotoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestFoto {
    @Autowired
    private IFotoRepository repository;

    @Test
    void consultarMensaje() {
        List<Foto> todos = repository.findAll();
    }
}