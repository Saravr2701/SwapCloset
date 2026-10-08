package es.safareyes.swapcloset.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")

@Getter @Setter
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
@NoArgsConstructor @AllArgsConstructor
@ToString
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Integer idUsuario;

    @Column(name = "nombreusu", unique = true, nullable = false, length = 50)
    private String nombreusu;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol",nullable = false)
    private RolUsuario rol;

    @Column(name = "contrasena",nullable = false, length = 250)
    private String contrasena;
}
