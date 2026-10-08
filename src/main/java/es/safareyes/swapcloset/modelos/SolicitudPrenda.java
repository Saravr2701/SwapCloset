package es.safareyes.swapcloset.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "solicitud_prenda")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SolicitudPrenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Integer id_SolicitudPrenda;

    @Enumerated(EnumType.STRING)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", nullable = false)
    private Solicitud solicitud;

    @Enumerated(EnumType.STRING)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prenda", nullable = false)
    private Prenda prenda;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol_prenda")
    private RolPrenda rolPrenda;
}
