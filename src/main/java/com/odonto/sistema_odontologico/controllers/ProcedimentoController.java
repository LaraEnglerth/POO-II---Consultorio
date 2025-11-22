package com.odonto.sistema_odontologico.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
 
import com.odonto.sistema_odontologico.dto.ProcedimentoDTO;
import com.odonto.sistema_odontologico.models.Procedimento;
import com.odonto.sistema_odontologico.services.ProcedimentoService;

import jakarta.validation.Valid;

/**
 * SRP requisições HTTP de Procedimento
 */
@RestController
@RequestMapping("/api/procedimentos")
@CrossOrigin(origins = "*")
public class ProcedimentoController {

    @Autowired
    private ProcedimentoService procedimentoService;

    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody ProcedimentoDTO dto) {
        try {
            Procedimento novoProcedimento = procedimentoService.cadastrarProcedimento(dto);
            return new ResponseEntity<>(novoProcedimento, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao cadastrar procedimento: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Procedimento>> listarTodos() {
        try {
            List<Procedimento> procedimentos = procedimentoService.listarTodos();
            if (procedimentos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(procedimentos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Optional<Procedimento> procedimento = procedimentoService.buscarPorId(id);
            if (procedimento.isPresent()) {
                return ResponseEntity.ok(procedimento.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Procedimento não encontrado com ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Procedimento>> buscarPorNome(@RequestParam String nome) {
        try {
            List<Procedimento> procedimentos = procedimentoService.buscarPorNome(nome);
            if (procedimentos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(procedimentos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Procedimento>> buscarPorPaciente(@PathVariable Long pacienteId) {
        try {
            List<Procedimento> procedimentos = procedimentoService.buscarPorPaciente(pacienteId);
            if (procedimentos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(procedimentos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/com-assistente")
    public ResponseEntity<List<Procedimento>> buscarPorAssistente(@RequestParam Boolean valor) {
        try {
            List<Procedimento> procedimentos = procedimentoService.buscarPorAssistente(valor);
            if (procedimentos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(procedimentos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            procedimentoService.deletarProcedimento(id);
            return ResponseEntity.ok("Procedimento deletado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}/detalhamento")
    public ResponseEntity<?> gerarDetalhamentoCalculo(@PathVariable Long id) {
        try {
            String detalhamento = procedimentoService.gerarDetalhamentoCalculo(id);
            return ResponseEntity.ok(detalhamento);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }
}
