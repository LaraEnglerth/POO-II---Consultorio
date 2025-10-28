package com.odonto.sistema_odontologico.repositories;

import com.odonto.sistema_odontologico.models.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    // Buscar materiais por nome
    List<Material> findByNomeMaterialContainingIgnoreCase(String nome);

    // Buscar materiais reutilizáveis
    List<Material> findByReutilizavel(Boolean reutilizavel);

    // Buscar materiais com estoque baixo
    List<Material> findByQuantidadeLessThanEqual(Integer quantidade);

    // Buscar materiais com estoque disponível
    List<Material> findByQuantidadeGreaterThan(Integer quantidade);
}