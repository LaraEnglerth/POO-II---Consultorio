package com.odonto.sistema_odontologico.repositories;

import com.odonto.sistema_odontologico.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    // Buscar pacientes por nome
    List<Paciente> findByNomePacienteContainingIgnoreCase(String nome);

    // Buscar pacientes por idade
    List<Paciente> findByIdade(Integer idade);

    // Buscar pacientes com fidelidade maior que x valor
    List<Paciente> findByFidelidadeGreaterThanEqual(Integer fidelidade);
}