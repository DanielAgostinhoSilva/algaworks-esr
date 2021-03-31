package com.algaworks.algafood.infrastructure.repository;

import com.algaworks.algafood.domain.model.Restaurante;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryImplQueries {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<Restaurante> consultarPorNome(String nome, BigDecimal taxaInicial, BigDecimal taxaFinal) {
        var jpql = "from Restaurante where nome like :nome and taxaFrete between :taxaInicial and :taxaFinal";
        return manager.createQuery(jpql, Restaurante.class)
                .setParameter("nome", "%" + nome + "%")
                .setParameter("taxaInicial", taxaInicial)
                .setParameter("taxaFinal", taxaFinal)
                .getResultList();
    }
}
