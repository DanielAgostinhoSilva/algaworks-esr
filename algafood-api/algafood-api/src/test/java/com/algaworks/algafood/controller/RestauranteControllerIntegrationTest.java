package com.algaworks.algafood.controller;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/load_database.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/clean_database.sql")
public class RestauranteControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Test
    public void deve_mostrar_todas_os_restaurantes() {
        ParameterizedTypeReference<List<Restaurante>> tipoRetorno = new ParameterizedTypeReference<>() {
        };

        ResponseEntity<List<Restaurante>> resposta = testRestTemplate
                .exchange("/restaurantes", HttpMethod.GET, null, tipoRetorno);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(resposta.getHeaders().getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8));
        assertThat(resposta.getBody(), hasItems(
                new Restaurante(1l, "Thai Gourmet", new BigDecimal("10"), new Cozinha(1l, "Tailandesa")),
                new Restaurante(2l, "Thai Delivery", new BigDecimal("9.50"), new Cozinha(1l, "Tailandesa")),
                new Restaurante(3l, "Tuk Tuk Comida Indiana", new BigDecimal("15"), new Cozinha(2l, "Indiana"))
        ));
    }

    @Test
    public void deve_adicionar_um_restaurante() {
        Restaurante novoRestaurante = new Restaurante(
                null, "Brasileriro", new BigDecimal("10"), new Cozinha(1l, "Tailandesa"));

        ResponseEntity<Restaurante> resposta = testRestTemplate
                .exchange("/restaurantes", HttpMethod.POST, new HttpEntity<>(novoRestaurante), Restaurante.class);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(resposta.getHeaders().getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8));
        assertThat(resposta.getBody(), hasProperty("nome", equalTo(novoRestaurante.getNome())));
        assertThat(resposta.getBody(), hasProperty("taxaFrete", equalTo(new BigDecimal("10"))));
        assertThat(resposta.getBody(), hasProperty("cozinha", equalTo(new Cozinha(1l, "Tailandesa"))));
    }

    @Test
    public void quando_tentar_adicionar_um_restaurante_com_uma_cozinha_invalida_deve_retonar_status_400() {
        Restaurante novoRestaurante = new Restaurante(
                null, "Brasileriro", new BigDecimal("10"), new Cozinha(6l, "Brasileira"));

        ResponseEntity<String> resposta = testRestTemplate
                .exchange("/restaurantes", HttpMethod.POST, new HttpEntity<>(novoRestaurante), String.class);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void deve_ataulizar_um_restaurante() {
        Restaurante restauranteAtualiado = new Restaurante(
                1l, "Thai Gourmet 2", new BigDecimal("10"), new Cozinha(2l, "Tailandesa")
        );

        ResponseEntity<Restaurante> resposta = testRestTemplate
                .exchange("/restaurantes/1", HttpMethod.PUT, new HttpEntity<>(restauranteAtualiado), Restaurante.class);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(resposta.getHeaders().getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8));
        assertThat(resposta.getBody(), equalTo(restauranteAtualiado));
    }

    @Test
    public void quando_tentar_atualizar_retaurante_com_uma_cozinha_invalida_deve_retonar_status_400() {
        Restaurante restauranteAtualiado = new Restaurante(
                1l, "Thai Gourmet 2", new BigDecimal("10"), new Cozinha(6l, "Brasileira")
        );

        ResponseEntity<String> resposta = testRestTemplate
                .exchange("/restaurantes/1", HttpMethod.PUT, new HttpEntity<>(restauranteAtualiado), String.class);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void quando_tentar_ataulizar_um_restaurante_nao_encontrado_deve_retornar_staus_404() {
        Restaurante restauranteAtualiado = new Restaurante(
                5l, "Thai Gourmet 2", new BigDecimal("10"), new Cozinha(2l, "Tailandesa")
        );

        ResponseEntity<String> resposta = testRestTemplate
                .exchange("/restaurantes/5", HttpMethod.PUT, new HttpEntity<>(restauranteAtualiado), String.class);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void deve_fazer_atualizacao_parcial() {
        Map<String, Object> campos = new HashMap<>();
        campos.put("nome" , "Thai Gourmet 2");

        // e preciso ultilizar o parametro '?_method=patch' por causa de uma limitacao do restRestTemplate
        ResponseEntity<Restaurante> resposta = testRestTemplate
                .postForEntity("/restaurantes/1?_method=patch", new HttpEntity<>(campos), Restaurante.class);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(resposta.getHeaders().getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8));
        assertThat(resposta.getBody(), hasProperty("nome", equalTo("Thai Gourmet 2")));
    }

    @Test
    public void quando_tentar_fazer_uma_atualizacao_parcial_com_uma_propriedade_invalida_deve_retornar_status_400() {
        Map<String, Object> campos = new HashMap<>();
        campos.put("teste" , "123");

        // e preciso ultilizar o parametro '?_method=patch' por causa de uma limitacao do restRestTemplate
        ResponseEntity<String> resposta = testRestTemplate
                .postForEntity("/restaurantes/1?_method=patch", new HttpEntity<>(campos), String.class);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void quando_tentar_fazer_uma_atualizacao_parcial_com_uma_cozinha_invalida_deve_retornar_status_400() {
        Map<String, Object> campos = new HashMap<>();
        campos.put("cozinha" , new Cozinha(6l, "Tailandesa 2"));

        // e preciso ultilizar o parametro '?_method=patch' por causa de uma limitacao do restRestTemplate
        ResponseEntity<String> resposta = testRestTemplate
                .postForEntity("/restaurantes/1?_method=patch", new HttpEntity<>(campos), String.class);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void quando_tentar_fazer_uma_atualizacao_parcial_com_um_restaurante_invalido_deve_retornar_status_404() {
        Map<String, Object> campos = new HashMap<>();
        campos.put("cozinha" , new Cozinha(6l, "Tailandesa 2"));

        // e preciso ultilizar o parametro '?_method=patch' por causa de uma limitacao do restRestTemplate
        ResponseEntity<String> resposta = testRestTemplate
                .postForEntity("/restaurantes/6?_method=patch", new HttpEntity<>(campos), String.class);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

}
