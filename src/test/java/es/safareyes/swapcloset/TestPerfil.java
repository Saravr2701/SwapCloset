package es.safareyes.swapcloset;

import es.safareyes.swapcloset.modelos.Perfil;
import es.safareyes.swapcloset.repositorios.IPerfilRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestPerfil {
    @Autowired
    private IPerfilRepository repository;

    @Test
    void consultarUsuarios() {
        List<Perfil> todos = repository.findAll();
    }
}