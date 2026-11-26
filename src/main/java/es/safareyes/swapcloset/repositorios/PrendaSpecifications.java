
package es.safareyes.swapcloset.repositorios;

import es.safareyes.swapcloset.modelos.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PrendaSpecifications {
    public static Specification<Prenda> filtrar(
            Integer idCategoria,
            String talla,
            CondicionPrenda condicion,
            Integer idEstilo,
            String ciudad,
            TipoSolicitud modalidad,
            String marca,
            Color color) {

        return (root, query, cb) -> {

            List<Predicate> condiciones = new ArrayList<>();

            condiciones.add(cb.equal(
                    root.get("estado"), EstadoPrenda.DISPONIBLE));

            condiciones.add(cb.isTrue(root.get("activo")));

            condiciones.add(cb.isNotEmpty(root.get("fotos")));

            if (idCategoria != null) {
                condiciones.add(cb.equal(
                        root.get("categoria").get("idCategoria"),
                        idCategoria));
            }

            if (talla != null && !talla.isBlank()) {
                condiciones.add(cb.equal(
                        cb.lower(root.get("talla")),
                        talla.trim().toLowerCase()));
            }

            if (marca != null && !marca.isBlank()) {
                condiciones.add(cb.equal(
                        cb.lower(root.get("marca")),
                        marca.trim().toLowerCase()));
            }

            if (color != null) {
                condiciones.add(cb.equal(
                        root.get("color"), color));
            }

            if (condicion != null) {
                condiciones.add(cb.equal(
                        root.get("condicion"), condicion));
            }

            if (idEstilo != null) {

                Subquery<Integer> subconsulta =
                        query.subquery(Integer.class);

                Root<Perfil> perfil = subconsulta.from(Perfil.class);

                Join<Perfil, Estilo> estilo =
                        perfil.join("estilos");

                subconsulta.select(perfil.get("idPerfil"))
                        .where(
                                cb.equal(
                                        perfil.get("idPerfil"),
                                        root.get("perfil").get("idPerfil")),
                                cb.equal(
                                        estilo.get("idEstilo"),
                                        idEstilo)
                        );

                condiciones.add(cb.exists(subconsulta));
            }

            if (ciudad != null && !ciudad.isBlank()) {
                condiciones.add(cb.like(
                        cb.lower(root.get("perfil").get("ciudad")),
                        "%" + ciudad.trim().toLowerCase() + "%"));
            }

            if (modalidad == TipoSolicitud.INTERCAMBIO) {
                condiciones.add(cb.isTrue(
                        root.get("permiteIntercambio")));

            } else if (modalidad == TipoSolicitud.PRESTAMO) {
                condiciones.add(cb.isTrue(
                        root.get("permitePrestamo")));
            }

            return cb.and(condiciones.toArray(new Predicate[0]));
        };
    }
}
