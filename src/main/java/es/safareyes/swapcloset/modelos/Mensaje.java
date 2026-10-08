package es.safareyes.swapcloset.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "mensaje")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Mensaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Integer idMensaje;

    @Enumerated(EnumType.STRING)
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud",nullable = false)
    private Solicitud solicitud;

    @Column(name = "fecha_envio")
    private LocalDate fechaEnvio;

    @Column(name = "hora_envio")
    private LocalTime horaEnvio;

    @Column(name = "contenido",length = 1000, nullable = false)
    private String contenido;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "envia_mensaje",
            joinColumns = @JoinColumn(name = "id_mensaje"),
            inverseJoinColumns = @JoinColumn(name = "id_perfil")
    )
    private Set<Perfil> emisores = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "recibe_mensaje",
            joinColumns = @JoinColumn(name = "id_mensaje"),
            inverseJoinColumns = @JoinColumn(name = "id_perfil")
    )

    private Set<Perfil> receptores = new HashSet<>();
}