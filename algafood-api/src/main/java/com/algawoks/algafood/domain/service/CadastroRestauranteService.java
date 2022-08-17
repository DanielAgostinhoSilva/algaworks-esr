package com.algawoks.algafood.domain.service;

import com.algawoks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algawoks.algafood.domain.model.Cozinha;
import com.algawoks.algafood.domain.model.Restaurante;
import com.algawoks.algafood.domain.repository.CozinhaRepository;
import com.algawoks.algafood.domain.repository.RestauranteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CadastroRestauranteService {
    private RestauranteRepository restauranteRepository;
    private CozinhaRepository cozinhaRepository;

    public Restaurante salvar(Restaurante restaurante) {
        Long cozinhaId = restaurante.getCozinha().getId();

        Cozinha cozinha = cozinhaRepository.findById(cozinhaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        String.format("Não existe cadastro de cozinha com código %d", cozinhaId)));

        restaurante.setCozinha(cozinha);

        return restauranteRepository.save(restaurante);
    }
}
