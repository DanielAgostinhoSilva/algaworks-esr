package com.algawoks.algafood.domain.service;

import com.algawoks.algafood.domain.exception.EntidadeEmUsoException;
import com.algawoks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algawoks.algafood.domain.model.Cozinha;
import com.algawoks.algafood.domain.repository.CozinhaRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@AllArgsConstructor
public class CadastroCozinhaService {
    private CozinhaRepository cozinhaRepository;
    @PersistenceContext
    private EntityManager manager;

    public Cozinha salvar(Cozinha cozinha) {
        return cozinhaRepository.save(cozinha);
    }

    public void excluir(Long cozinhaId) {
        try {
            cozinhaRepository.deleteById(cozinhaId);

        } catch (EmptyResultDataAccessException e) {
            throw new EntidadeNaoEncontradaException(
                    String.format("Não existe um cadastro de cozinha com código %d", cozinhaId));

        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format("Cozinha de código %d não pode ser removida, pois está em uso", cozinhaId));
        }
    }
}
