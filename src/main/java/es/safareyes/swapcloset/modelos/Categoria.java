package es.safareyes.swapcloset.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categorias")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idCategoria;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @OneToMany (mappedBy ="categoria", fetch = FetchType.LAZY)
    private Set<Prenda> prendas = new HashSet<>();
}
