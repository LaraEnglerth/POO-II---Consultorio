package com.odonto.sistema_odontologico.services;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.odonto.sistema_odontologico.models.Material;
import com.odonto.sistema_odontologico.models.Procedimento;

/**
 * SRP: calcular preços de procedimentos
 * Lógica de cálculo:
 * 1. Custo dos materiais (reutilizáveis têm 90% de desconto)
 * 2. Mão de obra (valor por hora * duração)
 * 3. Adicional de assistente (se houver, +50 reais fixo)
 * 4. Desconto do paciente (baseado em idade e fidelidade)
 */
@Service
public class CalculadoraPrecoService {

    private static final BigDecimal ADICIONAL_ASSISTENTE = new BigDecimal("50.00");

    public BigDecimal calcularValorFinal(Procedimento procedimento) {
        BigDecimal valorTotal = BigDecimal.ZERO;

        // 1. Custo dos materiais
        BigDecimal custoMateriais = calcularCustoMateriais(procedimento);
        valorTotal = valorTotal.add(custoMateriais);

        // 2. Mão de obra (valor por hora * duração)
        BigDecimal custoMaoObra = calcularCustoMaoObra(procedimento);
        valorTotal = valorTotal.add(custoMaoObra);

        // 3. Adicional de assistente
        if (procedimento.getAssistente()) {
            valorTotal = valorTotal.add(ADICIONAL_ASSISTENTE);
        }

        // 4. Aplicar desconto do paciente
        double percentualDesconto = procedimento.getPaciente().calcularDesconto();
        BigDecimal desconto = valorTotal.multiply(BigDecimal.valueOf(percentualDesconto));
        valorTotal = valorTotal.subtract(desconto);

        return valorTotal.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula o custo total dos materiais
     * Materiais reutilizáveis: cobram apenas 10% do valor
     * Materiais descartáveis: cobram valor integral
     */
    private BigDecimal calcularCustoMateriais(Procedimento procedimento) {
        return procedimento.getMateriais().stream()
                .map(Material::calcularValorProcedimento)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula o custo da mão de obra
     * Valor base por hora * duração em horas
     */
    private BigDecimal calcularCustoMaoObra(Procedimento procedimento) {
        BigDecimal valorPorHora = procedimento.getValorMaoObra();
        BigDecimal duracaoDecimal = BigDecimal.valueOf(procedimento.getDuracao()/60.0);
        return valorPorHora.multiply(duracaoDecimal);
    }

    /**
     * Retorna o detalhamento do cálculo (para ver no postman - back)
     */
    public String gerarDetalhamentoCalculo(Procedimento procedimento) {
        StringBuilder detalhamento = new StringBuilder();

        BigDecimal custoMateriais = calcularCustoMateriais(procedimento);
        BigDecimal custoMaoObra = calcularCustoMaoObra(procedimento);
        double percentualDesconto = procedimento.getPaciente().calcularDesconto();

        detalhamento.append("=== DETALHAMENTO DO CÁLCULO ===\n");
        detalhamento.append(String.format("Materiais: R$ %.2f\n", custoMateriais));
        detalhamento.append(String.format("Mão de obra: R$ %.2f (R$ %.2f/h x %.2fh)\n",
                custoMaoObra, procedimento.getValorMaoObra(), procedimento.getDuracao()));

        if (procedimento.getAssistente()) {
            detalhamento.append(String.format("Assistente: R$ %.2f\n", ADICIONAL_ASSISTENTE));
        }

        BigDecimal subtotal = custoMateriais.add(custoMaoObra);
        if (procedimento.getAssistente()) {
            subtotal = subtotal.add(ADICIONAL_ASSISTENTE);
        }

        detalhamento.append(String.format("Subtotal: R$ %.2f\n", subtotal));

        if (percentualDesconto > 0) {
            BigDecimal valorDesconto = subtotal.multiply(BigDecimal.valueOf(percentualDesconto));
            detalhamento.append(String.format("Desconto paciente (%.0f%%): -R$ %.2f\n",
                    percentualDesconto * 100, valorDesconto));
        }

        detalhamento.append(String.format("VALOR FINAL: R$ %.2f\n", procedimento.getValorFinal()));
        detalhamento.append("===============================");

        return detalhamento.toString();
    }
}
