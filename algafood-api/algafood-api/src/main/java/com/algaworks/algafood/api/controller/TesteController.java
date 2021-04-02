package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import static com.algaworks.algafood.infrastructure.repository.spec.RestauranteSpecs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
@RestController
@RequestMapping("/teste")
public class TesteController {

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @GetMapping("/cozinha/por-nome")
    public List<Cozinha> cozinhasPorNome(@RequestParam("nome") String nome) {
        return cozinhaRepository.findByNomeContaining(nome);
    }

    @GetMapping("/cozinha/existe")
    public boolean cozinhasExiste(@RequestParam("nome") String nome) {
        return cozinhaRepository.existsByNome(nome);
    }

    @GetMapping("/restaurante/primeiro-por-nome")
    public Optional<Restaurante> buscarPrimeiroRestuarantePorNome(@RequestParam("nome") String nome) {
        return restauranteRepository.findFirstByNomeContaining(nome);
    }

    @GetMapping("/restaurante/dois-primeiro-por-nome")
    public List<Restaurante> buscarDoisPrimeiroPorNome(@RequestParam("nome") String nome) {
        return restauranteRepository.findTop2ByNomeContaining(nome);
    }

    @GetMapping("/restaurante/por-nome")
    public List<Restaurante> consultarPorNome(String nome, Long cozinhaId) {
        return restauranteRepository.consultarPorNome(nome, cozinhaId);
    }

    @GetMapping("/restaurante/por-taxa-frete")
    public List<Restaurante> consultarPortaxafrete(String nome, BigDecimal taxaInicial, BigDecimal taxaFinal) {
        return restauranteRepository.consultarPorNome(nome, taxaInicial, taxaFinal);
    }

    @GetMapping("/restaurante/count-cozinha")
    public Integer cozinhasPorNome(Long cozinhaId) {
        return restauranteRepository.countByCozinhaId(cozinhaId);
    }

    @GetMapping("/restaurante/com-frete-gratis")
    public List<Restaurante> restaurantesComFreteGratis(String nome) {
        return restauranteRepository.findComFreteGratis(nome);
    }
}
