package com.algaworks.algafood.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@EqualsAndHashCode( onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
public class Cozinha {

    @EqualsAndHashCode.Include
    @Id
    private Long id;
    private String nome;
}
