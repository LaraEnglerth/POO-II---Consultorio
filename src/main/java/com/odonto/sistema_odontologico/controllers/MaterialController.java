package com.odonto.sistema_odontologico.controllers;

import com.odonto.sistema_odontologico.models.Material;
import com.odonto.sistema_odontologico.services.MaterialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller responsável APENAS por receber requisições HTTP de Material (SRP - Single Responsibility Principle)
 */
@RestController
@RequestMapping("/api/materiais")
@CrossOrigin(origins = "*")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody Material material) {
        try {
            Material novoMaterial = materialService.cadastrarMaterial(material);
            return new ResponseEntity<>(novoMaterial, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao cadastrar material: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Material>> listarTodos() {
        try {
            List<Material> materiais = materialService.listarTodos();
            if (materiais.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(materiais, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Optional<Material> material = materialService.buscarPorId(id);
            if (material.isPresent()) {
                return ResponseEntity.ok(material.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Material não encontrado com ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Material>> buscarPorNome(@RequestParam String nome) {
        try {
            List<Material> materiais = materialService.buscarPorNome(nome);
            if (materiais.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(materiais, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/reutilizaveis")
    public ResponseEntity<List<Material>> buscarPorReutilizavel(@RequestParam Boolean valor) {
        try {
            List<Material> materiais = materialService.buscarPorReutilizavel(valor);
            if (materiais.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(materiais, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/estoque-baixo")
    public ResponseEntity<List<Material>> buscarEstoqueBaixo(@RequestParam Integer quantidade) {
        try {
            List<Material> materiais = materialService.buscarEstoqueBaixo(quantidade);
            if (materiais.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(materiais, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id,
                                       @Valid @RequestBody Material material) {
        try {
            Material materialAtualizado = materialService.atualizarMaterial(id, material);
            return new ResponseEntity<>(materialAtualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            materialService.deletarMaterial(id);
            return ResponseEntity.ok("Material deletado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}/adicionar-estoque")
    public ResponseEntity<?> adicionarEstoque(@PathVariable Long id,
                                              @RequestParam Integer quantidade) {
        try {
            Material material = materialService.adicionarEstoque(id, quantidade);
            return new ResponseEntity<>(material, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}/remover-estoque")
    public ResponseEntity<?> removerEstoque(@PathVariable Long id,
                                            @RequestParam Integer quantidade) {
        try {
            Material material = materialService.removerEstoque(id, quantidade);
            return new ResponseEntity<>(material, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/verificar-estoque")
    public ResponseEntity<?> verificarEstoque(@PathVariable Long id,
                                              @RequestParam Integer quantidade) {
        try {
            boolean disponivel = materialService.verificarEstoque(id, quantidade);
            return ResponseEntity.ok()
                    .body("Estoque disponível: " + disponivel);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }
}