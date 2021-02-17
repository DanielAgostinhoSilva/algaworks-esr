package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;

import java.util.List;


public interface CidadeRepository {
    List<Cidade> todas();
    Cidade buscar(Long id);
    Cidade salvar(Cidade idade);
    void remover(Long id);
}
