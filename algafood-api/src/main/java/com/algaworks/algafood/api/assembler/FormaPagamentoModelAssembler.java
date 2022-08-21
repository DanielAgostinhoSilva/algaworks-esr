package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.domain.model.FormaPagamento;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class FormaPagamentoModelAssembler {

    private ModelMapper modelMapper;
    
    public FormaPagamentoModel toModel(FormaPagamento formaPagamento) {
        return modelMapper.map(formaPagamento, FormaPagamentoModel.class);
    }
    
    public List<FormaPagamentoModel> toCollectionModel(List<FormaPagamento> formasPagamentos) {
        return formasPagamentos.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
    
}