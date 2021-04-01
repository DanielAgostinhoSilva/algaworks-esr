package com.algaworks.algafood.infrastructure.repository;

import com.algaworks.algafood.domain.model.Restaurante;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryImplQueries {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<Restaurante> consultarPorNome(String nome, BigDecimal taxaInicial, BigDecimal taxaFinal) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();

        CriteriaQuery<Restaurante> criteria = builder.createQuery(Restaurante.class);

        Root<Restaurante> root = criteria.from(Restaurante.class);

        Predicate nomePredicate = builder.like(root.get("nome"), "%" + nome + "%");
        Predicate taxaInicialPredicate = builder.greaterThanOrEqualTo(root.get("taxaFrete"), taxaInicial);
        Predicate taxaFinalPredicate = builder.lessThanOrEqualTo(root.get("taxaFrete"), taxaFinal);

        criteria.where(nomePredicate, taxaInicialPredicate, taxaFinalPredicate);

        return manager.createQuery(criteria).getResultList();
    }
}
