package com.odonto.sistema_odontologico.services;

import com.odonto.sistema_odontologico.models.Paciente;
import com.odonto.sistema_odontologico.repositories.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service responsável APENAS pela lógica de negócio de Paciente (SRP - Single Responsibility Principle)
 */
@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Transactional
    public Paciente cadastrarPaciente(Paciente paciente) {
        // Validação de negócio: fidelidade inicial não pode ser negativa
        if (paciente.getFidelidade() == null) {
            paciente.setFidelidade(0);
        }
        return pacienteRepository.save(paciente);
    }

    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    public Optional<Paciente> buscarPorId(Long id) {
        return pacienteRepository.findById(id);
    }

    public List<Paciente> buscarPorNome(String nome) {
        return pacienteRepository.findByNomePacienteContainingIgnoreCase(nome);
    }

    @Transactional
    public Paciente atualizarPaciente(Long id, Paciente pacienteAtualizado) {
        Paciente pacienteExistente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado com ID: " + id));

        pacienteExistente.setNomePaciente(pacienteAtualizado.getNomePaciente());
        pacienteExistente.setIdade(pacienteAtualizado.getIdade());
        pacienteExistente.setFidelidade(pacienteAtualizado.getFidelidade());

        return pacienteRepository.save(pacienteExistente);
    }

    @Transactional
    public void deletarPaciente(Long id) {
        if (!pacienteRepository.existsById(id)) {
            throw new RuntimeException("Paciente não encontrado com ID: " + id);
        }
        // TODO: Verificar se há procedimentos associados antes de deletar
        pacienteRepository.deleteById(id);
    }

    @Transactional
    public Paciente adicionarPontosFidelidade(Long id, Integer pontos) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado com ID: " + id));

        paciente.setFidelidade(paciente.getFidelidade() + pontos);
        return pacienteRepository.save(paciente);
    }
}