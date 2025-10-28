package com.odonto.sistema_odontologico.services;

import com.odonto.sistema_odontologico.models.Material;
import com.odonto.sistema_odontologico.repositories.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service responsável APENAS pela lógica de negócio de Material (SRP - Single Responsibility Principle)
 */
@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Transactional
    public Material cadastrarMaterial(Material material) {
        // Validação de negócio
        if (material.getQuantidade() == null) {
            material.setQuantidade(0);
        }
        return materialRepository.save(material);
    }

    public List<Material> listarTodos() {
        return materialRepository.findAll();
    }

    public Optional<Material> buscarPorId(Long id) {
        return materialRepository.findById(id);
    }

    public List<Material> buscarPorNome(String nome) {
        return materialRepository.findByNomeMaterialContainingIgnoreCase(nome);
    }

    public List<Material> buscarPorReutilizavel(Boolean reutilizavel) {
        return materialRepository.findByReutilizavel(reutilizavel);
    }

    public List<Material> buscarEstoqueBaixo(Integer quantidadeMinima) {
        return materialRepository.findByQuantidadeLessThanEqual(quantidadeMinima);
    }

    @Transactional
    public Material atualizarMaterial(Long id, Material materialAtualizado) {
        Material materialExistente = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material não encontrado com ID: " + id));

        // Atualiza os campos
        materialExistente.setNomeMaterial(materialAtualizado.getNomeMaterial());
        materialExistente.setQuantidade(materialAtualizado.getQuantidade());
        materialExistente.setValor(materialAtualizado.getValor());
        materialExistente.setReutilizavel(materialAtualizado.getReutilizavel());

        return materialRepository.save(materialExistente);
    }

    @Transactional
    public void deletarMaterial(Long id) {
        if (!materialRepository.existsById(id)) {
            throw new RuntimeException("Material não encontrado com ID: " + id);
        }
        // TODO: Verificar se há procedimentos associados antes de deletar
        materialRepository.deleteById(id);
    }

    @Transactional
    public Material adicionarEstoque(Long id, Integer quantidade) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material não encontrado com ID: " + id));

        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }

        material.setQuantidade(material.getQuantidade() + quantidade);
        return materialRepository.save(material);
    }

    /**
     * Remove quantidade do estoque de um material, quando material NÃO é reutilizável e é usado em procedimento
     */
    @Transactional
    public Material removerEstoque(Long id, Integer quantidade) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material não encontrado com ID: " + id));

        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }

        if (material.getQuantidade() < quantidade) {
            throw new RuntimeException("Estoque insuficiente. Disponível: " + material.getQuantidade());
        }

        material.setQuantidade(material.getQuantidade() - quantidade);
        return materialRepository.save(material);
    }

    public boolean verificarEstoque(Long id, Integer quantidadeNecessaria) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material não encontrado com ID: " + id));

        return material.temEstoqueSuficiente(quantidadeNecessaria);
    }
}