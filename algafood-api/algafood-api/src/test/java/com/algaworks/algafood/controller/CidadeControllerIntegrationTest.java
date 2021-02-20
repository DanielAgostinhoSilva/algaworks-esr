package com.algaworks.algafood.controller;

import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.CidadeRepository;
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
public class CidadeControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Test
    public void deve_mostrar_todas_as_cidades() {
        ParameterizedTypeReference<List<Cidade>> tipoRetorno = new ParameterizedTypeReference<>() {
        };

        ResponseEntity<List<Cidade>> resposta = testRestTemplate
                .exchange("/cidades", HttpMethod.GET, null, tipoRetorno);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(resposta.getHeaders().getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8));
        assertThat(resposta.getBody(), hasItems(
                new Cidade(1l, "Uberlândia", new Estado(1l, "Minas Gerais")),
                new Cidade(2l, "Belo Horizonte", new Estado(1l, "Minas Gerais")),
                new Cidade(3l, "São Paulo", new Estado(2l, "São Paulo")),
                new Cidade(4l, "Campinas", new Estado(2l, "São Paulo")),
                new Cidade(5l, "Fortaleza", new Estado(3l, "Ceará"))
        ));
    }

    @Test
    public void deve_buscar_uma_cidade_por_id() {
        ResponseEntity<Cidade> resposta = testRestTemplate
                .exchange("/cidades/1", HttpMethod.GET, null, Cidade.class);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(resposta.getHeaders().getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8));
        assertThat(resposta.getBody(), equalTo(new Cidade(1l, "Uberlândia", new Estado(1l, "Minas Gerais"))));
    }

    @Test
    public void quando_uma_cidades_nao_for_encontrada_deve_retorna_status_404() {
        ResponseEntity<Cidade> resposta = testRestTemplate
                .exchange("/cidades/6", HttpMethod.GET, null, Cidade.class);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void deve_adicionar_uma_cidade() {
        Cidade novaCidade = new Cidade(6l, "Santo Andre", new Estado(2l, "São Paulo"));

        ResponseEntity<Cidade> resposta = testRestTemplate.postForEntity("/cidades", new HttpEntity<>(novaCidade), Cidade.class);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(resposta.getHeaders().getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8));
        assertThat(resposta.getBody(), equalTo(novaCidade));
    }

    @Test
    public void quando_tentar_adicionar_uma_cidade_com_um_estado_invalido_deve_retonar_status_400() {
        Cidade novaCidade = new Cidade(null, "Santo Andre", new Estado(4l, null));

        ResponseEntity<String> resposta = testRestTemplate
                .exchange("/cidades", HttpMethod.POST, new HttpEntity<>(novaCidade), String.class);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void deve_atualizar_uma_cidade() {
        Cidade cidadeAlterada = new Cidade(1l, "Uberlândia 2", new Estado(1l, "Minas Gerais"));


        ResponseEntity<Cidade> resposta = testRestTemplate
                .exchange("/cidades/1", HttpMethod.PUT, new HttpEntity<>(cidadeAlterada), Cidade.class);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(resposta.getHeaders().getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8));
        assertThat(resposta.getBody(), equalTo(cidadeAlterada));

    }

    @Test
    public void quando_tentar_atualizar_uma_cidade_que_nao_existe_deve_retornar_status_404() {
        Cidade cidadeAlterada = new Cidade(6l, "Teste 2", new Estado(1l, "Minas Gerais"));

        ResponseEntity<Cidade> resposta = testRestTemplate
                .exchange("/cidades/6", HttpMethod.PUT, new HttpEntity<>(cidadeAlterada), Cidade.class);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void quando_tentar_atualizar_uma_cidade_com_um_estado_invalido_deve_retornar_status_400() {
        Cidade cidadeAlterada = new Cidade(1l, "Uberlândia 2", new Estado(6l, "Minas Gerais"));

        ResponseEntity<String> resposta = testRestTemplate
                .exchange("/cidades/1", HttpMethod.PUT, new HttpEntity<>(cidadeAlterada), String.class);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void deve_remover_uma_cidade() {
        Cidade cidade = cidadeRepository.save(new Cidade(6l, "Santo Andre", new Estado(2l, "São Paulo")));

        ResponseEntity<String> resposta = testRestTemplate
                .exchange("/cidades/" + cidade.getId(), HttpMethod.DELETE, null, String.class);

        Optional<Cidade> cidadeOptional = cidadeRepository.findById(cidade.getId());

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
        assertThat(cidadeOptional.isEmpty(), equalTo(true));
    }

    @Test
    public void quando_tentar_remover_uma_cidade_nao_encontrada_deve_retornar_status_404() {
        ResponseEntity<String> resposta = testRestTemplate
                .exchange("/cidades/6", HttpMethod.DELETE, null, String.class);

        Optional<Cidade> cidadeOptional = cidadeRepository.findById(6l);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertThat(cidadeOptional.isEmpty(), equalTo(true));
    }
}
