package es.safareyes.swapcloset.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "prenda")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Prenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idPrenda;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "marca")
    private String marca;

    @Column(name = "talla")
    private String talla;

    @Column(name = "fecha_publicacion")
    private LocalDate fechaPublicacion;

    @Column(name = "permite_intercambio")
    private Boolean permiteIntercambio;

    @Column(name = "permite_prestamo")
    private Boolean permitePrestamo;

    @Enumerated(EnumType.STRING)
    @Column(name = "condicion")
    private CondicionPrenda condicion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoPrenda estado;

    @Enumerated(EnumType.STRING)
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_perfil", nullable = false)
    private Perfil perfil;

    @Enumerated(EnumType.STRING)
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categorias", nullable = false)
    private Categoria categoria;

    @OneToMany(mappedBy = "prenda", fetch = FetchType.LAZY)
    private Set<Foto> fotos = new HashSet<>();

    @OneToMany(mappedBy = "prenda", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<SolicitudPrenda> solicitudes = new HashSet<>();
}
