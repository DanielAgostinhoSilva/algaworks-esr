package com.algaworks.algafood.infrastructure.repository;

import com.algaworks.algafood.domain.model.Restaurante;

import java.math.BigDecimal;
import java.util.List;

public interface RestauranteRepositoryImplQueries {
    List<Restaurante> consultarPorNome(String nome, BigDecimal taxaInicial, BigDecimal taxaFinal);
}
