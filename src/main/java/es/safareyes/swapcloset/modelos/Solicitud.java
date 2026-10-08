package es.safareyes.swapcloset.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "solicitud")

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Integer idSolicitud;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_perfil", nullable = false)
    private Perfil perfil;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    @Column(name = "fecha_respuesta")
    private LocalDate fechaRespuesta;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_solicitud")
    private TipoSolicitud tipoSolicitud;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoSolicitud estadoSolicitud;

    @OneToMany(mappedBy = "solicitud", fetch = FetchType.LAZY)
    private Set<Mensaje> mensajes = new HashSet<>();

    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<SolicitudPrenda> SolicitudesPrendas = new HashSet<>();

}
