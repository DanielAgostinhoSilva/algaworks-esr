package com.algaworks.algafood.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode( onlyExplicitlyIncluded = true)
@Entity
public class Permissao {


    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( name = "nome", nullable = false)
    private String nome;

    @Column( name = "descricao", nullable = false)
    private String descricao;
}
