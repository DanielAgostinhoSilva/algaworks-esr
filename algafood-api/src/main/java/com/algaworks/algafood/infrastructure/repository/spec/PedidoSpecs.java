package com.algaworks.algafood.infrastructure.repository.spec;

import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.repository.filter.PedidoFilter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class PedidoSpecs {

    public static Specification<Pedido> usandoFiltro(PedidoFilter filtro) {
        return (root, query, builder) -> {
            root.fetch("restaurante").fetch("cozinha");
            root.fetch("cliente");

            var predicates = new ArrayList<Predicate>();

            if(isNotNull(filtro.getClienteId())) {
                predicates.add(builder.equal(root.get("cliente"), filtro.getClienteId()));
            }

            if(isNotNull(filtro.getRestauranteId())) {
                predicates.add(builder.equal(root.get("restaurante"), filtro.getRestauranteId()));
            }

            if(isNotNull(filtro.getDataCriacaoInicio())) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoInicio()));
            }

            if(isNotNull(filtro.getDataCriacaoFim())) {
                predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoFim()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static boolean isNotNull(Object object) {
        return object != null;
    }
}
