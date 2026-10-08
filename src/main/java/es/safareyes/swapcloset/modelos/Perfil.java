package es.safareyes.swapcloset.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "perfil")

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Integer idPerfil;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "ape1")
    private String ape1;

    @Column(name = "ape2")
    private String ape2;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "ciudad")
    private String ciudad;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "deposito")
    private Double deposito;

    @Column(name = "nivel")
    private Integer nivel;

    @Column(name = "biografia")
    private String biografia;

    @Enumerated(EnumType.STRING)
    @Column(name = "talla_pantalon", nullable = false)
    private TallaPantalon tallaPantalon;

    @Enumerated(EnumType.STRING)
    @Column(name = "talla_zapatos", nullable = false)
    private TallaZapatos tallaZapatos;

    @Enumerated(EnumType.STRING)
    @Column(name = "talla_camiseta", nullable = false)
    private TallaCamiseta tallaCamiseta;

    @ManyToMany(mappedBy = "emisores", fetch = FetchType.LAZY)
    private Set<Mensaje> mensajesEnviados = new HashSet<>();

    @ManyToMany(mappedBy = "receptores", fetch = FetchType.LAZY)
    private Set<Mensaje> mensajesRecibidos = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "estilos_perfil",
            joinColumns = @JoinColumn(name = "id_perfil"),
            inverseJoinColumns = @JoinColumn(name = "id_estilo")
    )
    private java.util.Set<Estilo> estilos = new HashSet<>();

    @OneToMany(mappedBy = "perfil", fetch = FetchType.LAZY)
    private Set<Notificacion> notificaciones = new HashSet<>();

    @OneToMany(mappedBy = "perfil", fetch = FetchType.LAZY)
    private Set<Solicitud> solicitudes = new HashSet<>();

    @OneToMany(mappedBy = "perfil", fetch = FetchType.LAZY)
    private Set<Valoracion> valoraciones = new HashSet<>();

    @OneToMany(mappedBy = "perfil", fetch = FetchType.LAZY)
    private Set<Prenda> prendas = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
