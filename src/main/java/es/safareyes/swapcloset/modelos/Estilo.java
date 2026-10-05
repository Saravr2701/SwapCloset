package es.safareyes.swapcloset.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estilos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class Estilo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idEstilo;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @ManyToMany(mappedBy = "estilos",fetch = FetchType.LAZY)
    private java.util.Set<Perfil> perfiles = new java.util.HashSet<>();
}
