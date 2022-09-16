package com.algaworks.algafood.api.model.view;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum RestauranteProjecaoView {

    DEFAULT("default", null),
    RESUMIDO("resumo", RestauranteView.Resumo.class),
    APENAS_NOME("apenas-nome", RestauranteView.ApenasNome.class);

    private String projecao;
    private Class view;

    RestauranteProjecaoView(String projecao, Class view) {
        this.projecao = projecao;
        this.view = view;
    }

    public static RestauranteProjecaoView getProjecaoView(String projecao) {
        return Arrays.stream(RestauranteProjecaoView.values())
                .filter(view -> view.projecao.equalsIgnoreCase(projecao))
                .findFirst()
                .orElse(DEFAULT);
    }
}
