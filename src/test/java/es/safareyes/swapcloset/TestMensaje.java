package es.safareyes.swapcloset;

import es.safareyes.swapcloset.modelos.Mensaje;
import es.safareyes.swapcloset.repositorios.ConversacionProyeccion;
import es.safareyes.swapcloset.repositorios.IMensajeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestMensaje {
    @Autowired
    private IMensajeRepository repository;

    @Test
    void consultarMensaje() {
        List<Mensaje> todos = repository.findAll();
    }

    @Test
    void consultarChat() {
        List<ConversacionProyeccion> todos = repository.obtenerConversaciones(1);
    }
}