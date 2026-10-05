package es.safareyes.swapcloset.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "foto")

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Foto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idFoto;

    @Column (name = "url")
    private String urlFoto;

    @Column (name = "orden")
    private Integer orden;

    @Enumerated(EnumType.STRING)
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prenda")
    private Prenda prenda;
}
