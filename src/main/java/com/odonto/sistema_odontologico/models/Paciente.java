package com.odonto.sistema_odontologico.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "pacientes")

public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do paciente é obrigatório")
    @Column(name = "nome_paciente", nullable = false, length = 100)
    private String nomePaciente;

    @NotNull(message = "Idade é obrigatória")
    @Min(value = 0, message = "Idade deve ser positiva")
    @Max(value = 150, message = "Idade inválida")
    @Column(nullable = false)
    private Integer idade;

    @Min(value = 0, message = "Fidelidade não pode ser negativa")
    @Column(nullable = false)
    private Integer fidelidade = 0;

    /**
     * Calcula desconto baseado em idade e fidelidade
     * @return percentual de desconto (0.0 a 1.0)
     */
    public double calcularDesconto() {
        double desconto = 0.0;

        // Desconto por idade (idosos)
        if (idade >= 60) {
            desconto += 0.10; // 10%
        }

        // Desconto por fidelidade
        if (fidelidade >= 100 && fidelidade < 200) {
            desconto += 0.05; // 5%
        } else if (fidelidade >= 200) {
            desconto += 0.10; // 10%
        }

        return Math.min(desconto, 0.25); // máximo 25% de desconto
    }

    @Override
    public String toString() {
        return "Paciente{" +
                "id=" + id +
                ", nomePaciente='" + nomePaciente + '\'' +
                ", idade=" + idade +
                ", fidelidade=" + fidelidade +
                '}';
    }
}
