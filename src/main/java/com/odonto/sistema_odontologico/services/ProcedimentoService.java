package com.odonto.sistema_odontologico.services;

import com.odonto.sistema_odontologico.dto.ProcedimentoDTO;
import com.odonto.sistema_odontologico.models.Material;
import com.odonto.sistema_odontologico.models.Paciente;
import com.odonto.sistema_odontologico.models.Procedimento;
import com.odonto.sistema_odontologico.repositories.MaterialRepository;
import com.odonto.sistema_odontologico.repositories.PacienteRepository;
import com.odonto.sistema_odontologico.repositories.ProcedimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * SRP lógica de negócio de Procedimento
 */
@Service
public class ProcedimentoService {

    @Autowired
    private ProcedimentoRepository procedimentoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private CalculadoraPrecoService calculadoraPrecoService;

    @Transactional
    public Procedimento cadastrarProcedimento(ProcedimentoDTO dto) {
        // Buscar paciente
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado com ID: " + dto.getPacienteId()));

        // Buscar materiais
        List<Material> materiais = materialRepository.findAllById(dto.getMateriaisIds());
        if (materiais.size() != dto.getMateriaisIds().size()) {
            throw new RuntimeException("Alguns materiais não foram encontrados");
        }

        // Verificar estoque de materiais descartáveis
        for (Material material : materiais) {
            if (!material.getReutilizavel() && !material.temEstoqueSuficiente(1)) {
                throw new RuntimeException("Material sem estoque suficiente: " + material.getNomeMaterial());
            }
        }

        // Criar procedimento
        Procedimento procedimento = new Procedimento();
        procedimento.setNomeProcedimento(dto.getNomeProcedimento());
        procedimento.setAssistente(dto.getAssistente());
        procedimento.setDuracao(dto.getDuracao());
        procedimento.setPaciente(paciente);
        procedimento.setMateriais(materiais);

        // Definir valor mão de obra (usa padrão se não informado)
        if (dto.getValorMaoObra() != null) {
            procedimento.setValorMaoObra(dto.getValorMaoObra());
        } else {
            procedimento.setValorMaoObra(new BigDecimal("100.00"));
        }

        // Calcular valor final
        BigDecimal valorFinal = calculadoraPrecoService.calcularValorFinal(procedimento);
        procedimento.setValorFinal(valorFinal);

        // Salvar procedimento
        Procedimento procedimentoSalvo = procedimentoRepository.save(procedimento);

        // Descontar estoque dos materiais descartáveis
        for (Material material : materiais) {
            if (!material.getReutilizavel()) {
                material.descontarEstoque(1);
                materialRepository.save(material);
            }
        }

        // Adicionar pontos de fidelidade ao paciente (10 pontos por procedimento)
        paciente.setFidelidade(paciente.getFidelidade() + 10);
        pacienteRepository.save(paciente);

        return procedimentoSalvo;
    }

    public List<Procedimento> listarTodos() {
        return procedimentoRepository.findAll();
    }

    public Optional<Procedimento> buscarPorId(Long id) {
        return procedimentoRepository.findById(id);
    }

    public List<Procedimento> buscarPorNome(String nome) {
        return procedimentoRepository.findByNomeProcedimentoContainingIgnoreCase(nome);
    }

    public List<Procedimento> buscarPorPaciente(Long pacienteId) {
        return procedimentoRepository.findByPacienteId(pacienteId);
    }

    public List<Procedimento> buscarPorAssistente(Boolean assistente) {
        return procedimentoRepository.findByAssistente(assistente);
    }

    /**
     * Não devolve materiais ao estoque
     */
    @Transactional
    public void deletarProcedimento(Long id) {
        if (!procedimentoRepository.existsById(id)) {
            throw new RuntimeException("Procedimento não encontrado com ID: " + id);
        }
        procedimentoRepository.deleteById(id);
    }

    public String gerarDetalhamentoCalculo(Long id) {
        Procedimento procedimento = procedimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Procedimento não encontrado com ID: " + id));

        return calculadoraPrecoService.gerarDetalhamentoCalculo(procedimento);
    }
}
