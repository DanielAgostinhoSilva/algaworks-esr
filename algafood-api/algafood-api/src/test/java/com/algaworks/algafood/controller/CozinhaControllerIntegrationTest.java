package com.algaworks.algafood;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlgafoodApiApplicationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void deveMostarTodasAsCozinhas() throws Exception {
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
}
