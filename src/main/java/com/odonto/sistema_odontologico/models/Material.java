package com.odonto.sistema_odontologico.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "materiais")

public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do material é obrigatório")
    @Column(name = "nome_material", nullable = false, length = 100)
    private String nomeMaterial;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 0, message = "Quantidade não pode ser negativa")
    @Column(nullable = false)
    private Integer quantidade;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Valor deve ser maior que zero")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @NotNull(message = "Campo reutilizável é obrigatório")
    @Column(nullable = false)
    private Boolean reutilizavel;

    /**
     * Calcula o valor real do material considerando se é reutilizável
     * - se reutilizável: aplica 90% de desconto (cobra apenas 10% do valor)
     * - se não reutilizável: cobra valor integral
     */
    public BigDecimal calcularValorProcedimento() {
        if (reutilizavel) {
            return valor.multiply(new BigDecimal("0.10"));
        }
        return valor;
    }

    public boolean temEstoqueSuficiente(Integer quantidadeNecessaria) {
        return this.quantidade >= quantidadeNecessaria;
    }

    /**
     * Diminui a quantidade em estoque se o material NÃO for reutilizável
     */
    public void descontarEstoque(Integer quantidadeUsada) {
        if (!reutilizavel && this.quantidade >= quantidadeUsada) {
            this.quantidade -= quantidadeUsada;
        }
    }

    @Override
    public String toString() {
        return "Material{" +
                "id=" + id +
                ", nomeMaterial='" + nomeMaterial + '\'' +
                ", quantidade=" + quantidade +
                ", valor=" + valor +
                ", reutilizavel=" + reutilizavel +
                '}';
    }

}
