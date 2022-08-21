package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.model.EnderecoModel;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.domain.model.Endereco;
import com.algaworks.algafood.domain.model.Restaurante;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RestauranteModelAssembler {

	private final ModelMapper modelMapper;

	public RestauranteModelAssembler(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
		configureMapping();
	}

	public RestauranteModel toModel(Restaurante restaurante) {
		return modelMapper.map(restaurante, RestauranteModel.class);
	}
	
	public List<RestauranteModel> toCollectionModel(List<Restaurante> restaurantes) {
		return restaurantes.stream()
				.map(this::toModel)
				.collect(Collectors.toList());
	}

	private void configureMapping(){
		modelMapper.createTypeMap(Endereco.class, EnderecoModel.class)
				.<String>addMapping(
						endereco -> endereco.getCidade().getEstado().getNome(),
						((enderecoModel, value) -> enderecoModel.getCidade().setEstado(value))
				);
	}
	
}