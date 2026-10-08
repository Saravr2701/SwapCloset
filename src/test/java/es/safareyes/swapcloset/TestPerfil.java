package es.safareyes.swapcloset;

import es.safareyes.swapcloset.modelos.Perfil;
import es.safareyes.swapcloset.repositorios.ConsultaPerfilProyeccion;
import es.safareyes.swapcloset.repositorios.IPerfilRepository;
import es.safareyes.swapcloset.repositorios.PerfilCoincidenteProyeccion;
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
    void consultarPerfilCompleto() {
        List<Perfil> todos = repository.findAll();
    }

    @Test
    void consultarPerfil() {
        List<ConsultaPerfilProyeccion> todos = repository.obtenerPerfil();
    }

    @Test
    void consultarPerfilCoincidentes() {
        List<PerfilCoincidenteProyeccion> todos = repository.obtenerPerfilCoincidente(2);
    }
}