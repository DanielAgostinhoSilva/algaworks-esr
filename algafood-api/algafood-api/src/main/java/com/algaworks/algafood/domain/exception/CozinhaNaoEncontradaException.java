package com.algaworks.algafood.domain.exception;

public class CozinhaNaoEncontradaException extends EntidadeNaoEncontradaException {
    private static final String MSG_COZINHA_NAO_ENCONTRADA = "Nao existe um cadastro de cozinha com codigo %d";

    public CozinhaNaoEncontradaException(String mensagem) {
        super(mensagem);
    }

    public CozinhaNaoEncontradaException(Long cozinhaId) {
        this(String.format(MSG_COZINHA_NAO_ENCONTRADA, cozinhaId));
    }
}
