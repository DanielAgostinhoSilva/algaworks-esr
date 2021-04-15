package com.algaworks.algafood.domain.exception;

public class RestauranteNaoEncontradoException extends EntidadeNaoEncontradaException {
    public static final String MSG_RESTURANTE_NAO_ENCONTRADO = "Nao existe cadastro de restaurante com codigo %d";

    public RestauranteNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public RestauranteNaoEncontradoException(Long restauranteId) {
        this(String.format(MSG_RESTURANTE_NAO_ENCONTRADO, restauranteId));
    }
}
