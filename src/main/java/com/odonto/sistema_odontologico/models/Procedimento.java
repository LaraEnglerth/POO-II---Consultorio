package com.odonto.sistema_odontologico.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "procedimentos")
public class Procedimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do procedimento é obrigatório")
    @Column(name = "nome_procedimento", nullable = false, length = 100)
    private String nomeProcedimento;

    @NotNull(message = "Campo assistente é obrigatório")
    @Column(nullable = false)
    private Boolean assistente;

    @NotNull(message = "Duração é obrigatória")
    @DecimalMin(value = "0.0", inclusive = false, message = "Duração deve ser maior que zero")
    @Column(nullable = false)
    private Float duracao; // em horas

    @ManyToOne //relacionamento com pacientes: m:1
    @JoinColumn(name = "paciente_id", nullable = false)
    @NotNull(message = "Paciente é obrigatório")
    private Paciente paciente;

    @ManyToMany //relacionamento com materiais: m:m
    @JoinTable(
            name = "procedimento_materiais",
            joinColumns = @JoinColumn(name = "procedimento_id"),
            inverseJoinColumns = @JoinColumn(name = "material_id")
    )
    private List<Material> materiais = new ArrayList<>();

    @Column(name = "valor_mao_obra", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorMaoObra = new BigDecimal("100.00"); // valor por hora

    @Column(name = "valor_final", precision = 10, scale = 2)
    private BigDecimal valorFinal;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Procedimento{" +
                "id=" + id +
                ", nomeProcedimento='" + nomeProcedimento + '\'' +
                ", assistente=" + assistente +
                ", duracao=" + duracao +
                ", paciente=" + (paciente != null ? paciente.getNomePaciente() : "null") +
                ", quantidadeMateriais=" + (materiais != null ? materiais.size() : 0) +
                ", valorFinal=" + valorFinal +
                '}';
    }
}
