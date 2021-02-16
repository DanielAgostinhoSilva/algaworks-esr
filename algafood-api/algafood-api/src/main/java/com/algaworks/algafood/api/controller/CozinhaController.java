package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cozinhas")
@AllArgsConstructor
public class CozinhaController {

    private CozinhaRepository cozinhaRepository;
    private CadastroCozinhaService cadastroCozinha;

    @GetMapping
    public List<Cozinha> listar() {
        return cozinhaRepository.listar();
    }

    @GetMapping("/{cozinhaId}")
    public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId){
        Cozinha cozinha = cozinhaRepository.buscar(cozinhaId);

        if (cozinha != null)
            return ResponseEntity.ok(cozinha);

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cozinha adicionar(@RequestBody Cozinha cozinha) {
        return cadastroCozinha.salvar(cozinha);
    }

    @PutMapping ("/{cozinhaId}")
    public ResponseEntity<Cozinha> atualizar(@PathVariable Long cozinhaId, @RequestBody Cozinha cozinha){
        Cozinha conzinhaEncontrada = cozinhaRepository.buscar(cozinhaId);

        if (conzinhaEncontrada == null)
            return ResponseEntity.notFound().build();

        BeanUtils.copyProperties(cozinha, conzinhaEncontrada, "id" );

        return ResponseEntity.ok(cozinhaRepository.salvar(conzinhaEncontrada));
    }


    @DeleteMapping("/{cozinhaId}")
    public ResponseEntity<Cozinha> remover(@PathVariable Long cozinhaId){
        Cozinha cozinha = cozinhaRepository.buscar(cozinhaId);

        if (cozinha == null)
            return ResponseEntity.notFound().build();

        try {
            cozinhaRepository.remover(cozinha);

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.noContent().build();
    }
}
