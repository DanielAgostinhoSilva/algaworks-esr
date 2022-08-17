package com.algawoks.algafood.api.controller;

import com.algawoks.algafood.domain.exception.EntidadeEmUsoException;
import com.algawoks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algawoks.algafood.domain.model.Estado;
import com.algawoks.algafood.domain.repository.EstadoRepository;
import com.algawoks.algafood.domain.service.CadastroEstadoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/estados")
public class EstadoController {
    private EstadoRepository estadoRepository;
    private CadastroEstadoService cadastroEstado;

    @GetMapping
    public List<Estado> listar() {
        return estadoRepository.findAll();
    }

    @GetMapping("/{estadoId}")
    public ResponseEntity<Estado> buscar(@PathVariable Long estadoId) {
        Optional<Estado> estado = estadoRepository.findById(estadoId);

        if (estado.isPresent()) {
            return ResponseEntity.ok(estado.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Estado adicionar(@RequestBody Estado estado) {
        return cadastroEstado.salvar(estado);
    }

    @PutMapping("/{estadoId}")
    public ResponseEntity<Estado> atualizar(@PathVariable Long estadoId,
                                            @RequestBody Estado estado) {
        Estado estadoAtual = estadoRepository.findById(estadoId).orElse(null);

        if (estadoAtual != null) {
            BeanUtils.copyProperties(estado, estadoAtual, "id");

            estadoAtual = cadastroEstado.salvar(estadoAtual);
            return ResponseEntity.ok(estadoAtual);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{estadoId}")
    public ResponseEntity<?> remover(@PathVariable Long estadoId) {
        try {
            cadastroEstado.excluir(estadoId);
            return ResponseEntity.noContent().build();

        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.notFound().build();

        } catch (EntidadeEmUsoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }
}
