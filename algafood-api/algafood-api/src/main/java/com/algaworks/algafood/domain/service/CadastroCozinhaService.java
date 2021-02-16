package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CadastroCozinhaService {

    private final String ENTIDADE_COZINHA_EM_USO = "Cozinha de codigo %d nao pode ser removida, pois esta em uso";
    private final String ENTIDADE_COZINHA_NAO_ENCONTRADA = "Nao existe um cadastro de cozinha com codigo %d";
    private CozinhaRepository cozinhaRepository;

    public Cozinha salvar(Cozinha cozinha) {
        return cozinhaRepository.salvar(cozinha);
    }

    public void excluir(Long id) {
        try {
            cozinhaRepository.remover(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntidadeNaoEncontradaException(String.format(ENTIDADE_COZINHA_NAO_ENCONTRADA, id));
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(String.format(ENTIDADE_COZINHA_EM_USO, id));
        }
    }
}
