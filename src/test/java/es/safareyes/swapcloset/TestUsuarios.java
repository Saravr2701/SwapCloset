package es.safareyes.swapcloset;

import es.safareyes.swapcloset.modelos.Usuario;
import es.safareyes.swapcloset.repositorios.IUsuariosRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestUsuarios {
    @Autowired
    private IUsuariosRepository repository;

    @Test
    void consultarUsuarios() {
        List<Usuario> todos = repository.findAll();
    }
}