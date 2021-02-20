package com.algaworks.algafood.controller;

import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
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
public class EstadoControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private EstadoRepository estadoRepository;

    @Test
    public void deve_mostrar_todas_os_estados() {
        ParameterizedTypeReference<List<Estado>> tipoRetorno = new ParameterizedTypeReference<>() {
        };

        ResponseEntity<List<Estado>> resposta = testRestTemplate
                .exchange("/estados", HttpMethod.GET, null, tipoRetorno);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(resposta.getHeaders().getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8));
        assertThat(resposta.getBody(), hasItems(
                new Estado(1l, "Minas Gerais"),
                new Estado(2l, "São Paulo"),
                new Estado(3l, "Ceará")
        ));
    }

    @Test
    public void deve_adicinar_um_estado() {
        Estado estado = new Estado(null, "Pernambuco");

        ResponseEntity<Estado> resposta = testRestTemplate
                .exchange("/estados", HttpMethod.POST, new HttpEntity<>(estado), Estado.class);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(resposta.getHeaders().getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8));
        assertThat(resposta.getBody(), hasProperty("nome", equalTo("Pernambuco")));
    }

    @Test
    public void deve_atualizar_um_estado() {
        Estado estadoAtualizado = new Estado(2l, "São Paulo 2");

        ResponseEntity<Estado> resposta = testRestTemplate
                .exchange("/estados/2", HttpMethod.PUT, new HttpEntity<>(estadoAtualizado), Estado.class);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(resposta.getHeaders().getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8));
        assertThat(resposta.getBody(), equalTo(estadoAtualizado));
    }

    @Test
    public void quando_tentar_atualizar_um_estado_que_nao_existe_deve_retornar_status_404() {
        Estado estadoInvalido = new Estado(4l, "Ceará 2");

        ResponseEntity<Estado> resposta = testRestTemplate
                .exchange("/estados/4", HttpMethod.PUT, new HttpEntity<>(estadoInvalido), Estado.class);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void deve_remer_um_estado() {
        Estado estadoRemovido = estadoRepository.save(new Estado(4l, "Ceará 2"));

        ResponseEntity<Estado> resposta = testRestTemplate
                .exchange("/estados/" + estadoRemovido.getId(), HttpMethod.DELETE, null, Estado.class);

        Optional<Estado> estadoOptional = estadoRepository.findById(estadoRemovido.getId());

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
        assertThat(estadoOptional.isEmpty(), equalTo(true));
    }

    @Test
    public void quando_tentar_remover_um_estado_nao_encontrado_deve_retornar_status_404() {
        ResponseEntity<Estado> resposta = testRestTemplate
                .exchange("/estados/4", HttpMethod.DELETE, null, Estado.class);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void quando_tentar_remover_um_estado_que_esta_sendo_usado_deve_retornar_status_409() {
        ResponseEntity<Estado> resposta = testRestTemplate
                .exchange("/estados/1", HttpMethod.DELETE, null, Estado.class);

        assertThat(resposta.getStatusCode(), equalTo(HttpStatus.CONFLICT));
    }
}
