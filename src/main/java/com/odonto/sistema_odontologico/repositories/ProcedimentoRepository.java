package com.odonto.sistema_odontologico.repositories;

import com.odonto.sistema_odontologico.models.Procedimento;
import com.odonto.sistema_odontologico.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcedimentoRepository extends JpaRepository<Procedimento, Long> {

    // Buscar procedimentos por nome
    List<Procedimento> findByNomeProcedimentoContainingIgnoreCase(String nome);

    // Buscar procedimentos de um paciente específico
    List<Procedimento> findByPaciente(Paciente paciente);

    // Buscar procedimentos de um paciente por ID
    List<Procedimento> findByPacienteId(Long pacienteId);

    // Buscar procedimentos que usaram assistente
    List<Procedimento> findByAssistente(Boolean assistente);
}