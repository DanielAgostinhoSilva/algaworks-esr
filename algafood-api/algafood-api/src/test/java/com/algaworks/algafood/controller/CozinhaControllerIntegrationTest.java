package com.algaworks.algafood.controller;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/load_database.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/clean_database.sql")
public class CozinhaControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Test
    public void deve_mostrar_todas_as_cozinhas() {
        ParameterizedTypeReference<List<Cozinha>> tipoRetorno = new ParameterizedTypeReference<>(){};

        ResponseEntity<List<Cozinha>> resposta = testRestTemplate
                .exchange("/cozinhas", HttpMethod.GET, null, tipoRetorno);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(resposta.getHeaders().getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8));
        assertThat(resposta.getBody(), hasItems(
                new Cozinha(1l, "Tailandesa"),
                new Cozinha(2l, "Indiana")
        ));
    }

    @Test
    public void deve_buscar_uma_cozinha_por_id() {
        ResponseEntity<Cozinha> resposta = testRestTemplate
                .exchange("/cozinhas/1", HttpMethod.GET, null, Cozinha.class);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(resposta.getHeaders().getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8));
        assertThat(resposta.getBody(), equalTo(new Cozinha(1l, "Tailandesa")));
    }

    @Test
    public void quando_uma_cozinha_nao_for_encontrada_deve_retorna_status_404() {
        Cozinha novaCozinha = getCozinhaBrasileira();

        ResponseEntity<Cozinha> resposta = testRestTemplate
                .postForEntity("/cozinhas", novaCozinha, Cozinha.class);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(resposta.getHeaders().getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8));
        assertThat(resposta.getBody(), equalTo(novaCozinha));
    }

    @Test
    public void deve_atualizar_uma_cozinha() {

        Cozinha cozinha = new Cozinha(1l, "Brasileira");

        ResponseEntity<Cozinha> resposta = testRestTemplate
                .exchange("/cozinhas/1" , HttpMethod.PUT, new HttpEntity<>(cozinha), Cozinha.class);

        Optional<Cozinha> cozinhaOptional = cozinhaRepository.findById(cozinha.getId());

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(resposta.getHeaders().getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8));
        assertThat(resposta.getBody(), equalTo(cozinha));
        assertThat(cozinhaOptional.isPresent(), is(true));
        assertThat(cozinhaOptional.get(), equalTo(cozinha));
    }

    @Test
    public void quando_uma_cozinha_a_ser_atualizado_nao_for_encontrada_deve_retorna_status_404() {
        Cozinha cozinhaBrasileira = getCozinhaBrasileira();

        ResponseEntity<Cozinha> resposta = testRestTemplate
                .exchange("/cozinhas/3", HttpMethod.PUT, new HttpEntity<>(cozinhaBrasileira), Cozinha.class);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void deve_remover_uma_cozinha_pelo_id() {
        Cozinha cozinhaSalva = cozinhaRepository.save(getCozinhaBrasileira());

        ResponseEntity<Cozinha> resposta = testRestTemplate
                .exchange("/cozinhas/" + cozinhaSalva.getId() , HttpMethod.DELETE, null, Cozinha.class);

        Optional<Cozinha> cozinhaRemovida = cozinhaRepository.findById(cozinhaSalva.getId());

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
        assertThat(cozinhaRemovida.isEmpty(), is(true));
    }

    @Test
    public void quando_tentar_remover_um_cozinha_em_uso_deve_retornar_status_409() {
        ResponseEntity<Cozinha> resposta = testRestTemplate
                .exchange("/cozinhas/1", HttpMethod.DELETE, null, Cozinha.class);

        Optional<Cozinha> cozinhaRemovida = cozinhaRepository.findById(1l);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.CONFLICT));
        assertThat(cozinhaRemovida.isPresent(), is(true));
    }

    @Test
    public void quando_tentar_remover_uma_cozinha_nao_encontrada_deve_retornar_status404() {
        ResponseEntity<Cozinha> resposta = testRestTemplate
                .exchange("/cozinhas/3", HttpMethod.DELETE, null, Cozinha.class);

        Optional<Cozinha> cozinhaRemovida = cozinhaRepository.findById(3l);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertThat(cozinhaRemovida.isEmpty(), is(true));
    }

    private Cozinha getCozinhaBrasileira() {
        return new Cozinha(3l, "Brasileira");
    }
}
