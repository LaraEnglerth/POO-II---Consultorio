package com.odonto.sistema_odontologico.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
/**
 * DTO para criação/atualização de Procedimento - Facilita o envio de IDs em vez de objetos completos
 */
public class ProcedimentoDTO {

    @NotBlank(message = "Nome do procedimento é obrigatório")
    private String nomeProcedimento;

    @NotNull(message = "Campo assistente é obrigatório")
    private Boolean assistente;

    @NotNull(message = "Duração é obrigatória")
    @DecimalMin(value = "0.0", inclusive = false, message = "Duração deve ser maior que zero")
    private Float duracao;

    @NotNull(message = "ID do paciente é obrigatório")
    private Long pacienteId;

    @NotNull(message = "Lista de materiais é obrigatória")
    @Size(min = 1, message = "Deve haver pelo menos um material")
    private List<Long> materiaisIds;

    private BigDecimal valorMaoObra; // opcional, usa valor padrão se não informado

}
