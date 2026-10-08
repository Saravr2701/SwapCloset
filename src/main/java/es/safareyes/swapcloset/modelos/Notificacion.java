package es.safareyes.swapcloset.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;
import java.util.*;

@Entity
@Table(name = "notificacion")

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Integer idNotificacion;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_perfil", nullable = false)
    private Perfil perfil;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoNotificacion tipoNotificacion;

    @Column(name = "mensaje")
    private String mensaje;

    @Column(name = "leida", nullable = false)
    private Boolean leida;

    @Column(name = "fecha")
    private LocalDate fecha;
}
