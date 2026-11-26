package es.safareyes.swapcloset;

import es.safareyes.swapcloset.modelos.Prenda;
import es.safareyes.swapcloset.repositorios.IPrendaRepository;
import es.safareyes.swapcloset.repositorios.InformePublicacionesProyeccion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestPrenda {
    @Autowired
    private IPrendaRepository repository;

    @Test
    void consultarPrendas() {
        List<Prenda> todos = repository.findAll();
    }

    @Test
    void consultarPrendaEntityGraph() {
        Prenda prenda = repository.findByIdPrenda(4).orElseThrow();
    }

    @Test
    void obtenerInformePublicaciones() {
        List<InformePublicacionesProyeccion> pagina = repository.obtenerInformePublicaciones(LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 30));
    }
}