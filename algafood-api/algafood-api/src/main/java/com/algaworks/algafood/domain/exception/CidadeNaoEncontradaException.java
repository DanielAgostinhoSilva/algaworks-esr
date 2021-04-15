package com.algaworks.algafood.domain.exception;

public class CidadeNaoEncontradaException extends EntidadeNaoEncontradaException {
    public static final String MSG_CIDADE_NAO_ENCONTRADA = "Nao existe cadastro de cidade com codigo %d";

    public CidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }

    public CidadeNaoEncontradaException(Long cidadeId) {
        this(String.format(MSG_CIDADE_NAO_ENCONTRADA, cidadeId));
    }
}
